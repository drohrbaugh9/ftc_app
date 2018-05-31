package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.File;

import android.graphics.Color;
import android.os.Environment;

/**
 * Created by elliot on 8/6/17.
 */
@TeleOp (name = "FileTest", group = "Test")
@Disabled

public class FileTest extends OpMode {

    private static Writer fileWriter;

    public void init() {
        try {
            File logfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "LogFile.txt");
            //logfile.createNewFile();
            FileWriter writer = new FileWriter(logfile);
            fileWriter = new BufferedWriter(writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write to file", e);
        }
    }

    public void loop() {
        log("Color Sensor Value: " + "RED " + Color.red(ColorSensor.RightColor) + "GREEN " + Color.green(ColorSensor.RightColor) + "BLUE " + Color.blue(ColorSensor.RightColor));
            // your loop code
    }

    public void stop() {
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

/* private Writer fileWriter;

    @Override
    public void init() {
        try {
            FileWriter writer = new FileWriter("/sdcard/path/to/your/file.ext");
            fileWriter = new BufferedWriter(writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot write to file", e);
        }

        // your code
    }

    @Override
    public void loop() {
        writeToFile(/** your data string, char[], char... );
        // your loop code
        }

@Override
public void stop() {
        try {
        fileWriter.close();
        } catch (IOException e) {
        throw new RuntimeException("Cannot close file", e);
        }

        // your stop code
        }

  */