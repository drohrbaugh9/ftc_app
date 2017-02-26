package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="LED_Test", group="Test")
//@Disabled
public class LED_Test extends LinearOpMode {

    DcMotor LED;

    boolean LED_on, aPressed = false;

    public void runOpMode() {

        LED = hardwareMap.dcMotor.get("led");

        LED.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LED.setPower(0);

        waitForStart();

        if (gamepad1.a && !aPressed) {
            if (!LED_on) LED.setPower(1);
            else LED.setPower(0);
            aPressed = true;
        }
        if (!gamepad1.a) aPressed = false;
    }
}
