package ece751.wisc.edu.detectpin;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    Button mLearningBtn;
    Button mRecognizerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLearningBtn = (Button) findViewById(R.id.btn_learning);
        mRecognizerBtn = (Button) findViewById(R.id.btn_recognizer);

        mLearningBtn.setOnClickListener(this);
        mRecognizerBtn.setOnClickListener(this);

        // FILEWRITER INITIALIZATION HERE
        FileWriter.setAppFilesDir(this.getFilesDir());
//        FileWriter.setAppFilesDir(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES));
    }

    @Override
    public void onClick(View v) {
        if(v == mLearningBtn){
            Intent intent = new Intent(this, LearningActivity.class);
            startActivity(intent);
        } else if(v == mRecognizerBtn){
            Intent intent = new Intent(this, RecognizerActivity.class);
            startActivity(intent);
        } else {
            // Do nothing
        }
    }
}
