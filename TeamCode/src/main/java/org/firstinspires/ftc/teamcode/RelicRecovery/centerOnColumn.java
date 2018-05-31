package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

import java.util.ArrayList;

/**
 * Created by lulzbot on 3/2/18.
 */

public class centerOnColumn{


    private centerOnColumn() throws Exception{
        throw new Exception();
    }

    public static void getToCenterRed(CVAutoLib cryptoDetect, LinearOpMode opMode) throws InterruptedException {
        ArrayList<Integer> centers;

        //IDK if time can be cut out talk to Joe

//        telemetry.addData("centers at ", centers);
//        telemetry.update();
        //Thread.sleep(500);
        centers = cryptoDetect.tryCenters(opMode, 10, 1);
        Util.telemetry("this many centers ", centers.size(), true);
        if(!cryptoDetect.foundCenters){
            Move.moveUntilRedPID(180, 0.2, opMode);
            Util.telemetry("Vision didn't work", 0, true);
            Thread.sleep(1000000); //until end of opmode
        }
        else if (cryptoDetect.foundCenters) {
            Util.telemetry("foundcenters is positive", centers.size(), true);
            cryptoDetect.cryptoAlign1D(opMode, centers);
            Move.strafeAngle(180, 0.3, 200, false);
            //centers = cryptoDetect.tryCenters(opMode, 10, 1);
//            Util.telemetry("this many centers", centers.size(), true);
//            if(!cryptoDetect.foundCenters){
//                Move.moveUntilRedPID(180, 0.2, opMode);
//                Util.telemetry("Vision didn't work", 0, true);
//                Thread.sleep(1000000); //until end of opmode
//            }

//        telemetry.addData("centers at ", centers);
//        telemetry.update();
//        //Thread.sleep(500);
            //cryptoDetect.cryptoAlign1D(opMode, centers);
            Move.moveUntilRedPID(180, 0.2, opMode);
            Thread.sleep(100);
            Move.strafeAngle(0, 0.2, 50, true);
        }
        else {
            Move.moveUntilRedPID(180, 0.2, opMode);
            Util.telemetry("Vision didn't work", 0, true);
            Thread.sleep(1000000); //until end of opmode

        }
    }

    public static void getToCenterBlue(CVAutoLib cryptoDetect, LinearOpMode opMode) throws InterruptedException {
        ArrayList<Integer> centers;

        //IDK if time can be cut out talk to Joe

//        telemetry.addData("centers at ", centers);
//        telemetry.update();
        //Thread.sleep(500);
        centers = cryptoDetect.tryCenters(opMode, 10, 1);

        if(!cryptoDetect.foundCenters){
            Move.moveUntilBluePID(180, 0.2, opMode);
            Util.telemetry("Vision didn't work (didn't find centers)", 0, true);
            Thread.sleep(1000000); //until end of opmode
        }
        else if (cryptoDetect.foundCenters) {
            Util.telemetry("foundcenters is positive", centers.size(), true);
            cryptoDetect.cryptoAlign1D(opMode, centers);
            //Move.strafeAngle(180, 0.3, 200, true);
//            centers = cryptoDetect.tryCenters(opMode, 10, 2);
//            Util.telemetry("this many centers", centers.size(), true);
//            if(!cryptoDetect.foundCenters){
//                Move.moveUntilBluePID(180, 0.2, opMode);
//                Util.telemetry("Vision didn't work", 0, true);
//                Thread.sleep(1000000); //until end of opmode
//            }

//        telemetry.addData("centers at ", centers);
//        telemetry.update();
//        //Thread.sleep(500);
           // cryptoDetect.cryptoAlign1D(opMode, centers);
            Move.moveUntilBluePID(180, 0.2, opMode);
            Thread.sleep(100);
            Move.strafeAngle(0, 0.2, 50, true);
        }
        else {
            Move.moveUntilBluePID(180, 0.2, opMode);
            Util.telemetry("Vision didn't work", 0, true);
            Thread.sleep(1000000); //until end of opmode

        }
    }
    public static void getToRightRED(CVAutoLib cryptoDetect, LinearOpMode opMode) throws InterruptedException {
        getToCenterRed(cryptoDetect, opMode);
        Move.strafeAngle(-90, 0.4, 275, true);
    }

    public static void getToLeftRED(CVAutoLib cryptoDetect, LinearOpMode opMode) throws InterruptedException {
        getToCenterRed(cryptoDetect, opMode);
        Move.strafeAngle(90, 0.4, 275, true);

    }

    public static void getToRightBLUE(CVAutoLib cryptoDetect, LinearOpMode opMode) throws InterruptedException {
        getToCenterBlue(cryptoDetect, opMode);
        Move.strafeAngle(-90, 0.4, 310, true);

    }

    public static void getToLeftBLUE(CVAutoLib cryptoDetect, LinearOpMode opMode) throws InterruptedException {
        getToCenterBlue(cryptoDetect, opMode);
        Move.strafeAngle(90, 0.4, 325, true);

    }




}
