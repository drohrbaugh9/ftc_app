package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

public final class AutoUtil {

    protected static boolean init = false;
    protected static LinearOpMode linearOpMode;
    private static float stallEnabledTime;
    private static double powerFactor = Util.POWER_LIMIT;
    protected static DcMotor r, l;
    private static final double MOTOR_POWER_THRESHOLD = 0.8 * Util.POWER_LIMIT, TIME_THRESHOLD = 0.3 * Util.SEC_TO_NSEC;
    private static final double MIN_POWER = 0.2;

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

    public static void encoderForward(int dist, double power, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setAllPowers(power);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) < (pos + dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    public static void encoderBackward(int dist, double power, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setAllPowers(-power);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) > (pos + dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    public static void encoderSteerForward(int dist, double power, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setRightPowers(power * 1.08);
        Util.setLeftPowers(power / 1.08);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) < (pos + dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    public static void encoderSteerBackward(int dist, double power, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setRightPowers(-power * 1.08);
        Util.setLeftPowers(-power / 1.08);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) > (pos + dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    public static void encoderSteerForwardLine(double threshold, double power, boolean stop) throws InterruptedException {
        Util.setRightPowers(power * 1.08);
        Util.setLeftPowers(power / 1.08);

        while (Util.ods.getLightDetected() < threshold) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    public static void encoderSteerBackwardLine(double threshold, double power, boolean stop) throws InterruptedException {
        Util.setRightPowers(-power * 1.08);
        Util.setLeftPowers(-power / 1.08);

        while (Util.ods.getLightDetected() < threshold) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    public static void PID_Forward(double distance, double power, boolean stop, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        PID.resetDriveIntegral();
        double start = Util.rightBack.getCurrentPosition();
        Util.setAllPowers(0.1);
        Thread.sleep(30);
        while (Util.rightBack.getCurrentPosition() < (start + (distance * 0.98))) {
            PID.PIsetMotors(gyro, powerFactor * power);
            Thread.sleep(10);
        }
        if (stop) Util.setAllPowers(0);
    }

    public static void PID_Backward(double distance, double power, boolean stop, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        PID.resetDriveIntegral();
        double start = Util.rightBack.getCurrentPosition();
        Util.setAllPowers(-0.1);
        Thread.sleep(30);
        while (Util.rightBack.getCurrentPosition() > (start - (distance * 0.98))) {
            PID.PIsetMotors(gyro, powerFactor * -power);
            Thread.sleep(10);
        }
        if (stop) Util.setAllPowers(0);
    }

    final static double RAMP_UP_DELTA = 0.02, RAMP_DOWN_DELTA = 0.03;
    final static int EXTRA_DEGREES = 3; // 1

    public static void encoderTurnRight(double degrees, double power) throws InterruptedException {
        Util.resetEncoders();

        double dist = degrees / 360;
        dist = dist * 15 / 4 * 1120;

        Util.setRightPowers(-power);
        Util.setLeftPowers(power);

        while (((Math.abs(r.getCurrentPosition()) + Math.abs(l.getCurrentPosition())) / 2) < dist) Thread.sleep(20);

        Util.setAllPowers(0);
    }

    public static void encoderTurnLeft(int degrees, double power) throws InterruptedException { encoderTurnRight(degrees, -power); }

    public static void gyroTurnRight(double degreeTarget, double targetPower, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        double power = MIN_POWER;
        while (PID.heading(gyro) < (degreeTarget / 2)) {
            power += RAMP_UP_DELTA;
            if (power > targetPower) {
                Util.setRightPowers(-targetPower);
                Util.setLeftPowers(targetPower);
                break;
            }
            Util.setRightPowers(-power);
            Util.setLeftPowers(power);
            Thread.sleep(10);
        }
        power = targetPower;
        double rampUpDegrees = PID.heading(gyro);
        while (degreeTarget - PID.heading(gyro) > rampUpDegrees * 2) Thread.sleep(10);
        while (PID.heading(gyro) - degreeTarget > EXTRA_DEGREES) {
            power -= RAMP_DOWN_DELTA;
            if (power < MIN_POWER) {
                Util.setRightPowers(-MIN_POWER);
                Util.setLeftPowers(MIN_POWER);
            } else {
                Util.setRightPowers(-power);
                Util.setLeftPowers(power);
            }
            Thread.sleep(10);
        }
        Util.setAllPowers(0);
    }

    public static void gyroTurnLeft(double degreeTarget, double targetPower, GyroSensor gyro) throws InterruptedException {
        degreeTarget = -degreeTarget;
        resetGyroHeading(gyro);
        double power = MIN_POWER;
        while (PID.heading(gyro) > (degreeTarget / 2)) {
            power += RAMP_UP_DELTA;
            if (power > targetPower) {
                Util.setRightPowers(targetPower);
                Util.setLeftPowers(-targetPower);
                break;
            }
            Util.setRightPowers(power);
            Util.setLeftPowers(-power);
            Thread.sleep(10);
        }
        power = targetPower;
        double rampUpDegrees = PID.heading(gyro);
        while (degreeTarget - PID.heading(gyro) < rampUpDegrees) Thread.sleep(10);
        while (PID.heading(gyro) - degreeTarget > EXTRA_DEGREES) {
            power -= RAMP_DOWN_DELTA;
            if (power < MIN_POWER) {
                Util.setRightPowers(MIN_POWER);
                Util.setLeftPowers(-MIN_POWER);
            } else {
                Util.setRightPowers(power);
                Util.setLeftPowers(-power);
            }
            Thread.sleep(10);
        }
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

    public static void beaconUp(Servo servo) throws InterruptedException {
        servo.setPosition(Util.BEACON_UP);
        Thread.sleep(400);
    }

    public static void beaconDown(Servo servo) throws InterruptedException {
        servo.setPosition(Util.BEACON_DOWN);
        Thread.sleep(400);
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
