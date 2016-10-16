package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.LinkedList;
import java.util.Queue;

public class StallProtection {

    private static double timeDiff, backAvg, frontAvg, avgDiff, ratio;
    private static int rightBackPos, leftBackPos, rightFrontPos, leftFrontPos;
    private static int lastRightBackPos = 0, lastLeftBackPos = 0, lastRightFrontPos = 0, lastLeftFrontPos = 0;
    private static int rightBackDiff, leftBackDiff, rightFrontDiff, leftFrontDiff;
    private static int rightBackSum = 0, leftBackSum = 0, rightFrontSum = 0, leftFrontSum = 0;
    //protected static boolean init = false;
    private static boolean telemetry = true;
    //private static double ratioMax = 0, ratioMin = 100;

    private static Queue<Integer> rightBackQueue, leftBackQueue, rightFrontQueue, leftFrontQueue;

    private static DcMotor right = Util.rightBack;
    private static DcMotor left = Util.leftBack;
    private static DcMotor rightFront = Util.rightFront;
    private static DcMotor leftFront = Util.leftFront;

    private final static int MOVING_AVERAGE_LENGTH = 10;          // these settings are decent (we think)
    private final static double STALL_RATIO_THRESHOLD_MAX = 1.5;  //1.2
    private final static double STALL_RATIO_THRESHOLD_MIN = 0.4;  //0.65
    private final static double STALL_DIFF_THRESHOLD = 20;        //30

    final static double SEC_TO_NSEC = Util.SEC_TO_NSEC;

    private StallProtection() throws Exception {
        throw new Exception();
    }

    public static void init() {
        rightBackQueue = new LinkedList<Integer>();
        leftBackQueue = new LinkedList<Integer>();
        rightFrontQueue = new LinkedList<Integer>();
        leftFrontQueue = new LinkedList<Integer>();

        for (int i = 0; i < MOVING_AVERAGE_LENGTH; i++) {
            rightBackQueue.add(0);
            leftBackQueue.add(0);
            rightFrontQueue.add(0);
            leftFrontQueue.add(0);
        }

        //init = true;
    }

    public static int stalled() {
        /*int result = -1;
        if (!init) return result;*/
        int result = 0;
        if (backAvg == 0) {
            ratio = 10;
        } else {
            ratio = frontAvg / backAvg;
            /*if (ratio > ratioMax) {
                ratioMax = ratio;
            }
            if (ratio < ratioMin) {
                ratioMin = ratio;
            }*/
        }
        Util.log("my_debug ratio: " + ratio);
        Util.log("my_debug gyro_heading " + PID.heading(Util.gyro));
        Util.linearOpMode.telemetry.addData("gyro heading", PID.heading(Util.gyro));
        /*Util.linearOpMode.telemetry.addData("ratioMax", ratioMax);
        Util.linearOpMode.telemetry.addData("ratioMin", ratioMin);*/
        if ((ratio > STALL_RATIO_THRESHOLD_MAX) ||
                (ratio < STALL_RATIO_THRESHOLD_MIN) ||
                (avgDiff < STALL_DIFF_THRESHOLD)) {
            result = 1;
            if (telemetry && (ratio > STALL_RATIO_THRESHOLD_MAX)) {
                Util.linearOpMode.telemetry.addData("above max ratio", ratio);
                Util.log("my_debug RAISON_DU_STALL: above max ratio");
            }
            if (telemetry && (ratio < STALL_RATIO_THRESHOLD_MIN)) {
                Util.linearOpMode.telemetry.addData("below min ratio", ratio);
                Util.log("my_debug RAISON_DU_STALL: below min ratio");
            }
            if (telemetry && (avgDiff < STALL_DIFF_THRESHOLD)) {
                Util.linearOpMode.telemetry.addData("low average diff", avgDiff);
                Util.log("my_debug RAISON_DU_STALL: low average diff");
            }
            telemetry = false;
        }
        return result;
    }

    public static void reset() {
        lastRightBackPos = right.getCurrentPosition();
        lastLeftBackPos = left.getCurrentPosition();
        lastRightFrontPos = rightFront.getCurrentPosition();
        lastLeftFrontPos = leftFront.getCurrentPosition();

        for (int i = 0; i < MOVING_AVERAGE_LENGTH; i++) {
            rightBackQueue.poll();
            rightBackQueue.add(0);
            leftBackQueue.poll();
            leftBackQueue.add(0);
            rightFrontQueue.poll();
            rightFrontQueue.add(0);
            leftFrontQueue.poll();
            leftFrontQueue.add(0);
        }

        rightBackSum = 0;
        leftBackSum = 0;
        rightFrontSum = 0;
        leftFrontSum = 0;

        /*ratioMax = 0;
        ratioMin = 100;*/
        avgDiff = 0;

        telemetry = true;
    }

    public static void manageEncoderData() {
        rightBackDiff = Math.abs(rightBackPos - lastRightBackPos);
        leftBackDiff = Math.abs(leftBackPos - lastLeftBackPos);
        rightFrontDiff = Math.abs(rightFrontPos - lastRightFrontPos);
        leftFrontDiff = Math.abs(leftFrontPos - lastLeftFrontPos);

        rightBackSum = rightBackSum + rightBackDiff - rightBackQueue.poll();
        rightBackQueue.add(rightBackDiff);
        leftBackSum = leftBackSum + leftBackDiff - leftBackQueue.poll();
        leftBackQueue.add(leftBackDiff);
        rightFrontSum = rightFrontSum + rightFrontDiff - rightFrontQueue.poll();
        rightFrontQueue.add(rightFrontDiff);
        leftFrontSum = leftFrontSum + leftFrontDiff - leftFrontQueue.poll();
        leftFrontQueue.add(leftFrontDiff);

        backAvg = (rightBackSum + leftBackSum) / (2 * MOVING_AVERAGE_LENGTH);
        frontAvg = (rightFrontSum + leftFrontSum) / (2 * MOVING_AVERAGE_LENGTH);
        //Util.log("my_debug backAvg " + backAvg);
        //Util.log("my_debug frontAvg " + frontAvg);
        avgDiff = (backAvg + frontAvg) / 2;
        Util.log("my_debug avgDiff: " + avgDiff);
        Util.linearOpMode.telemetry.addData("avgDiff", avgDiff);

        lastRightBackPos = rightBackPos;
        lastLeftBackPos = leftBackPos;
        lastRightFrontPos = rightFrontPos;
        lastLeftFrontPos = leftFrontPos;
    }

    public static void storeEncoderData() {
        rightBackPos = right.getCurrentPosition();
        leftBackPos = left.getCurrentPosition();
        rightFrontPos = rightFront.getCurrentPosition();
        leftFrontPos = leftFront.getCurrentPosition();
    }
}
