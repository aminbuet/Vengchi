package com.azan.android.vengchi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by Md Aminur Rahman on 11/11/17.
 */

public class Emojifier {
    private  static final String LOG_TAG = Emojifier.class.getSimpleName();

    /**
     * Method for detecting faces in a bitmap.
     *
     * @param context The application context.
     * @param picture The picture in which to detect the faces.
     */
    static  void detectFaces(Context context, Bitmap picture){
        //Create face detector, disable tracking and enable classification
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        //Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        //Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        //Log the number of faces
        Log.d(LOG_TAG, "detectedFaces: number of faces = " + faces.size());

        //If there are no face detected , show a Toast message
        if (faces.size() == 0){
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < faces.size(); ++i){
                Face face = faces.valueAt(i);

                //Log the classification probabilities for each face
                getClassifications(face);
            }
        }

        //Release the detector
        detector.release();
    }

    /**
     * Method for logging classification probabilities.
     *
     * @param face The face to get the classification probabilities.
     */
    private static void getClassifications(Face face) {

        Log.d(LOG_TAG, "getClassifications : smilingProb = " + face.getIsSmilingProbability());
        Log.d(LOG_TAG, "getClassifications : leftEyeOpenProb = " + face.getIsLeftEyeOpenProbability());
        Log.d(LOG_TAG, "getClassifications : rightEyeOpenProb = " + face.getIsRightEyeOpenProbability());
    }
}
