package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

public class Auto extends LinearOpMode {

	DcMotor right, left, rightFront, leftFront;
	Servo bumper;
	GyroSensor gyro;
	ColorSensor color;

	final double SEC_TO_NSEC = Util.SEC_TO_NSEC;
	
	public Auto() {}
	
	public void runOpMode() throws InterruptedException {
		Util.init(this);
		AutoUtil.resetEncoders();
		right = Util.rightBack;
		left = Util.leftBack;
		rightFront = Util.rightFront;
		leftFront = Util.leftFront;
		bumper = Util.rightTrigger;
		/*color = Util.color;

		color.enableLed(false);

		//color.enableLed(true);*/

		waitForStart();

		Thread.sleep(500);

		//while (right.getCurrentPosition() < 2700);

		Util.setFrontPowers(-0.13f);
		//Util.setBackPowers(-0.1f * 1.3f);

		float time = System.nanoTime();

		Thread.sleep(3000);

		//while ((System.nanoTime() - time) / SEC_TO_NSEC < 3) {
			/*RobotLog.w("color_debug red " + color.red());
			RobotLog.w("color_debug blue " + color.blue());
			RobotLog.w("color_debug green " + color.green());*/
			//Thread.sleep(10);
		//}

		Util.setAllPowers(0);

		//telemetry.addData("battery: ", Util.getBatteryVoltage());
	}
}