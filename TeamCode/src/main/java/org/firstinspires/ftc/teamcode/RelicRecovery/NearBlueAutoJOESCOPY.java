package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/3/18.
 */


@Autonomous(name = "Near BLUE - JOES COPY", group = "A JOE auto")
@Disabled
public class NearBlueAutoJOESCOPY extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Util.init(this); //imu initialized in here
        IntakeControl.init();
        KnockOffJewel.init(this);
        Vuforia.init(this);
        RelicRecoveryVuMark vuMark = null;
        Util.resetEncoders();
        CVAutoLib cryptoDetect = new CVAutoLib();


        cryptoDetect.columnDetection = false;
        cryptoDetect.redDetection = false;
        cryptoDetect.meanthreshold = 12;
        cryptoDetect.numsmooths = 3;

      /*
      I don't Reset the encoders anywhere - doesn't seem to be a problem yet
      Don't have anywhere that sets motors to brake
       */

        telemetry.addLine("READY FOR START");
        telemetry.update();

        waitForStart();

        int i = 0;
        while (i < 60){
            Vuforia.VuMarkID(this);
            VuforiaGoToColumn.vuMark = Vuforia.VuMarkID(this);
            i+=1;
            Thread.sleep(10);
        }
        CameraDevice.getInstance().setFlashTorchMode(false) ;


        KnockOffJewel.KnockOffRedJewel(this);
        DriveOffStone.NearBlue(this);
        Move.moveUntilBluePIDDualProcesses(-90, 0.3, this);

        /**
         * IF I CHANGE ANYTHING HERE CHANGE IN OTHER CORRESPONDING AUTOS ALSO
         */

        VuforiaGoToColumn.NearBlue();

        Move.strafeAngle(0, 0.35, 550, false); //change brake to false after test
        getGlyphs.RunWithStatesBLUE();

        Thread.sleep(500);

        switch (VuforiaGoToColumn.column){
            case LEFT:
                centerOnColumn.getToRightBLUE(cryptoDetect, this);
                break;
            case RIGHT:
                centerOnColumn.getToLeftBLUE(cryptoDetect, this);
                break;
            case CENTER:
                centerOnColumn.getToLeftBLUE(cryptoDetect, this);
                break;
        }

        DistanceSensorLineUp.lineUpBlue(true);

       // PutGlyphInBox.putInBoxWithPush();

        Thread.sleep(20000);
        //Move.strafeAngle(270, 0.3, 30, true);
//            centers = cryptoDetect.forceFindCenters(this);
//            telemetry.addData("centers at ",centers);
//            telemetry.update();
        //cryptoDetect.centerAlign(this,centers);


    }
    //use for laterswitch (VuforiaGoToColumn.column){
//            case LEFT:
//            if (getGlyphs.firstGlyphColor == getGlyphs.glyphColor.BROWN){
//        if (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.BROWN){
//
//        }
//        else if (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.GREY){
//
//        }
//        else if (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.UNKNOWN){
//
//        }
//        else {
//
//        }
//    }
//                if (getGlyphs.firstGlyphColor == getGlyphs.glyphColor.GREY){
//
//    }
//                centerOnColumn.getToRightBLUE(cryptoDetect, this);
//                break;
//            case RIGHT:
//            centerOnColumn.getToLeftBLUE(cryptoDetect, this);
//                break;
//            case CENTER:
//            centerOnColumn.getToRightBLUE(cryptoDetect, this);
//                break;
//}

}