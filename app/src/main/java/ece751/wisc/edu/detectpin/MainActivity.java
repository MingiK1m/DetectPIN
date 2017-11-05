package ece751.wisc.edu.detectpin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ece751.wisc.edu.detectpin.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";

    private SensorManager mSensorManager;

    private Sensor mAccelSensor;
    private Sensor mGyroSensor;
    private Sensor mMagnetSensor;

    private TextView mAccelValTextView;
    private TextView mGyroValTextView;
    private TextView mMagnetValTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccelValTextView = (TextView) findViewById(R.id.accelValText);
        mGyroValTextView = (TextView) findViewById(R.id.gyroValText);
        mMagnetValTextView = (TextView) findViewById(R.id.magnetValText);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor s = event.sensor;
        if(s == mAccelSensor){
            String msg = "";
            for(float v : event.values) {
                msg += v +",";
            }
            mAccelValTextView.setText(msg);
        } else if (s == mGyroSensor){
            String msg = "";
            for(float v : event.values) {
                msg += v +",";
            }
            mGyroValTextView.setText(msg);
        } else if (s == mMagnetSensor){
            String msg = "";
            for(float v : event.values) {
                msg += v +",";
            }
            mMagnetValTextView.setText(msg);
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
