package org.firstinspires.ftc.teamcode.RelicRecovery;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by elliot on 3/19/18.
 */

@Autonomous(name = "Intake Color and Distance Data to file", group = "Test")
@Disabled
public class ColorGlyphDetectWriteToFile extends LinearOpMode{
    double deltaRight;
    double deltaLeft;
    int red;
    int green;
    int blue;
    int alpha;
    double distance;
    static Writer fileWriter;

    long currentTime;
    long oldTime = System.nanoTime() / 1000000;

    public static DcMotor intake1;
    public static DcMotor intake2;

    public void runOpMode() throws InterruptedException {
        Util.init(this);
        IntakeControl.init();
        intake1 = hardwareMap.dcMotor.get("intake_left"); intake1.setDirection(DcMotorSimple.Direction.REVERSE);
        intake2 = hardwareMap.dcMotor.get("intake_right");

        intake1.setPower(0.9);
        intake2.setPower(0.9);

        try {
            File logfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "LogFile.csv");
            //logfile.createNewFile();
            FileWriter writer = new FileWriter(logfile);
            fileWriter = new BufferedWriter(writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write to file", e);
        }


        waitForStart();

        while (opModeIsActive()) {
            currentTime = System.nanoTime() / 1000000;
            IntakeControl.ManageEncoderData(currentTime - oldTime);
            deltaRight = IntakeControl.rightIntakeSum / IntakeControl.MOVING_AVERAGE_LENGTH;
            deltaLeft = IntakeControl.leftIntakeSum / IntakeControl.MOVING_AVERAGE_LENGTH;
            red = Util.glyphCounterColor.red();
            green = Util.glyphCounterColor.green();
            blue = Util.glyphCounterColor.blue();
            alpha = Util.glyphCounterColor.alpha();
            distance = Util.glyphCounterDistance.getDistance(DistanceUnit.CM);
            log(deltaLeft + "," + deltaRight + "," + red + "," + green + "," + blue + "," + alpha + "," + distance);
            oldTime = currentTime;
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot close file", e);
        }

        // your stop code
    }

    public static void log(String s) {
        try {
            fileWriter.write(s + "\n");
        } catch (IOException e) {

        }


    }
}
