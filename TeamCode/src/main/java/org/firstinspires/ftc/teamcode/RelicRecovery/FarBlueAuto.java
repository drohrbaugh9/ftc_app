package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/12/18.
 */

@Autonomous(name = "Far BLUE", group = "Far auto")
@Disabled
public class FarBlueAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Util.init(this); //imu initialized in here
        KnockOffJewel.init(this);
        Vuforia.init(this);
        RelicRecoveryVuMark vuMark = null;
        Util.resetEncoders();


        telemetry.addLine("READY FOR START");
        telemetry.update();

        waitForStart();

        int i = 0;
        while (i < 100){
            Vuforia.VuMarkID(this);
            vuMark = Vuforia.VuMarkID(this);
            i+=1;
            Thread.sleep(10);
        }
        CameraDevice.getInstance().setFlashTorchMode(false) ;


        KnockOffJewel.KnockOffRedJewel(this);
        DriveOffStone.FarBlue(this);
        Move.moveUntilBluePID(0, 0.2, this);

        Move.rotateClockwise(85); //rotate to face cryptobox
       // Move.strafeAngleWithoutPID(180, 0.2, 25);

        VuforiaGoToColumn.FarBlue();

        PutGlyphInBox.putInBox();

        telemetry.addData("IMU", Util.imu.getAngularOrientation().firstAngle);
        telemetry.update();

        Thread.sleep(12000);

        //will not? need
        //KnockOffJewel.servo.setPosition(0.19);
    }
}
