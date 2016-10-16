package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

public final class Util {

    protected static DcMotor rightBack, rightFront, leftBack, leftFront, arm, intake, hanger;
    protected static Servo rightDoor, leftDoor, rightTrigger, leftTrigger;
    protected static GyroSensor gyro;
    protected static ColorSensor color;
    protected static boolean init = false;
    protected static ModernRoboticsUsbDcMotorController c;
    //protected static boolean gyroEnabled = false;

    protected static final double LEFT_DOOR_MIN = 0.02 /*CLOSED*/, LEFT_DOOR_MAX = 0.4; //OPEN
    protected static final double LEFT_DOOR_CLOSED = LEFT_DOOR_MIN;
    protected static final double RIGHT_DOOR_MIN = 0.5 /*OPEN*/, RIGHT_DOOR_MAX = 0.89; //CLOSED
    protected static final double RIGHT_DOOR_CLOSED = RIGHT_DOOR_MAX;
    protected static final double POWER_LIMIT = 0.7, BACK_SCALE = 1.3;
    protected static final double STARTING_POWER = 0.5;
    protected static final double LEFT_TRIGGER_OUT = 0, LEFT_TRIGGER_IN = 0.6, LEFT_TRIGGER_STOW = 0.65;
    protected static final double RIGHT_TRIGGER_OUT = 0.67, RIGHT_TRIGGER_IN = 0.06, RIGHT_TRIGGER_STOW = 0.04;
    protected static final boolean SENSORS = true, SERVOS = true;

    protected final static double SEC_TO_NSEC = 1000000000, POWER_FLOAT = 100;

    //private static LinearOpMode linearOpMode;
    protected static LinearOpMode linearOpMode;
    private static DcMotor[] motors, motorsWithEncoders;

    private Util() throws Exception {
        throw new Exception();
    }

    public static void init(LinearOpMode opMode) throws InterruptedException {
        linearOpMode = opMode;

        // motors
        rightBack = getMotor("right");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack = getMotor("left");
        rightFront = getMotor("rightFront");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftFront = getMotor("leftFront");
        arm = getMotor("arm");
        intake = getMotor("intake");
        hanger = getMotor("hanger");

        c = (ModernRoboticsUsbDcMotorController) rightBack.getController();

        DcMotor[] temp = {rightBack, leftBack, rightFront, leftFront, arm, intake};
        DcMotor[] tempWithEncoders = {rightBack, leftBack, rightFront, leftFront};//, arm};

        motors = temp;
        motorsWithEncoders = tempWithEncoders;

        // servos
        if (SERVOS) {
            leftDoor = getServo("leftDoor");
            leftDoor.setPosition(LEFT_DOOR_CLOSED);
            rightDoor = getServo("rightDoor");
            rightDoor.setPosition(RIGHT_DOOR_CLOSED);
            rightTrigger = getServo("rightTrigger");
            rightTrigger.setPosition(RIGHT_TRIGGER_STOW);
            leftTrigger = getServo("leftTrigger");
            leftTrigger.setPosition(LEFT_TRIGGER_STOW);
        }

        // sensors
        if (SENSORS) {
            gyro = linearOpMode.hardwareMap.gyroSensor.get("gyro");
            //color = linearOpMode.hardwareMap.colorSensor.get("colorSensor1");

            AutoUtil.calibrateGyro(gyro);
            AutoUtil.resetGyroHeading(gyro);
        }
        
        resetEncoders();

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
        for (int i = 0; i < 11; i++) opMode.waitOneFullHardwareCycle();
        //while (motorList[0].getMode() != DcMotorController.RunMode.RESET_ENCODERS);
        for (DcMotor motor : motorList) motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        for (int i = 0; i < 11; i++) opMode.waitOneFullHardwareCycle();
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
        return c.getVoltage();
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
        if (p == POWER_FLOAT) {
            rightBack.setPowerFloat();
            rightFront.setPowerFloat();
            return;
        }
        rightBack.setPower(p);
        rightFront.setPower(p);
    }

    public static void setLeftPowers(double p) {
        if (p == POWER_FLOAT) {
            leftBack.setPowerFloat();
            leftFront.setPowerFloat();
            return;
        }
        leftBack.setPower(p);
        leftFront.setPower(p);
    }

    public static void setFrontPowers(double p) {
        if (p == POWER_FLOAT) {
            rightFront.setPowerFloat();
            leftFront.setPowerFloat();
            return;
        }
        rightFront.setPower(p);
        leftFront.setPower(p);
    }

    public static void setBackPowers(double p) {
        if (p == POWER_FLOAT) {
            rightBack.setPowerFloat();
            leftBack.setPowerFloat();
            return;
        }
        rightBack.setPower(p);
        leftBack.setPower(p);
    }

    public static void setAllPowers(double p) {
        if (p == POWER_FLOAT) {
            rightBack.setPowerFloat();
            rightFront.setPowerFloat();
            leftBack.setPowerFloat();
            leftFront.setPowerFloat();
            return;
        }
        rightBack.setPower(p);
        rightFront.setPower(p);
        leftBack.setPower(p);
        leftFront.setPower(p);
    }
    
    public static void setMotorsPowers(DcMotor[] motors, double p) {
        if (p == POWER_FLOAT) {
            for (DcMotor motor : motors) {
                motor.setPowerFloat();
            }
            return;
        }
        for (DcMotor motor : motors) {
            motor.setPower(p);
        }
    }

    public static void log(String message) {
        if (!FtcRobotControllerActivity.LOG) return;
        RobotLog.i(message);
    }
}
