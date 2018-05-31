package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/9/18.
 */


public class PutGlyphInBox {

    private PutGlyphInBox() throws Exception {
        throw new Exception();
    }

    public static void putInBox() throws InterruptedException {
        //half up for the lift to unfold intake - may be over kill but we'll see
//        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(-2));
//        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(-2));
//        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        Util.lift.setTargetPosition(-762);
//        Util.lift.setPower(0.5); //used to unfold the intake - needs this much power
//        Thread.sleep(1000); // DON'T CHANGE
//        //put tray back down with gravity same as in teleop
//        Util.lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        Util.lift.setPower(0.3);
//        Thread.sleep(50);
//        Util.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        Util.lift.setPower(0);
//        Thread.sleep(700);
        //Move.strafeAngleWithoutPID(180, 0.2, 120); // dist was 300
        //move toward cryptobox
//        Util.setAllPowers(-0.2);
//        Thread.sleep(500);
//        Util.setAllPowers(0);
//        Thread.sleep(100);
        //Move.strafeAngleWithoutPID(180,0.2, 100);
        // drop glyph here
        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(93));
        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(93));


        Thread.sleep(700);


        //Move.strafeAngle(180, 0.2, 300);
        // push glyph in

//        Move.strafeAngleforTimeWithoutPID(180, 0.2, 0.5);
//
//        //back up
        Move.strafeAngleWithoutPID(0, 0.3, 50); // may not be needed see below
        // Untilt tray
        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(-17));
        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(-17));
        Thread.sleep(500);
        Util.lift.setPower(0.15);
        Thread.sleep(200);
        Util.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.lift.setPower(0);
        //Move.strafeAngle(180, 0.2, 300);
        // push glyph in
        //Util.setAllPowers(-0.2);
        //Thread.sleep(500);
        // pull away from glyph
        //Move.strafeAngleWithoutPID(0, 0.2, 80);
    }
    public static void putInBoxWithPush(boolean lift) throws InterruptedException {
        //half up for the lift to unfold intake - may be over kill but we'll see
//        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(-2));
//        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(-2));
//        Util.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        Util.lift.setTargetPosition(-762);
//        Util.lift.setPower(0.5); //used to unfold the intake - needs this much power
//        Thread.sleep(1000); // DON'T CHANGE
//        //put tray back down with gravity same as in teleop
//        Util.lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        Util.lift.setPower(0.3);
//        Thread.sleep(50);
//        Util.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        Util.lift.setPower(0);
//        Thread.sleep(700);
        //Move.strafeAngleWithoutPID(180, 0.2, 120); // dist was 300
        //move toward cryptobox
//        Util.setAllPowers(-0.2);
//        Thread.sleep(500);
//        Util.setAllPowers(0);
//        Thread.sleep(100);
        //Move.strafeAngleWithoutPID(180,0.2, 100);
        // drop glyph here
        if (lift) {
            Util.lift.setTargetPosition(-190);
        }

        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(93));
        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(93));


        Thread.sleep(700);


        //Move.strafeAngle(180, 0.2, 300);
        // push glyph in

       Move.strafeAngleforTimeWithoutPID(180, 0.2, 0.5);
//
//        //back up
        Move.strafeAngleWithoutPID(0, 0.2, 100); // may not be needed see below
        // Untilt tray
        Util.leftTiltServo.setPosition(MecanumTeleop.LeftServoDegreesToServoPos(-17));
        Util.rightTiltServo.setPosition(MecanumTeleop.RightServoDegreesToServoPos(-17));
        Thread.sleep(500);
        Util.lift.setPower(0.15);
        if (lift){
            Thread.sleep(200);
        }
        Thread.sleep(200);
        Util.lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.lift.setPower(0);
        //Move.strafeAngle(180, 0.2, 300);
        // push glyph in
        //Util.setAllPowers(-0.2);
        //Thread.sleep(500);
        // pull away from glyph
        //Move.strafeAngleWithoutPID(0, 0.2, 80);
    }

    public static void putInBoxNoLift() {
    }

}
