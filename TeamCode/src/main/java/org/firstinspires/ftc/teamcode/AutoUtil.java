package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/*This class contains a few methods that you may find useful
  when you are writing an autonomous routine
*/
public final class AutoUtil {

    protected static boolean init = false; // true if opmode is initialized, false otherwise
    protected static LinearOpMode linearOpMode; // variable to hold reference to opmode
    private static float stallEnabledTime; // variable to keep track of how long stall protection has been enabled
    private static double powerFactor = Util.POWER_LIMIT; // variable to hold value drive motors are scaled by
    protected static DcMotor r, l; // variable to hold references to right and left front drive motors
    private static final double MOTOR_POWER_THRESHOLD = 0.8 * Util.POWER_LIMIT;
    private static final double TIME_THRESHOLD = 0.3 * Util.SEC_TO_NSEC;
    private static final double MIN_POWER = 0.25; // minimum turning power
    public static double offBeaconPower = 0.15, onBeaconPower = 0.11; // powers to move on and off beacons during Velocity Vortex autonomous

    // don't allow objects of type AutoUtil to be created, only allow calling of methods
    private AutoUtil() throws Exception {
        throw new Exception();
    }

    // called by an autonomous opmode to initialize all sensors, motors, etc
    public static void init(LinearOpMode opmode, GyroSensor gyro) throws InterruptedException {
        Util.init(opmode);
        StallProtection.init();
        linearOpMode = Util.linearOpMode;
        resetEncoders();
        calibrateGyro(gyro);
        init = true;
    }

