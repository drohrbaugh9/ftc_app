package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/12/18.
 */

@Autonomous(name = "Near BLUE - WRONG JEWEL", group = "B Near auto wrong jewel")
@Disabled
public class NearBlueAutoWrongJewel extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Util.init(this); //imu initialized in here
        KnockOffJewel.init(this);
        VuMarkDetector.init(this);
        RelicRecoveryVuMark vuMark = null;
        Util.resetEncoders();
      /*
      I don't Reset the encoders anywhere - doesn't seem to be a problem yet
      Don't have anywhere that sets motors to brake
       */

        telemetry.addLine("READY FOR START");
        telemetry.update();

        waitForStart();

        int i = 0;
        while (i < 100){
            VuMarkDetector.VuMarkID(this);
            vuMark = VuMarkDetector.VuMarkID(this);
            i+=1;
            Thread.sleep(10);
        }


        KnockOffJewel.KnockOffBlueJewel(this);
        DriveOffStone.NearBlue(this);
        Move.moveUntilBluePID(-90, 0.3, this);

        /**
         * IF I CHANGE ANYTHING HERE CHANGE IN OTHER CORRESPONDING AUTOS ALSO
         */

        VuforiaGoToColumn.NearBlue();

        PutGlyphInBox.putInBox();

        Thread.sleep(12000);

        //will not? need
        //KnockOffJewel.servo.setPosition(0.19);
    }
}