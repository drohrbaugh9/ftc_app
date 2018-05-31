package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by elliot on 3/17/18.
 */

public class IntakeControl {
    static int red;
    static int green;
    static int blue;
    static int alpha;
    static double distance;

    static int rightIntakePos;
    static int leftIntakePos;
    static int lastRightIntakePos;
    static int lastLeftIntakePos;

    static double rightIntakeDiff;
    static double leftIntakeDiff;

    static double deltaRight;
    static double deltaLeft;

    static double lowestDeltaRight;
    static double lowestDeltaLeft;

    static double rightIntakeSum;
    static double leftIntakeSum;

    static double MOVING_AVERAGE_LENGTH = 20, MEASURING_INTERVAL = 10;

    static boolean canCount;

    static Queue<Double> rightIntakeQueue, leftIntakeQueue;
    static boolean queueClear;
    static boolean queueFill;

    static long timeSum;

    static long currentTime;
    static long oldTime;

    public static void init() throws InterruptedException {
        Util.resetEncoders();
        fillQueue();
        getGlyphs.glyphCount = 0;
        canCount = true;
        lowestDeltaLeft = 30;
        lowestDeltaRight = 30;
        oldTime = System.nanoTime() / 1000000;
    }

    public static void teleOpinit() {
        canCount = true;
    }

    public static void ManageEncoderData(double elapsedTime) throws InterruptedException {
        rightIntakePos = Util.intake2.getCurrentPosition();
        leftIntakePos = Util.intake1.getCurrentPosition();

        rightIntakeDiff = Math.abs(rightIntakePos - lastRightIntakePos);
        leftIntakeDiff = Math.abs(leftIntakePos - lastLeftIntakePos);

        rightIntakeDiff = (MEASURING_INTERVAL / elapsedTime) * rightIntakeDiff;
        leftIntakeDiff = (MEASURING_INTERVAL / elapsedTime) * leftIntakeDiff;

        rightIntakeSum = rightIntakeSum + rightIntakeDiff - rightIntakeQueue.poll();
        rightIntakeQueue.add(rightIntakeDiff);
        leftIntakeSum = leftIntakeSum + leftIntakeDiff - leftIntakeQueue.poll();
        leftIntakeQueue.add(leftIntakeDiff);

        lastRightIntakePos = rightIntakePos;
        lastLeftIntakePos = leftIntakePos;

        queueClear = false;
        queueFill = false;
    }

    public static void handleStalls() throws InterruptedException {
        //Handle Stalls
        deltaRight = rightIntakeSum / MOVING_AVERAGE_LENGTH;
        deltaLeft = leftIntakeSum / MOVING_AVERAGE_LENGTH;

        if (deltaRight < lowestDeltaRight){
            lowestDeltaRight = deltaRight;
        }
        if (deltaLeft < lowestDeltaLeft){
            lowestDeltaLeft = deltaLeft;
        }

        Util.telemetry("Lowest right delta", lowestDeltaRight);
        Util.telemetry("Lowest left delta", lowestDeltaLeft);

        if ((deltaRight < 8 || deltaLeft < 8)) {
            Util.intake1.setPower(-0.9);
            Util.intake2.setPower(-0.9);
            Thread.sleep(500);
            Util.intake1.setPower(0.9);
            Util.intake2.setPower(0.9);
            fillQueue();
        }

    }

    public static void ManageDataAndHandleStalls() throws InterruptedException {
        currentTime = System.nanoTime() / 1000000;
        IntakeControl.ManageEncoderData(currentTime - oldTime);
        oldTime = currentTime;
        IntakeControl.handleStalls();
       // Thread.sleep(10);
    }

    public static void clearQueue() {
        if (queueClear) return;

        rightIntakeQueue = new LinkedList<>();
        leftIntakeQueue = new LinkedList<>();

        rightIntakeSum = 0;
        leftIntakeSum = 0;

        for (int i = 0; i < MOVING_AVERAGE_LENGTH; i++){
            rightIntakeQueue.add(0.0);
            leftIntakeQueue.add(0.0);
        }

        queueClear = true;
    }

