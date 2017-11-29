package ece751.wisc.edu.detectpin;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Mingi on 11/18/2017.
 */

public class FileWriter {
    private static File sInternalAppFilesDir;
    public static void setAppFilesDir(File filesDir){
        sInternalAppFilesDir = filesDir;
    }

    private File mFile;
    private PrintWriter mPrintWriter;

    public FileWriter(String fileName) throws FileNotFoundException {
        this.mFile = new File(sInternalAppFilesDir, fileName);
        this.mPrintWriter = new PrintWriter(this.mFile);
    }

    public void print(String string){
        this.mPrintWriter.print(string);
    }

    public void println(String string){
        this.mPrintWriter.println(string);
    }

    public void close() throws IOException{
        this.mPrintWriter.close();
    }
}
