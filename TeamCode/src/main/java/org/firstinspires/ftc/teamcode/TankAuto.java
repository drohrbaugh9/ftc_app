package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "TankAuto", group = "Autonomous")
@Disabled
public class TankAuto extends LinearOpMode {

    DcMotor right, left;
    DcMotor[] motors;
    Servo upDown;
    OpticalDistanceSensor ods;

    int pos;

    final boolean RED_ALLIANCE = true;

    public void runOpMode() throws InterruptedException {
        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotor.Direction.REVERSE);
        left = hardwareMap.dcMotor.get("left");

        motors = new DcMotor[2]; motors[0] = right; motors[1] = left;

        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        I2C_ColorSensor.init(this);

        upDown = hardwareMap.servo.get("upDown");

        upDown.setPosition(0.6);

        ods = hardwareMap.opticalDistanceSensor.get("ods");

        Util.resetEncoders(this, motors);

        waitForStart();

        right.setPower(0.3);
        left.setPower(0.3);

        while (right.getCurrentPosition() < 4000);

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(400);

        pos = left.getCurrentPosition();

        right.setPower(-0.3);
        left.setPower(0.3);

        while (left.getCurrentPosition() < (pos + 200));

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);

        pos = left.getCurrentPosition();

        right.setPower(0.36);
        left.setPower(0.24);

        while (left.getCurrentPosition() < (pos + 2500));

        right.setPower(0.24);
        left.setPower(0.16);

        while (ods.getLightDetected() < 0.25);

        right.setPower(0);
        left.setPower(0);

        telemetry.addData("beacon status", "what's a beacon?");

        telemetry.update();

        if (RED_ALLIANCE && I2C_ColorSensor.beaconIsRed()) {
            telemetry.addData("beacon status", "RED");
            forward();
            upDown.setPosition(0.9);
            backward();
        } else if (RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue()) {
            telemetry.addData("beacon status", "BLUE");
            backward();
            upDown.setPosition(0.9);
            forward();
        } else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsRed()) {
            telemetry.addData("beacon status", "RED");
            backward();
            upDown.setPosition(0.9);
            forward();
        } else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue()) {
            telemetry.addData("beacon status", "BLUE");
            forward();
            upDown.setPosition(0.9);
            backward();
        }

        telemetry.update();

        Thread.sleep(200);

        pos = left.getCurrentPosition();

        right.setPower(-0.36);
        left.setPower(-0.24);

        while (left.getCurrentPosition() > (pos - 750));

        upDown.setPosition(0.6);

        right.setPower(-0.24);
        left.setPower(-0.16);

        while (ods.getLightDetected() < 0.25);

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);
        pos = left.getCurrentPosition();

        right.setPower(0.24);
        left.setPower(0.16);

        while (left.getCurrentPosition() < (pos + 250));

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);

        if (RED_ALLIANCE && I2C_ColorSensor.beaconIsRed()) {
            telemetry.addData("beacon status", "RED");
            forward();
            upDown.setPosition(0.9);
            backward();
        } else if (RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue()) {
            telemetry.addData("beacon status", "BLUE");
            backward();
            upDown.setPosition(0.9);
            forward();
        } else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsRed()) {
            telemetry.addData("beacon status", "RED");
            backward();
            upDown.setPosition(0.9);
            forward();
        } else if (!RED_ALLIANCE && I2C_ColorSensor.beaconIsBlue()) {
            telemetry.addData("beacon status", "BLUE");
            forward();
            upDown.setPosition(0.9);
            backward();
        }
    }

    public void forward() {
        pos = left.getCurrentPosition();

        right.setPower(0.24);
        left.setPower(0.16);

        while (left.getCurrentPosition() < (pos + 750));

        right.setPower(0);
        left.setPower(0);
    }

    public void backward() throws InterruptedException {
        pos = left.getCurrentPosition();

        right.setPower(-0.24);
        left.setPower(-0.16);

        while (left.getCurrentPosition() > (pos - 250));

        right.setPower(0);
        left.setPower(0);

        Thread.sleep(200);

        pos = left.getCurrentPosition();

        right.setPower(0.24);
        left.setPower(0.16);

        while (left.getCurrentPosition() > (pos + 500));

        right.setPower(0);
        left.setPower(0);
    }
}
