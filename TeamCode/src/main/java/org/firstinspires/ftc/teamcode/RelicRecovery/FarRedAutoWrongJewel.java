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

@Autonomous(name = "Far RED - WRONG JEWEL", group = "Far auto wrong jewel")
@Disabled
public class FarRedAutoWrongJewel extends LinearOpMode {
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
        while (i < 60){
            VuMarkDetector.VuMarkID(this);
            vuMark = VuMarkDetector.VuMarkID(this);
            i+=1;
            Thread.sleep(10);
        }


        KnockOffJewel.KnockOffRedJewel(this);
        DriveOffStone.FarRed(this);
        Move.moveUntilRedPID(0, 0.2, this);

        Move.rotateCounterClockwise(88); //rotate to face cryptobox
        //Move.strafeAngleWithoutPID(180,0.2, 50);

        VuforiaGoToColumn.FarRed();

        PutGlyphInBox.putInBox();

        telemetry.addData("IMU", Util.imu.getAngularOrientation().firstAngle);
        telemetry.update();

        Thread.sleep(12000);

        //will not? need
        //KnockOffJewel.servo.setPosition(0.19);
    }
}
