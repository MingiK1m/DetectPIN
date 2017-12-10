package ece751.wisc.edu.detectpin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LearningActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = " LearningActivity";

    private SensorManager mSensorManager;

    private Sensor mAccelSensor;
    private SensorQueue mAccelQueue;
    private Sensor mAccelGravSensor;
    private SensorQueue mAccelGravQueue;
    private Sensor mGyroSensor;
    private SensorQueue mGyroQueue;
    private Sensor mMagnetSensor;
    private SensorQueue mMagnetQueue;
    private Sensor mRotationSensor;
//    private SensorQueue mRotationQueue;

    private Vibrator mVibrator;

    private boolean mIsVibrating;

    private TextView mGuideTextView;
    private EditText mInputEditText;
    private ToggleButton mVibationToggleButton;

    private int[] mCollectedSamplesCountAry = new int[10]; // init with 0 automatically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        mGuideTextView = findViewById(R.id.tv_guide);
        mInputEditText = findViewById(R.id.pinEditText);
        mVibationToggleButton = findViewById(R.id.btn_vib_toggle);

        mIsVibrating = mVibationToggleButton.isChecked();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mAccelGravSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mAccelQueue = new SensorQueue(Sensor.TYPE_LINEAR_ACCELERATION, mAccelSensor.getMinDelay());
        mAccelGravQueue = new SensorQueue(Sensor.TYPE_ACCELEROMETER, mAccelGravSensor.getMinDelay());
        mGyroQueue = new SensorQueue(Sensor.TYPE_GYROSCOPE, mGyroSensor.getMinDelay());
        mMagnetQueue = new SensorQueue(Sensor.TYPE_MAGNETIC_FIELD, mMagnetSensor.getMinDelay());
//        mRotationQueue = new SensorQueue(Sensor.TYPE_ROTATION_VECTOR, mRotationSensor.getMinDelay());

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mIsVibrating){
                    mVibrator.vibrate(200);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) return;

                mGuideTextView.setText(s);
                try{
                    int input = Integer.parseInt(s.toString());
                    int sampleCount = mCollectedSamplesCountAry[input]++;

                    mAccelQueue.writeAsFileAfterOneSec(input,sampleCount,mIsVibrating);
                    mAccelGravQueue.writeAsFileAfterOneSec(input,sampleCount,mIsVibrating);
                    mGyroQueue.writeAsFileAfterOneSec(input,sampleCount,mIsVibrating);
                    mMagnetQueue.writeAsFileAfterOneSec(input,sampleCount,mIsVibrating);
//                    mRotationQueue.writeAsFile(input,sampleCount);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                s.clear();
            }
        });

        mVibationToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsVibrating = isChecked;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // see URL below to figure out what values come from sensors.
        // https://developer.android.com/reference/android/hardware/SensorEvent.html
        Sensor s = event.sensor;
        float[] values = event.values;
        if (s == mAccelSensor) {
            mAccelQueue.addData(values);
        } else if (s == mAccelGravSensor) {
            mAccelGravQueue.addData(values);
        } else if (s == mGyroSensor) {
            mGyroQueue.addData(values);
        } else if (s == mMagnetSensor) {
            mMagnetQueue.addData(values);
        }
//        else if (s == mRotationSensor) {
//            mRotationQueue.addData(values);
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        // Do nothing
        Log.d(TAG, "AccuracyChanged");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mAccelGravSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mMagnetSensor, SensorManager.SENSOR_DELAY_FASTEST);
//        mSensorManager.registerListener(this, mRotationSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
