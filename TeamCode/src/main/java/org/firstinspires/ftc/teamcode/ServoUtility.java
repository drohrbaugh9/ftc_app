package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "ServoUtility", group = "Util")
@Disabled

public class ServoUtility extends LinearOpMode {

    Servo leftDoor, rightDoor, bumper;

    public ServoUtility() {}

    public void runOpMode() throws InterruptedException {
        leftDoor = Util.getServo(hardwareMap, "leftDoor");
        rightDoor = Util.getServo(hardwareMap, "rightDoor");
        bumper = Util.getServo(hardwareMap, "rightTrigger");

        leftDoor.setPosition(Util.LEFT_DOOR_CLOSED);
        rightDoor.setPosition(Util.RIGHT_DOOR_CLOSED);

        telemetry.addData("left door position", leftDoor.getPosition());
        telemetry.addData("right door position", rightDoor.getPosition());
        telemetry.addData("rightTrigger position", bumper.getPosition());
    }
}
