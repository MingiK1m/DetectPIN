package ece751.wisc.edu.detectpin;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * Created by ksmk9 on 12/7/2017.
 */

public class WekaInstance {
    private final Classifier mClassifier;

    public WekaInstance(String classificationModelFilepath) throws Exception {
        mClassifier = (Classifier) SerializationHelper.read(classificationModelFilepath);
    }

    /**
     *
     * @param singleUnlabeledARFFFilepath filepath for single unlabeled arff
     * @return null if there is any errors, string of class if it classified successfully
     */
    public String OnlinePrediction(String singleUnlabeledARFFFilepath){
        String classStr = null;
        try {
            Instances unlabeled = new Instances(
                    new BufferedReader(new FileReader(FileWriter.sInternalAppFilesDir.getAbsolutePath() + "/" + singleUnlabeledARFFFilepath)));

            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
            double clsLabel = mClassifier.classifyInstance(unlabeled.instance(0));

            classStr = unlabeled.classAttribute().value((int) clsLabel);
        } catch(Exception e){
            Log.e("WekeInstance", e.toString());
        }
        return classStr;
    }
}
