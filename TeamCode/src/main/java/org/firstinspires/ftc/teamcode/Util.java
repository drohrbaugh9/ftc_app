package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.RelicRecovery.getGlyphs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.File;
import android.os.Environment;

public final class Util {

    protected static Writer fileWriter;

    public static boolean init = false;
    //protected static boolean gyroEnabled = false;

    public static boolean liftTrayforMulti;

    public static DcMotor rightBack;
    public static DcMotor rightFront;
    public static DcMotor leftBack;
    public static DcMotor leftFront;
    public static DcMotor intake1;
    public static DcMotor intake2;
    public static DcMotor lift;
    public static DcMotor relic;
    public static DcMotor pusher;

    public static Servo JewelArmServo;
    public static Servo JewelWhackerServo;
    public static Servo RelicArmServo;
    public static Servo RelicClawServo;
//    public static Servo gripper1;
//    public static Servo gripper2;
   // public static Servo wackerHolder;

    public static Servo rightTiltServo;
    public static Servo leftTiltServo;
    public static Servo tableBackstopServo;
    public static Servo distanceSensorArm;

    protected static OpticalDistanceSensor ods, ods2;

    public static DistanceSensor glyphCounterDistance;
    public static ColorSensor glyphCounterColor;

    public static DistanceSensor columnAlignDistance;

    protected static GyroSensor gyro;

    public static BNO055IMU imu;

    protected static boolean colorSensors = false, otherSensors = true, servos = true;

    protected final static int NEVEREST_TICS_PER_ROTATION = 1120;
    protected final static double SEC_TO_NSEC = 1000000000;
    protected final static double NEVEREST_37_TICS_PER_ROTATION = 103.6;
    protected final static double POWER_LIMIT = 1;

    static double JewelArmServoUp = 0.85;
    static double JewelWhackerServoUp = 1;

    static double tableBackstopServoDown = 0.57;
    static double tableBackstopServoUp = 0.73;


    public static double distanceSensorArmUp = 0.0;
    public static double distanceSensorArmDown = 0.93;
    public static double distanceSensorArmThreeQuartersDown = 0.65;

    //    static double gripper1init = 0.7;
//    static double gripper2init = 0.4;

    //private static LinearOpMode linearOpMode;
    public static LinearOpMode linearOpMode;
    public static DcMotor[] /*motors,*/ motorsWithEncoders;

    //useless variables
    static DcMotor right,left;

    private Util() throws Exception {
        throw new Exception();
    }

    /* called at the beginning of an opmode to set up all the electronics for control
     * i.e. to get all the things from the hardware map
     * This standardizes all the setup and saves a lot of repeated coding for each new opmode
     */
    public static void init(LinearOpMode opMode) throws InterruptedException {
        linearOpMode = opMode;

        DcMotor[] temp;
        DcMotor[] tempWithEncoders;

        //imu

        opMode.telemetry.addData("imu status", "Wait for the imu to initialize!"); opMode.telemetry.update();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = linearOpMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // drive motors
        rightBack = opMode.hardwareMap.dcMotor.get("rightBack");
        leftBack = opMode.hardwareMap.dcMotor.get("leftBack");
        rightFront = opMode.hardwareMap.dcMotor.get("rightFront");
        leftFront = opMode.hardwareMap.dcMotor.get("leftFront");


        //other motors

        intake1 = opMode.hardwareMap.dcMotor.get("intake_left"); intake1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake2 = opMode.hardwareMap.dcMotor.get("intake_right");
        lift = opMode.hardwareMap.dcMotor.get("lift");
        relic = opMode.hardwareMap.dcMotor.get("relic");


        JewelArmServo = opMode.hardwareMap.servo.get("JewelArmServo");
        JewelArmServo.setPosition(JewelArmServoUp);

        JewelWhackerServo = opMode.hardwareMap.servo.get("JewelWhackerServo");
        JewelWhackerServo.setPosition(JewelWhackerServoUp);

        RelicArmServo = opMode.hardwareMap.servo.get("relicArm");
        RelicArmServo.setPosition(0.07);

        RelicClawServo = opMode.hardwareMap.servo.get("relicGrabber");
        RelicClawServo.setPosition(0.53);

        //TODO: set init positions for the tilt servos
        rightTiltServo = opMode.hardwareMap.servo.get("rightTiltServo");
        leftTiltServo = opMode.hardwareMap.servo.get("leftTiltServo");
        tableBackstopServo = opMode.hardwareMap.servo.get("tableBackstopServo");
        tableBackstopServo.setPosition(tableBackstopServoDown);


        motorsWithEncoders = new DcMotor[]{rightFront, rightBack, leftFront, leftBack, lift, relic};


        glyphCounterColor = opMode.hardwareMap.colorSensor.get("glyphCounterDistanceColor");
        glyphCounterDistance = opMode.hardwareMap.get(DistanceSensor.class,"glyphCounterDistanceColor");


        distanceSensorArm = opMode.hardwareMap.servo.get("distanceSensorArm");
        distanceSensorArm.setPosition(distanceSensorArmUp);
        columnAlignDistance = opMode.hardwareMap.get(DistanceSensor.class, "columnDistanceSensor");

        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        liftTrayforMulti = false;

        setDriveModeBrake();

        AutoUtil.r = rightBack; AutoUtil.l = leftBack;


        try {
            File logfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "LogFile" + System.nanoTime() + ".txt");
            //logfile.createNewFile();
            FileWriter writer = new FileWriter(logfile);
            fileWriter = new BufferedWriter(writer);
        } catch (IOException e) {
            opMode.telemetry.addData("log", "log file unavailable");
            //throw new RuntimeException("Cannot write to file", e);
        }
        
