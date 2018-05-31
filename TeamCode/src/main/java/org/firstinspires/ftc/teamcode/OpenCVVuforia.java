//package org.firstinspires.ftc.teamcode;
//
//import com.disnodeteam.dogecv.CameraViewDisplay;
//import com.disnodeteam.dogecv.detectors.CryptoboxDetector;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuMarkIdentification;
//
///**
// * Created by lulzbot on 1/15/18.
// */
//
//public class OpenCVVuforia extends OpMode {
//    //@Override
//    private ElapsedTime runtime = new ElapsedTime();
//    ConceptVuMarkIdentification vuforia = new ConceptVuMarkIdentification();
//    private CryptoboxDetector cryptoboxDetector = null;
//    @Override
//    public void init() {
//        telemetry.addData("Status", "Initialized");
//
//
//        cryptoboxDetector = new CryptoboxDetector();
//        cryptoboxDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
//
//        cryptoboxDetector.rotateMat = true;
//        //cryptoboxDetector.transposeMat = false;
//        //Optional Test Code to load images via Drawables
//        //cryptoboxDetector.useImportedImage = true;
//        //cryptoboxDetector.SetTestMat(com.qualcomm.ftcrobotcontroller.R.drawable.test_cv4);
//
//        cryptoboxDetector.enable();
//
//
//    }
//    public void init_loop() {
//        telemetry.addData("Status", "Initialized");
//    }
//
//    @Override
//    public void start() {
//        runtime.reset();
//        vuforia.runOpMode();
//
//    }
//    @Override
//    public void loop() {
//
//
//
//        telemetry.addData("Status", "Run Time: " + runtime.toString());
//        telemetry.addData("isCryptoBoxDetected", cryptoboxDetector.isCryptoBoxDetected());
//        telemetry.addData("isColumnDetected ",  cryptoboxDetector.isColumnDetected());
//
//        telemetry.addData("Column Left ",  cryptoboxDetector.getCryptoBoxLeftPosition());
//        telemetry.addData("Column Center ",  cryptoboxDetector.getCryptoBoxCenterPosition());
//        telemetry.addData("Column Right ",  cryptoboxDetector.getCryptoBoxRightPosition());
//
//
//    }
//
//    /*
//     * Code to run ONCE after the driver hits STOP
//     */
//    @Override
//    public void stop() {
//        cryptoboxDetector.disable();
//    }
//
//
//}
//
