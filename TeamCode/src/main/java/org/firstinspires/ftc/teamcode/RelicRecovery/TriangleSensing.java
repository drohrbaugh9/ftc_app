/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.RelicRecovery;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@Autonomous(name = "Triangle Sensing", group = "Test")
@Disabled

public class TriangleSensing extends LinearOpMode {

    NormalizedColorSensor [] array = new NormalizedColorSensor[2];

    NormalizedColorSensor RightColorSensor;
    NormalizedColorSensor LeftColorSensor;


    @Override public void runOpMode() throws InterruptedException {

       runSample();
    }

    protected void runSample() throws InterruptedException {

        // values is a reference to the hsvValues array.
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        // Get a reference to our sensor object.

        RightColorSensor = hardwareMap.get(NormalizedColorSensor.class, "RightColorSensor");
        LeftColorSensor = hardwareMap.get(NormalizedColorSensor.class, "LeftColorSensor");

        array [0] = RightColorSensor;
        array [1] = LeftColorSensor;

        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        for (Object colorsensor : array) {
            if (colorsensor instanceof SwitchableLight) {
                ((SwitchableLight) colorsensor).enableLight(true);
            }
        }

        // Wait for the start button to be pressed.
        waitForStart();

        // Loop until we are asked to stop
        while (opModeIsActive()) {


            NormalizedRGBA RightColors = RightColorSensor.getNormalizedColors();
            NormalizedRGBA LeftColors = LeftColorSensor.getNormalizedColors();

            /** We also display a conversion of the colors to an equivalent Android color integer.
             * @see Color */


            float Rightmax = Math.max(Math.max(Math.max(RightColors.red, RightColors.green), RightColors.blue), RightColors.alpha);
            RightColors.red   /= Rightmax;
            RightColors.green /= Rightmax;
            RightColors.blue  /= Rightmax;
            int RightColor = RightColors.toColor();
            float Leftmax = Math.max(Math.max(Math.max(LeftColors.red, LeftColors.green), LeftColors.blue), LeftColors.alpha);
            LeftColors.red   /= Leftmax;
            LeftColors.green /= Leftmax;
            LeftColors.blue  /= Leftmax;
            int LeftColor = LeftColors.toColor();

            telemetry.addLine("normalized color (Right):  ")
                    .addData("a", "%02x", Color.alpha(RightColor))
                    .addData("r", "%02x", Color.red(RightColor))
                    .addData("g", "%02x", Color.green(RightColor))
                    .addData("b", "%02x", Color.blue(RightColor));
            telemetry.addLine("normalized color (Left):  ")
                    .addData("a", "%02x", Color.alpha(LeftColor))
                    .addData("r", "%02x", Color.red(LeftColor))
                    .addData("g", "%02x", Color.green(LeftColor))
                    .addData("b", "%02x", Color.blue(LeftColor));
            telemetry.update();

            if (Color.red(LeftColor) > 128) {
                telemetry.addLine("Over Red Tape!");
            }
            if (Color.red(RightColor) > 128){
                telemetry.addLine("Over Red Tape!");
            }
            if (Color.red(LeftColor) > 128 & Color.red(RightColor) > 128){
                telemetry.addLine("Centered on Red CryptoBox");
            }

            if (Color.blue(LeftColor) > 96) {
                telemetry.addLine("Over Blue Tape!");
            }
            if (Color.blue(RightColor) > 96){
                telemetry.addLine("Over Blue Tape!");
            }
            if (Color.blue(LeftColor) > 96 & Color.blue(RightColor) > 96){
                telemetry.addLine("Centered on Blue CryptoBox");
            }

            // convert the RGB values to HSV values.
            Color.RGBToHSV(Color.red(RightColor), Color.green(RightColor), Color.blue(RightColor), hsvValues);

            // convert the RGB values to HSV values.
            Color.RGBToHSV(Color.red(LeftColor), Color.green(LeftColor), Color.blue(LeftColor), hsvValues);
        }
    }
}
