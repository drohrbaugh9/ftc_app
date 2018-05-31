package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.RelicRecovery.CVAutoLib;


@TeleOp(name="OpenCV OpMode", group="DogeCV")
@Disabled
public class CryptoboxOpMode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();


    private CVAutoLib cryptoboxDetector = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        cryptoboxDetector = new CVAutoLib();
        cryptoboxDetector.meanthreshold = 12;
        cryptoboxDetector.redDetection = false;
        cryptoboxDetector.columnDetection = false;
        //True = Red cryptobox detection,False = Blue cryptobox detection
        cryptoboxDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        //cryptoboxDetector.rotateMat = false;

        //Optional Test Code to load images via Drawables
        //cryptoboxDetector.useImportedImage = true;
        //cryptoboxDetector.SetTestMat(com.qualcomm.ftcrobotcontroller.R.drawable.test_cv4);

        cryptoboxDetector.enable();


    }

    @Override
    public void init_loop() {
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();


    }

    @Override
    public void loop() {
        //Use this to adjust vision parameters with a gamepad while running
        //        if(gamepad1.y){
////            cryptoboxDetector.meanthreshold+=1;
////        }
////        if(gamepad1.a){
////            cryptoboxDetector.meanthreshold-=1;
////        }
////        if(gamepad1.b){
////            cryptoboxDetector.numsmooths=6;
////        }
////        if (gamepad1.x){
////            cryptoboxDetector.numsmooths = 4;
////        }
////        if (gamepad1.x && gamepad1.b){
////            cryptoboxDetector.numsmooths=2;
////        }
//        telemetry.addData("Threshold is at",cryptoboxDetector.meanthreshold);
//        telemetry.addData("Numsmooths is at",cryptoboxDetector.numsmooths);
//        telemetry.addData("Status", "Run Time: " + runtime.toString());
//        telemetry.addData("isCryptoBoxDetected", cryptoboxDetector.isCryptoBoxDetected());
//        telemetry.addData("isColumnDetected ",  cryptoboxDetector.isColumnDetected());

//        telemetry.addData("Column Left ",  cryptoboxDetector.getCryptoBoxLeftPosition());
//        telemetry.addData("Column Center ",  cryptoboxDetector.getCryptoBoxCenterPosition());
//        telemetry.addData("Column Right ",  cryptoboxDetector.getCryptoBoxRightPosition());


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        cryptoboxDetector.disable();
    }

}