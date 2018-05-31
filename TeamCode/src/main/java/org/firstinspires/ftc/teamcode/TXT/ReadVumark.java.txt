package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by elliot on 2/9/18.
 */

public class ReadVumark {

    private ReadVumark() throws Exception {
        throw new Exception();
    }

    public static RelicRecoveryVuMark vuMark = null;

    public void ReadVumark(LinearOpMode opMode) throws InterruptedException {
        Vuforia.init(opMode);
        //could extend loop time if needed
        int i = 0;
        while (i < 60){
            Vuforia.VuMarkID(opMode);
            vuMark = Vuforia.VuMarkID(opMode);
            i+=1;
            Thread.sleep(10);
        }
    }
}
