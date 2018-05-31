package org.firstinspires.ftc.teamcode;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Arrays;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

import java.util.HashSet;
import java.util.List;
/**
 * Created by lulzbot on 1/7/18.
 */

public class columnDetector {

    /*public Mat crypto_mask(Mat image){
        List<Mat> channels = new ArrayList<>();
        Core.split(image, channels);

        List<Mat> colorThreshold1 = new ArrayList<>();
        Imgproc.threshold(channels.get(0), colorThreshold1.get(0),100,255,Imgproc.THRESH_BINARY );
        Imgproc.threshold(channels.get(1), colorThreshold1.get(0),0,70,Imgproc.THRESH_BINARY );
        Imgproc.threshold(channels.get(2), colorThreshold1.get(0),0,70,Imgproc.THRESH_BINARY );

        List<Mat> colorThreshold2 = new ArrayList<>();
        Imgproc.threshold(channels.get(0), colorThreshold2.get(0),80,255,Imgproc.THRESH_BINARY );
        Imgproc.threshold(channels.get(1), colorThreshold2.get(0),0,20,Imgproc.THRESH_BINARY );
        Imgproc.threshold(channels.get(2), colorThreshold2.get(0),0,20,Imgproc.THRESH_BINARY);

        List<Mat> colorThreshold3 = new ArrayList<>();
        Imgproc.threshold(channels.get(0), colorThreshold3.get(0),170,255,Imgproc.THRESH_BINARY );
        Imgproc.threshold(channels.get(1), colorThreshold3.get(0),0,150,Imgproc.THRESH_BINARY );
        Imgproc.threshold(channels.get(2), colorThreshold3.get(0),0,150,Imgproc.THRESH_BINARY);

        /* Need to add these python lines here
        line_image = np.zeros_like(image)
        line_image[color_threshold1 | color_threshold2 | color_threshold3] = [255, 255, 255]
        return line_image

        I can create a 3D array with all zeroes, but I don't know how to check the colorThresholds
        against the data in the Mat */

    //}

    //public Mat locateCrypto(Mat image, double threshold){
        //Mat maskedImage = crypto_mask(image);  //Somehow splice 200 pixels off the bottom
        //Mat grayImage = new Mat();
        //Imgproc.cvtColor(maskedImage, grayImage, Imgproc.COLOR_RGB2GRAY);

        
    //}





}
