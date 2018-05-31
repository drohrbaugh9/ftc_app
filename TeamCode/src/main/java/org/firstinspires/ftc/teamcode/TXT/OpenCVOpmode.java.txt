package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by lulzbot on 1/17/18.
 */
@Autonomous(name = "OpenCVCamera", group = "vision")
@Disabled
public class OpenCVOpmode extends OpMode{
    CameraView camera = new CameraView();

    @Override
    public void init() {
        telemetry.addLine("hello world");
        telemetry.update();
        camera.open();
        if(camera.checkOpened()){
            telemetry.addLine("camera is opened");
        }

    }






    @Override
    public void loop(){
        telemetry.addLine("running");

//        camera.getImage();
//        camera.returnImage();
        telemetry.update();
    }
}
