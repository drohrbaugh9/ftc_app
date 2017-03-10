package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.RobotLog;

public class ____TestingTiming extends LinearOpMode {

    //DcMotor right;
    long l;

    public void runOpMode() throws InterruptedException{

        /*Util.init(hardwareMap);
        AutoUtil.resetEncoders(this);
        right = Util.right;*/

        waitForStart();

        long startTime = System.nanoTime();

        for (int i = 0; i < 1000; i++) {
            waitOneFullHardwareCycle();
        }

        long endTime = System.nanoTime();

        telemetry.addData("average time (ns)", (endTime - startTime) / 1000);
    }
}

/*already tested
RobotLog.i("test");
l = System.nanoTime();          945 ns
waitOneFullHardwareCycle();     1.0674638*10^7 ns
*/