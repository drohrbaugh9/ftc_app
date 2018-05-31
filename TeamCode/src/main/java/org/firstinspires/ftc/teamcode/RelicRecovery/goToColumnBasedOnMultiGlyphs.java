package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 4/11/18.
 */

public class goToColumnBasedOnMultiGlyphs {

    public static VuforiaGoToColumn.columnState NearRed (VuforiaGoToColumn.columnState column, LinearOpMode opMode) throws InterruptedException {
        if (getGlyphs.glyphCount == 1){
            if (getGlyphs.firstGlyphColor == getGlyphs.glyphColor.BROWN){
                Util.telemetry("CHEKERBOARD/FROG CYPHER", 0);
                Util.liftTrayforMulti = true;
                return column;
            }
            else {
                Util.telemetry("SNAKE CYPHER", 0);
                if (column != VuforiaGoToColumn.columnState.CENTER){
                    Util.liftTrayforMulti = true;
                    return column;
                }
                else {
                   return VuforiaGoToColumn.columnState.LEFT;
                }
            }
        }
        else if (getGlyphs.glyphCount == 2){
            if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.BROWN) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.BROWN)){
                Util.telemetry("SNAKE CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT){
                    return VuforiaGoToColumn.columnState.LEFT;
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER){
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
                else if (column == VuforiaGoToColumn.columnState.LEFT){
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
            }
            else if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.GREY) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.GREY)){
                Util.telemetry("SNAKE CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT){
                    Util.telemetry("REMOVE FIRST GLYPH", 0);
                    return VuforiaGoToColumn.columnState.LEFT;
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER){
                    return VuforiaGoToColumn.columnState.LEFT;
                }
                else if (column == VuforiaGoToColumn.columnState.LEFT){
                    Util.telemetry("REMOVE FIRST GLYPH", 0);
                    return VuforiaGoToColumn.columnState.RIGHT;
                }
            }
            else if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.GREY) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.BROWN)){
                Util.telemetry("CHEKERBOARD/FROG CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT){
                    return VuforiaGoToColumn.columnState.LEFT;
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER){
                    Util.telemetry("REMOVE FIRST GLYPH", 0);
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
                else if (column == VuforiaGoToColumn.columnState.LEFT){
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
            }
            else if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.BROWN) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.GREY)){
                Util.telemetry("CHEKERBOARD/FROG CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT){
                    return VuforiaGoToColumn.columnState.CENTER;
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER){
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
                else if (column == VuforiaGoToColumn.columnState.LEFT){
                    return VuforiaGoToColumn.columnState.CENTER;

                }
            }
            else {
                if (column == VuforiaGoToColumn.columnState.RIGHT){
                    return VuforiaGoToColumn.columnState.LEFT;
                }
                else if (column == VuforiaGoToColumn.columnState.CENTER){
                    return VuforiaGoToColumn.columnState.LEFT;

                }
                else if (column == VuforiaGoToColumn.columnState.LEFT){
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
            }
        }

        return VuforiaGoToColumn.columnState.UNKNOWN;
    }

    public static VuforiaGoToColumn.columnState NearBlue (VuforiaGoToColumn.columnState column, LinearOpMode opMode) throws InterruptedException {
        if (getGlyphs.glyphCount == 1) {
            if (getGlyphs.firstGlyphColor == getGlyphs.glyphColor.BROWN) {
                Util.telemetry("CHEKERBOARD/FROG CYPHER", 0);
                Util.liftTrayforMulti = true;
                return column;
            } else {
                Util.telemetry("SNAKE CYPHER", 0);
                if (column != VuforiaGoToColumn.columnState.CENTER) {
                    Util.liftTrayforMulti = true;
                    return column;
                } else {
                    return VuforiaGoToColumn.columnState.LEFT;
                }
            }
        } else if (getGlyphs.glyphCount == 2) {
            if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.BROWN) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.BROWN)) {
                Util.telemetry("SNAKE CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT) {
                    return VuforiaGoToColumn.columnState.LEFT;
                } else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    return VuforiaGoToColumn.columnState.RIGHT;

                } else if (column == VuforiaGoToColumn.columnState.LEFT) {
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
            } else if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.GREY) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.GREY)) {
                Util.telemetry("SNAKE CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT) {
                    Util.telemetry("REMOVE FIRST GLYPH", 0);
                    return VuforiaGoToColumn.columnState.LEFT;
                } else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    return VuforiaGoToColumn.columnState.LEFT;
                } else if (column == VuforiaGoToColumn.columnState.LEFT) {
                    Util.telemetry("REMOVE FIRST GLYPH", 0);
                    return VuforiaGoToColumn.columnState.RIGHT;
                }
            } else if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.GREY) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.BROWN)) {
                Util.telemetry("CHEKERBOARD/FROG CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT) {
                    return VuforiaGoToColumn.columnState.LEFT;
                } else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    Util.telemetry("REMOVE FIRST GLYPH", 0);
                    return VuforiaGoToColumn.columnState.LEFT;

                } else if (column == VuforiaGoToColumn.columnState.LEFT) {
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
            } else if ((getGlyphs.firstGlyphColor == getGlyphs.glyphColor.BROWN) && (getGlyphs.secondGlyphColor == getGlyphs.glyphColor.GREY)) {
                Util.telemetry("CHEKERBOARD/FROG CYPHER", 0);
                if (column == VuforiaGoToColumn.columnState.RIGHT) {
                    return VuforiaGoToColumn.columnState.CENTER;

                } else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    return VuforiaGoToColumn.columnState.LEFT;

                } else if (column == VuforiaGoToColumn.columnState.LEFT) {
                    return VuforiaGoToColumn.columnState.CENTER;

                }
            } else {
                if (column == VuforiaGoToColumn.columnState.RIGHT) {
                    return VuforiaGoToColumn.columnState.LEFT;

                } else if (column == VuforiaGoToColumn.columnState.CENTER) {
                    return VuforiaGoToColumn.columnState.LEFT;

                } else if (column == VuforiaGoToColumn.columnState.LEFT) {
                    return VuforiaGoToColumn.columnState.RIGHT;

                }
            }
        } else {
            Util.telemetry("COLUMN UNKNOWN", 0);
            return VuforiaGoToColumn.columnState.UNKNOWN;
        }

        return VuforiaGoToColumn.columnState.UNKNOWN;
    }
}
