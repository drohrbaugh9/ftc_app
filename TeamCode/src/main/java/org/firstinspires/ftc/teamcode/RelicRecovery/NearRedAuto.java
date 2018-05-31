package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.Move;
import org.firstinspires.ftc.teamcode.Util;

/**
 * Created by elliot on 1/3/18.
 */

@Autonomous(name = "Near RED", group = "B Near auto")
@Disabled
public class NearRedAuto extends LinearOpMode {
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
        while (i < 60){
            Vuforia.VuMarkID(this);
            VuforiaGoToColumn.vuMark = Vuforia.VuMarkID(this);
            i+=1;
            Thread.sleep(10);
        }
        CameraDevice.getInstance().setFlashTorchMode(false) ;


        KnockOffJewel.KnockOffBlueJewel(this);
        DriveOffStone.NearRed(this);
        Move.moveUntilRedPIDDualProcesses(90, 0.3, this);

        /**
         * IF I CHANGE ANYTHING HERE CHANGE IN OTHER CORRESPONDING AUTOS ALSO
         */

        VuforiaGoToColumn.NearRed();

        Thread.sleep(20000);

        //will not? need
        //KnockOffJewel.servo.setPosition(0.19);
    }
}
