package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

import static org.firstinspires.ftc.teamcode.RelicRecovery.getGlyphs.glyphState.BREAK_LOOP;
import static org.firstinspires.ftc.teamcode.RelicRecovery.getGlyphs.glyphState.LEAVE_PILE;
import static org.firstinspires.ftc.teamcode.RelicRecovery.getGlyphs.glyphState.TURN_LEFT;
import static org.firstinspires.ftc.teamcode.RelicRecovery.getGlyphs.glyphState.TURN_RIGHT;

/**
 * Created by lulzbot on 2/26/18.
 */

public class getGlyphs {
    public static int glyphCount;
    private static int encoderTicks;
    private static boolean isOpen;
    private static boolean canOpen = true;
    private static boolean turnedRight;
    private static boolean turnedLeft;

    private static boolean moving;


    public enum glyphState {
        STRAIGHT, TURN_RIGHT, TURN_LEFT, LEAVE_PILE, BREAK_LOOP
    }

    public enum glyphColor {
        GREY, BROWN, UNKNOWN
    }

    private static glyphState myState;
    public static glyphColor myColor;
    public static glyphColor firstGlyphColor;
    public static glyphColor secondGlyphColor;

    private getGlyphs() throws Exception {
        throw new Exception();
    }

    static boolean onTheRight = false; //same right as column
    static boolean onTheLeft = false; //same left as column


    public static void REDNoStateMachineGetGlyphs() throws InterruptedException {
        Util.resetEncoders();
        Util.intake1.setPower(0.9);
        Util.intake2.setPower(0.9);
        Move.strafeAngle(0, 0.25, 300, true); //6in?
        Move.rotateClockwiseForMultiGlyph();
        Thread.sleep(100);
        Move.strafeAngleWithoutPID(0, 0.25, 200); //2in?
        Thread.sleep(500);
        Util.intake1.setPower(-0.9);
        Util.intake2.setPower(-0.9);
        Thread.sleep(250);
        Util.intake1.setPower(0);
        Util.intake2.setPower(0);
        Move.strafeAngleWithoutPID(180, 0.25, 200); //2in?
        Move.rotateCounterClockwiseForMultiGlyph();
        Thread.sleep(100);
        Move.strafeAngle(180, 0.25, 300, true);
        Move.strafeAngle(90, 0.3, 425, true); //should put robot in center
    }
    public static void BLUENoStateMachineGetGlyphs() throws InterruptedException {
        Util.resetEncoders();
        Util.intake1.setPower(0.9);
        Util.intake2.setPower(0.9);
        Move.strafeAngle(0, 0.25, 300, true); //6in?
        Move.rotateCounterClockwiseForMultiGlyph();
        Thread.sleep(100);
        Move.strafeAngleWithoutPID(0, 0.25, 200); //2in?
        Thread.sleep(500);
        Util.intake1.setPower(-0.9);
        Util.intake2.setPower(-0.9);
        Thread.sleep(250);
        Util.intake1.setPower(0);
        Util.intake2.setPower(0);
        Move.strafeAngleWithoutPID(180, 0.25, 200); //2in?
        Move.rotateClockwiseForMultiGlyph();
        Thread.sleep(100);
        Move.strafeAngle(180, 0.25, 300, true);
        Move.strafeAngle(-90, 0.3, 400, true); //should put robot in center
    }

