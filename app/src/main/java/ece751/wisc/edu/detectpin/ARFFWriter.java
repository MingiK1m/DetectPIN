package ece751.wisc.edu.detectpin;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ksmk9 on 12/7/2017.
 */

public class ARFFWriter {
    private final static String ARFF_HEADER =
            "% \n"
                    + "% ECE751 EMBEDDED COMPUTING SYSTEM\n"
                    + "% Dongjin, Kyuin, Mingi, Yongwoo \n"
                    + "% SENSOR DATA FOR PIN\n"
                    + "% \n";

    private final static String ARFF_RELATION =
            "@RELATION pin\n";

    private final static String ARFF_ATTR_NUMERIC_FORMAT =
            "@ATTRIBUTE %s NUMERIC\n";

    private final static String ARFF_CLASS =
            "@ATTRIBUTE class {1,2,3,4,5,6,7,8,9,0}\n";

    private final static String ARFF_DATA =
            "@DATA\n";

    private final static String ATTRIBUTES[] = {
            "accel_x_min",
            "accel_y_min",
            "accel_z_min",
            "accel_x_max",
            "accel_y_max",
            "accel_z_max",
            "accel_x_avg",
            "accel_y_avg",
            "accel_z_avg",
            "accel_fft_x_min",
            "accel_fft_y_min",
            "accel_fft_z_min",
            "accel_fft_x_max",
            "accel_fft_y_max",
            "accel_fft_z_max",
            "accel_fft_x_avg",
            "accel_fft_y_avg",
            "accel_fft_z_avg",

            "accel_g_x_min",
            "accel_g_y_min",
            "accel_g_z_min",
            "accel_g_x_max",
            "accel_g_y_max",
            "accel_g_z_max",
            "accel_g_x_avg",
            "accel_g_y_avg",
            "accel_g_z_avg",
            "accel_g_fft_x_min",
            "accel_g_fft_y_min",
            "accel_g_fft_z_min",
            "accel_g_fft_x_max",
            "accel_g_fft_y_max",
            "accel_g_fft_z_max",
            "accel_g_fft_x_avg",
            "accel_g_fft_y_avg",
            "accel_g_fft_z_avg",

            "gyro_x_min",
            "gyro_y_min",
            "gyro_z_min",
            "gyro_x_max",
            "gyro_y_max",
            "gyro_z_max",
            "gyro_x_avg",
            "gyro_y_avg",
            "gyro_z_avg",
            "gyro_fft_x_min",
            "gyro_fft_y_min",
            "gyro_fft_z_min",
            "gyro_fft_x_max",
            "gyro_fft_y_max",
            "gyro_fft_z_max",
            "gyro_fft_x_avg",
            "gyro_fft_y_avg",
            "gyro_fft_z_avg",

            "magnet_x_min",
            "magnet_y_min",
            "magnet_z_min",
            "magnet_x_max",
            "magnet_y_max",
            "magnet_z_max",
            "magnet_x_avg",
            "magnet_y_avg",
            "magnet_z_avg",
            "magnet_fft_x_min",
            "magnet_fft_y_min",
            "magnet_fft_z_min",
            "magnet_fft_x_max",
            "magnet_fft_y_max",
            "magnet_fft_z_max",
            "magnet_fft_x_avg",
            "magnet_fft_y_avg",
            "magnet_fft_z_avg"
    };

    /**
     * @param filename filename to write
     * @param data     float[8][9] @see ATTRIBUTES
     * @throws FileNotFoundException
     */
    public static void writeSingleUnlabeledARFF(String filename, float[][] data) throws IllegalArgumentException, IOException {
        if (data.length != 8) throw new IllegalArgumentException();
        for (int i = 0; i < data.length; i++) {
            if (data[i].length != 9) throw new IllegalArgumentException();
        }

        FileWriter writer = new FileWriter(filename);
        writer.print(ARFF_HEADER);
        writer.print(ARFF_RELATION);
        for (String attr : ATTRIBUTES) {
            writer.print(String.format(ARFF_ATTR_NUMERIC_FORMAT, attr));
        }
        writer.print(ARFF_CLASS);
        writer.print(ARFF_DATA);

        for (int i=0; i<data.length;i++) {
            for(int j=0;j<data[i].length;j++){
                writer.print(data[i][j]+",");
            }
        }
        writer.print("?"); // unlabeled class
        writer.close();
    }
}
