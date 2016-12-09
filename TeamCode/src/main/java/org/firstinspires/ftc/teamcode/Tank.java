package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@TeleOp(name = "Tank", group = "Test")
@Disabled
public class Tank extends OpMode {

    DcMotor right, left;
    OpticalDistanceSensor ods;
    I2cDevice color;
    final double POWER_FACTOR = 0.8;

    public void init() {
        right = hardwareMap.dcMotor.get("right"); right.setDirection(DcMotor.Direction.REVERSE);
        left = hardwareMap.dcMotor.get("left");
        ods = hardwareMap.opticalDistanceSensor.get("ods");
        color = hardwareMap.i2cDevice.get("color");
    }

    public void loop() {
        double r = -gamepad1.right_stick_y;
        double l = -gamepad1.left_stick_y;

        right.setPower(r * POWER_FACTOR);
        left.setPower(l * POWER_FACTOR);

        /*telemetry.addData("d", "getLightDetected(): " + ods.getLightDetected());
        telemetry.addData("r", "getRawLightDetected(): " + ods.getRawLightDetected());

        updateTelemetry(telemetry);*/
    }
}
