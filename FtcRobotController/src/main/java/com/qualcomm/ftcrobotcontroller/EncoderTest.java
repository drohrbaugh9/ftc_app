package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class EncoderTest extends LinearOpMode {
//public class EncoderTest extends OpMode {

    DcMotor motor;

    public EncoderTest() {}

    ///*
    public void runOpMode() throws InterruptedException {
        motor = hardwareMap.dcMotor.get("right");
        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        for (int i = 0; i < 11; i++) waitOneFullHardwareCycle();
        motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        for (int i = 0; i < 11; i++) waitOneFullHardwareCycle();

        waitForStart();
        motor.setPower(0.4);

        while (motor.getCurrentPosition() < 1010) { //1024
            waitOneFullHardwareCycle();
            telemetry.addData("encoder", motor.getCurrentPosition());
        }

        motor.setPower(0);
        telemetry.addData("encoder", motor.getCurrentPosition());
    }
    //*/

    /*
    public void init() {
        motor = hardwareMap.dcMotor.get("motor_1"); //1024
    }

    public void loop() {
        telemetry.addData("encoder", motor.getCurrentPosition());
    }
    */
}