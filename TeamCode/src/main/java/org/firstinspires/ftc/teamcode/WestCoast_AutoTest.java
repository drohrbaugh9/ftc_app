package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "WestCoast_AutoTest", group = "Test")
//@Disabled
public class WestCoast_AutoTest extends LinearOpMode {

    int pos = 0;

    final boolean RED_ALLIANCE = true;

    DcMotor rightFront, leftFront, rightBack, leftBack;
    DcMotor[] motors;

    Servo upDown;

    OpticalDistanceSensor ods;

    public void runOpMode() throws InterruptedException {
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        leftBack = hardwareMap.dcMotor.get("leftBack");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        motors = new DcMotor[1]; motors[0] = leftBack;

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        Util.resetEncoders(this, motors);

        I2C_ColorSensor.init(this);

        upDown = hardwareMap.servo.get("upDown");

        upDown.setPosition(0.6);

        ods = hardwareMap.opticalDistanceSensor.get("ods");

        waitForStart();

        rightFront.setPower(0.3);
        leftFront.setPower(0.3);
        rightBack.setPower(0.3);
        leftBack.setPower(0.3);

        while (leftBack.getCurrentPosition() < 4000);

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        Thread.sleep(400);

        pos = leftBack.getCurrentPosition();

        rightFront.setPower(-0.3);
        leftFront.setPower(0.3);
        rightBack.setPower(-0.3);
        leftBack.setPower(0.3);

        while (leftBack.getCurrentPosition() < (pos + 500));

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        Thread.sleep(200);

        pos = leftBack.getCurrentPosition();

        rightFront.setPower(0.33);
        leftFront.setPower(0.27);
        rightBack.setPower(0.33);
        leftBack.setPower(0.27);

        while (leftBack.getCurrentPosition() < (pos + 2500));

        rightFront.setPower(0.11);
        leftFront.setPower(0.09);
        rightBack.setPower(0.11);
        leftBack.setPower(0.09);

        telemetry.addData("ods", ods.getLightDetected());
        telemetry.update();

        while (ods.getLightDetected() < 0.50);

        telemetry.addData("ods", ods.getLightDetected());
        telemetry.update();

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        Thread.sleep(200);

        pos = leftBack.getCurrentPosition();

        rightBack.setPower(-0.24);
        leftBack.setPower(-0.16);
        rightFront.setPower(-0.24);
        leftFront.setPower(-0.16);

        while (leftBack.getCurrentPosition() > (pos - 150));

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

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

        /*while (opModeIsActive()) {
            telemetry.addData("ods", ods.getLightDetected());
            telemetry.update();
        }*/
    }

    public void forward() {
        pos = leftBack.getCurrentPosition();

        rightBack.setPower(0.24);
        leftBack.setPower(0.16);
        rightFront.setPower(0.24);
        leftFront.setPower(0.16);

        while (leftBack.getCurrentPosition() < (pos + 500)) ;

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
    }

    public void backward() throws InterruptedException {
        pos = leftBack.getCurrentPosition();

        rightBack.setPower(-0.24);
        leftBack.setPower(-0.16);
        rightFront.setPower(-0.24);
        leftFront.setPower(-0.16);

        while (leftBack.getCurrentPosition() > (pos - 250));

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);

        Thread.sleep(200);

        pos = leftBack.getCurrentPosition();

        rightBack.setPower(0.24);
        leftBack.setPower(0.16);
        rightFront.setPower(0.24);
        leftFront.setPower(0.16);

        while (leftBack.getCurrentPosition() > (pos + 500));

        rightBack.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        leftFront.setPower(0);
    }
}
