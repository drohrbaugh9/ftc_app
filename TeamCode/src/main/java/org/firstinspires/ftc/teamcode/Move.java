package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.RelicRecovery.ColorSensor;
import org.firstinspires.ftc.teamcode.RelicRecovery.IntakeControl;
import org.firstinspires.ftc.teamcode.RelicRecovery.MecanumTeleop;
import org.firstinspires.ftc.teamcode.RelicRecovery.Vuforia;
import org.firstinspires.ftc.teamcode.RelicRecovery.VuforiaGoToColumn;
import org.firstinspires.ftc.teamcode.RelicRecovery.getGlyphs;

public class Move {

    private Move() throws Exception {
        throw new Exception();
    }

    // start the robot from a stop and gradually speed it up until <targetPower> is reached
    public static void accelerateForward(double targetPower) throws InterruptedException {
        double currentPower = 0.06;
        while ((currentPower + 0.06) < targetPower) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower += 0.06;
            Thread.sleep(20);
        }
        Util.setAllPowers(targetPower);
    }

    public static void accelerateBackward(double targetPower) throws InterruptedException {
        targetPower = Math.abs(targetPower);
        double currentPower = -0.06;
        while ((currentPower - 0.06) > (-targetPower)) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower -= 0.06;
            Thread.sleep(20);
        }
        Util.setAllPowers(-targetPower);
    }

    // slow down the robot smoothly, as opposed to jerking it to a stop
    public static void decelerateForward(double currentPower) throws InterruptedException {
        currentPower -= 0.06;
        while ((currentPower - 0.06) > 0) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower -= 0.06;
            Thread.sleep(20);
        }

        Util.setAllPowers(0);
    }

    public static void decelerateBackward(double currentPower) throws InterruptedException {
        currentPower = -Math.abs(currentPower) + 0.06;
        while ((currentPower + 0.06) < 0) {
            Util.setAllPowers(Range.clip(currentPower, -1, 1));
            currentPower += 0.06;
            Thread.sleep(20);
        }

        Util.setAllPowers(0);
    }

    public static void accelerateRight(double targetPower) throws InterruptedException {
        targetPower = Math.abs(targetPower);
        double currentPower = 0.06;
        while ((currentPower + 0.06) < targetPower){
            Util.rightFront.setPower(-Range.clip(currentPower, -1, 1));
            Util.rightBack.setPower(Range.clip(currentPower, -1, 1));
            Util.leftFront.setPower(Range.clip(currentPower, -1, 1));
            Util.leftBack.setPower(-Range.clip(currentPower, -1, 1));
            currentPower += 0.06;
            Thread.sleep(20);
        }
    }

    public static void decelerateRight(double currentPower) throws InterruptedException {
        while ((currentPower + 0.06) > 0){
            Util.rightFront.setPower(-Range.clip(currentPower, -1, 1));
            Util.rightBack.setPower(Range.clip(currentPower, -1, 1));
            Util.leftFront.setPower(Range.clip(currentPower, -1, 1));
            Util.leftBack.setPower(-Range.clip(currentPower, -1, 1));
            currentPower -= 0.06;
            Thread.sleep(20);
        }

        Util.setAllPowers(0);
    }

    public static void accelerateLeft(double targetPower) throws InterruptedException {
        targetPower = Math.abs(targetPower);
        double currentPower = 0.06;
        while ((currentPower + 0.06) < targetPower){
            Util.rightBack.setPower(-Range.clip(currentPower, -1, 1));
            Util.rightFront.setPower(Range.clip(currentPower, -1, 1));
            Util.leftBack.setPower(Range.clip(currentPower, -1, 1));
            Util.leftFront.setPower(-Range.clip(currentPower, -1, 1));
            currentPower += 0.06;
            Thread.sleep(20);
        }
    }

    public static void decelerateLeft(double currentPower) throws InterruptedException {
        while ((currentPower + 0.06) < 0){
            Util.rightBack.setPower(-Range.clip(currentPower, -1, 1));
            Util.rightFront.setPower(Range.clip(currentPower, -1, 1));
            Util.leftBack.setPower(Range.clip(currentPower, -1, 1));
            Util.leftFront.setPower(-Range.clip(currentPower, -1, 1));
            currentPower -= 0.06;
            Thread.sleep(20);
        }

        Util.setAllPowers(0);
    }

    // accelerate up to the target power but continue a specified distance, then stop if needed
    public static void startForward(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightFront.getCurrentPosition();
        accelerateForward(power);
        while (Math.abs((Util.rightFront.getCurrentPosition() - pos)) < dist) Thread.sleep(20);
        if (stop) decelerateForward(power);
    }

    public static void startBackward(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightFront.getCurrentPosition();
        accelerateBackward(power);
        while (Math.abs((Util.rightFront.getCurrentPosition() - pos)) < dist) Thread.sleep(20);
        if (stop) decelerateBackward(power);
    }

    public static void startRight(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightFront.getCurrentPosition();
        //accelerateRight(power);
        Util.rightFront.setPower(-power);
        Util.rightBack.setPower(power);
        Util.leftFront.setPower(power);
        Util.leftBack.setPower(-power);
        while (Math.abs((Util.rightFront.getCurrentPosition() - pos)) < dist) {
            Thread.sleep(20);
        }

        //if (stop) decelerateRight(power);
    }

    public static void startLeft(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.leftFront.getCurrentPosition();
       // accelerateLeft(power);
        Util.rightFront.setPower(power);
        Util.rightBack.setPower(-power);
        Util.leftFront.setPower(-power);
        Util.leftBack.setPower(power);
        while (Math.abs((Util.leftFront.getCurrentPosition() - pos)) < dist) {
            Thread.sleep(20);
        }
       // if (stop) decelerateLeft(power);
    }

    public static void continueForward(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightFront.getCurrentPosition();
        while (Math.abs((Util.rightFront.getCurrentPosition() - pos)) < dist) Thread.sleep(20);
        if (stop) decelerateForward(power);
    }

    public static void continueBackward(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightFront.getCurrentPosition();
        while (Math.abs((Util.rightFront.getCurrentPosition() - pos)) < dist) Thread.sleep(20);
        if (stop) decelerateBackward(power);
    }

    public static void continueRight(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.rightFront.getCurrentPosition();
        while (Math.abs((Util.rightFront.getCurrentPosition() - pos)) < dist) Thread.sleep(20);
        if (stop) decelerateRight(power);
    }

    public static void continueLeft(double power, int dist, boolean stop) throws InterruptedException {
        int pos = Util.leftFront.getCurrentPosition();
        while (Math.abs((Util.rightFront.getCurrentPosition() - pos)) < dist) Thread.sleep(20);
        if (stop) decelerateLeft(power);
    }

    public static void rotateCounterClockwiseForMultiGlyph() throws InterruptedException {
        long initialTime;

        Util.leftFront.setPower(-0.5);
        Util.leftBack.setPower(-0.5);
        Util.rightBack.setPower(0.5);
        Util.rightFront.setPower(0.5);

        initialTime = System.nanoTime();

        while (System.nanoTime() - initialTime < 200000000L){
            Thread.sleep(10);
            IntakeControl.ManageGlyphCounterData();
            if (getGlyphs.glyphCount > 1){
                break;
            }
        }

        Util.setAllPowers(0);
    }
    public static void rotateClockwiseForMultiGlyph() throws InterruptedException {
        long initialTime;

        Util.leftFront.setPower(0.5);
        Util.leftBack.setPower(0.5);
        Util.rightBack.setPower(-0.5);
        Util.rightFront.setPower(-0.5);

        initialTime = System.nanoTime();

        while (System.nanoTime() - initialTime < 200000000L){
            Thread.sleep(10);
            IntakeControl.ManageGlyphCounterData();
            if (getGlyphs.glyphCount > 1){
                break;
            }
        }

        Util.setAllPowers(0);
    }
    public static void rotateCounterClockwise(double heading) throws InterruptedException {
        Orientation angles = Util.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        float pos = angles.firstAngle;

        //float target = -pos + degrees; // this is to try and make it so that you can theoretically do two turns back to back

        double finalpower = 0.35; //don't change
        double minPower = 0.25; //don't change // oops i changed it, was 0.15
        double currentPower = 0.05; //don't change

        int decelerationDegrees = 40; // change this to tweak turns

        while ((currentPower < finalpower)) { // this loop takes 1/10 of a second total. To tweak turns that time may change
            Util.leftFront.setPower(-currentPower);
            Util.leftBack.setPower(-currentPower);
            Util.rightBack.setPower(currentPower);
            Util.rightFront.setPower(currentPower);
            currentPower += .025;

            Thread.sleep(10);
        }

        Util.leftFront.setPower(-finalpower);
        Util.leftBack.setPower(-finalpower);
        Util.rightBack.setPower(finalpower);
        Util.rightFront.setPower(finalpower);


        while ((heading - decelerationDegrees) > (Util.imu.getAngularOrientation().firstAngle)){
            Thread.sleep(5);
        }

        while (currentPower > minPower){ // this loop takes 1/10 of a second total. To tweak turns that time may change. Needs to finish ramping down before angle is reached
            Util.leftFront.setPower(-currentPower);
            Util.leftBack.setPower(-currentPower);
            Util.rightBack.setPower(currentPower);
            Util.rightFront.setPower(currentPower);
            currentPower -= .02;

            Thread.sleep(10);
        }

        while ((heading) > (Util.imu.getAngularOrientation().firstAngle)){ //wait till reach angle (remember - we are going slower now) then stop
            Thread.sleep(5);
        }

        Util.telemetry("IMU Data", Util.imu.getAngularOrientation().firstAngle);
        Util.linearOpMode.telemetry.update();

        Util.setAllPowers(0);
    }

    public static void rotateClockwise(double heading) throws InterruptedException {
        Orientation angles = Util.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        float pos = angles.firstAngle;

        //float target = pos + degrees; // this is to try and make it so that you can theoretically do two turns back to back
        // target is difference between where you are and the degrees you want to go

        double finalpower = 0.35; //don't change
        double minPower = 0.25; //don't change // oops i changed it, was 0.15
        double currentPower = 0.05; //don't change

        int decelerationDegrees = 40; // change this to tweak turns

        while ((currentPower < finalpower)) { // this loop takes 1/10 of a second total. To tweak turns that time may change
            Util.leftFront.setPower(currentPower);
            Util.leftBack.setPower(currentPower);
            Util.rightBack.setPower(-currentPower);
            Util.rightFront.setPower(-currentPower);
            currentPower += .025;

            Thread.sleep(10);
        }

        Util.leftFront.setPower(finalpower);
        Util.leftBack.setPower(finalpower);
        Util.rightBack.setPower(-finalpower);
        Util.rightFront.setPower(-finalpower);

        while ((heading - decelerationDegrees > Math.abs(Util.imu.getAngularOrientation().firstAngle))) {
            Thread.sleep(5);
        }

        while (currentPower > minPower) { // this loop takes 1/10 of a second total. To tweak turns that time may change. Needs to finish ramping down before angle is reached
            Util.leftFront.setPower(currentPower);
            Util.leftBack.setPower(currentPower);
            Util.rightBack.setPower(-currentPower);
            Util.rightFront.setPower(-currentPower);
            currentPower -= .02;

            Thread.sleep(10);
        }

        while ((heading) > Math.abs(Util.imu.getAngularOrientation().firstAngle)) { //wait till reach angle (remember - we are going slower now) then stop
            Thread.sleep(5);
        }
        Util.telemetry("IMU Data", Util.imu.getAngularOrientation().firstAngle);
        Util.linearOpMode.telemetry.update();

        Util.setAllPowers(0);
    }

    public static boolean strafeAngle(double angle, double power, int dist, boolean brake) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        while (((Math.abs(Util.leftBack.getCurrentPosition() - lB)
                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4) < dist) {
        //for (int i = 0; i < 75; i++) {
            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        if (brake) {
            Util.setDriveModeBrake();
        }
        else {
            Util.setDriveModeFloat();
        }

        Util.setAllPowers(0);

        return true;
    }
    public static boolean strafeAngle(double angle, double power) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);


        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;



        rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
        Util.leftFront.setPower(leftFront + rotate);
        Util.rightFront.setPower(rightFront - rotate);
        Util.leftBack.setPower(leftBack + rotate);
        Util.rightBack.setPower(rightBack - rotate);
            /**/

        Thread.sleep(10);

        return true;
    }

    public static boolean strafeAngleforTime (double angle, double power, double seconds) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        double i = 0;
        while (i < seconds) {
            //for (int i = 0; i < 75; i++) {
            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            i += 0.02; // because it takes 2/100 of a second to complete loop

            Thread.sleep(20);
        }

        Util.setDriveModeFloat();
        Util.setAllPowers(0);

        return true;
    }

    public static boolean strafeAngleforTimeWithoutPID (double angle, double power, double seconds) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        double i = 0;
        while (i < seconds) {
            //for (int i = 0; i < 75; i++) {
            rotate = 0;

            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            i += 0.02; // because it takes 2/100 of a second to complete loop

            Thread.sleep(20);
        }

        Util.setDriveModeFloat();
        Util.setAllPowers(0);

        return true;
    }

    protected static float RightAmountofRed;
    protected static float RightAmountofBlue;

    protected static float LeftAmountofRed;
    protected static float LeftAmountofBlue;

    public static boolean moveUntilRedPID (double angle, double power, LinearOpMode opMode) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (rightRatio < 1.5 && leftRatio < 1.5) {
            ColorSensor.ReadSensor(opMode);
            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        Util.setAllPowers(0);

        return true;
    }

    public static double moveSquareUpUntilRedPID (LinearOpMode opMode, VuforiaGoToColumn.columnState column) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        boolean leftHitFirst = false;
        boolean rightHitFirst = false;

        double distanceToMiddle;
        double distanceToTravel = 0;

        double angle = 180;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = 0.25 * Math.sin(Math.toRadians(angle));
        double strafe = 0.25 * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (leftRatio < 1.5 && rightRatio < 1.5) {
            ColorSensor.ReadSensor(opMode);
            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(10);
        }

        Util.setAllPowers(0);

        if (leftRatio > 1.5) { //left sensor saw it
            leftHitFirst = true;
            Util.resetEncoders();
            while (rightRatio < 1.5) {
                ColorSensor.ReadSensor(opMode);

                RightAmountofRed = Color.red(ColorSensor.RightColor);
                RightAmountofBlue = Color.blue(ColorSensor.RightColor);
                rightRatio = RightAmountofRed/((double)RightAmountofBlue);

                strafeAngle(-90, 0.3);

                Thread.sleep(10);
            }
            Util.setAllPowers(0);
            distanceToMiddle = 0.5 * ((Math.abs(Util.rightFront.getCurrentPosition()) + Math.abs(Util.rightBack.getCurrentPosition()) + Math.abs(Util.leftFront.getCurrentPosition()) + Math.abs(Util.leftBack.getCurrentPosition())) / 4);
            Util.telemetry("distance to middle", distanceToMiddle, true);
            if (column == VuforiaGoToColumn.columnState.LEFT){
                distanceToTravel = distanceToMiddle + 275;
            }
            else if (column == VuforiaGoToColumn.columnState.CENTER){
                strafeAngle(90, 0.4, (int) (distanceToMiddle), true);
            }
            else if (column == VuforiaGoToColumn.columnState.RIGHT){
                distanceToTravel = 405 - distanceToMiddle;
            }
        }
        else {
            rightHitFirst = true;
            Util.resetEncoders();
            while (leftRatio < 1.5) {
                ColorSensor.ReadSensor(opMode);

                LeftAmountofRed = Color.red(ColorSensor.LeftColor);
                LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
                leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

                strafeAngle(90, 0.3);

                Thread.sleep(10);
            }
            Util.setAllPowers(0);
            distanceToMiddle = 0.5 * ((Math.abs(Util.rightFront.getCurrentPosition()) + Math.abs(Util.rightBack.getCurrentPosition()) + Math.abs(Util.leftFront.getCurrentPosition()) + Math.abs(Util.leftBack.getCurrentPosition())) / 4);
            Util.telemetry("distance to middle", distanceToMiddle, true);
            if (column == VuforiaGoToColumn.columnState.RIGHT){
                distanceToTravel = distanceToMiddle + 325;
            }
            else if (column == VuforiaGoToColumn.columnState.CENTER){
                strafeAngle(-90, 0.4, (int) (distanceToMiddle), true);
            }
            else if (column == VuforiaGoToColumn.columnState.LEFT){
                distanceToTravel = 325 - distanceToMiddle;
            }
        }

        if (distanceToMiddle < 40){ //starts center
            strafeAngle(0, 0.3, 100, true);
            if (column == VuforiaGoToColumn.columnState.CENTER) {
                strafeAngle(-90, 0.4, 50, true);
            }
            else if (column == VuforiaGoToColumn.columnState.LEFT){
                strafeAngle(90, 0.4, 300, true);
            }
            else if (column == VuforiaGoToColumn.columnState.RIGHT) {
                strafeAngle(-90, 0.4, 350, true);
            }

        }
        else {
            if (rightHitFirst) {
                if (column == VuforiaGoToColumn.columnState.RIGHT){
                    strafeAngle(-90, 0.4, (int) (distanceToTravel), true);
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    strafeAngle(90, 0.4, 20, true);
                }
                else if (column == VuforiaGoToColumn.columnState.LEFT){
                    strafeAngle(90, 0.4, (int) (distanceToTravel), true);
                }
            }
            else if (leftHitFirst) {
                if (column == VuforiaGoToColumn.columnState.LEFT){
                    strafeAngle(90, 0.4, (int) distanceToTravel, true);
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    strafeAngle(-90, 0.4, 50, true);
                }
                else if (column == VuforiaGoToColumn.columnState.RIGHT){
                    strafeAngle(-90, 0.4, (int) (distanceToTravel), true);
                }

            }
        }

        return distanceToMiddle;
    }

    public static boolean moveUntilRedPIDDualProcesses (double angle, double power, LinearOpMode opMode) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        Util.distanceSensorArm.setPosition(0.7);
        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(-2));
        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(-2));
        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Util.lift.setTargetPosition(-762);
        Util.lift.setPower(0.5); //used to unfold the intake - needs this much power

        //put tray back down with gravity same as in teleop


        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (rightRatio < 1.5 && leftRatio < 1.5) {
            ColorSensor.ReadSensor(opMode);
            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/


        }

        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Util.lift.setTargetPosition(-20);
        Util.lift.setPower(0.5);

        return true;
    }

    public static boolean moveUntilBluePID (double angle, double power, LinearOpMode opMode) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (leftRatio > 0.8 && rightRatio > 0.8) {
            ColorSensor.ReadSensor(opMode);
            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        Util.setAllPowers(0);

        return true;
    }

    public static double moveSquareUpUntilBluePID (LinearOpMode opMode, VuforiaGoToColumn.columnState column) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        boolean leftHitFirst = false;
        boolean rightHitFirst = false;

        double distanceToMiddle;
        double distanceToTravel = 0;

        double angle = 180;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = 0.25 * Math.sin(Math.toRadians(angle));
        double strafe = 0.25 * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (leftRatio > 0.8 && rightRatio > 0.8) {
            ColorSensor.ReadSensor(opMode);
            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(10);
        }

        Util.setAllPowers(0);

        if (leftRatio < 0.8) { //left sensor saw it
            leftHitFirst = true;
            Util.resetEncoders();
            while (rightRatio > 0.8) {
                ColorSensor.ReadSensor(opMode);

                RightAmountofRed = Color.red(ColorSensor.RightColor);
                RightAmountofBlue = Color.blue(ColorSensor.RightColor);
                rightRatio = RightAmountofRed/((double)RightAmountofBlue);

                strafeAngle(-90, 0.3);

                Thread.sleep(10);
            }
            Util.setAllPowers(0);
            distanceToMiddle = 0.5 * ((Math.abs(Util.rightFront.getCurrentPosition()) + Math.abs(Util.rightBack.getCurrentPosition()) + Math.abs(Util.leftFront.getCurrentPosition()) + Math.abs(Util.leftBack.getCurrentPosition())) / 4);
            Util.telemetry("distance to middle", distanceToMiddle, true);
            if (column == VuforiaGoToColumn.columnState.LEFT){
                distanceToTravel = distanceToMiddle + 275;
            }
            else if (column == VuforiaGoToColumn.columnState.CENTER){
                strafeAngle(90, 0.4, (int) (distanceToMiddle), true);
            }
            else if (column == VuforiaGoToColumn.columnState.RIGHT){
                distanceToTravel = 405 - distanceToMiddle;
            }
        }
        else {
            rightHitFirst = true;
            Util.resetEncoders();
            while (leftRatio > 0.8) {
                ColorSensor.ReadSensor(opMode);

                LeftAmountofRed = Color.red(ColorSensor.LeftColor);
                LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
                leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

                strafeAngle(90, 0.3);

                Thread.sleep(10);
            }
            Util.setAllPowers(0);
            distanceToMiddle = 0.5 * ((Math.abs(Util.rightFront.getCurrentPosition()) + Math.abs(Util.rightBack.getCurrentPosition()) + Math.abs(Util.leftFront.getCurrentPosition()) + Math.abs(Util.leftBack.getCurrentPosition())) / 4);
            Util.telemetry("distance to middle", distanceToMiddle, true);
            if (column == VuforiaGoToColumn.columnState.RIGHT){
                distanceToTravel = distanceToMiddle + 325;
            }
            else if (column == VuforiaGoToColumn.columnState.CENTER){
                strafeAngle(-90, 0.4, (int) (distanceToMiddle), true);
            }
            else if (column == VuforiaGoToColumn.columnState.LEFT){
                distanceToTravel = 325 - distanceToMiddle;
            }
        }

        if (distanceToMiddle < 40){
            strafeAngle(0, 0.3, 100, true);
            if (column == VuforiaGoToColumn.columnState.CENTER) {
                strafeAngle(-90, 0.4, 100, true);
            }
            else if (column == VuforiaGoToColumn.columnState.LEFT){
                strafeAngle(90, 0.4, 300, true);
            }
            else if (column == VuforiaGoToColumn.columnState.RIGHT) {
                strafeAngle(-90, 0.4, 450, true);
            }

        }
        else {
            if (rightHitFirst) {
                if (column == VuforiaGoToColumn.columnState.RIGHT){
                    strafeAngle(-90, 0.4, (int) (distanceToTravel), true);
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    strafeAngle(90, 0.4, 20, true);
                }
                else if (column == VuforiaGoToColumn.columnState.LEFT){
                    strafeAngle(90, 0.4, (int) (distanceToTravel), true);
                }
            }
            else if (leftHitFirst) {
                if (column == VuforiaGoToColumn.columnState.LEFT){
                    strafeAngle(90, 0.4, (int) distanceToTravel, true);
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    strafeAngle(-90, 0.4, 120, true);
                }
                else if (column == VuforiaGoToColumn.columnState.RIGHT){
                    strafeAngle(-90, 0.4, (int) (distanceToTravel), true);
                }

            }
        }

        return distanceToMiddle;
    }

    public static boolean moveUntilBluePIDDualProcesses (double angle, double power, LinearOpMode opMode) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        Util.distanceSensorArm.setPosition(0.7);
        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(-2));
        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(-2));
        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Util.lift.setTargetPosition(-762);
        Util.lift.setPower(0.5); //used to unfold the intake - needs this much power

        //put tray back down with gravity same as in teleop

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (leftRatio > 0.8 && rightRatio > 0.8) {
            ColorSensor.ReadSensor(opMode);
            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Util.lift.setTargetPosition(-20);
        Util.lift.setPower(0.5); //used to unfold the intake - needs this much power

        return true;
    }
    public static boolean strafeAngleWithoutPID (double angle, double power, int dist) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        while (((Math.abs(Util.leftBack.getCurrentPosition() - lB)
                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4) < dist) {
            //for (int i = 0; i < 75; i++) {
            //rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);
            rotate = 0;
            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        Util.setAllPowers(0);

        return true;
    }

    public static boolean strafeAngleWithoutPID(double angle, double power) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        double originalAngle = 0;// PID.heading(Util.imu);


        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;



        //rotate = -0.01 * (PID.heading(Util.imu) - originalAngle);
        rotate = 0;
            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
        Util.leftFront.setPower(leftFront + rotate);
        Util.rightFront.setPower(rightFront - rotate);
        Util.leftBack.setPower(leftBack + rotate);
        Util.rightBack.setPower(rightBack - rotate);
            /**/

        Thread.sleep(10);

        return true;
    }
    public static boolean strafeAngleWithHeading(double angle, double power, int dist, boolean brake, double heading) throws InterruptedException{
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;


        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        while (((Math.abs(Util.leftBack.getCurrentPosition() - lB)
                + Math.abs(Util.rightBack.getCurrentPosition() - rB)
                + Math.abs(Util.leftFront.getCurrentPosition() - lF)
                + Math.abs(Util.rightFront.getCurrentPosition() - rF)) / 4) < dist) {
            //for (int i = 0; i < 75; i++) {
            rotate = -0.01 * (PID.heading(Util.imu) - heading);

            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        if (brake) {
            Util.setDriveModeBrake();
        }
        else {
            Util.setDriveModeFloat();
        }

        Util.setAllPowers(0);

        return true;
    }
    public static boolean strafeAngleWithHeading(double angle, double power, double heading) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;



        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;



        rotate = -0.01 * (PID.heading(Util.imu) - heading);

            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
        Util.leftFront.setPower(leftFront + rotate);
        Util.rightFront.setPower(rightFront - rotate);
        Util.leftBack.setPower(leftBack + rotate);
        Util.rightBack.setPower(rightBack - rotate);
            /**/

        Thread.sleep(10);

        return true;
    }
    public static boolean strafeAngleforTimeWithHeading (double angle, double power, double seconds, double heading) throws InterruptedException {
        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;


        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        double i = 0;
        while (i < seconds) {
            //for (int i = 0; i < 75; i++) {
            rotate = -0.01 * (PID.heading(Util.imu) - heading);

            /*/
            Util.leftFront.setPower(leftFront);
            Util.rightFront.setPower(rightFront);
            Util.leftBack.setPower(leftBack);
            Util.rightBack.setPower(rightBack);
            /**/

            /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            i += 0.02; // because it takes 2/100 of a second to complete loop

            Thread.sleep(20);
        }

        Util.setDriveModeFloat();
        Util.setAllPowers(0);

        return true;
    }
    public static boolean moveUntilRedPIDwithHeading (double angle, double power, LinearOpMode opMode, double heading) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;

        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (rightRatio < 1.5 && leftRatio < 1.5) {
            ColorSensor.ReadSensor(opMode);
            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - heading);
            opMode.telemetry.addData("Left Ratio", leftRatio);
            opMode.telemetry.addData("Right Ratio", rightRatio);
            opMode.telemetry.update();
             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(10);
        }

        Util.setAllPowers(0);

        return true;
    }

    public static boolean  moveUntilBluePIDWithHeading(double angle, double power, LinearOpMode opMode, double heading) throws InterruptedException{
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;


        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (leftRatio > 0.8 && rightRatio > 0.8) {
            ColorSensor.ReadSensor(opMode);
            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - heading);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        Util.setAllPowers(0);

        return true;
    }
    public static boolean moveUntilBluePIDDualProcessesWithHeading (double angle, double power, LinearOpMode opMode, double heading) throws InterruptedException {
        ColorSensor.init(opMode);
        ColorSensor.ReadSensor(opMode);

        if (power <= 0) return false;
        if (power > 0.7) power = 0.7;


        int lB = Util.leftBack.getCurrentPosition();
        int rB = Util.rightBack.getCurrentPosition();
        int lF = Util.leftFront.getCurrentPosition();
        int rF = Util.rightFront.getCurrentPosition();

        angle = -angle + 90;

        double frontBack = power * Math.sin(Math.toRadians(angle));
        double strafe = power * Math.cos(Math.toRadians(angle));

        double rotate;

        double leftFront, rightFront, leftBack, rightBack;

        Util.distanceSensorArm.setPosition(0.7);
        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(-2));
        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(-2));
        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Util.lift.setTargetPosition(-762);
        Util.lift.setPower(0.5); //used to unfold the intake - needs this much power

        //put tray back down with gravity same as in teleop

        leftFront = frontBack + strafe;
        rightFront = frontBack - strafe;
        leftBack = frontBack - strafe;
        rightBack = frontBack + strafe;

        RightAmountofRed = Color.red(ColorSensor.RightColor);
        RightAmountofBlue = Color.blue(ColorSensor.RightColor);

        LeftAmountofRed = Color.red(ColorSensor.LeftColor);
        LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);

        double rightRatio = RightAmountofRed/((double)RightAmountofBlue);
        double leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

        while (leftRatio > 0.8 && rightRatio > 0.8) {
            ColorSensor.ReadSensor(opMode);
            LeftAmountofRed = Color.red(ColorSensor.LeftColor);
            LeftAmountofBlue = Color.blue(ColorSensor.LeftColor);
            leftRatio = LeftAmountofRed/((double)LeftAmountofBlue);

            RightAmountofRed = Color.red(ColorSensor.RightColor);
            RightAmountofBlue = Color.blue(ColorSensor.RightColor);
            rightRatio = RightAmountofRed/((double)RightAmountofBlue);

            rotate = -0.01 * (PID.heading(Util.imu) - heading);

             /**/
            Util.leftFront.setPower(leftFront + rotate);
            Util.rightFront.setPower(rightFront - rotate);
            Util.leftBack.setPower(leftBack + rotate);
            Util.rightBack.setPower(rightBack - rotate);
            /**/

            Thread.sleep(20);
        }

        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Util.lift.setTargetPosition(-20);
        Util.lift.setPower(0.5); //used to unfold the intake - needs this much power

        return true;
    }
}
