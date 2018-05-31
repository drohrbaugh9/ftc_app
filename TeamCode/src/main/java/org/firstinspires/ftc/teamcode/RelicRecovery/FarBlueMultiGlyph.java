package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by lulzbot on 4/10/18.
 */

@Autonomous(name = "FarBlueMultiGlyph", group = "Far Auto")
public class FarBlueMultiGlyph extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Util.init(this); //imu initialized in here
        IntakeControl.init();
        KnockOffJewel.init(this);
        Vuforia.init(this);
        RelicRecoveryVuMark vuMark = null;
        Util.resetEncoders();

        VuforiaGoToColumn.columnState vuforiaColumn;
        VuforiaGoToColumn.columnState columnTarget;

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
        CameraDevice.getInstance().setFlashTorchMode(false);


        KnockOffJewel.KnockOffRedJewel(this);
        DriveOffStone.FarBlue(this);
        //Move.strafeAngle(0,0.3, 100, false);
        Move.moveUntilBluePIDDualProcesses(0, 0.25, this);

        Move.rotateClockwise(75); //rotate to face cryptobox
        //Move.strafeAngleWithoutPID(180,0.2, 50);

        vuforiaColumn = VuforiaGoToColumn.FarBlueMulti();

        //will not? need
        //KnockOffJewel.servo.setPosition(0.19);
        getGlyphs.RunWithStatesFarBlue();
        columnTarget = goToColumnBasedOnMultiGlyphs.NearBlue(vuforiaColumn, this);
        Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
        Move.strafeAngleWithHeading(180, 0.3, 150, false, 90);
        Move.moveUntilBluePIDWithHeading(180, 0.2, this, 90);
        if(columnTarget != VuforiaGoToColumn.columnState.UNKNOWN){
            if(columnTarget == VuforiaGoToColumn.columnState.LEFT){
                Move.strafeAngleWithHeading(90, 0.4, 675, true, 75);
            }
            if(columnTarget == VuforiaGoToColumn.columnState.CENTER){
                Move.strafeAngleWithHeading(90, 0.4, 380, true, 75 );
            }

            //Move.strafeAngleWithHeading(0, 0.3, 75, false, -90);
            DistanceSensorLineUp.lineUpBlueHeading(true, 90);
            PutGlyphInBox.putInBoxWithPush(true);

        }
        else{
            Move.strafeAngleWithHeading(0, 0.2, 50, true, 90);
        }

        Util.setAllPowers(0);

    }

}


