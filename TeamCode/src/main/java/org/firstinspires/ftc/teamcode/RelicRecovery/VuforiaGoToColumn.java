package org.firstinspires.ftc.teamcode.RelicRecovery;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

import static org.firstinspires.ftc.teamcode.RelicRecovery.ReadVumark.vuMark;

/**
 * Created by elliot on 3/16/18.
 */

public class VuforiaGoToColumn {

    private VuforiaGoToColumn() throws Exception {
        throw new Exception();
    }

    public static RelicRecoveryVuMark vuMark;
    //int columnState; //This is our column, -1 is left, 0 is center, 1 is right
    public enum columnState {
        LEFT, CENTER, RIGHT, UNKNOWN

    }

    static columnState column;

    //Some states will need Manual Tuning - make sure you go and do that
    private final static int tuningVariable = -70;

    public static columnState NearRed() throws InterruptedException{
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            if (vuMark == RelicRecoveryVuMark.RIGHT) {
                Util.telemetry("Move To Right Column", true);
                //Move.startRight(0.5, 50, true);
                Move.strafeAngle(-90, 0.4, 45, true); //Manually tune (originally angle: 90 and dist: 25)
                DistanceSensorLineUp.lineUpRed(false);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBoxWithPush(false);

                column = columnState.RIGHT;
                //columnState = 1;

            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {
                Util.telemetry("Move To Center Column", true);
                //Move.startRight(0.5, 300, true);
                Move.strafeAngle(90, 0.4, 300 + tuningVariable, true);
                DistanceSensorLineUp.lineUpRed(false);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBoxWithPush(false);
                Move.strafeAngle(-90, 0.5, 275, true);
                column = columnState.CENTER;
                //columnState = 0;

            }
            else if (vuMark == RelicRecoveryVuMark.LEFT) { //probs need to move further
                Util.telemetry("Move To Left Column", true);
                //Move.startRight(0.5, 600, true);
                Move.strafeAngle(90, 0.4, 700 + tuningVariable, true);
                DistanceSensorLineUp.lineUpRed(false);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBoxWithPush(false);
                //set up to get more glyphs - move to right column
                Move.strafeAngle(-90, 0.5, 650, true);
                column = columnState.LEFT;
                //columnState = -1;
            }

        }
        else {
            Util.telemetry("No VuMark Seen", true);
            Move.strafeAngle(-90, 0.5, 45, true); //Manually tune (originally angle: 90 and dist: 25)
            // move to right just to score
            DistanceSensorLineUp.lineUpRed(false);
            Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
            PutGlyphInBox.putInBoxWithPush(false);

            column = columnState.RIGHT;
            //columnState = 1;

        }

        return column;

    }

    public static columnState NearBlue() throws InterruptedException{
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            if (vuMark == RelicRecoveryVuMark.RIGHT) {
                Util.telemetry("Move To Right Column", true);
                //Move.startRight(0.5, 50, true);
                Move.strafeAngle(-90, 0.4, 700 + tuningVariable, true); //.3 enough to move on mat
                DistanceSensorLineUp.lineUpBlue(false);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBoxWithPush(false);
                Move.strafeAngle(90, 0.5, 650, true);
                column = columnState.RIGHT;
            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {
                Util.telemetry("Move To Center Column", true);
                //Move.startRight(0.5, 300, true);
                Move.strafeAngle(-90, 0.4, 300 + tuningVariable, true);
                DistanceSensorLineUp.lineUpBlue(false);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBoxWithPush(false);
                Move.strafeAngle(90, 0.5, 275, true);
                column = columnState.CENTER;
            }
            else if (vuMark == RelicRecoveryVuMark.LEFT) {
                Util.telemetry("Move To Left Column", true);
                //Move.startRight(0.5, 600, true);
                Move.strafeAngle(90, 0.4, 45, true); //Manually tune (originally angle: -90 and dist: 25)
                DistanceSensorLineUp.lineUpBlue(false);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBoxWithPush(false);
                //set up to get more glyphs - move to right column
                column = columnState.LEFT;
            }

        }
        else {
            Util.telemetry("No VuMark Seen", true);
            Move.strafeAngle(90, 0.4, 45, true); //Manually tune (originally angle: -90 and dist: 25)
            // move to left just to score
            DistanceSensorLineUp.lineUpBlue(false);
            Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
            PutGlyphInBox.putInBoxWithPush(false);
            column = columnState.LEFT;
        }

        return column;
    }

