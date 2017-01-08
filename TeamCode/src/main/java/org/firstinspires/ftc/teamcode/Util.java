package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

public final class Util {

    protected static boolean init = false;
    //protected static boolean gyroEnabled = false;

    protected static DcMotor rightBack, leftBack, rightFront, leftFront;
    protected static DcMotor shooter1, shooter2, intake;

    protected static Servo ballFeeder, upDown;

    protected static OpticalDistanceSensor ods;

    protected static GyroSensor gyro;

    protected static boolean sensors = true, servos = true;

    protected final static double SEC_TO_NSEC = 1000000000, NEVEREST_37_TICS_PER_ROTATION = 44.4;
    protected final static double POWER_LIMIT = 1;
    protected static final double SHOOT = 0.5, LOAD = 0.95;
    protected static final double BEACON_UP = 0.6, BEACON_DOWN = 0.95;

    //private static LinearOpMode linearOpMode;
    protected static LinearOpMode linearOpMode;
    private static DcMotor[] /*motors,*/ motorsWithEncoders;

    private Util() throws Exception {
        throw new Exception();
    }

    public static void init(LinearOpMode opMode) throws InterruptedException {
        linearOpMode = opMode;

        DcMotor[] temp;
        DcMotor[] tempWithEncoders;

        // drive motors
        rightBack = opMode.hardwareMap.dcMotor.get("rightBack"); rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack = opMode.hardwareMap.dcMotor.get("leftBack");
        rightFront = opMode.hardwareMap.dcMotor.get("rightFront"); rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftFront = opMode.hardwareMap.dcMotor.get("leftFront");

        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        AutoUtil.r = rightFront; AutoUtil.l = leftFront;


        temp = new DcMotor[4]; temp[0] = rightBack; temp[1] = leftBack; temp[2] = rightFront; temp[3] = leftFront;
        tempWithEncoders = temp;

        //motors = temp;
        motorsWithEncoders = tempWithEncoders;

        // shooter motors
        shooter1 = getMotor("shooter1");
        shooter2 = getMotor("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // intake motor
        intake = getMotor("intake");

        // servos
        if (servos) {
            ballFeeder = getServo("ballFeeder"); ballFeeder.setPosition(LOAD);
            upDown = getServo("upDown"); upDown.setPosition(BEACON_UP);
        }

        // sensors
        if (sensors) {
            ods = opMode.hardwareMap.opticalDistanceSensor.get("ods");
            I2C_ColorSensor.init(opMode);
            gyro = opMode.hardwareMap.gyroSensor.get("gyro");
        }

        //resetEncoders();

        init = true;
    }

    public static DcMotor getMotor(HardwareMap map, String deviceName) {
        return map.dcMotor.get(deviceName);
    }

    public static DcMotor getMotor(String deviceName) {
        return linearOpMode.hardwareMap.dcMotor.get(deviceName);
    }

    public static Servo getServo(HardwareMap map, String deviceName) {
        return map.servo.get(deviceName);
    }

    public static Servo getServo(String deviceName) {
        return linearOpMode.hardwareMap.servo.get(deviceName);
    }

    public static void resetEncoders(LinearOpMode opMode, DcMotor[] motorList) throws InterruptedException {
        for (DcMotor motor : motorList) motor.setMode(DcMotor.RunMode.RESET_ENCODERS);
        Thread.sleep(200);
        //while (motorList[0].getMode() != DcMotorController.RunMode.RESET_ENCODERS);
        for (DcMotor motor : motorList) motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        Thread.sleep(200);
    }

    public static void resetEncoders(LinearOpMode opMode) throws InterruptedException {
        resetEncoders(opMode, motorsWithEncoders);
    }

    public static void resetEncoders(DcMotor[] motorList) throws InterruptedException {
        resetEncoders(linearOpMode, motorList);
    }

    public static void resetEncoders() throws InterruptedException {
        resetEncoders(linearOpMode, motorsWithEncoders);
    }

    public static double getBatteryVoltage() {
        return linearOpMode.hardwareMap.voltageSensor.iterator().next().getVoltage();
    }

    public static double getGamepadRightJoystickY(Gamepad gamepad) {
        double joystick;
        joystick = gamepad.right_stick_y;
        if (joystick != 0) return -joystick;
        return joystick;
    }

    public static double getGamepadLeftJoystickY(Gamepad gamepad) {
        double joystick;
        joystick = gamepad.left_stick_y;
        if (joystick != 0) return -joystick;
        return joystick;
    }

    public static void setRightPowers(double p) {
        rightBack.setPower(p);
        rightFront.setPower(p);
    }

    public static void setLeftPowers(double p) {
        leftBack.setPower(p);
        leftFront.setPower(p);
    }

    public static void setFrontPowers(double p) {
        rightFront.setPower(p);
        leftFront.setPower(p);
    }

    public static void setBackPowers(double p) {
        rightBack.setPower(p);
        leftBack.setPower(p);
    }

    public static void setAllPowers(double p) {
        rightBack.setPower(p);
        leftBack.setPower(p);
        rightFront.setPower(p);
        leftFront.setPower(p);
    }

    public static void setMotorsPowers(DcMotor[] motors, double p) {
        for (DcMotor motor : motors) {
            motor.setPower(p);
        }
    }

    public static void log(String message) {
        if (!FtcRobotControllerActivity.LOG) return;
        RobotLog.i(message);
    }

    public static void telemetry(String key, String data) {
        Util.linearOpMode.telemetry.addData(key, data);
        Util.linearOpMode.telemetry.update();
    }

    public static void telemetry(String key, int data) {
        Util.linearOpMode.telemetry.addData(key, data);
        Util.linearOpMode.telemetry.update();
    }

    public static void telemetry(String key, double data) {
        Util.linearOpMode.telemetry.addData(key, data);
        Util.linearOpMode.telemetry.update();
    }

    public static void telemetry(String key, String data, boolean update) {
        Util.linearOpMode.telemetry.addData(key, data);
        if (update) Util.linearOpMode.telemetry.update();
    }

    public static void telemetry(String key, int data, boolean update) {
        Util.linearOpMode.telemetry.addData(key, data);
        if (update) Util.linearOpMode.telemetry.update();
    }

    public static void telemetry(String key, double data, boolean update) {
        Util.linearOpMode.telemetry.addData(key, data);
        if (update) Util.linearOpMode.telemetry.update();
    }
}
