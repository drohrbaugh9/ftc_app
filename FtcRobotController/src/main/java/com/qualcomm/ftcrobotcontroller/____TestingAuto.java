package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.concurrent.TimeUnit;

public class ____TestingAuto extends LinearOpMode {

    DcMotor right, left, rightFront, leftFront;
    Servo servo;

    ColorSensor color;
    GyroSensor gyro;

    public ____TestingAuto() {
    }

    // gyro testing
    /*public void runOpMode() throws InterruptedException {
        gyro = hardwareMap.gyroSensor.get("gyro");

        AutoUtil.calibrateGyro(gyro);

        AutoUtil.resetGyroHeading(gyro);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("gyro heading", PID.heading(gyro));
        }
    }*/

    // encoder testing
    /*public void runOpMode() throws InterruptedException {
        Util.init(this);
        AutoUtil.resetEncoders();

        left = Util.leftBack;

        telemetry.addData("left encoder start", left.getCurrentPosition());

        waitForStart();

        left.setPower(0.3);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            telemetry.addData("left encoder end", left.getCurrentPosition());
        }

        left.setPowerFloat();

        telemetry.addData("Run Mode", Util.c.getMotorChannelMode(1));
    }*/

    // rightTrigger
    /*public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("rightTrigger");

        waitForStart();

        servo.setPosition(0.15);
    }*/

    // color sensor
    /*public void runOpMode() throws InterruptedException {
        Util.init(this);
        right = Util.right;
        left = Util.left;
        rightFront = Util.rightFront;
        left = Util.leftFront;
        servo = hardwareMap.servo.get("rightTrigger");


        color = hardwareMap.colorSensor.get("mr");

        servo.setPosition(0.12);

        waitForStart();

        //color.enableLed(false);

        //while (!gamepad1.a);

        //Util.setAllPowers(-0.2);

        while(true) {
            int red = color.red();
            int green = color.green();
            int blue = color.blue();
            RobotLog.e("red: " + red);
            RobotLog.i("green: " + green);
            RobotLog.w("blue: " + blue);
            telemetry.addData("red", red);
            telemetry.addData("green", green);
            telemetry.addData("blue", blue);
        }

        while (gamepad1.a) {
            int red = color.red();
            int green = color.green();
            int blue = color.blue();
            //RobotLog.e("red: " + red);
            //RobotLog.i("green: " + green);
            //RobotLog.w("blue: " + blue);
            if (true) { //if (color.red() > 7 && color.green() < 2 && color.blue() < 2) {
                //Util.setAllPowers(0);
                //RobotLog.i("stopped");
                telemetry.addData("blue", blue);
                telemetry.addData("green", green);
                telemetry.addData("red", red);
                //break;
            }
        }

        Util.setAllPowers(0);
    }*/

    // stall protection

    int loops = 0;
    double lastRightBackPos, lastLeftBackPos, lastRightFrontPos, lastLeftFrontPos;
    double rightBackPos, leftBackPos, rightFrontPos, leftFrontPos;
    double diffPos, accumulatedDiff = 0;
    float opModeTime, stallProtectionTime, currentTime;
    double timeDiff;

    double[] backRight = new double[50000];
    double[] backLeft = new double[50000];
    double[] frontRight = new double[50000];
    double[] frontLeft = new double[50000];

    public void runOpMode() throws InterruptedException {
        Util.init(this);
        right = Util.rightBack;
        left = Util.leftBack;
        rightFront = Util.rightFront;
        leftFront = Util.leftFront;

        waitForStart();

        Util.setAllPowers(0);

        Thread.sleep(50);

        float requestedPower = 0.5f;
        float powerFactor = 1;

        Util.setFrontPowers(requestedPower * powerFactor);
        Util.setBackPowers(requestedPower * powerFactor * 1.3f);

        //Thread.sleep(200);

        lastRightBackPos = right.getCurrentPosition();
        lastLeftBackPos = left.getCurrentPosition();
        lastRightFrontPos = rightFront.getCurrentPosition();
        lastLeftFrontPos = leftFront.getCurrentPosition();

        opModeTime = System.nanoTime();
        stallProtectionTime = System.nanoTime();

        //while ((System.nanoTime() - opModeTime) / SEC_TO_NSEC < 3) {
        while (loops < 100) {
            currentTime = System.nanoTime();
            timeDiff = (currentTime - stallProtectionTime) / Util.SEC_TO_NSEC;
            rightBackPos = right.getCurrentPosition();
            leftBackPos = left.getCurrentPosition();
            rightFrontPos = rightFront.getCurrentPosition();
            leftFrontPos = leftFront.getCurrentPosition();
            if (timeDiff > 0.01) {
                loops++;
                diffPos = (Math.abs(rightBackPos - lastRightBackPos) +
                           Math.abs(leftBackPos - lastLeftBackPos) +
                           Math.abs(rightFrontPos - lastRightFrontPos) +
                           Math.abs(leftFrontPos - lastLeftFrontPos)) / 4;
                accumulatedDiff += diffPos;
                backRight[loops] = Math.abs(rightBackPos - lastRightBackPos);
                backLeft[loops] = Math.abs(leftBackPos - lastLeftBackPos);
                frontRight[loops] = Math.abs(rightFrontPos - lastRightFrontPos);
                frontLeft[loops] = Math.abs(leftFrontPos - lastLeftFrontPos);
                //if (diffPos < minDiff) minDiff = diffPos;
                //if (diffPos < 100) {
                    //powerFactor -= 0.2;
                    //if (powerFactor <= 0) {
                        //Util.setAllPowers(0);
                        //telemetry.addData("stall", "stall protection engaged");
                        //stalled = true;
                        //break;
                    //}
                    //telemetry.addData("stall", "stall protection engaged");
                //}
                //else {
                    //powerFactor += 0.005;
                    //powerFactor = Range.clip(powerFactor, 0, 1.2);
                //}
                //Util.setFrontPowers(powerFactor * requestedPower);
                //Util.setBackPowers(powerFactor * requestedPower * 1.3);
                lastRightBackPos = rightBackPos;
                lastLeftBackPos = leftBackPos;
                lastRightFrontPos = rightFrontPos;
                lastLeftFrontPos = leftFrontPos;
                stallProtectionTime = System.nanoTime();
            }
            Thread.sleep(2);
        }

        Thread.sleep(100);

        Util.setAllPowers(0);

        //telemetry.addData("minDiff", minDiff);
        telemetry.addData("avgDiff", accumulatedDiff / loops);
        //if (stalled) telemetry.addData("stall", "stall protection engaged");
        //telemetry.addData("rDiff", rightBackPos - lastRightBackPos);
        //telemetry.addData("lDiff", leftBackPos - lastLeftBackPos);
        //telemetry.addData("rfDiff", rightFrontPos - lastRightFrontPos);
        //telemetry.addData("lfDiff", leftFrontPos - lastLeftFrontPos);

        RobotLog.w("my_debug BEGIN ARM_45_POWER_50");

        for (int i = 0; i < loops; i++) {
            RobotLog.i("my_debug right_back_encoder " + backRight[i]);
            RobotLog.i("my_debug left_back_encoder " + backLeft[i]);
            RobotLog.i("my_debug right_front_encoder " + frontRight[i]);
            RobotLog.i("my_debug left_front_encoder " + frontLeft[i]);
        }

        //RobotLog.w("my_debug minDiff " + minDiff);
        RobotLog.w("my_debug avgDiff " + accumulatedDiff / loops);
        RobotLog.w("my_debug batteryVoltage " + Util.getBatteryVoltage());
    }
}
