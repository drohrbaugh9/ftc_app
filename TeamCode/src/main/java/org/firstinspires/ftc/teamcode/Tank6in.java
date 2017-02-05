package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Tank6in", group="Testing")
@Disabled
public class Tank6in extends OpMode {

    DcMotor right, left;

    public void init() {
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");
    }

    public void loop() {
        right.setPower(gamepad1.right_stick_y);
        left.setPower(-gamepad1.left_stick_y);
    }
}
