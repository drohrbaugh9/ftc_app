package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by pdutoit on 8/4/17.
 */
@Autonomous(name = "EncoderTest", group = "Test")
@Disabled
public class EncoderTest extends OpMode {

    DcMotor leftBack;
    DcMotor rightBack;
    DcMotor rightFront;
    DcMotor leftFront;
    public void init(){
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightBack = hardwareMap.dcMotor.get("rightBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftFront = hardwareMap.dcMotor.get("leftFront");

        leftFront.setMode(DcMotor.RunMode.RESET_ENCODERS);
        rightBack.setMode(DcMotor.RunMode.RESET_ENCODERS);
        rightFront.setMode(DcMotor.RunMode.RESET_ENCODERS);
        leftBack.setMode(DcMotor.RunMode.RESET_ENCODERS);


    }
    public void loop(){
        telemetry.addData("LB Encoder", leftBack.getCurrentPosition());
        telemetry.addData("RB Encoder", rightBack.getCurrentPosition());
        telemetry.addData("LF Encoder", leftFront.getCurrentPosition());
        telemetry.addData("RF Encoder", rightFront.getCurrentPosition());
        telemetry.update();

    }
}