        opMode.telemetry.addData("imu status", "imu is initialized"); opMode.telemetry.update();

        init = true;
    }

    // get the DcMotor with name <deviceName> from the hardware map <map>
    public static DcMotor getMotor(HardwareMap map, String deviceName) {
        return map.dcMotor.get(deviceName);
    }

    public static DcMotor getMotor(String deviceName) {
        return linearOpMode.hardwareMap.dcMotor.get(deviceName);
    }
    // get the servo with name <deviceName> from the hardware map <map>
    public static Servo getServo(HardwareMap map, String deviceName) {
        return map.servo.get(deviceName);
    }

    public static Servo getServo(String deviceName) {
        return linearOpMode.hardwareMap.servo.get(deviceName);
    }

    // reset the encoders, includes several methods with different parameters
    public static void resetEncoders(LinearOpMode opMode, DcMotor[] motorList) throws InterruptedException {
        for (DcMotor motor : motorList) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Thread.sleep(200);
        //while (motorList[0].getMode() != DcMotorController.RunMode.RESET_ENCODERS);
        for (DcMotor motor : motorList) motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        leftBack.setPower(p);

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


    public static  void mechanumsBackspinStop(double p) {
        rightBack.setPower(-p); //finetune
        leftBack.setPower(-p);
        rightFront.setPower(-p);
        leftFront.setPower(-p);


    }

    // tell the motors to just coast when given a power of 0
    public static void setDriveModeFloat() {
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }

    // tell the motors to brake when given a power of 0
    public static void setDriveModeBrake() {
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public static void setMotorsPowers(DcMotor[] motors, double p) {
        for (DcMotor motor : motors) {
            motor.setPower(p);
        }
    }

    public static void log(String message) {
        //if (!FtcRobotControllerActivity.LOG) return;
        //RobotLog.i(message);

        try {
            fileWriter.write(message + "\n");
        } catch (IOException e) {}

        /* put this code in your opmode's stop method:
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException("Cannot close file", e);
            }
        */
    }

    // add data to the telemetry and then update, duplicate methods with different parameters below
    public static void telemetry(String key, boolean data) {
        Util.linearOpMode.telemetry.addData(key, data);
        Util.linearOpMode.telemetry.update();
    }

    public static void telemetry(String key, getGlyphs.glyphColor data) {
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

    public static void telemetry(String key, getGlyphs.glyphColor data, boolean update) {
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

    public static void telemetry(String key, float data, boolean update) {
        Util.linearOpMode.telemetry.addData(key, data);
        if (update) Util.linearOpMode.telemetry.update();
    }
}
