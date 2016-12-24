package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class DriveWithIntake extends OpMode {

    public void init() {

    }

    public void loop() {

    }
}

/*
private void handleIntake() {
        if ((gamepad1.right_bumper || gamepad2.right_bumper) && !intakeChanged) {
            /* if the intake is off, intake
             * if the intake is intaking, outtake
             * if the intake is outtaking, intake
             *//*
switch (intakeStatus) {
        case OFF:
        intake();
        break;
        case INTAKE:
        outtake();
        break;
        case OUTTAKE:
        intake();
        break;
        }
        intakeChanged = true;
        }
        if ((gamepad1.left_bumper || gamepad2.left_bumper) && !intakeChanged) {
            /*
             * if the intake is off, outtake
             * if the intake is intaking or outtaking, turn if off
             *//*
        switch (intakeStatus) {
        case OFF:
        outtake();
        break;
        case INTAKE:
        case OUTTAKE:
        intakeOff();
        break;
        }
        intakeChanged = true;
        }
        // wait until the user releases all intake-related buttons before allowing the user to change the intake again
        else if (!gamepad1.right_bumper && !gamepad1.left_bumper && !gamepad2.right_bumper && !gamepad2.left_bumper)
        intakeChanged = false;
        }
 */
