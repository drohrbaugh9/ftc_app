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

@Autonomous(name = "Column Distance Data to file", group = "Test")
@Disabled
public class ColumnDistanceWriteToFile extends LinearOpMode{

    double distance;
    static Writer fileWriter;

    long currentTime;
    long oldTime = System.nanoTime() / 1000000;


    public void runOpMode() throws InterruptedException {
        Util.init(this);

        try {
            File logfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "LogFile.csv");
            //logfile.createNewFile();
            FileWriter writer = new FileWriter(logfile);
            fileWriter = new BufferedWriter(writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write to file", e);
        }


        waitForStart();
        Util.distanceSensorArm.setPosition(Util.distanceSensorArmDown);

        while (opModeIsActive()) {
            currentTime = System.nanoTime() / 1000000;
            distance = Util.columnAlignDistance.getDistance(DistanceUnit.CM);
            log(""+distance);
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

