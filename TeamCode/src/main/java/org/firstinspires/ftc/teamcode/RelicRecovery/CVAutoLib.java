package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.disnodeteam.dogecv.detectors.CryptoDetect2;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;
import org.opencv.core.Mat;

import java.util.ArrayList;

/**
 * Created by lulzbot on 2/3/18.
 */

public class CVAutoLib extends CryptoDetect2 {

    public boolean foundCenters;
    final private double power = 0.4;

    public Mat getImage(LinearOpMode opMode) {
        //Get frames from Vuforia
        Mat outputMat = new Mat();
        for (int img = 0; img < 100; img++) {
            Mat inputMat = Vuforia.getImage(opMode);
            if (inputMat != null) {
                outputMat = inputMat;
                return outputMat;
            }
        }
        return outputMat;
    }

    public ArrayList<Integer> getCenters(LinearOpMode opMode) {
        //Returns an array of centers' positions from center of the camera at 0
        Mat inputMat = getImage(opMode);
        return returnCenters(inputMat);
    }

    public ArrayList<Integer> tryCenters (LinearOpMode opMode, int trytimes, int numCols){
        //Attempts to find columns many times. Use this when you are expecting to see centers
        //Num Cols should be 2 for farther away and 1 for closer in but we may need to change this
        ArrayList<Integer> centers = new ArrayList<>();
        for (int i = 0; i < trytimes; i++) {
            centers = getCenters(opMode);
            if(centers.size()>=numCols) {
                foundCenters = true;
                Util.telemetry("tryCenters found centers", "foundCenters", true);
                return centers;
            }
        }

        foundCenters = false;
        centers.add(0);
        return centers;
    }

    public void cryptoAlign1D(LinearOpMode opMode, ArrayList<Integer> centers) throws InterruptedException {
        //Aligns with the cryptobox in one dimension.Use where you can see at least two centers of the cryptobox
        int cryptocenter = centers.get(0);
        opMode.telemetry.addData("center at ", cryptocenter);
        opMode.telemetry.update();

            if (cryptocenter > 5) {
                opMode.telemetry.addLine("center pos is greater than 2");
                opMode.telemetry.update();
                Move.strafeAngle(270, power, (int)Math.floor(1.5*cryptocenter), true);
                opMode.telemetry.addLine("moved?");
                opMode.telemetry.update();
            }

            if (cryptocenter < -5) {
                opMode.telemetry.addLine("center pos is less than -2");
                opMode.telemetry.update();
                Move.strafeAngle(90, power,(int)Math.floor(1.5*-cryptocenter), true);
                opMode.telemetry.addLine("moved?");
                opMode.telemetry.update();
            }

            Util.setAllPowers(0);
    }

    public void centerAlign(LinearOpMode opMode, int center) throws InterruptedException{
        //Aligns with one column. Use this when you are close and you can only see one center.
        //Be sure to set columnDetection to true
        opMode.telemetry.addData("center at ", center);
        opMode.telemetry.update();

        if (center > 5){
            opMode.telemetry.addLine("center pos is greater than 2");
            opMode.telemetry.update();
            Move.strafeAngle(270, power, (int)Math.floor(1.5*center), true);
            opMode.telemetry.addLine("moved?");
            opMode.telemetry.update();
        }

        if (center < -5){
            opMode.telemetry.addLine("center pos is less than -2");
            opMode.telemetry.update();
            Move.strafeAngle(90, power,(int)Math.floor(1.5*-center), true);
            opMode.telemetry.addLine("moved?");
            opMode.telemetry.update();
        }

        Util.setAllPowers(0);
    }


    //TODO: These are all extinct methods that could be beneficial later with work

//    private static float RightAmountofRed;
//    private static float RightAmountofBlue;
//
//    private static float LeftAmountofRed;
//    private static float LeftAmountofBlue;

