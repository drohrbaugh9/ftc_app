package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

public final class AutoUtil {

    protected static boolean init = false;
    protected static LinearOpMode linearOpMode;
    private static float stallEnabledTime;
    private static double powerFactor = Util.POWER_LIMIT;
    private static final double MOTOR_POWER_THRESHOLD = 0.8 * Util.POWER_LIMIT, TIME_THRESHOLD = 0.3 * Util.SEC_TO_NSEC;

    private AutoUtil() throws Exception {
        throw new Exception();
    }

    public static void init(LinearOpMode opmode, GyroSensor gyro) throws InterruptedException {
        Util.init(opmode);
        StallProtection.init();
        linearOpMode = Util.linearOpMode;
        resetEncoders();
        calibrateGyro(gyro);
        init = true;
    }

    public static void moveForward(double distance, double power, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        double start = Util.rightBack.getCurrentPosition();
        stallEnabledTime = System.nanoTime();
        while (Util.rightBack.getCurrentPosition() < (start + (distance * 0.98))) {
            float currentTime = System.nanoTime();
            //if (TeleOpLinear.stallProtectionGloballyEnabled && (Math.abs(power) > MOTOR_POWER_THRESHOLD) && ((currentTime - stallEnabledTime) > TIME_THRESHOLD)) {
            if ((Math.abs(power) > MOTOR_POWER_THRESHOLD) && ((currentTime - stallEnabledTime) > TIME_THRESHOLD)) {
                int isStalled = StallProtection.stalled();
                if (isStalled == 1) {
                    powerFactor -= 0.05;
                    if (powerFactor < 0) powerFactor = 0;
                }
                else if (isStalled == 0) {
                    powerFactor += 0.02;
                    if (powerFactor > Util.POWER_LIMIT) powerFactor = Util.POWER_LIMIT;
                }
            }
            //PID.PIsetMotors(gyro, powerFactor * power);
            Util.setFrontPowers(powerFactor * power);
            Util.setBackPowers(powerFactor * Util.BACK_SCALE * power);
            Thread.sleep(10);
        }
        Util.setAllPowers(0);
    }

    public static void moveBackward(double distance, double power, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        double start = Util.rightBack.getCurrentPosition();
        stallEnabledTime = System.nanoTime();
        while (Util.rightBack.getCurrentPosition() > (start - (distance * 0.98))) {
            float currentTime = System.nanoTime();
            if ((Math.abs(power) > MOTOR_POWER_THRESHOLD) && ((currentTime - stallEnabledTime) > TIME_THRESHOLD)) {
                int isStalled = StallProtection.stalled();
                if (isStalled == 1) {
                    powerFactor -= 0.05;
                    if (powerFactor < 0) powerFactor = 0;
                }
                else if (isStalled == 0) {
                    powerFactor += 0.02;
                    if (powerFactor > Util.POWER_LIMIT) powerFactor = Util.POWER_LIMIT;
                }
            }
            //PID.PIsetMotors(gyro, powerFactor * -power);
            Util.setFrontPowers(powerFactor * -power);
            Util.setBackPowers(powerFactor * Util.BACK_SCALE * -power);
            Thread.sleep(10);
        }
        Util.setAllPowers(0);
    }

    public static void turnRight(double distance, float power) {//, GyroSensor gyro) {
        //resetGyroHeading(gyro);
        double start = Util.leftBack.getCurrentPosition();
        Util.setRightPowers(-power);
        Util.setLeftPowers(power);
        while (Util.leftBack.getCurrentPosition() < (start + distance * 0.98));
        Util.setAllPowers(0);
    }

    public static void turnLeft(double distance, float power) {
        double start = Util.rightBack.getCurrentPosition();
        Util.setRightPowers(power);
        Util.setLeftPowers(-power);
        while (Util.rightBack.getCurrentPosition() < (start + distance * 0.98));
        Util.setAllPowers(0);
    }

    public static void resetEncoders(DcMotor[] motors) throws InterruptedException {
        Util.resetEncoders(motors);
    }

    public static void resetEncoders() throws InterruptedException {
        Util.resetEncoders();
    }

    public static void calibrateGyro(GyroSensor gyro) throws InterruptedException {
        gyro.calibrate();
        //linearOpMode.telemetry.addData("gyro status", "calibrating");
        while (gyro.isCalibrating()) Thread.sleep(50);
        //linearOpMode.telemetry.addData("gyro status", "calibrated");
    }

    public static void resetGyroHeading(GyroSensor gyro) {
        gyro.resetZAxisIntegrator();
    }

    /*public static double gyroDrift(GyroSensor gyro) throws InterruptedException {
        resetGyro(gyro);
        Thread.sleep(15000);
        int heading = gyro.getHeading();
        double error = heading;
        if (heading > 180) error = 360 - heading;
        return error / 15000;
    }*/
}