    public static void RunWithStatesRED() throws InterruptedException {
        Util.resetEncoders();
        glyphCount = 0;
        Util.intake1.setPower(0.9); //trying .8 may give counter longer
        Util.intake2.setPower(0.9);
        IntakeControl.ManageGlyphCounterData();
        myState = glyphState.STRAIGHT;
        turnedRight = false;
        turnedLeft = false;
        moving = false;

        while (myState != BREAK_LOOP) {
            switch (myState) {
                case STRAIGHT:
                    //Move into pile and try to collect
                    //glyphCounter(); //Get glyph count
                    if (!moving) {
                        Move.strafeAngle(0, 0.25);
                        Thread.sleep(5);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                   // IntakeControl.ManageDataAndHandleStalls();
                    //Get glyph count

                    if (glyphCount == 2) {
                        //if we have 2 glyphs, leave
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);

                        //added without testing
                        IntakeControl.ManageGlyphCounterData();

                        //myState = LEAVE_PILE;
                    }

                    if (Util.rightFront.getCurrentPosition() > 400) { // cut out to improve proformance glyphCount == 1 ||
                        //if we have gone over our encoder position go to the next collection
                        myState = TURN_RIGHT;
                        Move.rotateClockwiseForMultiGlyph();
                        Util.resetEncoders();
                        moving = false;
                    }
                    break;
                case TURN_RIGHT:
                    //try to collect after turning right
                    //glyphCounter(); //Get glyph count
                    turnedRight = true; //we need to turn left leaving
                    if (!moving) {
                        Move.strafeAngleWithoutPID(0, 0.25);
                        Thread.sleep(5);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                    //IntakeControl.ManageDataAndHandleStalls();

                    if (glyphCount == 2) {
                        //if we have 2 glyphs
                        //myState = LEAVE_PILE;
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);

                        //added without testing
                        IntakeControl.ManageGlyphCounterData();

                    }

                    if (Util.rightFront.getCurrentPosition() > 400) {
                        myState = LEAVE_PILE;
                       // Move.rotateCounterClockwiseForMultiGlyph();
                        //use this rotation if consistently only get one
                        Util.telemetry("glyphs", glyphCount, true);
                        Util.resetEncoders();
                        moving = false;
                        // (hopefully by this point we have 2)
                    }
                    break;
//                case TURN_LEFT:
//                    //glyphCounter(); //Get glyph count
//                    if (VuforiaGoToColumn.column != VuforiaGoToColumn.columnState.LEFT){
//                        Util.telemetry("Turn Left!!", 0);
//                        turnedLeft = true; //used to help with leaving pile
//                        //try to collect after turning left
//                        if (!moving) {
//                            Move.strafeAngleWithoutPID(0, 0.25);
//                            moving = true;
//                        } else {
//                            Thread.sleep(10);
//                        }
//                        IntakeControl.ManageGlyphCounterData();
//                        IntakeControl.ManageDataAndHandleStalls();
//
//
//                        Util.telemetry("glyph count", glyphCount, true);
//                        if (glyphCount == 2) {
//                            //if we have 2 glyphs
//                            myState = LEAVE_PILE;
//                        } else if (Util.rightFront.getCurrentPosition() > 400) {
//                            myState = LEAVE_PILE;
//                        }
//                    }
//                    else {
//                        myState = LEAVE_PILE;
//                    }
//                    break;
                case LEAVE_PILE:
                    //Intakes off
                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);
                    Move.strafeAngleWithoutPID(180, 0.3, 100);
                    Util.intake1.setPower(0.9);
                    Util.intake2.setPower(0.9);

                    //added without testing
                    IntakeControl.ManageGlyphCounterData();

                    Thread.sleep(100);
                    Util.telemetry("Leave Pile!!", 0);

                    //COULD SPEED UP COMING OUT

                    //Leave pile using encoder ticks so we end up in a semi-consistent place
                    Move.rotateCounterClockwiseForMultiGlyph();
                    Move.strafeAngle(180, 0.3, 600, true);
                    Move.strafeAngle(90, 0.4, 300, true);

//                    if (!turnedRight) {
//                        //get how far we've gone and go back that far
//                        Move.strafeAngle(180, 0.3, Math.abs(Util.rightFront.getCurrentPosition()), true);
//                        Move.strafeAngle(90, 0.4, 400, true);
//                    } else if (turnedRight) {
//                        int currentPos = Util.rightFront.getCurrentPosition();
//                        Move.rotateCounterClockwiseForMultiGlyph();
//                        Move.strafeAngle(180, 0.3, (int) (400 + (0.6 * currentPos)), true);
//                        Move.strafeAngle(90, 0.4, (int) (400 - (0.8 * currentPos)), true);
//                    }

                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);
// else if (turnedRight && turnedLeft) {
//                        // pull out of pile with turn
//                        //retrace steps
//                        //get how far we've gone and go back that far and pull out of the pile (went 560 ticks in(estimated assuming degree turned is 30))
//                        encoderTicks = Util.rightFront.getCurrentPosition();
//                        Move.strafeAngle(180, 0.3, encoderTicks + 450, true); //COULD SPEED UP
//                        //strafe to middle for vision code
//                        Thread.sleep(100);
//                        Move.strafeAngle(90, 0.4, 100, true);
//                    }
//                    else {
//                        //pull out of pile without turn
//                        Move.strafeAngle(180, 0.35, 250, false);
//                        Move.strafeAngle(90, 0.4, 300, false);
//                    }
                    myState = BREAK_LOOP;
                    break;

            }
        }
        Util.telemetry("WHILE Loop broken", 0);
    }

    public static void RunWithStatesBLUE() throws InterruptedException {
        Util.resetEncoders();
        glyphCount = 0;
        Util.intake1.setPower(0.9); //trying .8 may give counter longer
        Util.intake2.setPower(0.9);
        IntakeControl.ManageGlyphCounterData();
        myState = glyphState.STRAIGHT;
        turnedRight = false;
        turnedLeft = false;
        moving = false;

        while (myState != BREAK_LOOP) {
            switch (myState) {
                case STRAIGHT:
                    //Move into pile and try to collect
                    //glyphCounter(); //Get glyph count
                    if (!moving) {
                        Move.strafeAngle(0, 0.25);
                        Thread.sleep(5);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                    // IntakeControl.ManageDataAndHandleStalls();
                    //Get glyph count

                    if (glyphCount == 1){
                        firstGlyphColor = myColor;
                    }

                    if (glyphCount == 2) {
                        //if we have 2 glyphs, leave
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);
                        secondGlyphColor = myColor;
                        //myState = LEAVE_PILE;
                    }

                    if (Util.rightFront.getCurrentPosition() > 400) { // cut out to improve proformance glyphCount == 1 ||
                        //if we have gone over our encoder position go to the next collection
                        myState = TURN_LEFT;
                        Move.rotateCounterClockwiseForMultiGlyph();
                        Util.resetEncoders();
                        moving = false;
                    }
                    break;
                case TURN_LEFT:
                    //try to collect after turning right
                    //glyphCounter(); //Get glyph count
                    turnedLeft = true; //we need to turn left leaving
                    if (!moving) {
                        Move.strafeAngleWithoutPID(0, 0.25);
                        Thread.sleep(5);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                    //IntakeControl.ManageDataAndHandleStalls();

                    if (glyphCount == 1){
                        firstGlyphColor = myColor;
                    }
                    if (glyphCount == 2) {
                        //if we have 2 glyphs
                        //myState = LEAVE_PILE;
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);
                        secondGlyphColor = myColor;
                    }

                    if (Util.rightFront.getCurrentPosition() > 400) {
                        myState = LEAVE_PILE;
                        // Move.rotateCounterClockwiseForMultiGlyph();
                        //use this rotation if consistently only get one
                        Util.telemetry("glyphs", glyphCount, true);
                        Util.resetEncoders();
                        moving = false;
                        // (hopefully by this point we have 2)
                    }
                    break;
//
                case LEAVE_PILE:
                    //Intakes off
                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);
                    Move.strafeAngleWithoutPID(180, 0.3, 100);
                    Util.intake1.setPower(0.9);
                    Util.intake2.setPower(0.9);
                    Thread.sleep(100);
                    Util.telemetry("Leave Pile!!", 0);

                    IntakeControl.ManageGlyphCounterData();

                    //COULD SPEED UP COMING OUT

                    //Leave pile using encoder ticks so we end up in a semi-consistent place
                    Move.rotateClockwiseForMultiGlyph();
                    Move.strafeAngle(180, 0.3, 600, true);
                    Move.strafeAngle(-90, 0.4, 150, true);

//                    if (!turnedRight) {
//                        //get how far we've gone and go back that far
//                        Move.strafeAngle(180, 0.3, Math.abs(Util.rightFront.getCurrentPosition()), true);
//                        Move.strafeAngle(90, 0.4, 400, true);
//                    } else if (turnedRight) {
//                        int currentPos = Util.rightFront.getCurrentPosition();
//                        Move.rotateCounterClockwiseForMultiGlyph();
//                        Move.strafeAngle(180, 0.3, (int) (400 + (0.6 * currentPos)), true);
//                        Move.strafeAngle(90, 0.4, (int) (400 - (0.8 * currentPos)), true);
//                    }

                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);
// else if (turnedRight && turnedLeft) {
//                        // pull out of pile with turn
//                        //retrace steps
//                        //get how far we've gone and go back that far and pull out of the pile (went 560 ticks in(estimated assuming degree turned is 30))
//                        encoderTicks = Util.rightFront.getCurrentPosition();
//                        Move.strafeAngle(180, 0.3, encoderTicks + 450, true); //COULD SPEED UP
//                        //strafe to middle for vision code
//                        Thread.sleep(100);
//                        Move.strafeAngle(90, 0.4, 100, true);
//                    }
//                    else {
//                        //pull out of pile without turn
//                        Move.strafeAngle(180, 0.35, 250, false);
//                        Move.strafeAngle(90, 0.4, 300, false);
//                    }
                    myState = BREAK_LOOP;
                    break;

            }
        }
        Util.telemetry("WHILE Loop broken", 0);
    }

    public static void RunWithStatesFarRed() throws InterruptedException{
        //Move.strafeAngleWithHeading(90,0.4 ,600,true, -88);
        Move.strafeAngleWithHeading(0,0.5, 550,false, -88);
        Util.intake1.setPower(0.9); //trying .8 may give counter longer
        Util.intake2.setPower(0.9);
        Move.strafeAngleWithHeading(0,0.4, 400,true, -88);
//
        Util.resetEncoders();
        glyphCount = 0;
        Util.intake1.setPower(0.9); //trying .8 may give counter longer
        Util.intake2.setPower(0.9);
        IntakeControl.ManageGlyphCounterData();
        myState = glyphState.STRAIGHT;
        turnedRight = false;
        turnedLeft = false;
        moving = false;

        while (myState != BREAK_LOOP) {
            switch (myState) {
                case STRAIGHT:
                    //Move into pile and try to collect
                    //glyphCounter(); //Get glyph count
                    if (!moving) {
                        Move.strafeAngleWithHeading(0, 0.25, -88);
                        Thread.sleep(5);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                    // IntakeControl.ManageDataAndHandleStalls();
                    //Get glyph count

                    if (glyphCount == 2) {
                        //if we have 2 glyphs, leave
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);
                        //myState = LEAVE_PILE;
                    }

                    if (Util.rightFront.getCurrentPosition() > 100) { // cut out to improve proformance glyphCount == 1 ||
                        //if we have gone over our encoder position go to the next collection
                        myState = TURN_RIGHT;
                        Move.rotateClockwiseForMultiGlyph();
                        Util.resetEncoders();
                        moving = false;
                    }
                    break;
                case TURN_RIGHT:
                    //try to collect after turning right
                    //glyphCounter(); //Get glyph count
                    turnedRight = true; //we need to turn left leaving
                    if (!moving) {
                        Move.strafeAngleWithoutPID(0, 0.25);
                        Thread.sleep(5);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                    //IntakeControl.ManageDataAndHandleStalls();

                    if (glyphCount == 2) {
                        //if we have 2 glyphs
                        //myState = LEAVE_PILE;
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);
                    }

                    if (Util.rightFront.getCurrentPosition() > 550) {
                        myState = LEAVE_PILE;
                        // Move.rotateCounterClockwiseForMultiGlyph();
                        //use this rotation if consistently only get one
                        Util.telemetry("glyphs", glyphCount, true);
                        Util.resetEncoders();
                        moving = false;
                        // (hopefully by this point we have 2)
                    }
                    break;
//                case TURN_LEFT:
//                    //glyphCounter(); //Get glyph count
//                    if (VuforiaGoToColumn.column != VuforiaGoToColumn.columnState.LEFT){
//                        Util.telemetry("Turn Left!!", 0);
//                        turnedLeft = true; //used to help with leaving pile
//                        //try to collect after turning left
//                        if (!moving) {
//                            Move.strafeAngleWithoutPID(0, 0.25);
//                            moving = true;
//                        } else {
//                            Thread.sleep(10);
//                        }
//                        IntakeControl.ManageGlyphCounterData();
//                        IntakeControl.ManageDataAndHandleStalls();
//
//
//                        Util.telemetry("glyph count", glyphCount, true);
//                        if (glyphCount == 2) {
//                            //if we have 2 glyphs
//                            myState = LEAVE_PILE;
//                        } else if (Util.rightFront.getCurrentPosition() > 400) {
//                            myState = LEAVE_PILE;
//                        }
//                    }
//                    else {
//                        myState = LEAVE_PILE;
//                    }
//                    break;
                case LEAVE_PILE:
                    //Intakes off
                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);
                    Move.strafeAngleWithoutPID(180, 0.3, 550);
                    Util.setAllPowers(0);
                    Move.rotateCounterClockwiseForMultiGlyph();
                    Util.setAllPowers(0);
                    Util.intake1.setPower(0.9);
                    Util.intake2.setPower(0.9);

                    IntakeControl.ManageGlyphCounterData();

                    Move.strafeAngleWithHeading(-90, 0.4, 250,false, -88);
                    Move.strafeAngleforTimeWithHeading(-90, 0.35, 0.35, -88);
                    Move.strafeAngleWithHeading(90,0.4, 20, true, -88);
                    Util.telemetry("Leave Pile!!", 0);


                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);

                    myState = BREAK_LOOP;
                    break;

            }
        }
        Util.telemetry("WHILE Loop broken", 0);
    }
    public static void RunWithStatesFarBlue() throws InterruptedException{
        //Move.strafeAngleWithHeading(90,0.4 ,600,true, -88);
        Move.strafeAngleWithHeading(0,0.5, 550,false, 90);
        Util.intake1.setPower(0.9); //trying .8 may give counter longer
        Util.intake2.setPower(0.9);
        Move.strafeAngleWithHeading(0,0.4, 400,true, 90);
//
        Util.resetEncoders();
        glyphCount = 0;
        Util.intake1.setPower(0.9); //trying .8 may give counter longer
        Util.intake2.setPower(0.9);
        IntakeControl.ManageGlyphCounterData();
        myState = glyphState.STRAIGHT;
        turnedRight = false;
        turnedLeft = false;
        moving = false;

        while (myState != BREAK_LOOP) {
            switch (myState) {
                case STRAIGHT:
                    //Move into pile and try to collect
                    //glyphCounter(); //Get glyph count
                    if (!moving) {
                        Move.strafeAngleWithHeading(0, 0.25, 90);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                    // IntakeControl.ManageDataAndHandleStalls();
                    //Get glyph count

                    if (glyphCount == 2) {
                        //if we have 2 glyphs, leave
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);
                        //myState = LEAVE_PILE;
                    }

                    if (Util.leftFront.getCurrentPosition() > 100) { // cut out to improve proformance glyphCount == 1 ||
                        //if we have gone over our encoder position go to the next collection
                        myState = TURN_RIGHT;
                        Move.rotateCounterClockwiseForMultiGlyph();
                        Util.resetEncoders();
                        moving = false;
                    }
                    break;
                case TURN_RIGHT:
                    //try to collect after turning right
                    //glyphCounter(); //Get glyph count
                    turnedRight = true; //we need to turn left leaving
                    if (!moving) {
                        Move.strafeAngleWithoutPID(0, 0.25);
                        moving = true;
                    } else {
                        Thread.sleep(10);
                    }

                    IntakeControl.ManageGlyphCounterData();
                    //IntakeControl.ManageDataAndHandleStalls();

                    if (glyphCount == 2) {
                        //if we have 2 glyphs
                        //myState = LEAVE_PILE;
                        Util.intake1.setPower(0);
                        Util.intake2.setPower(0);
                    }

                    if (Util.rightFront.getCurrentPosition() > 550) { //needs to be 550
                        myState = LEAVE_PILE;
                        // Move.rotateCounterClockwiseForMultiGlyph();
                        //use this rotation if consistently only get one
                        Util.telemetry("glyphs", glyphCount, true);
                        Util.resetEncoders();
                        moving = false;
                        // (hopefully by this point we have 2)
                    }
                    break;
//                case TURN_LEFT:
//                    //glyphCounter(); //Get glyph count
//                    if (VuforiaGoToColumn.column != VuforiaGoToColumn.columnState.LEFT){
//                        Util.telemetry("Turn Left!!", 0);
//                        turnedLeft = true; //used to help with leaving pile
//                        //try to collect after turning left
//                        if (!moving) {
//                            Move.strafeAngleWithoutPID(0, 0.25);
//                            moving = true;
//                        } else {
//                            Thread.sleep(10);
//                        }
//                        IntakeControl.ManageGlyphCounterData();
//                        IntakeControl.ManageDataAndHandleStalls();
//
//
//                        Util.telemetry("glyph count", glyphCount, true);
//                        if (glyphCount == 2) {
//                            //if we have 2 glyphs
//                            myState = LEAVE_PILE;
//                        } else if (Util.rightFront.getCurrentPosition() > 400) {
//                            myState = LEAVE_PILE;
//                        }
//                    }
//                    else {
//                        myState = LEAVE_PILE;
//                    }
//                    break;
                case LEAVE_PILE:
                    //Intakes off
                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);
                    Move.strafeAngleWithoutPID(180, 0.3, 550); //needs to be 550
                    Util.setAllPowers(0);
                    Move.rotateClockwiseForMultiGlyph();
                    Util.setAllPowers(0);
                    Util.intake1.setPower(0.9);
                    Util.intake2.setPower(0.9);

                    IntakeControl.ManageGlyphCounterData();

                    Move.strafeAngleWithHeading(90, 0.4, 250,false, 90);
                    Move.strafeAngleforTimeWithHeading(90, 0.35, 0.35, 90);
                    Move.strafeAngleWithHeading(-90,0.4, 20, true, 90);
                    Util.telemetry("Leave Pile!!", 0);


                    Util.intake1.setPower(0);
                    Util.intake2.setPower(0);

                    myState = BREAK_LOOP;
                    break;

            }
        }
        Util.telemetry("WHILE Loop broken", 0);
    }
}
