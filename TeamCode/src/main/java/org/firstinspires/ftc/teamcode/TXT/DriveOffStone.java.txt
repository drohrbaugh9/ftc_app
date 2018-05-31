package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/5/18.
 */

public class DriveOffStone {

    private DriveOffStone() throws Exception {
        throw new Exception();
    }

    public static void NearRed(LinearOpMode opMode) throws InterruptedException {
        //Util.init(opMode); // uncomment if running individually
        //Move.startRight(0.5, 1100, true); // Why is this .5? Maybe change to .3

        Move.strafeAngleforTime(90, 0.15, 0.1);
        Move.strafeAngle(90, 0.4, 1200, true); //uses a P to stay straight
        Util.JewelWhackerServo.setPosition(KnockOffJewel.JewelWhackerServoUp);
        Thread.sleep(50);
//        Util.rightFront.setPower(0.3); //drive back to the stone for 1 sec to square up
//        Util.rightBack.setPower(-0.3);
//        Util.leftFront.setPower(-0.3);
//        Util.leftBack.setPower(0.3);
//
//        Thread.sleep(1000);
//        Move.strafeAngleforTime(-90, 0.3, 0.5);
//        Thread.sleep(50);

    }

    public static void FarRed(LinearOpMode opMode) throws InterruptedException {
        //Util.init(opMode); // uncomment if running individually
        //Move.startRight(0.5, 1100, true); // Why is this .5? Maybe change to .3

        Move.strafeAngleforTime(90, 0.15, 0.1);
        Move.strafeAngle(90, 0.4, 1400, true); //uses a P to stay straight
        Util.JewelWhackerServo.setPosition(KnockOffJewel.JewelWhackerServoUp);
        Thread.sleep(50);

    }

    public static void NearBlue(LinearOpMode opMode) throws InterruptedException {
        //Util.init(opMode); // uncomment if running individually
        //Move.startRight(0.5, 1100, true); // Why is this .5? Maybe change to .3

        Move.strafeAngleforTime(-90, 0.15, 0.1);
        Move.strafeAngle(-90, 0.4, 1300, true); //uses a P to stay straight
        Util.JewelWhackerServo.setPosition(KnockOffJewel.JewelWhackerServoUp);
        Thread.sleep(50);
//        Util.rightFront.setPower(-0.3); //drive back to the stone for 1 sec to square up
//        Util.rightBack.setPower(0.3);
//        Util.leftFront.setPower(0.3);
//        Util.leftBack.setPower(-0.3);
//
//        Thread.sleep(1000);
//        Move.strafeAngleforTime(90, 0.3, 0.5); //drive back to the stone for .5 sec to square up
//        Thread.sleep(50);
    }

    public static void FarBlue(LinearOpMode opMode) throws InterruptedException {
        //Util.init(opMode); // uncomment if running individually
        //Move.startRight(0.5, 1100, true); // Why is this .5? Maybe change to .3

        Move.strafeAngleforTime(-90, 0.15, 0.1);
        Move.strafeAngle(-90, 0.4, 1400, true); //uses a P to stay straight
        Util.JewelWhackerServo.setPosition(KnockOffJewel.JewelWhackerServoUp);
        Thread.sleep(50);


    }
}