    // move forward for a specified distance at a specified power; stop if directed to
    public static void encoderForward(int dist, double power, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setAllPowers(power);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) < (pos + dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // move backward for a specified distance at a specified power; stop if directed to
    public static void encoderBackward(int dist, double power, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setAllPowers(-power);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) > (pos - dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // variables that dictate how much robot steers in some of following "steer" methods
    private static double FORWARD_STEER = 1.15, BACKWARD_STEER = 1.17;

    // steer forward for a specified distance at the powers specified for left and right; stop if directed to
    public static void encoderSteerForward(int dist, double powerR, double powerL, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setRightPowers(powerR);
        Util.setLeftPowers(powerL);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) < (pos + dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // steer forward for a specified distance at a specified power; stop if directed to (uses FORWARD_STEER)
    public static void encoderSteerForward(int dist, double power, boolean stop) throws InterruptedException {

        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setRightPowers(power * FORWARD_STEER);
        Util.setLeftPowers(power / FORWARD_STEER);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) < (pos + dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // steer backward for a specified distance at the powers specified for left and right; stop if directed to
    public static void encoderSteerBackward(int dist, double powerR, double powerL, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setRightPowers(-powerR);
        Util.setLeftPowers(-powerL);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) > (pos - dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // steer backward for a specified distance at a specified power; stop if directed to (uses BACKWARD_STEER)
    public static void encoderSteerBackward(int dist, double power, boolean stop) throws InterruptedException {
        int pos = (r.getCurrentPosition() + l.getCurrentPosition()) / 2;

        Util.setRightPowers(-power * BACKWARD_STEER);
        Util.setLeftPowers(-power / BACKWARD_STEER);

        while (((r.getCurrentPosition() + l.getCurrentPosition()) / 2) > (pos - dist)) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // steer forward for a specified distance at a specified power; stop when a line is detected (uses FORWARD_STEER)
    public static void encoderSteerForwardLine(double threshold, double power, boolean stop) throws InterruptedException {
        Util.setRightPowers(power * FORWARD_STEER);
        Util.setLeftPowers(power / FORWARD_STEER);

        while (Util.ods.getLightDetected() < threshold) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // steer forward for a specified distance at a specified power; stop when a line is detected or max distance is exceeded (uses FORWARD_STEER)
    public static int encoderSteerForwardLineSafe(double threshold, double power, int maxDist, boolean stop) throws InterruptedException {
        int start = Util.right.getCurrentPosition();

        Util.setRightPowers(power * FORWARD_STEER);
        Util.setLeftPowers(power / FORWARD_STEER);

        while ((Util.ods.getLightDetected() < threshold) && (Util.right.getCurrentPosition() - start) < maxDist) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);

        if ((Util.right.getCurrentPosition() - start) > maxDist) return -1;
        return 0;
    }

    // steer backward for a specified distance at a specified power; stop when a line is detected (uses FORWARD_STEER)
    public static void encoderSteerBackwardLine(double threshold, double power, boolean stop) throws InterruptedException {
        Util.setRightPowers(-power * BACKWARD_STEER);
        Util.setLeftPowers(-power / BACKWARD_STEER);

        while (Util.ods.getLightDetected() < threshold) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);
    }

    // steer backward for a specified distance at a specified power; stop when a line is detected or max distance is exceeded (uses FORWARD_STEER)
    public static int encoderSteerBackwardLineSafe(double threshold, double power, int maxDist, boolean stop) throws InterruptedException {
        int start = Util.right.getCurrentPosition();

        Util.setRightPowers(-power * FORWARD_STEER);
        Util.setLeftPowers(-power / FORWARD_STEER);

        while ((Util.ods2.getLightDetected() < threshold) && (start - Util.right.getCurrentPosition()) < maxDist) Thread.sleep(20);

        if (stop) Util.setAllPowers(0);

        if (start - (Util.right.getCurrentPosition()) > maxDist) return -1;
        return 0;
    }

    // move straight forward with gyro for a specified distance at a specified power; stop if directed to
    public static void PID_Forward(double distance, double power, boolean stop, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        PID.resetDriveIntegral();
        double start = Util.right.getCurrentPosition();
        Util.setAllPowers(0.1);
        Thread.sleep(30);
        Util.setAllPowers(0.15);
        Thread.sleep(75);
        while (Util.right.getCurrentPosition() < (start + (distance * 0.98))) {
            PID.PIsetMotors(gyro, powerFactor * power);
            Thread.sleep(10);
        }
        if (stop) Util.setAllPowers(0);
    }

    // move straight backward with gyro for a specified distance at a specified power; stop if directed to
    public static void PID_Backward(double distance, double power, boolean stop, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        PID.resetDriveIntegral();
        double start = Util.right.getCurrentPosition();
        Util.setAllPowers(-0.1);
        Thread.sleep(30);
        while (Util.right.getCurrentPosition() > (start - (distance * 0.98))) {
            PID.PIsetMotors(gyro, powerFactor * -power);
            Thread.sleep(10);
        }
        if (stop) Util.setAllPowers(0);
    }

    public static void PID_Forward(double distance, double power, boolean stop, BNO055IMU imu) throws InterruptedException {
        double zero = PID.heading(imu);
        PID.resetDriveIntegral();
        double start = Util.right.getCurrentPosition();
        Util.setAllPowers(0.1);
        Thread.sleep(30);
        Util.setAllPowers(0.15);
        Thread.sleep(75);
        while (Util.right.getCurrentPosition() < (start + (distance * 0.98))) {
            PID.PIsetMotors(imu, powerFactor * power, zero);
            Thread.sleep(10);
        }
        if (stop) Util.setAllPowers(0);
    }

    // TODO: confirm that WHEELBASE_PROBABLY is actually wheelbase
    private static int WHEELBASE_PROBABLY = 15, WHEEL_DIAM = 4, TICS_PER_ROTATION = Util.NEVEREST_TICS_PER_ROTATION;

    // turn right a specified distance at a specified power
    public static void encoderTurnRight(double degrees, double power) throws InterruptedException {
        Util.resetEncoders();

        double dist = degrees / 360;
        dist = dist * WHEELBASE_PROBABLY / WHEEL_DIAM * TICS_PER_ROTATION;

        Util.setRightPowers(-power);
        Util.setLeftPowers(power);

        while (((Math.abs(r.getCurrentPosition()) + Math.abs(l.getCurrentPosition())) / 2) < dist) {
            /*Util.telemetry("rf", Util.rightFront.getCurrentPosition(), false);
            Util.telemetry("lf", Util.leftFront.getCurrentPosition(), false);
            Util.telemetry("rb", Util.right.getCurrentPosition(), false);
            Util.telemetry("lb", Util.left.getCurrentPosition(), true);*/
            Thread.sleep(20);
        }

        Util.setAllPowers(0);
    }

    // turn left a specified distance at a specified power (calls encoderTurnRight with opposite power)
    public static void encoderTurnLeft(int degrees, double power) throws InterruptedException {
        encoderTurnRight(degrees, -power);
    }

    // variables used by ramped turns
    final static double RAMP_UP_DELTA = 0.02, RAMP_DOWN_DELTA = 0.025;
    final static int EXTRA_DEGREES = 3; // 1

    // turn right a specified distance; ramp up to and then down from a target power
    public static void rampEncoderTurnRight(double targetDegrees, double targetPower) throws InterruptedException {
        Util.resetEncoders();

        double dist = targetDegrees / 360;
        dist = dist * WHEELBASE_PROBABLY / WHEEL_DIAM * TICS_PER_ROTATION;

        double power = MIN_POWER - RAMP_UP_DELTA;
        boolean reachedTargetPower = false;
        while (((Math.abs(r.getCurrentPosition()) + Math.abs(l.getCurrentPosition())) / 2) < (dist / 2)) {
            if (!reachedTargetPower) power += RAMP_UP_DELTA;
            if (power > targetPower) {
                power = targetPower;
                reachedTargetPower = true;
            }
            Util.setRightPowers(-power);
            Util.setLeftPowers(power);
            Thread.sleep(10);
        }
        power = targetPower;
        boolean reachedMinPower = false;
        while (((Math.abs(r.getCurrentPosition()) + Math.abs(l.getCurrentPosition())) / 2) < dist) {
            if (!reachedMinPower) power -= RAMP_DOWN_DELTA;
            if (power < MIN_POWER) {
                power = MIN_POWER;
                reachedMinPower = true;
            }
            Util.setRightPowers(-power);
            Util.setLeftPowers(power);
            Thread.sleep(10);
        }
        Util.setAllPowers(0);
    }

    // turn left a specified distance; ramp up to and then down from a target power
    public static void rampEncoderTurnLeft(double targetDegrees, double targetPower) throws InterruptedException {
        Util.resetEncoders();

        double dist = targetDegrees / 360;
        dist = dist * WHEELBASE_PROBABLY / WHEEL_DIAM * TICS_PER_ROTATION;

        double power = MIN_POWER - RAMP_UP_DELTA;
        boolean reachedTargetPower = false;
        while (((Math.abs(r.getCurrentPosition()) + Math.abs(l.getCurrentPosition())) / 2) < (dist / 2)) {
            if (!reachedTargetPower) power += RAMP_UP_DELTA;
            if (power > targetPower) {
                power = targetPower;
                reachedTargetPower = true;
            }
            Util.setRightPowers(power);
            Util.setLeftPowers(-power);
            Thread.sleep(10);
        }
        power = targetPower;
        boolean reachedMinPower = false;
        while (((Math.abs(r.getCurrentPosition()) + Math.abs(l.getCurrentPosition())) / 2) < dist) {
            if (!reachedMinPower) power -= RAMP_DOWN_DELTA;
            if (power < MIN_POWER) {
                power = MIN_POWER;
                reachedMinPower = true;
            }
            Util.setRightPowers(power);
            Util.setLeftPowers(-power);
            Thread.sleep(10);
        }
        Util.setAllPowers(0);
    }

    // turn right a specified number of degrees at a specified power using gyro for accuracy
    // not as accurate as encoder turns because Modern Robotics gyro does not handle fast spins
    public static void gyroTurnRight(double degreeTarget, double targetPower, GyroSensor gyro) throws InterruptedException {
        resetGyroHeading(gyro);
        double power = MIN_POWER;
        boolean reachedMinPower = false;
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

    // turn left a specified number of degrees at a specified power using gyro for accuracy
    // not as accurate as encoder turns because Modern Robotics gyro does not handle fast spins
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

    // reset encoders in specified list
    public static void resetEncoders(DcMotor[] motors) throws InterruptedException {
        Util.resetEncoders(motors);
    }

    // reset encoders (resets all motors with encoders in use)
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
