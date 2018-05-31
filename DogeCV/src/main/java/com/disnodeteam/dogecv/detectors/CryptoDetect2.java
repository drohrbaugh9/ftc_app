package com.disnodeteam.dogecv.detectors;


import android.content.IntentFilter;
import android.util.Log;

import com.disnodeteam.dogecv.OpenCVPipeline;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// You will need these if writing to file
//import android.os.Environment;
//import android.util.Log;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;

public class CryptoDetect2 extends OpenCVPipeline{

    /////////////////////////////////////////////////
    // EDIT THESE PARAMETERS

    public boolean redDetection = true;
    public boolean columnDetection = true;
    private final int cropsize = 200; // How much to crop off the bottom of the image

            ;  // peaks will only be accepted above this threshold
    public double meanthreshold = 35;  // peaks will only be accepted above this threshold
    private final double alpha1 = 0.05; // Exponential smoothing parameter
    public int numsmooths = 2;       // number of times to apply exponential smoothing
//    private final int detectTimes = 5; //Number of detection iterations to compare data to
//    private final int errorMargin = 20; //Error marin for the difference between old data and new detections
    // storing information from previous detections


    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
//    static Mat prevState;
//    private Mat state;
    //Combines detection and previous knowledge to make accurate predictions where columns are



    private ArrayList<Integer> findPeaks(int imy, double[] ps) {
        // imy is length of array ps

        // intialize memory for our result
        //Pillars is the pixel location for the columns; peaks is the actual values for them
        ArrayList<Integer> pillars = new ArrayList<>();
//        ArrayList<peak> pillars = new ArrayList<>();
//        ArrayList<Integer> pillarPos = new ArrayList<>();
        //ArrayList<Double> peaks = new ArrayList<>();

        // check if left margin is a peak
        int state = 0;
        if (ps[0] > ps[1]) {
            if (ps[0] > meanthreshold) {
                if (ps[1] > ps[0]){
                    state = 1; // we are going uphill
                }
                if (ps[1] < ps[0]){
                    state = -1; // we are going downhill
                    pillars.add(0);
                }
//                peak peak = new peak();
//                peak.setValues(0,ps[0]);
//                pillars.add(peak);
            }
        } else if (ps[0] < ps[1]) {
            state = 1; // we are going uphill
        }
        // Now, for each pixel in the image
        for (int ii = 1; ii < imy; ii++) {
            if (ps[ii] > meanthreshold) {
                if (ps[ii] > ps[ii - 1]) {
                    state = 1;
                } else if ((ps[ii] < ps[ii - 1])) {
                    if (state >= 0) {
                        state = -1;
//                        peak peak = new peak();
//                        peak.setValues(ii-1,ps[ii-1]);
//                        pillars.add(peak);
                        pillars.add(ii - 1);

                    }
                } else {
                    state = 0;
                }
            }
        }
        // Check the last pixel
        if (ps[imy - 1] > meanthreshold && ps[imy - 1] > ps[imy - 2]) {
//            peak peak = new peak();
//            peak.setValues(imy-1,ps[imy-1]);
//            pillars.add(peak);

            pillars.add(imy - 1);
        }
        Log.v("Columns at", ": "+pillars);

        //If we're in column specific detection we try to get two pillars and we want the maximum pillars
//        if(columnDetection){
//            //if we have more than two pillars
//            if(pillars.size()>2){
//                ArrayList<Integer> maxPeaks = new ArrayList<>(pillars.size());
                //maxPeaks(Comparator.comparing(item->-item.); //NOTE: the minus sign sorts descending
//
//                //Find max position twice
//                for(int t=0;t<2;t++){
//                    int xPos = 0;
//                    int peakPos = 0;
//                    double max = pillars.get(0).value;
//                    for(int pk=0;pk<pillars.size();pk++) {
//                        //peaks and pillars should have the same size
//                        if (pillars.get(pk).value >= max) {
//                            max = pillars.get(pk).value;
//                            xPos = pillars.get(pk).pos;
//                            peakPos = pk;
//                        }
//                    }
//                    pillars.remove(peakPos);
//                    maxPeaks.add(xPos);
//                }
//                pillarPos = maxPeaks;
//                Log.v("Max Pillars are at",": "+pillarPos);
//            }
//        }
//        else{
//            for(int x=0;x<pillars.size();x++){
//                pillarPos.add(pillars.get(x).pos);
//            }
//            Log.v("Pillars at",":::::"+pillarPos);     return pillarPos;
//            }
//        }
        //return pillars;

        return pillars;
    }

