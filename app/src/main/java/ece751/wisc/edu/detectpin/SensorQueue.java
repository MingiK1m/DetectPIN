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

    public Queue<SensorData> getQueueCopy(){
        return new LinkedList<SensorData>(this.mQueue);
    }

    public void writeAsFileAfterOneSec(final int numPressed, final int count, final boolean isVibrating){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String str = isVibrating? "v_" : "";
                    FileWriter writer = new FileWriter(str + mSensorType + "_" + numPressed + "_" + count);
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
