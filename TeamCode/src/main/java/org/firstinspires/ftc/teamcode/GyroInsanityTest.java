package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="GyroInsanityTest", group="Test")
@Disabled
public class GyroInsanityTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        /*Util.gyro.calibrate();

        while (Util.gyro.isCalibrating()) Thread.sleep(20);

        Thread.sleep(1000); // excessive sleep*/

        waitForStart();

        AutoUtil.gyroTurnRight(90, 0.3, Util.gyro);

        while (opModeIsActive()) {
            telemetry.addData("gyro", PID.heading(Util.gyro));
            telemetry.update();
            Thread.sleep(20);
        }
    }
}
