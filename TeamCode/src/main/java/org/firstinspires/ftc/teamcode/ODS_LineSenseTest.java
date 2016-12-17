package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@Autonomous(name="ODS_LineSenseTest", group="test")
@Disabled
public class ODS_LineSenseTest extends LinearOpMode {

    DcMotor rightBack;

    OpticalDistanceSensor ods;

    public void runOpMode() throws InterruptedException {
        Util.init(this);

        this.rightBack = Util.rightBack;

        Util.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        Util.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        this.ods = Util.ods;

        waitForStart();

        int start = this.rightBack.getCurrentPosition();

        Util.setAllPowers(0.5);

        while (this.rightBack.getCurrentPosition() - start < 3000) {
            Util.log("ODS_TEST " + ods.getLightDetected());

            Thread.sleep(1);
        }

        Util.setAllPowers(0);
    }
}
