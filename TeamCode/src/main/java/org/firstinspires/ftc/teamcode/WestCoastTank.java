package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "WestCoastTank", group = "Test")
//@Disabled
public class WestCoastTank extends OpMode {

    DcMotor rightFront, leftFront, rightBack, leftBack;
    final double POWER_FACTOR = 1;
    boolean straight;

    public void init() {
        rightFront = hardwareMap.dcMotor.get("rightFront"); rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightBack = hardwareMap.dcMotor.get("rightBack"); rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.dcMotor.get("leftBack");

        straight = false;
    }

    public void loop() {
        double r = -gamepad1.right_stick_y;
        double l = -gamepad1.left_stick_y;

        if (straight) {
            rightFront.setPower(r * POWER_FACTOR);
            leftFront.setPower(r * POWER_FACTOR);
            rightBack.setPower(r * POWER_FACTOR);
            leftBack.setPower(r * POWER_FACTOR);
        } else {
            rightFront.setPower(r * POWER_FACTOR);
            leftFront.setPower(l * POWER_FACTOR);
            rightBack.setPower(r * POWER_FACTOR);
            leftBack.setPower(l * POWER_FACTOR);
        }

        if (gamepad1.a && !straight) straight = true;
        else if (gamepad1.b && straight) straight = false;
    }
}
