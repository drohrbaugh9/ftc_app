package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import static org.firstinspires.ftc.teamcode.I2C_ColorSensor.synchBack;

@TeleOp(name = "Test", group = "Test")
//@Autonomous(name = "Test", group = "Test")
//@Disabled
public class TestOpMode extends LinearOpMode {

    DcMotor shooter1;
    DcMotor[] motors;

    // shooter encoder test
    /**/
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

    // color sensors test
    /*/
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

    // color sensors test
    /*/
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
