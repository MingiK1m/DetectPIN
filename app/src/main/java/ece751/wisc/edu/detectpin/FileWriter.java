package ece751.wisc.edu.detectpin;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Mingi on 11/18/2017.
 */

public class FileWriter {
    private Context mContext;
    private File mFile;
    private FileOutputStream outputStream;

    public FileWriter(Context context, String fileName) throws FileNotFoundException {
        this.mContext = context;
        this.mFile = new File(context.getFilesDir(), fileName);
        this.outputStream = new FileOutputStream(this.mFile);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        this.outputStream.write(bytes);
    }

    public void close() throws IOException{
        this.outputStream.close();
    }
}
