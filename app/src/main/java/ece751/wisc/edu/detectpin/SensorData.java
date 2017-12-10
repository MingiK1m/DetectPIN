package ece751.wisc.edu.detectpin;

/**
 * Created by Mingi on 12/6/2017.
 */

public class SensorData{
    public float x;
    public float y;
    public float z;

    public SensorData(float[] values){
        if(values.length != 3) throw new IllegalArgumentException();
        x = values[0];
        y = values[1];
        z = values[2];
    }

    @Override
    public String toString(){
        return x+","+y+","+z;
    }
}