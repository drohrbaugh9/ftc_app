package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

//@TeleOp(name = "Test", group = "Test")
@Autonomous(name = "Test", group = "Test")
@Disabled
public class TestOpMode extends LinearOpMode {

    DcMotor shooter1, shooter2;
    DcMotor[] motors;

    CRServo servo1;

    /**/ //autonmous movement test
    public void runOpMode() throws InterruptedException {
        Util.otherSensors = false; Util.colorSensors = false; Util.servos = false;
        Util.init(this);

        Util.setDriveModeBrake();

        waitForStart();

        Move.accelerateForward(0.5);

        Thread.sleep(2000);

        Move.decelerateForward(0.5);
    }
    /**/

    /*/ // low power test
    public void runOpMode() throws InterruptedException {
        Util.otherSensors = false; Util.colorSensors = false; Util.servos = false;
        Util.init(this);

        Util.setDriveModeFloat();

        waitForStart();

        Util.setAllPowers(.08);

        Thread.sleep(4000);

        Util.setAllPowers(0);

        Thread.sleep(500);
    }
    /**/

    /*/ // steering method test
    public void runOpMode() throws InterruptedException {
        Util.otherSensors = true; Util.colorSensors = false; Util.servos = false;
        Util.init(this);

        waitForStart();

        AutoUtil.encoderSteerForward(4000, 0.3, true);

        Thread.sleep(10000);

        AutoUtil.encoderSteerBackward(4000, 0.3, true);

        Thread.sleep(300);
    }
    /**/

    /*/ // steering test
    public void runOpMode() throws InterruptedException {
        Util.otherSensors = true; Util.colorSensors = false; Util.servos = false;
        Util.init(this);

        waitForStart();

        Util.setAllPowers(0.2);

        Thread.sleep(1000);

        Util.setRightPowers(0.1);
        Util.setLeftPowers(0.9);

        Util.setDriveModeFloat();

        Thread.sleep(500);

        Util.setAllPowers(0);
    }
    /**/

    /*/ // servo test
    public void runOpMode() throws InterruptedException {
        Util.linearOpMode = this;

        servo1 = hardwareMap.crservo.get("servo1");

        servo1.setPower(0);

        waitForStart();

        servo1.setPower(0.9);

        while (opModeIsActive()) Thread.sleep(10);
    }
    /**/

    /*/ // shooter RUN_USING_ENCODER test
    public void runOpMode() throws InterruptedException {
        Util.linearOpMode = this;

        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2");

        shooter1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        shooter1.setPower(0.05);
        shooter2.setPower(-0.05);

        Thread.sleep(5000);

        shooter1.setPower(0);
        shooter2.setPower(0);
    }
    /**/

    /*/ // shooter encoder test
    public void runOpMode() throws InterruptedException {
        Util.linearOpMode = this;

        shooter1 = hardwareMap.dcMotor.get("shooter1");
        motors = new DcMotor[1]; motors[0] = shooter1;

        AutoUtil.resetEncoders(motors);

        waitForStart();

        while (opModeIsActive()) {
            Util.telemetry("shooter1 encoder", shooter1.getCurrentPosition(), true);
        }
    }/**/

    /*/ // color sensors test
    public void runOpMode() throws InterruptedException {
        Util.linearOpMode = this;
        I2C_ColorSensor.init(this);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                I2C_ColorSensor.disable();
                Util.telemetry("color sensors enabled", "" + I2C_ColorSensor.enabled, true);
                Thread.sleep(5000);
                I2C_ColorSensor.enable();
            }

            Util.telemetry("color sensors enabled", "" + I2C_ColorSensor.enabled, false);

            Util.telemetry("frontRed", I2C_ColorSensor.normalizedRed(I2C_ColorSensor.synchFront), false);
            Util.telemetry("frontBlue", I2C_ColorSensor.normalizedBlue(I2C_ColorSensor.synchFront), false);
            Util.telemetry("backRed", I2C_ColorSensor.normalizedRed(synchBack), false);
            Util.telemetry("backBlue", I2C_ColorSensor.normalizedBlue(synchBack), true);

            Thread.sleep(100);
        }
    }/**/

    /*/ // color sensors test
    I2cDevice frontColor, backColor;

    //I2cAddr backColorAddress  = I2cAddr.create8bit(0x3c);
    //I2cAddr frontColorAddress = I2cAddr.create8bit(0x3e);

    I2cController backColorController;
    I2cController frontColorController;

    I2cController.I2cPortReadyCallback backColorCallback;
    I2cController.I2cPortReadyCallback frontColorCallback;

    boolean colorSensorsDisabled = false;

    public void runOpMode() throws InterruptedException {
        Util.linearOpMode = this;

        backColor = hardwareMap.i2cDevice.get("colorBack");
        //backColor.setI2cAddress(backColorAddress);
        backColorController = backColor.getI2cController();
        backColorCallback = backColorController.getI2cPortReadyCallback(backColor.getPort());

        I2C_ColorSensor.synchBack = new I2cDeviceSynchImpl(backColor, I2cAddr.create8bit(0x3c), false);
        I2C_ColorSensor.synchBack.engage();

        frontColor = hardwareMap.i2cDevice.get("colorFront");
        //frontColor.setI2cAddress(frontColorAddress);
        frontColorController = frontColor.getI2cController();
        frontColorCallback = frontColorController.getI2cPortReadyCallback(frontColor.getPort());

        I2C_ColorSensor.synchFront = new I2cDeviceSynchImpl(frontColor, I2cAddr.create8bit(0x3e), false);
        I2C_ColorSensor.synchFront.engage();

        colorSensorsDisabled = false;

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                colorDisable();
                Util.telemetry("color sensors enabled", "" + !colorSensorsDisabled, true);
                Thread.sleep(5000);
                colorEnable();
            }

            Util.telemetry("color sensors enabled", "" + !colorSensorsDisabled, false);

            Util.telemetry("frontRed", I2C_ColorSensor.normalizedRed(I2C_ColorSensor.synchFront), false);
            Util.telemetry("frontBlue", I2C_ColorSensor.normalizedBlue(I2C_ColorSensor.synchFront), false);
            Util.telemetry("backRed", I2C_ColorSensor.normalizedRed(synchBack), false);
            Util.telemetry("backBlue", I2C_ColorSensor.normalizedBlue(synchBack), true);

            Thread.sleep(100);
        }
    }

    public void colorEnable() {
        if(colorSensorsDisabled) {
            if (backColorCallback != null)
                backColorController.registerForI2cPortReadyCallback(backColorCallback, backColor.getPort());
            if (frontColorCallback != null)
                frontColorController.registerForI2cPortReadyCallback(frontColorCallback, frontColor.getPort());
        }
        colorSensorsDisabled = false;

        I2C_ColorSensor.synchBack.engage();
        I2C_ColorSensor.synchFront.engage();
    }

    public void colorDisable() {
        if (!colorSensorsDisabled) {
            backColorController.deregisterForPortReadyCallback(backColor.getPort());
            frontColorController.deregisterForPortReadyCallback(frontColor.getPort());
        }
        colorSensorsDisabled = true;

        I2C_ColorSensor.synchBack.disengage();
        I2C_ColorSensor.synchFront.disengage();
    }
    /**/
}