    public static void fillQueue() {
        if (queueFill) return;

        rightIntakeQueue = new LinkedList<>();
        leftIntakeQueue = new LinkedList<>();

        rightIntakeSum = 500; //change to reflect limit on the deltas
        leftIntakeSum = 500;

        for (int i = 0; i < MOVING_AVERAGE_LENGTH; i++){
            rightIntakeQueue.add(30.0);
            leftIntakeQueue.add(30.0);
        }

        queueFill = true;
    }

    public static void ManageGlyphCounterData() {
        distance = Util.glyphCounterDistance.getDistance(DistanceUnit.CM);

//        if (Double.isNaN(distance)){
//            distance = 100;
//        }


        if (Double.isNaN(distance)){
            canCount = true;
            //Util.telemetry("Can Count!", true);
        }

        if ((!Double.isNaN(distance)) && canCount){
            //Util.telemetry("counting!", true);
            getGlyphs.glyphCount += 1;
            red = Util.glyphCounterColor.red();
            blue = Util.glyphCounterColor.green();
            green = Util.glyphCounterColor.blue();
            alpha = Util.glyphCounterColor.alpha();

            if (red > 100){
                red = 0;
            }
            if (green > 100){
                green = 0;
            }
            if (blue > 100){
                blue = 0;
            }
            if (alpha > 250){
                alpha = 0;
            }

            if (red >= 25 || green >= 20 || blue >= 20 || alpha >= 60){
                getGlyphs.myColor = getGlyphs.glyphColor.GREY;
                Util.telemetry("glyph color" , getGlyphs.myColor, true);
            }
            else if (((0 < red) && (red < 25)) && ((0 < green) && (green < 20)) && ((0 < blue) && (blue < 20)) && ((0 < alpha) && (alpha < 60))){
                getGlyphs.myColor = getGlyphs.glyphColor.BROWN;
                Util.telemetry("glyph color" , getGlyphs.myColor, true);
            }
            else {
                getGlyphs.myColor = getGlyphs.glyphColor.UNKNOWN;
                Util.telemetry("glyph color" , getGlyphs.myColor, true);
            }
            Util.telemetry("glyph number", getGlyphs.glyphCount, true);

            canCount = false;
            //Util.telemetry("Distance", distance, true);
        }
    }

    public static void ManageGlyphCounterDataTeleOp() {
        distance = MecanumTeleop.glyphCounterDistance.getDistance(DistanceUnit.CM);

//        if (Double.isNaN(distance)){
//            distance = 100;
//        }


        if (Double.isNaN(distance)){
            canCount = true;
            //Util.telemetry("Can Count!", true);
        }

        if (!(Double.isNaN(distance)) && canCount){
            //Util.telemetry("counting!", true);
            getGlyphs.glyphCount += 1;
            red = MecanumTeleop.glyphCounterColor.red();
            blue = MecanumTeleop.glyphCounterColor.green();
            green = MecanumTeleop.glyphCounterColor.blue();
            alpha = MecanumTeleop.glyphCounterColor.alpha();

            if (red > 100){
                red = 0;
            }
            if (green > 100){
                green = 0;
            }
            if (blue > 100){
                blue = 0;
            }
            if (alpha > 250){
                alpha = 0;
            }

            if (red >= 25 || green >= 20 || blue >= 20 || alpha >= 60){
                getGlyphs.myColor = getGlyphs.glyphColor.GREY;
            }
            else if (((0 < red) && (red < 25)) && ((0 < green) && (green < 20)) && ((0 < blue) && (blue < 20)) && ((0 < alpha) && (alpha < 60))){
                getGlyphs.myColor = getGlyphs.glyphColor.BROWN;
            }
            else {
                getGlyphs.myColor = getGlyphs.glyphColor.UNKNOWN;
            }

            canCount = false;
            //Util.telemetry("Distance", distance, true);
        }
    }
}