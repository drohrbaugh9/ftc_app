package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 3/19/18.
 */

@Autonomous(name = "get glyphs", group = "test")
//@Disabled

public class test2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Util.init(this);
        waitForStart();
//        VuforiaGoToColumn.columnState column;
//        column = VuforiaGoToColumn.columnState.RIGHT;
//        Move.moveUntilRedPIDwithHeading(180, 0.25, this, 0);
//        if(column == VuforiaGoToColumn.columnState.RIGHT){
//            Move.strafeAngleWithHeading(-90, 0.4, 750, true, 0);
//        }
//        if(column == VuforiaGoToColumn.columnState.CENTER) {
//            Move.strafeAngleWithHeading(-90, 0.4, 380, true, 0);
//        }
//        Util.setAllPowers(0);
//        Move.strafeAngleWithHeading(0, 0.3, 75, false, 0);
//        //DistanceSensorLineUp.lineUp(true);
//        //PutGlyphInBox.putInBoxWithPush();
//
//        DistanceSensorLineUp.lineUpRed(true);
//        PutGlyphInBox.putInBoxWithPush(false);

        Move.rotateCounterClockwise(88);

        Thread.sleep(20000);
    }
}



