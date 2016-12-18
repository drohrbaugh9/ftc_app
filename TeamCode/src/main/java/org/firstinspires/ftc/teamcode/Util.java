package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
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

    protected static Servo upDown;

    protected static OpticalDistanceSensor ods;

    protected static GyroSensor gyro;

    protected static final boolean SENSORS = true, SERVOS = false;

    protected final static double SEC_TO_NSEC = 1000000000, NEVEREST_37_TICS_PER_ROTATION = 44.4;
    protected final static double POWER_LIMIT = 1;
    protected final static boolean TANK = false;

    //private static LinearOpMode linearOpMode;
    protected static LinearOpMode linearOpMode;
    private static DcMotor[] motors, motorsWithEncoders;

    private Util() throws Exception {
        throw new Exception();
    }

    public static void init(LinearOpMode opMode) throws InterruptedException {
        linearOpMode = opMode;

        /*DcMotor[] temp;
        DcMotor[] tempWithEncoders;*/

        // motors
        if (TANK) {
            rightBack = getMotor(opMode.hardwareMap, "right"); rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
            leftBack = getMotor(opMode.hardwareMap, "left");
            /*temp = new DcMotor[2]; temp[0] = rightBack; temp[1] = leftBack;
            tempWithEncoders = temp;*/
        } else {
            rightBack = opMode.hardwareMap.dcMotor.get("rightBack"); rightBack.setDirection(DcMotor.Direction.REVERSE);
            leftBack = opMode.hardwareMap.dcMotor.get("leftBack");
            rightFront = opMode.hardwareMap.dcMotor.get("rightFront"); rightFront.setDirection(DcMotor.Direction.REVERSE);
            leftFront = opMode.hardwareMap.dcMotor.get("leftFront");
            /*temp = new DcMotor[4]; temp[0] = rightBack; temp[1] = leftBack; temp[2] = rightFront; temp[3] = leftFront;
            tempWithEncoders = new DcMotor[1]; tempWithEncoders[0] = leftBack;*/
        }

        /*motors = temp;
        motorsWithEncoders = tempWithEncoders;*/

        // servos
        if (SERVOS) {
            upDown = getServo(opMode.hardwareMap, "upDown");
            upDown.setPosition(0.6);
        }

        // sensors
        if (SENSORS) {
            ods = opMode.hardwareMap.opticalDistanceSensor.get("ods");
            //I2C_ColorSensor.init(opMode); // commented out for gyro testing
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

    /*public static double getBatteryVoltage() {
        return c.getVoltage();
    }*/

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
        if (!TANK) rightFront.setPower(p);
    }

    public static void setLeftPowers(double p) {
        leftBack.setPower(p);
        if (!TANK) leftFront.setPower(p);
    }

    public static void setFrontPowers(double p) {
        if (TANK) return;
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
        if (!TANK) {
            rightFront.setPower(p);
            leftFront.setPower(p);
        }
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
}
