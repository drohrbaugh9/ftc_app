package org.firstinspires.ftc.teamcode.RelicRecovery;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Util;

@Autonomous(name = "MoveClassTest", group = "Test")
@Disabled
public class MoveClassTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        //telemetry.addData("imu heading", PID.heading(Util.imu)); telemetry.update();

        waitForStart();

        /*/ sqaure
        Move.strafeAngle(0, 0.3, 500);
        Thread.sleep(500);
        Move.strafeAngle(90, 0.5, 500);
        Thread.sleep(500);
        Move.strafeAngle(180, 0.3, 500);
        Thread.sleep(500);
        Move.strafeAngle(-90, 0.5, 500);
        /**/

        /*/ strafe angle test
        Move.strafeAngle(90, 0.5, 1000);
        Thread.sleep(500);
        Move.strafeAngle(-90, 0.5, 1000);
        Thread.sleep(500);
        Move.strafeAngle(90, 0.5, 1000);
        Thread.sleep(500);
        Move.strafeAngle(-90, 0.5, 1000);
        /*/

        /*/ code from KnockOffJewel:
        Util.rightFront.setPower(0.25);
        Util.rightBack.setPower(0.25);
        Util.leftFront.setPower(-0.25);
        Util.leftBack.setPower(-0.25); //rotate counterclockwise
        Thread.sleep(200); //change based on tests - affects number of deg turned
        Util.setAllPowers(0);
        Thread.sleep(20);
        Util.rightFront.setPower(-0.25); //rotate back to start pos
        Util.rightBack.setPower(-0.25);
        Util.leftFront.setPower(0.25);
        Util.leftBack.setPower(0.25);
        Thread.sleep(200); // make sure same as sleep above
        Util.setAllPowers(0);
        Thread.sleep(20); // pause still seemed long

        Move.strafeAngle(90, 0.4, 2000);
        /**/
    }
}
