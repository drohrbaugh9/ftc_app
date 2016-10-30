package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Tank", group = "Test")
//@Disabled
public class Tank extends OpMode {

    DcMotor right, left;
    final double POWER_FACTOR = 0.8;

    public void init() {
        right = hardwareMap.dcMotor.get("right"); right.setDirection(DcMotor.Direction.REVERSE);
        left = hardwareMap.dcMotor.get("left");
    }

    public void loop() {
        double r = -gamepad1.right_stick_y;
        double l = -gamepad1.left_stick_y;

        right.setPower(r * POWER_FACTOR);
        left.setPower(l * POWER_FACTOR);
    }
}
