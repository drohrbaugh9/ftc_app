package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/4/18.
 */




public class DistanceSensorLineUp {

    protected static double distance;
    protected static double distanceToColumn;
    protected static double moveExtraDistance;
    protected static double sleepTime;
    private static double initialTime;
    private static double elapsedTime;


    public static void lineUpRed(boolean multiGlyph) throws InterruptedException {

        Util.distanceSensorArm.setPosition(Util.distanceSensorArmDown);

        if (multiGlyph){
            Move.strafeAngle(0, 0.2, 50, true);
        }
        else {
            Thread.sleep(200);
        }

        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        Util.telemetry("distance", distance, true);

        if (Double.isNaN(distance)) {
            distance = 100;
        }

        Util.telemetry("distance", distance, true);
        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        if (distance > 16){
            Move.strafeAngle(180, 0.18);
            initialTime = System.nanoTime();
            while (distance > 16){
                Thread.sleep(10);
                elapsedTime = System.nanoTime() - initialTime;
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
                if (elapsedTime > 1500000000L){ //1.5 second
                    Move.strafeAngle(0, 0.18, 50, true);
                    Move.strafeAngle(-90, 0.3);
                    break;
                }
            }
        }

        //????????????????
       // if (distance > 16)
        //????????????????

        distanceToColumn = (Math.abs(Util.leftBack.getCurrentPosition() - lB)
                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4;
        Util.telemetry("distance to column", distanceToColumn, true);

        if(distanceToColumn > 250){
            distanceToColumn = 250;
        }

        Thread.sleep(50);

        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);

        while (Double.isNaN(distance)) {
            Thread.sleep(10);
            distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        }
        //moveExtraDistance = 15 * (1/4) * (10 - distance);
        //Move.strafeAngle(180, 0.18, (int)Math.round(moveExtraDistance), true);
        if(distance > 10){
            distance = 10;
        }
        if(distance < 6){
            distance = 6;
        }

        sleepTime = (20 * (10 - distance));
        Thread.sleep((int)sleepTime);
        Util.setAllPowers(0);
        Thread.sleep(500);
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        if(distance > 8) {
            Move.strafeAngle(-90, 0.3);
            while (distance > 8) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
            }
        }
        else if(distance <  7) {
            Move.strafeAngle(90, 0.3);
            while (distance < 7) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 1;
                }
            }
        }




       Util.setAllPowers(0);
    }

    public static void lineUpBlue(boolean multiGlyph) throws InterruptedException {
        Util.distanceSensorArm.setPosition(Util.distanceSensorArmDown);
        if (multiGlyph){
            Move.strafeAngle(0, 0.2, 50, true);
        }
        else {
            Thread.sleep(200);
        }
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        Util.telemetry("distance", distance, true);
        if (Double.isNaN(distance)) {
            distance = 100;
        }
        Util.telemetry("distance", distance, true);
        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();
        if (distance > 16){
            Move.strafeAngle(180, 0.18);
            initialTime = System.nanoTime();
            while (distance >16){
                Thread.sleep(10);
                elapsedTime = System.nanoTime() - initialTime;
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
                if (elapsedTime > 1500000000L){ //1.5 second
                    Move.strafeAngle(0, 0.18, 50, true);
                    Move.strafeAngle(-90, 0.3);
                    break;
                }
            }
        }

        distanceToColumn = (Math.abs(Util.leftBack.getCurrentPosition() - lB)
                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4;
        Util.telemetry("distance to column", distanceToColumn, true);

        if(distanceToColumn > 250){
            distanceToColumn = 250;
        }

        Thread.sleep(50);
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        //moveExtraDistance = 15 * (1/4) * (10 - distance);
        //Move.strafeAngle(180, 0.18, (int)Math.round(moveExtraDistance), true);
        while (Double.isNaN(distance)) {
            Thread.sleep(10);
            distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        }

        if(distance > 10){
            distance = 10;
        }
        if(distance < 6){
            distance = 6;
        }
        sleepTime = (20 * (10 - distance));
        Thread.sleep((int)sleepTime);
        Util.setAllPowers(0);
        Thread.sleep(500);
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        if(distance > 9.5) {
            Move.strafeAngle(-90, 0.3);
            while (distance > 9.5) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
            }
        }
        else if(distance <  8.5) {
            Move.strafeAngle(90, 0.3);
            while (distance < 8.5) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 1;
                }
            }
        }


        Util.setAllPowers(0);
    }


    public static void lineUpWithHeading(boolean multiGlyph, double heading) throws InterruptedException {
        Util.distanceSensorArm.setPosition(Util.distanceSensorArmDown);
        if (multiGlyph){
            Move.strafeAngleWithHeading(0, 0.2, 50, true, heading);
        }
        else {
            Thread.sleep(200);
        }
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        Util.telemetry("distance", distance, true);
        if (Double.isNaN(distance)) {
            distance = 100;
        }
        Util.telemetry("distance", distance, true);
        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();
        if (distance > 16){
            Move.strafeAngleWithHeading(180, 0.18, heading);
            initialTime = System.nanoTime();
            while (distance >16){
                Thread.sleep(10);
                elapsedTime = System.nanoTime() - initialTime;
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
                if (elapsedTime > 1500000000L){ //1.5 second
                    Move.strafeAngleWithHeading(0, 0.18, 50, true, heading);
                    Move.strafeAngleWithHeading(-90, 0.3, heading);
                    break;
                }
            }
        }
        distanceToColumn = (Math.abs(Util.leftBack.getCurrentPosition() - lB)
                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4;
        Util.telemetry("distance to column", distanceToColumn, true);
        if(distanceToColumn > 250){
            distanceToColumn = 250;
        }
        Thread.sleep(100);
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        //moveExtraDistance = 15 * (1/4) * (10 - distance);
        //Move.strafeAngle(180, 0.18, (int)Math.round(moveExtraDistance), true);
        while (Double.isNaN(distance)) {
            Thread.sleep(10);
            distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        }

        if(distance > 10){
            distance = 10;
        }
        if(distance < 6){
            distance = 6;
        }
        sleepTime = (20 * (10 - distance));
        Thread.sleep((int)sleepTime);
        Util.setAllPowers(0);
        Thread.sleep(300);
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        if(distance > 8) {
            Move.strafeAngleWithHeading(-90, 0.3, heading);
            while (distance > 8) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
            }
        }
        else if(distance <  7) {
            Move.strafeAngleWithHeading(90, 0.3, heading);
            while (distance < 7) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 1;
                }
            }
        }




        Util.setAllPowers(0);
    }
    public static void lineUpBlueHeading(boolean multiGlyph, double heading) throws InterruptedException {
        Util.distanceSensorArm.setPosition(Util.distanceSensorArmDown);
        if (multiGlyph){
            Move.strafeAngleWithHeading(0, 0.2, 50, true, heading);
        }
        else {
            Thread.sleep(200);
        }
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        Util.telemetry("distance", distance, true);
        if (Double.isNaN(distance)) {
            distance = 100;
        }
        Util.telemetry("distance", distance, true);
        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();
        if (distance > 16){
            Move.strafeAngleWithHeading(180, 0.18, heading);
            initialTime = System.nanoTime();
            while (distance >16){
                Thread.sleep(10);
                elapsedTime = System.nanoTime() - initialTime;
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
                if (elapsedTime > 1500000000L){ //1.5 second
                    Move.strafeAngleWithHeading(0, 0.18, 50, true, heading);
                    Move.strafeAngleWithHeading(-90, 0.3, heading);
                    break;
                }
            }
        }
        distanceToColumn = (Math.abs(Util.leftBack.getCurrentPosition() - lB)
                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4;
        Util.telemetry("distance to column", distanceToColumn, true);
        if(distanceToColumn > 250){
            distanceToColumn = 250;
        }
        Thread.sleep(50);
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        //moveExtraDistance = 15 * (1/4) * (10 - distance);
        //Move.strafeAngle(180, 0.18, (int)Math.round(moveExtraDistance), true);
        while (Double.isNaN(distance)) {
            Thread.sleep(10);
            distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        }

        if(distance > 10){
            distance = 10;
        }
        if(distance < 6){
            distance = 6;
        }
        sleepTime = (20 * (10 - distance));
        Thread.sleep((int)sleepTime);
        Util.setAllPowers(0);
        Thread.sleep(500);
        distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
        if(distance > 9.5) {
            Move.strafeAngleWithHeading(-90, 0.3, heading);
            while (distance > 9.5) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 100;
                }
            }
        }
        else if(distance <  8.5) {
            Move.strafeAngleWithHeading(90, 0.3, heading);
            while (distance < 8.5) { //can tune (lowest is like 5 something)
                Thread.sleep(10);
                distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
                Util.telemetry("Distance to column", distance, true);
                if (Double.isNaN(distance)) {
                    distance = 1;
                }
            }
        }


        Util.setAllPowers(0);
    }

}
