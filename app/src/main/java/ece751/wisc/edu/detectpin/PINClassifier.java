package ece751.wisc.edu.detectpin;

import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Queue;

/**
 * How to classify the class of an instance of a set of sensor data
 * 1) Construct PINClassifier with weka classification model filename
 * 2) Set all 4 sensor data queue (the classification data will be returned if it is ready)
 * 3) ARFF will be stored as a file
 * Created by ksmk9 on 12/6/2017.
 */

public class PINClassifier {
    public static final int TYPE_ACCEL = 10;
    public static final int TYPE_ACCEL_G = 1;
    public static final int TYPE_GYRO = 4;
    public static final int TYPE_MAGNET = 2;

    private final int SENSOR_TYPES[] = {
            TYPE_ACCEL,
            TYPE_ACCEL_G,
            TYPE_GYRO,
            TYPE_MAGNET
    };

    public static final String FILENAME_ARFF_INSTANCE = "unknownPinSensorData.arff";

    private Queue<SensorData> mRawAccelDataQueue = null;
    private Queue<SensorData> mRawAccelGDataQueue = null;
    private Queue<SensorData> mRawGyroDataQueue = null;
    private Queue<SensorData> mRawMagnetDataQueue = null;

    private final WekaInstance mWekaInstance;

    public PINClassifier(String wekaClassificationModelFilename) throws Exception{
        this.mWekaInstance = new WekaInstance(FileWriter.sInternalAppFilesDir.getAbsolutePath()+"/"+wekaClassificationModelFilename);
    }

    public void setNewClassification(){
        this.mRawAccelDataQueue = null;
        this.mRawAccelGDataQueue = null;
        this.mRawGyroDataQueue = null;
        this.mRawMagnetDataQueue = null;
    }

    private boolean isReady(){
        if(this.mRawAccelDataQueue == null
                || this.mRawAccelGDataQueue == null
                || this.mRawGyroDataQueue == null
                || this.mRawMagnetDataQueue == null){
            return false;
        }
        return true;
    }

    /**
     *
     * @return null if classification is not ready, otherwise classification label as String
     */
    public synchronized String setAccelDataAndGetClassIfCan(Queue<SensorData> accelDataQueue){
        String classLabel = null;
        this.mRawAccelDataQueue = accelDataQueue;

        if(isReady()){
            classLabel = classifyCurrentSensorData();
            setNewClassification();
        }

        return classLabel;
    }

    /**
     *
     * @return null if classification is not ready, otherwise classification label as String
     */
    public synchronized String setAccelGDataAndGetClassIfCan(Queue<SensorData> accelGDataQueue){
        String classLabel = null;
        this.mRawAccelGDataQueue = accelGDataQueue;

        if(isReady()){
            classLabel = classifyCurrentSensorData();
            setNewClassification();
        }

        return classLabel;

    }

    /**
     *
     * @return null if classification is not ready, otherwise classification label as String
     */
    public synchronized String setGyroDataAndGetClassIfCan(Queue<SensorData> gyroDataQueue){
        String classLabel = null;
        this.mRawGyroDataQueue = gyroDataQueue;

        if(isReady()){
            classLabel = classifyCurrentSensorData();
            setNewClassification();
        }

        return classLabel;
    }

    /**
     *
     * @return null if classification is not ready, otherwise classification label as String
     */
    public synchronized String setMagnetDataAndGetClassIfCan(Queue<SensorData> magnetDataQueue){
        String classLabel = null;
        this.mRawMagnetDataQueue = magnetDataQueue;

        if(isReady()){
            classLabel = classifyCurrentSensorData();
            setNewClassification();
        }

        return classLabel;
    }

