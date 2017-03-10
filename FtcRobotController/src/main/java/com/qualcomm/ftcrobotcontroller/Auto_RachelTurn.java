
// 1 encodercount is about 18.3 inches
package com.qualcomm.ftcrobotcontroller;



import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

//import com.qualcomm.robotcore.hardware.TouchSensor;
public class Auto_RachelTurn extends LinearOpMode {

    DcMotor motorRight, motorLeft, motorRightFront, motorLeftFront, arm, hanger;
    GyroSensor gyro;
   // int counter=0;
    int count=1024;
    float timer,timer1;
   //TouchSensor touch;

    public Auto_RachelTurn() {}

    public void runOpMode() throws InterruptedException {
        Util.init(this);
        gyro = hardwareMap.gyroSensor.get("gyro");
        AutoUtil.init(this, gyro);

        motorRight = Util.rightBack;
        motorLeft = Util.leftBack;
        motorRightFront = Util.rightFront;
        motorLeftFront = Util.leftFront;
        arm = Util.arm;
        hanger = Util.hanger;

        //touch = hardwareMap.touchSensor.get("Tsensor");

        /*motorRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        arm.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);*/

        waitForStart();

        //AutoUtil.moveForward(5.57 * count, 0.5f, gyro);

        //Thread.sleep(500);
        /*timer1=System.nanoTime();
        while ((System.nanoTime()-timer1)<(10 *Util.SEC_TO_NSEC)) {
            AutoUtil.moveBackward(5.37 * count, .5f, gyro);
            //AutoUtil.turnLeft(1 * count, .5f);
            AutoUtil.moveBackward(5.37 * count, .5f, gyro);
            Thread.sleep(500);
        }

        //AutoUtil.turnRight(1 * count, .5f);*/

        // Thread.sleep(500);
        timer=System.nanoTime();
        arm.setPower(-0.5);
        hanger.setPower(0.25);
        Thread.sleep(2500);
        //while ((System.nanoTime()-timer)<(2 *Util.SEC_TO_NSEC)) {

            //counter++;
       // }



        arm.setPower(0);
        hanger.setPower(0);
        Thread.sleep(2000);
        Util.setAllPowers(0);
        waitOneFullHardwareCycle();
        ;
       // }

        gyro.getHeading();


        //if (gyro.getHeading()==4)
        //{
            //AutoUtil.moveForward(6*1024,.7,gyro);
        //}




    }
}