    //Very simple exponential smoothing algorithm
    private void smoother1d(final int n, double[] arr){
        final double alpha0 = 1.0 - alpha1;

        for(int ii=1; ii<n; ii++){
            arr[ii]= alpha1*arr[ii] + alpha0*arr[ii-1];
        }
        for(int ii=n-2; ii>=0; ii--){
            arr[ii] = alpha1*arr[ii] + alpha0*arr[ii+1];
        }
    }

    private Mat maskImage(Mat rgba) {

        // get info about this image
        final int imx = rgba.width();
        final int imy = rgba.height();

//        crop to the region of interest
        Rect roi = new Rect(cropsize, 0, imx-cropsize, imy);
        Mat rgbac = new Mat(rgba,roi);

        // split the image into component colors
        List<Mat> rgbimage = new ArrayList<>();
        Core.split(rgbac, rgbimage);

        // Threshold each of the color channels
        Mat rt = new Mat();
        Mat gt = new Mat();
        Mat bt = new Mat();
        Mat temp = new Mat();
        Mat ct1 = new Mat();
        Mat ct2 = new Mat();
        Mat ct3 = new Mat();
        Mat ct = new Mat();

        if(redDetection){
            //Red Cryptobox mask
            Imgproc.threshold(rgbimage.get(0), rt, 100, 255, Imgproc.THRESH_BINARY);
            Imgproc.threshold(rgbimage.get(1), gt, 70, 255, Imgproc.THRESH_BINARY);
            Imgproc.threshold(rgbimage.get(2), bt, 70, 255, Imgproc.THRESH_BINARY_INV);
            Core.bitwise_and(gt, bt, temp);
            Core.bitwise_and(temp, rt, ct1);
            Imgproc.threshold(rgbimage.get(0), rt, 80, 255, Imgproc.THRESH_BINARY);
            Imgproc.threshold(rgbimage.get(1), gt, 20, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(2), bt, 20, 255, Imgproc.THRESH_BINARY_INV);
            Core.bitwise_and(gt, bt, temp);
            Core.bitwise_and(temp, rt, ct2);
            Imgproc.threshold(rgbimage.get(0), rt, 170, 255, Imgproc.THRESH_BINARY);
            Imgproc.threshold(rgbimage.get(1), gt, 150, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(2), bt, 150, 255, Imgproc.THRESH_BINARY_INV);
            Core.bitwise_and(gt, bt, temp);
            Core.bitwise_and(temp, rt, ct3);
        }else {
            //Blue cryptobox mask
            //This was rather hasty experiment for a better one
            Imgproc.threshold(rgbimage.get(0), rt, 55, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(1), gt, 55, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(2), bt, 60, 255, Imgproc.THRESH_BINARY);
            Core.bitwise_and(gt, bt, temp);
            Core.bitwise_and(temp, rt, ct1);
            Imgproc.threshold(rgbimage.get(0), rt, 35, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(1), gt, 35, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(2), bt, 40, 255, Imgproc.THRESH_BINARY);
            Core.bitwise_and(gt, bt, temp);
            Core.bitwise_and(temp, rt, ct2);
            Imgproc.threshold(rgbimage.get(0), rt, 40, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(1), gt, 40, 255, Imgproc.THRESH_BINARY_INV);
            Imgproc.threshold(rgbimage.get(2), bt, 70, 255, Imgproc.THRESH_BINARY);
            Core.bitwise_and(gt, bt, temp);
            Core.bitwise_and(temp, rt, ct3);
        }

        Core.bitwise_or(ct1, ct2, temp);
        Core.bitwise_or(temp, ct3, ct);

        return ct;
    }