    //Aligns with the cryptobox in 2 dimensions (while moving) use if you can initially see three columns
//    public void cryptoAlign2D(LinearOpMode opMode, ArrayList<Integer> centers) throws InterruptedException {
////        ColorSensor.init(opMode);
//        int cryptocenter = centers.get(0);
////        ColorSensor.ReadSensor(opMode);
//        int lB = Util.leftBack.getCurrentPosition();
//        int rB = Util.rightBack.getCurrentPosition();
//        int lF = Util.leftFront.getCurrentPosition();
//        int rF = Util.rightFront.getCurrentPosition();
////        while(centers.size()<3){
////            Move.strafeAngle(270,0.25,15,false);
////        }
//
////        RightAmountofRed = Color.red(ColorSensor.RightColor);
////        RightAmountofBlue = Color.blue(ColorSensor.RightColor);
////        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
////        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
////
////
////        double ratioR = RightAmountofRed/((double)RightAmountofBlue);
////        double ratioL = LeftAmountofRed/((double)LeftAmountofRed);
//
//        while (((Math.abs(Util.leftBack.getCurrentPosition() - lB)
//                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
//                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
//                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4) < dist) {            lB = Util.leftBack.getCurrentPosition();
//            rB = Util.rightBack.getCurrentPosition();
//            lF = Util.leftFront.getCurrentPosition();
//            rF = Util.rightFront.getCurrentPosition();
//            if (cryptocenter > 5) {
//                opMode.telemetry.addLine("center pos is greater than 2");
//                opMode.telemetry.update();
//                //Thread.sleep(1000);
//                //Move.strafeAngle(270, 0.3, 20, false);
//
//                long start = System.nanoTime();
//                centers = forceFindCenters(opMode);
//                if(centers.size() >= 2) {
//                    cryptocenter = centers.get(1);
//                }
//                else {
//                    cryptocenter = centers.get(0);
//                }
//                //cryptocenter = centers.get(0);
//                lastLoc = cryptocenter;
//                long stop = System.nanoTime();
//                opMode.telemetry.addData("Time is ",(stop-start)/1000000.0);
//                opMode.telemetry.update();
//                Thread.sleep(5000);
//                Move.strafeAngle(180+(cryptocenter/angleChange),0.2);
//                opMode.telemetry.addLine("moved?");
//                opMode.telemetry.update();
//                //Thread.sleep(1000);
//            }
//
//            if (cryptocenter < -5) {
//                opMode.telemetry.addLine("center pos is less than -2");
//                opMode.telemetry.update();
//                //Thread.sleep(1000);
//                //Move.strafeAngle(90, 0.3, 20, false);
//                long start = System.nanoTime();
//                centers = forceFindCenters(opMode);
//                if(centers.size() >= 2) {
//                    cryptocenter = centers.get(1);
//                }
//                else {
//                    cryptocenter = centers.get(0);
//                }
//                //cryptocenter = centers.get(0);
//                lastLoc = cryptocenter;
//                long stop = System.nanoTime();
//                opMode.telemetry.addData("Time is ",(stop-start)/1000000.0);
//                opMode.telemetry.update();
//                Thread.sleep(5000);
//                Move.strafeAngle(180-(cryptocenter/angleChange),0.2);
//                opMode.telemetry.addLine("moved?");
//                opMode.telemetry.update();
//                //Thread.sleep(1000);
//            }
//
//            if (cryptocenter >= -5 && cryptocenter <= 5) {
//                Move.strafeAngle(180,0.2);
//            }
//
////            ColorSensor.ReadSensor(opMode);
////            RightAmountofRed = Color.red(ColorSensor.RightColor);
////            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
////            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
////            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
////
////            ratioR = RightAmountofRed/((double)RightAmountofBlue);
////            ratioL = LeftAmountofRed/((double)LeftAmountofRed);
//        }
//
//        Util.setAllPowers(0);
//
//
//        // TODO: 2/21/18 Add a gyro fix for the angle here
//    }
    //if we don't see pillars, decrease the threshold until we do
//    public ArrayList<Integer> forceFindPillars(LinearOpMode opMode) {
//        ArrayList<Integer> centers;
//        centers = getPillars(opMode);
//        //for(int f=0;f<100;f++){
//        for (int t = 0; t < 20; t++) {
//            while (centers.size() == 0) {
//                centers = getCenters(opMode);
//                meanthreshold -= 1;
//                if (meanthreshold <= 5) {
//                    foundCenters = false;
//                    ArrayList<Integer> fakePillars = new ArrayList<>();
//                    fakePillars.add(lastLoc);
//                    centers = fakePillars;
//                }
//            }
//        }
//        return centers;
//    }

    // Need tpo adjust this to a lower threshold.
    //Aligns with a center of the cryptobox NOT FUNCTIONAL YET
    // TODO: 2/21/18 Make a peak class that lets us see the "best" centers

    //Takes an arraylist (pillars or columns and finds the one closest to the middle
//    private int closestToZero(ArrayList<Integer> centers) {
//        int closest = 0;
//        for (int c = 0; c < centers.size(); c++) {
//            int distance = Math.abs(0 - centers.get(c));
//            if (distance < 20) {
//                closest = centers.get(c);
//            }
//        }
//        return closest;
//    }

    //if we don't see centers, decrease the threshold until we do
//    public ArrayList<Integer> forceFindCenters(LinearOpMode opMode) {
//        ArrayList<Integer> centers;
//        centers = getCenters(opMode);
//        //for(int f=0;f<100;f++){
//        for (int t = 0; t < 20; t++) {
//            while (centers.size() == 0) {
//                centers = getCenters(opMode);
//                meanthreshold -= 1;
//                if (meanthreshold <= 0) {
//                    opMode.telemetry.addLine("TOO LOW");
//                    centers.add(lastLoc);
//                }
//            }
//        }
//        return centers;
//    }
//    public ArrayList<Integer> getPillars(LinearOpMode opMode) {
//        Mat inputMat = getImage(opMode);
//        return returnPillars(inputMat);
//    }
}