    public static columnState FarRed() throws InterruptedException {
        Util.telemetry("We are in the far red", 1, true);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            if (vuMark == RelicRecoveryVuMark.RIGHT) {
                Util.telemetry("Move to Right Column", true);

                //Move.strafeAngleWithHeading(-90, 0.4, 665, true, -88);

                Move.strafeAngleWithHeading(-90, 0.4, 610 + tuningVariable, true, -88);

                //Move.strafeAngle(0, 0.3, 60); //.3 enough to move on mat
                //will need to fine tune
                DistanceSensorLineUp.lineUpWithHeading(false, -88);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(90, 0.6, 1200, false, -88);
                column = columnState.RIGHT;
            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {
                Util.telemetry("Move to Center Column", true);
                Move.strafeAngleWithHeading(-90, 0.4, 240 + tuningVariable, true, -88);
                //Move.strafeAngle(0, 0.3, 10);
                //will need to fine tune
                DistanceSensorLineUp.lineUpWithHeading(false, -88);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(90, 0.6, 800, false, -88);
                column = columnState.CENTER;

            }
            else if (vuMark == RelicRecoveryVuMark.LEFT) {
                Util.telemetry("Move to Left Column", true);
                Move.strafeAngleWithHeading(-90, 0.4, 10, true, -88); //Manually tune (originally angle: 90 and dist: 60)
                //Move.strafeAngle(180, 0.3, 40);
                //will need to fine tune
                DistanceSensorLineUp.lineUpWithHeading(false, -88);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(90,0.6, 525, true, -88);
                column = columnState.LEFT;
            }

        }
        else {
            Util.telemetry("No VuMark Seen", true);
            Move.strafeAngleWithHeading(-90, 0.4, 10, true, -88); //Manually tune (originally angle: 90 and dist: 60)
            //move to center column just to score points
            DistanceSensorLineUp.lineUpWithHeading(false, -88);
            Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
            PutGlyphInBox.putInBox();
            Move.strafeAngleWithHeading(90,0.6, 525, true, -88);
            column = columnState.LEFT;
        }

        return column;

    }
    public  static columnState FarBlueMulti() throws InterruptedException{
        Util.telemetry("We are in the far blue", 1, true);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            if (vuMark == RelicRecoveryVuMark.LEFT) {
                Util.telemetry("Move to Left Column", true);
                Move.strafeAngleWithHeading(90, 0.4, 610 + tuningVariable, true, 75);
                //Move.strafeAngle(0, 0.3, 60); //.3 enough to move on mat
                //will need to fine tune
                DistanceSensorLineUp.lineUpBlueHeading(false, 75);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(-90, 0.6, 1200, false, 75);
                column = columnState.LEFT;
            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {
                Util.telemetry("Move to Center Column", true);
                Move.strafeAngleWithHeading(90, 0.4, 240 + tuningVariable, true, 75);
                //Move.strafeAngle(0, 0.3, 10);
                //will need to fine tune
                DistanceSensorLineUp.lineUpBlueHeading(false, 75);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(-90, 0.6, 800, false, 75);
                column = columnState.CENTER;

            }
            else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                Util.telemetry("Move to Right Column", true);
                Move.strafeAngleWithHeading(90, 0.4, 10, true, 75); //Manually tune (originally angle: -90 and dist: 60)
                //Move.strafeAngle(180, 0.3, 40);
                //will need to fine tune
                DistanceSensorLineUp.lineUpBlueHeading(false, 75);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(-90,0.6, 525, true, 75);
                column = columnState.RIGHT;
            }

        }
        else {
            Util.telemetry("No VuMark Seen", true);
            Move.strafeAngleWithHeading(90, 0.4, 10, true, 75); //Manually tune (originally angle: -90 and dist: 60)
            //move to center column just to score points
            DistanceSensorLineUp.lineUpBlueHeading(false, 75);
            Util.distanceSensorArm.setPosition(Util.distanceSensorArmUp);
            PutGlyphInBox.putInBox();
            Move.strafeAngleWithHeading(-90,0.6, 525, true, 75);
            column = columnState.RIGHT;
        }

        return column;

    }

    public static columnState FarBlue() throws InterruptedException{
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            if (vuMark == RelicRecoveryVuMark.RIGHT) {
                Move.strafeAngleWithHeading(90, 0.4, 375, true, -88);
                //Move.strafeAngle(180, 0.3, 40);
                //will need to fine tune
                DistanceSensorLineUp.lineUpWithHeading(false, -88);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBox();
                column = columnState.RIGHT;
            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {
                Util.telemetry("Move to Center Column", true);
                Move.strafeAngleWithHeading(-90, 0.4, 320, true, -88);
                //Move.strafeAngle(0, 0.3, 10);
                //will need to fine tune
                DistanceSensorLineUp.lineUpWithHeading(false, -88);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(-90, 0.4, 275, false, -88);
                column = columnState.CENTER;

            }
            else if (vuMark == RelicRecoveryVuMark.LEFT) {
                Move.strafeAngleWithHeading(90, 0.4, 665, true, -88);
                //Move.strafeAngle(0, 0.3, 60); //.3 enough to move on mat
                //will need to fine tune
                DistanceSensorLineUp.lineUpWithHeading(false, -88);
                Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
                PutGlyphInBox.putInBox();
                Move.strafeAngleWithHeading(-90, 0.4, 675, false, -88);
                column = columnState.LEFT;
            }

        }
        else {
            Move.strafeAngleWithHeading(90, 0.4, 375, true, -88);
            //Move.strafeAngle(180, 0.3, 40);
            //will need to fine tune
            DistanceSensorLineUp.lineUpWithHeading(false, -88);
            Util.distanceSensorArm.setPosition(Util.distanceSensorArmThreeQuartersDown);
            PutGlyphInBox.putInBox();
            column = columnState.RIGHT;
        }

        return column;
    }



}