    /**
     * Get FFT min max avg for each axes.
     * @param dataQueue input data queue
     * @return float[9] {min_x,max_x,avg_x, min_y,max_y,avg_y, min_z,max_z,avg_z}
     */
    private float[] getFftMinMaxAvg(Queue<SensorData> dataQueue){
        float[] retArray = new float[9];

        double min_x,max_x,avg_x, min_y,max_y,avg_y, min_z,max_z,avg_z, itemNum;
        double sum_x, sum_y, sum_z;

        max_x = max_y = max_z = -Float.MAX_VALUE;
        min_x = min_y = min_z = Float.MAX_VALUE;
        sum_x = sum_y = sum_z = 0.0;

        itemNum = (double) dataQueue.size();
        int powerOfTwoSize = 1;
        while(itemNum > powerOfTwoSize){
            powerOfTwoSize *= 2;
        }
        Object[] dataArray = dataQueue.toArray();
        double[] x_data = new double[powerOfTwoSize]; // add padding with zero
        double[] y_data = new double[powerOfTwoSize]; // add padding with zero
        double[] z_data = new double[powerOfTwoSize]; // add padding with zero
        for(int i=0;i<dataArray.length;i++){
            SensorData data = (SensorData) dataArray[i];
            x_data[i] = data.x;
            y_data[i] = data.y;
            z_data[i] = data.z;
        }

        FFT fft = new FFT(powerOfTwoSize);
        double[] imaginary = new double[powerOfTwoSize];
        fft.fft(x_data,imaginary);
        for(int i=0;i<powerOfTwoSize;i++){
            double v = Math.sqrt(Math.pow(x_data[i],2) + Math.pow(imaginary[i],2));
            max_x = max_x<v? v:max_x;
            min_x = min_x>v? v:min_x;
            sum_x += v;
        }
        avg_x = sum_x/itemNum;

        imaginary = new double[powerOfTwoSize];
        fft.fft(y_data,imaginary);
        for(int i=0;i<powerOfTwoSize;i++){
            double v = Math.sqrt(Math.pow(y_data[i],2) + Math.pow(imaginary[i],2));
            max_y = max_y<v? v:max_y;
            min_y = min_y>v? v:min_y;
            sum_y += v;
        }
        avg_y = sum_y/itemNum;

        imaginary = new double[powerOfTwoSize];
        fft.fft(z_data,imaginary);
        for(int i=0;i<powerOfTwoSize;i++){
            double v = Math.sqrt(Math.pow(z_data[i],2) + Math.pow(imaginary[i],2));
            max_z = max_z<v? v:max_z;
            min_z = min_z>v? v:min_z;
            sum_z += v;
        }
        avg_z = sum_z/itemNum;

        retArray[0] = (float) min_x;
        retArray[1] = (float) min_y;
        retArray[2] = (float) min_z;
        retArray[3] = (float) max_x;
        retArray[4] = (float) max_y;
        retArray[5] = (float) max_z;
        retArray[6] = (float) avg_x;
        retArray[7] = (float) avg_y;
        retArray[8] = (float) avg_z;

        return retArray;

    }

    /**
     * Get min max avg for each axes.
     * @param dataQueue input data queue
     * @return float[9] {min_x,max_x,avg_x, min_y,max_y,avg_y, min_z,max_z,avg_z}
     */
    private float[] getMinMaxAvg(Queue<SensorData> dataQueue){
        float[] retArray = new float[9];

        float min_x,max_x,avg_x, min_y,max_y,avg_y, min_z,max_z,avg_z, itemNum;
        double sum_x, sum_y, sum_z;

        max_x = max_y = max_z = -Float.MAX_VALUE;
        min_x = min_y = min_z = Float.MAX_VALUE;
        sum_x = sum_y = sum_z = 0.0;
        itemNum = (float) dataQueue.size();
        for(SensorData data : dataQueue){
            max_x = max_x<data.x? data.x:max_x;
            max_y = max_y<data.y? data.y:max_y;
            max_z = max_z<data.z? data.z:max_z;

            min_x = min_x>data.x? data.x:min_x;
            min_y = min_y>data.y? data.y:min_y;
            min_z = min_z>data.z? data.z:min_z;

            sum_x += data.x;
            sum_y += data.y;
            sum_z += data.z;
        }
        avg_x = (float)(sum_x/itemNum);
        avg_y = (float)(sum_y/itemNum);
        avg_z = (float)(sum_z/itemNum);

        retArray[0] = min_x;
        retArray[1] = min_y;
        retArray[2] = min_z;
        retArray[3] = max_x;
        retArray[4] = max_y;
        retArray[5] = max_z;
        retArray[6] = avg_x;
        retArray[7] = avg_y;
        retArray[8] = avg_z;

        return retArray;
    }

    /**
     * Save sensor data into ARFF file format
     * @param filename name of the file
     * @return true if success, otherwise false
     */
    private boolean saveARFFfile(String filename){// min_x,max_x,avg_x, min_y,max_y,avg_y, min_z,max_z,avg_z
        float[][] attributes = new float[8][];

        // get min max avg of accel
        attributes[0] = getMinMaxAvg(this.mRawAccelDataQueue);
        attributes[1] = getFftMinMaxAvg(this.mRawAccelDataQueue);

        // get min max avg of accelG
        attributes[2] = getMinMaxAvg(this.mRawAccelGDataQueue);
        attributes[3] = getFftMinMaxAvg(this.mRawAccelGDataQueue);

        // get min max avg of gyro
        attributes[4] = getMinMaxAvg(this.mRawGyroDataQueue);
        attributes[5] = getFftMinMaxAvg(this.mRawGyroDataQueue);

        // get min max avg of magnet
        attributes[6] = getMinMaxAvg(this.mRawMagnetDataQueue);
        attributes[7] = getFftMinMaxAvg(this.mRawMagnetDataQueue);

        try{
            ARFFWriter.writeSingleUnlabeledARFF(filename,attributes);
        } catch(Exception e){
            Log.e("PINClaasifier", e.toString());
            return false;
        }

        return true;
    }

    /**
     * Start classification with sensordata
     * This method is called when all the queues are ready.
     * @return class of this instance
     */
    private String classifyCurrentSensorData(){
        if(!saveARFFfile(FILENAME_ARFF_INSTANCE)){
            return null;
        }
        String classLabel = mWekaInstance.OnlinePrediction(FILENAME_ARFF_INSTANCE);
        Log.i("PINClassifier", "LABEL = " + classLabel);
        return classLabel;
    }
}