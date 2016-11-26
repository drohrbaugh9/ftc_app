package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous (name = "WestCoast_AutoTest", group = "Test")
@Disabled
public class WestCoast_AutoTest extends LinearOpMode {

    int pos = 0;

    DcMotor rightFront, leftFront, rightBack, leftBack;
    DcMotor[] motors;

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

        waitForStart();

        rightFront.setPower(0.3);
        leftFront.setPower(0.3);
        rightBack.setPower(0.3);
        leftBack.setPower(0.3);

        while (leftBack.getCurrentPosition() < 3750);

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

        while (leftBack.getCurrentPosition() < (pos + 200));

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        Thread.sleep(200);

        pos = leftBack.getCurrentPosition();

        rightFront.setPower(0.33);
        leftFront.setPower(0.27);
        rightBack.setPower(0.33);
        leftBack.setPower(0.27);

        while (leftBack.getCurrentPosition() < (pos + 3500));

        rightFront.setPower(0.11);
        leftFront.setPower(0.09);
        rightBack.setPower(0.11);
        leftBack.setPower(0.09);

        pos = leftBack.getCurrentPosition();

        while (leftBack.getCurrentPosition() < (pos + 3000));

        rightFront.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        leftBack.setPower(0);

        //Thread.sleep(200);
    }
}