    private ArrayList<Integer> findPillars(Mat mask){
        // mask is cropped, and has only one component

        // Get info about this image
        final int imy = mask.height();

        // Compute row sums
        Mat rowsum = new Mat();
        Core.reduce(mask, rowsum, 1, Core.REDUCE_AVG);
        //dim: 0 means that the matrix is reduced to a single row.
        // 1 means that the matrix is reduced to a single column.

        // Next, convert the rowsum to an array of doubles
        Mat rowsum16 = rowsum.clone();    // setup memory for rowsum16
        rowsum.convertTo(rowsum16, CvType.CV_64FC3);     // convert to double (8 bytes)
        double[] ps = new double[imy];
        rowsum16.get(0,0,ps);       // put the pixel values into the double array

        // Smooth the array of rowsums
        for(int ss=0;ss<numsmooths;ss++){
            smoother1d(imy,ps);     // smooths the ps array in place
        }
//        final double rowMax = Core.minMaxLoc(rowsum).minVal;
//        //Log.v("MAX COLUMN","Our max value is:"+rowMax);

        // Now, we find peaks

        return findPeaks(imy,ps);


        // OR USE THE CODE BELOW TO WRITE TO FILE
//        ArrayList<Integer> peaks = findPeaks(imy,ps);  // find the peaks in array ps[]

//        // Output to file for debugging
//        ByteBuffer bb = ByteBuffer.allocate(ps.length * 8);
//        for(double d : ps) {
//            bb.putDouble(d);
//        }
//
//        try {
//            File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/CRYPTOdata/");
//            final boolean result = dir.mkdir();
//            if(result) {
//                Log.v("PILLARS","Created Directory for file output");
//            }
//            File file = new File(dir, "crypto.raw");
//            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true));
//            dos.writeInt(imy);
//            dos.write(bb.array());
//            dos.writeInt(peaks.size());
//            for(double d : peaks) {
//                dos.writeDouble(d);
//            }
//            dos.close();
//        }
//        catch (IOException e) {
//            Log.w("write log", e.getMessage(), e);
//        }
//        return peaks;

    }

//    private ArrayList<Integer> trackPils(ArrayList<Integer> detectedPils){
//
//    }




    private ArrayList<Integer> findCenters(ArrayList<Integer> pillars) {
        ArrayList<Integer> centers = new ArrayList<>();
        if (pillars.size() > 1) {
            for (int plr = 0; plr < pillars.size()-1; plr++) {
                //Log.v("ON", "Pillar number" +pillars.get(plr)+ ". This is the "+plr+" pillar");
                Log.v("Column length is ", ":" + (pillars.get(plr+1)-pillars.get(plr)));
                centers.add((pillars.get(plr) + pillars.get(plr+1))/2);
                //Find the average between columns (centers)
            }
        }
        Log.v("centers at", ":" + centers);
        return centers;
    }

    @Override
    public Mat processFrame(Mat inputMat, Mat inputGray){
        // This returns an annotated image for testing and debugging

        final int originalwidth = inputMat.width();

        final Mat mask = maskImage(inputMat);

        ArrayList<Integer> pillars = findPillars(mask);
        ArrayList<Integer> centers = findCenters(pillars);

        // Here is an example of how to do easy Logging for debugging purposes.
        //Log.v("PILLARS VS CENTERS","Found pillars at"+pillars+" and centers at"+centers);


        // Annotate the image with the pillars.
        // Choose original image, or masked image.

        // plot the pillars on the masked image
//        Imgproc.resize(mask,mask,inputMat.size());
//        final int imagewidth = mask.cols();
//        for (int pil = 0; pil < pillars.size(); pil++) {
//            Imgproc.line(mask, new Point(0, pillars.get(pil)), new Point(originalwidth, pillars.get(pil)), new Scalar(255, 255, 255), 10);
//        }
//        return mask;


//         plot the pillars on the original image
        for (int pil = 0; pil < pillars.size(); pil++) {
            Imgproc.line(inputMat, new Point(0, pillars.get(pil)), new Point(originalwidth, pillars.get(pil)), new Scalar(0, 0, 255), 10);
        }
        for(int cent = 0; cent<centers.size();cent++){
            Imgproc.circle(inputMat, new Point(originalwidth/2, centers.get(cent)), 5, new Scalar(0,255, 0), 10);
        }

        return inputMat;


    }

    public ArrayList<Integer> returnPillars(Mat inputMat){
        // Returns image coordinates of the pillars
        // Coordinates are in the interval [-inputMat.height()/2 , inputMat.height()/2 ]
        //Log.v("IN LOOP","POSITIVE");
        final Mat mask = maskImage(inputMat);
        ArrayList<Integer>  pillars = findPillars(mask);

        // shift the origin to the center
        final int offset = inputMat.height()/2;
        for (int ii = 0; ii < pillars.size(); ii++) {
            pillars.set(ii, pillars.get(ii)-offset);
        }
        Log.v("PILLARS",":"+pillars);
        return pillars;
    }

    public ArrayList<Integer> returnCenters(Mat inputMat){
        //Returns locations of centers of pillars
        ArrayList<Integer> pillars = returnPillars(inputMat);
        ArrayList<Integer> centers = findCenters(pillars);
        Log.v("CENTERS",":"+centers);
        return centers;
    }
}



