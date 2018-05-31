package org.firstinspires.ftc.teamcode.RelicRecovery;

import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by elliot on 1/3/18.
 */

//Can use to test stall stuff
@Autonomous(name = "Stall Protect", group = "Test")
@Disabled
public class IntakeStallProtection extends LinearOpMode {

    long currentTime;
    long oldTime;

    public static DcMotor intake1;
    public static DcMotor intake2;

    public void runOpMode() throws InterruptedException {
        Util.init(this);
        IntakeControl.init();
        intake1 = hardwareMap.dcMotor.get("intake_left"); intake1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake2 = hardwareMap.dcMotor.get("intake_right");

        intake1.setPower(0.9);
        intake2.setPower(0.9);


        waitForStart();

        IntakeControl.lastLeftIntakePos = intake1.getCurrentPosition();
        IntakeControl.lastRightIntakePos = intake2.getCurrentPosition();

        for (int i = 0; i < IntakeControl.MOVING_AVERAGE_LENGTH; i++){
            currentTime = System.nanoTime() / 1000000;
            IntakeControl.ManageEncoderData(currentTime - oldTime);
            oldTime = currentTime;
        }

        while (opModeIsActive()) {
            IntakeControl.ManageDataAndHandleStalls();
        }



    }


}