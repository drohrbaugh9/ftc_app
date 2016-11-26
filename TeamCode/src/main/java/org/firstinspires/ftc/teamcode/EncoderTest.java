package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Encoder Test", group = "Test")
//@Disabled
public class EncoderTest extends LinearOpMode {

    DcMotor motor;

    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.dcMotor.get("right");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Thread.sleep(100);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Thread.sleep(100);

        waitForStart();

        motor.setPower(0.4);

        while (motor.getCurrentPosition() < 1010) { //1024
            Thread.sleep(2);
            telemetry.addData("encoder", motor.getCurrentPosition());
        }

        motor.setPower(0);
        telemetry.addData("encoder", motor.getCurrentPosition());

    }
}