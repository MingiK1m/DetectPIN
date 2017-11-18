package ece751.wisc.edu.detectpin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class LearningActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = " LearningActivity";

    private SensorManager mSensorManager;

    private Sensor mAccelSensor;
    private Sensor mGyroSensor;
    private Sensor mMagnetSensor;

    private TextView mGuideTextView;
    private EditText mInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        mGuideTextView = (TextView) findViewById(R.id.tv_guide);
        mInputEditText = (EditText) findViewById(R.id.pinEditText);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) return;

                mGuideTextView.setText(s);
                try{
                    int input = Integer.parseInt(s.toString());

                    // TODO: Click event. Save the sensor data here.
                    // NOTE: you can use helper class FileWriter to write file.
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                s.clear();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // see URL below to figure out what values come from sensors.
        // https://developer.android.com/reference/android/hardware/SensorEvent.html
        Sensor s = event.sensor;
        if(s == mAccelSensor){
            // event.values is an array { x, y, z }
            for(float v : event.values) {
                // Do something
            }
        } else if (s == mGyroSensor){
            // event.values is an array { x, y, z }
            for(float v : event.values) {
                // Do something
            }
        } else if (s == mMagnetSensor){
            // event.values is an array { x, y, z }
            for(float v : event.values) {
                // Do something
            }
        }
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
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mMagnetSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
