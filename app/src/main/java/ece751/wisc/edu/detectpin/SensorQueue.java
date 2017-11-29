package ece751.wisc.edu.detectpin;

import android.os.Handler;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Collects data from - 1s to + 1s
 * Created by Mingi on 11/28/2017.
 */

public class SensorQueue {
    private class SensorData{
        public float x;
        public float y;
        public float z;

        public SensorData(float[] values){
            if(values.length != 3) throw new IllegalArgumentException();
            x = values[0];
            y = values[1];
            z = values[2];
        }

        public String toString(){
            return x+","+y+","+z;
        }
    }
    private final Queue<SensorData> mQueue;
    private final long samplesForOneSecond;
    private final int mSensorType;

    public SensorQueue(int sensorType, long usDelay){
        this.mQueue = new LinkedList<SensorData>();
        // 1 s = 1000000 us
        this.samplesForOneSecond = 1000000 / usDelay;
        this.mSensorType = sensorType;
    }

    public void addData(float[] values){
        synchronized (this.mQueue) {
            this.mQueue.add(new SensorData(values));
            if (this.mQueue.size() > this.samplesForOneSecond * 2) {
                this.mQueue.remove();
            }
        }
    }

    public void writeAsFile(final int numPressed, final int count){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    FileWriter writer = new FileWriter(mSensorType + "_" + numPressed + "_" + count);
                    synchronized (mQueue) {
                        for (SensorData data : mQueue) {
                            writer.println(data.toString());
                        }
                    }
                    writer.close();
                } catch(Exception e){
                    Log.e("SENSORQUEUE","CANT WRITE FILE");
                }
            }
        }, 1000);
    }
}
