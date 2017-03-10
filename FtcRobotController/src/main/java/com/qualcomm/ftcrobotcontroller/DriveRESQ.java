package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/* This is a basic drive program with an added functionality
 * for allowing random people to drive your robot.
 * Gamepad1 controls the robot unless gamepad2 overrides
 * gamepad1 for safety reasons or otherwise.
 */

public class DriveRESQ extends OpMode {

    DcMotor rightBack, leftBack, rightFront, leftFront;
    private boolean override = false;

    public DriveRESQ() {
    }

    public void init() {
        rightBack = hardwareMap.dcMotor.get("right");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.dcMotor.get("left");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftFront = hardwareMap.dcMotor.get("leftFront");
    }

    public void loop() {
        double r;
        double l;

        if (override) {
            r = -gamepad2.right_stick_y;
            l = -gamepad2.left_stick_y;
        } else {
            r = -gamepad1.right_stick_y;
            l = -gamepad1.left_stick_y;
        }

        r = Range.clip(r, -1, 1);
        l = Range.clip(l, -1, 1);

        rightBack.setPower(r * 0.7);
        rightFront.setPower(r);
        leftBack.setPower(l * 0.7);
        leftFront.setPower(l);

        if (gamepad2.a && !override) {
            override = true;
            while (gamepad1.a) ;
        } else if (gamepad2.a && override) {
            override = false;
        }
    }
}
