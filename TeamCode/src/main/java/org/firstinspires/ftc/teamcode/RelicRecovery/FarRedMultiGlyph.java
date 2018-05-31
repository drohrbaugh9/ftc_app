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

@Autonomous(name = "FarRedMultiGlyph", group = "Far Auto")
public class FarRedMultiGlyph extends LinearOpMode {
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
        CameraDevice.getInstance().setFlashTorchMode(false) ;

        KnockOffJewel.KnockOffBlueJewel(this);
        DriveOffStone.FarRed(this);
        //Move.strafeAngle(0,0.3, 100, false);
        Move.moveUntilRedPIDDualProcesses(0, 0.25, this);

        Move.rotateCounterClockwise(88); //rotate to face cryptobox
        //Move.strafeAngleWithoutPID(180,0.2, 50);

        vuforiaColumn = VuforiaGoToColumn.FarRed();

        //will not? need
        //KnockOffJewel.servo.setPosition(0.19);
        getGlyphs.RunWithStatesFarRed();
        columnTarget = goToColumnBasedOnMultiGlyphs.NearRed(vuforiaColumn, this);
        Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
        Move.strafeAngleWithHeading(180, 0.3, 150, false, -88);
        Move.moveUntilRedPIDwithHeading(180, 0.2, this, -88);
        if(columnTarget != VuforiaGoToColumn.columnState.UNKNOWN){
            if(columnTarget == VuforiaGoToColumn.columnState.RIGHT){
                Move.strafeAngleWithHeading(-90, 0.4, 675, true, -88);
            }
            if(columnTarget == VuforiaGoToColumn.columnState.CENTER){
                Move.strafeAngleWithHeading(-90, 0.4, 380, true, -88 );
            }
            //Move.strafeAngleWithHeading(0, 0.3, 75, false, -88);
            DistanceSensorLineUp.lineUpWithHeading(true, -88);

            PutGlyphInBox.putInBoxWithPush(true);
        }
        else{
            Move.strafeAngleWithHeading(0, 0.2, 50, true, -88);
        }

        Util.setAllPowers(0);

    }

}


