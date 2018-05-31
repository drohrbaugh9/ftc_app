package com.disnodeteam.dogecv.detectors;

import android.util.Log;

import com.disnodeteam.dogecv.OpenCVPipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


/**
 * Created by lulzbot on 2/16/18.
 */

public class BallDetect extends OpenCVPipeline{




    // Returns 1 for red, 2 for blue and 0 if it cannot decide
    public int findColor(Mat inputMat) {
        final double confidence = 1.1;
        final int minColor = 50;

        final int imx = inputMat.width();
        final int imy = inputMat.height();


//        crop to the region of interest
        Rect roi = new Rect(0, imy / 2, imx / 4, imy / 2);
        inputMat = new Mat(inputMat, roi);

        Scalar rgbMeans = Core.mean(inputMat);
        final double redMean = rgbMeans.val[0];
        final double greenMean = rgbMeans.val[1];
        final double blueMean = rgbMeans.val[2];

        Log.v("RGB ", "are" + redMean + " " + greenMean + " " + blueMean);
        if (redMean > (confidence * blueMean) && redMean > minColor) {
            Log.v("Color Status", "Ball is red");
            return 1;
        } else if (blueMean > (confidence * redMean) && blueMean > minColor) {
            Log.v("Color Status", "Ball is blue");
            return 2;
        } else {
            Log.v("Color Status", "Cannot Decide Ball Color");
            return 0;
        }
    }


    @Override
    public Mat processFrame(Mat inputC, Mat inputGray){
        return inputC;
    }

}
