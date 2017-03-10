package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

public class ResetEncoders extends LinearOpMode {

    ArrayList<DcMotor> motors;

    public ResetEncoders() {}

    public void runOpMode() throws InterruptedException {
        telemetry.addData("status", "Wait");

        Util.init(this);
        Util.resetEncoders();

        telemetry.addData("status", "Encoders reset");
    }
}
