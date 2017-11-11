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
    private static final String LOG_TAG = Emojifier.class.getSimpleName();
    private static final double SMILING_PROB_THRESHOLD = .15;
    private static final double EYE_OPEN_PROB_THRESHOLD = .5;

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

                //Get the appropriate emoji for each face
                whichEmoji(face);
            }
        }

        //Release the detector
        detector.release();
    }

    /**
     * Method for logging classification probabilities.
     *
     * @param face The face for which you can pick an emoji.
     */
    private static void whichEmoji(Face face) {

        Log.d(LOG_TAG, "whichEmoji : smilingProb = " + face.getIsSmilingProbability());
        Log.d(LOG_TAG, "whichEmoji : leftEyeOpenProb = "
                + face.getIsLeftEyeOpenProbability());
        Log.d(LOG_TAG, "whichEmoji : rightEyeOpenProb = "
                + face.getIsRightEyeOpenProbability());


        boolean smiling = face.getIsSmilingProbability() > SMILING_PROB_THRESHOLD;
        boolean leftEyeClosed = face.getIsLeftEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;
        boolean rightEyeClosed = face.getIsRightEyeOpenProbability() < EYE_OPEN_PROB_THRESHOLD;

        //Determine and log appropriate emoji
        Emoji emoji;

        if (smiling){
            if (leftEyeClosed && !rightEyeClosed){
                emoji = Emoji.LEFT_WINK;
            } else if (rightEyeClosed & !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_SMILE;
            } else {
                emoji = Emoji.SMILE;
            }
        } else {
            if (leftEyeClosed && !rightEyeClosed){
                emoji = Emoji.LEFT_WINK_FROWN;
            } else if (rightEyeClosed && !leftEyeClosed){
                emoji = Emoji.RIGHT_WINK_FROWN;
            } else if (leftEyeClosed){
                emoji = Emoji.CLOSED_EYE_FROWN;
            } else {
                emoji = Emoji.FROWN;
            }
        }

        //Log the chosen Emoji
        Log.d(LOG_TAG, "whichEmoji : " + emoji.name());

    }

    //Enum for all possible emojis
    private enum Emoji{
        SMILE,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
    }

}
