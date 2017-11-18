package ece751.wisc.edu.detectpin;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class RecognizerActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "RecognizerActivity";

    private SensorManager mSensorManager;

    private Sensor mAccelSensor;
    private Sensor mGyroSensor;
    private Sensor mMagnetSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor s = event.sensor;
        if(s == mAccelSensor){
            for(float v : event.values) {
                // Do something
            }
        } else if (s == mGyroSensor){
            for(float v : event.values) {
                // Do something
            }
        } else if (s == mMagnetSensor){
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
