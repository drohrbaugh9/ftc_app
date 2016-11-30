package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Shooter extends LinearOpMode {

    DcMotor shooter1, shooter2;

    public void runOpMode() throws InterruptedException {
        shooter1 = hardwareMap.dcMotor.get("shooter1");
        shooter2 = hardwareMap.dcMotor.get("shooter2"); shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        double stick = gamepad1.right_stick_y;

        if (stick < 0) stick = 0;

        shooter1.setPower(stick);
        shooter2.setPower(stick);

        Thread.sleep(20);
    }
}
