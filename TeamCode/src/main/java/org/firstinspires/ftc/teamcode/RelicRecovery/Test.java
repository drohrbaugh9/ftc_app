package org.firstinspires.ftc.teamcode.RelicRecovery;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;


import java.util.Locale;

import java.util.ArrayList;


/**
 * Created by elliot on 1/3/18.
 */

@Autonomous(name = "Test", group = "Test")
//@Disabled
public class Test extends LinearOpMode {

    public void runOpMode() throws InterruptedException {


        Util.init(this); //imu initialized in here
        KnockOffJewel.init(this);
        Vuforia.init(this);
        RelicRecoveryVuMark vuMark = null;
        Util.resetEncoders();


        telemetry.addLine("READY FOR START");
        telemetry.update();

        waitForStart();

        Move.rotateClockwise(75);

        Thread.sleep(20000);

        //will not? need
        //KnockOffJewel.servo.setPosition(0.19);
    }
}
