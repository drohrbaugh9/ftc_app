package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.disnodeteam.dogecv.detectors.BallDetect;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by lulzbot on 2/19/18.
 */
@Autonomous(name = "CVBallDetect", group = "vision")
@Disabled
public class CVBallDetect extends LinearOpMode {
    //@Override

    public void runOpMode() throws InterruptedException {
        VuMarkDetector.init(this);
        BallDetect ballDetect = new BallDetect();
        CVAutoLib CVfuncs = new CVAutoLib();
        telemetry.addData("1 for red, 2 for blue, 0 for undecided:", ballDetect.findColor(CVfuncs.getImage(this)) );
        telemetry.update();
        Thread.sleep(100000000);
    }
}
