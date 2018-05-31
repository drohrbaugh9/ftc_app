package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/3/18.
 */


@Autonomous(name = "Near BLUE Multi-Glyph - No Vision", group = "A JOE auto")
//@Disabled
public class NearBlueAutoMultiNoVision extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Util.init(this); //imu initialized in here
        IntakeControl.init();
        KnockOffJewel.init(this);
        Vuforia.init(this);
        RelicRecoveryVuMark vuMark = null;
        Util.resetEncoders();

        VuforiaGoToColumn.columnState vuforiaColumn;
        VuforiaGoToColumn.columnState goToThisColumn;


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

        vuforiaColumn = VuforiaGoToColumn.NearBlue();

        Move.strafeAngle(0, 0.35, 550, false); //change brake to false after test
        getGlyphs.RunWithStatesBLUE();

        Thread.sleep(500);

        goToThisColumn = goToColumnBasedOnMultiGlyphs.NearBlue(vuforiaColumn, this);

        if (goToThisColumn != VuforiaGoToColumn.columnState.UNKNOWN){
            Move.moveSquareUpUntilBluePID(this, goToThisColumn);
        }
        else {
            Move.moveUntilBluePID(180, 0.3, this);
            Move.strafeAngle(0, 0.2, 50, true);
            Thread.sleep( 20000);

        }


        DistanceSensorLineUp.lineUpRed(true);

        PutGlyphInBox.putInBoxWithPush(Util.liftTrayforMulti);

        Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);

        Thread.sleep(20000);



    }


}