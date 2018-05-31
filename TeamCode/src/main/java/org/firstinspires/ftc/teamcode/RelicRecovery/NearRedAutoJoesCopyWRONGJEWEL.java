package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/3/18.
 */


@Autonomous(name = "Near RED - JOES COPY - WRONG JEWEL", group = "A JOE auto wrong jewel")
@Disabled
public class NearRedAutoJoesCopyWRONGJEWEL extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Util.init(this); //imu initialized in here
        KnockOffJewel.init(this);
        Vuforia.init(this);
        RelicRecoveryVuMark vuMark = null;
        Util.resetEncoders();
        CVAutoLib cryptoDetect = new CVAutoLib();


        cryptoDetect.columnDetection = false;
        cryptoDetect.redDetection = true;
        cryptoDetect.meanthreshold = 15;
        cryptoDetect.numsmooths = 3;

      /*
      I don't Reset the encoders anywhere - doesn't seem to be a problem yet
      Don't have anywhere that sets motors to brake
       */

        telemetry.addLine("READY FOR START");
        telemetry.update();

        waitForStart();

        int i = 0;
        while (i < 100){
            Vuforia.VuMarkID(this);
            vuMark = Vuforia.VuMarkID(this);
            i+=1;
            Thread.sleep(10);
        }


        KnockOffJewel.KnockOffRedJewel(this); //Can cut time out
        DriveOffStone.NearRed(this); //Don't need to cut time out
        Move.moveUntilRedPID(90, 0.3, this);

        /**
         * IF I CHANGE ANYTHING HERE CHANGE IN OTHER CORRESPONDING AUTOS ALSO
         */

       VuforiaGoToColumn.NearRed();


        Move.strafeAngle(0, 0.35, 550, false); //change brake to false after test
        getGlyphs.REDNoStateMachineGetGlyphs();

        switch (VuforiaGoToColumn.column){
            case LEFT:
                centerOnColumn.getToRightRED(cryptoDetect, this);
                break;
            case RIGHT:
                centerOnColumn.getToLeftRED(cryptoDetect, this);
                break;
            case CENTER:
                centerOnColumn.getToRightRED(cryptoDetect, this);
                break;
        }



        PutGlyphInBox.putInBoxNoLift();

        Thread.sleep(20000);

        //Move.strafeAngle(270, 0.3, 30, true);
//            centers = cryptoDetect.forceFindCenters(this);
//            telemetry.addData("centers at ",centers);
//            telemetry.update();
        //cryptoDetect.centerAlign(this,centers);


    }
}