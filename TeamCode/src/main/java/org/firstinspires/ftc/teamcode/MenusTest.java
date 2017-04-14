package org.firstinspires.ftc.teamcode;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

@TeleOp(name="MenusTest", group="Test")
//@Disabled

public class MenusTest extends LinearOpMode {

    Deque<String> COLORS;
    Deque<String> PARTICLES;
    Deque<String> PARKS;
    int DELAYS = 0; final int DELAY_MAX = 20;

    String[] menuItems = {"COLOR", "balls", " park", "delay"};
    String S = ": ";

    int activeItem = 0, ACTIVE_ITEM_MAX = 3;
    boolean gamepadPressed = false;

    public void runOpMode() throws InterruptedException {
        initializeDeques();

        waitForStart();

        displayMenu();

        while (opModeIsActive()) {
            if (gamepad1.dpad_down) {
                menuItems[activeItem] = menuItems[activeItem].toLowerCase();
                activeItem++;
                if (activeItem > ACTIVE_ITEM_MAX) activeItem = 0;
                else if (activeItem < 0) activeItem = ACTIVE_ITEM_MAX;
                menuItems[activeItem] = menuItems[activeItem].toUpperCase();
                gamepadPressed = true;
            } else if (gamepad1.dpad_up) {
                menuItems[activeItem] = menuItems[activeItem].toLowerCase();
                activeItem--;
                if (activeItem < 0) activeItem = ACTIVE_ITEM_MAX;
                else if (activeItem > ACTIVE_ITEM_MAX) activeItem = 0;
                menuItems[activeItem] = menuItems[activeItem].toUpperCase();
                gamepadPressed = true;
            } else if (gamepad1.dpad_right) {
                if (activeItem == 3) {
                    DELAYS++;
                    if (DELAYS > DELAY_MAX) DELAYS = 0;
                    if (DELAYS < 0) DELAYS = DELAY_MAX;
                } else if (activeItem == 0) {
                    COLORS.addLast(COLORS.pollFirst());
                } else if (activeItem == 1) {
                    PARTICLES.addLast(PARTICLES.pollFirst());
                } else if (activeItem == 2) {
                    PARKS.addLast(PARKS.pollFirst());
                }
                gamepadPressed = true;
            } else if (gamepad1.dpad_left) {
                if (activeItem == 3) {
                    DELAYS--;
                    if (DELAYS < 0) DELAYS = DELAY_MAX;
                    else if (DELAYS > DELAY_MAX) DELAYS = 0;
                } else if (activeItem == 0) COLORS.addFirst(COLORS.pollLast());
                else if (activeItem == 1) PARTICLES.addFirst(PARTICLES.pollLast());
                else if (activeItem == 2) PARKS.addFirst(PARKS.pollLast());
                gamepadPressed = true;
            } else {
                gamepadPressed = false;
            }

            displayMenu();

            Thread.sleep(200);
        }
    }

    private void displayMenu() {
        telemetry.addData("1", menuItems[0] + S + COLORS.peekFirst());
        telemetry.addData("2", menuItems[1] + S + PARTICLES.peekFirst());
        telemetry.addData("3", menuItems[2] + S + PARKS.peekFirst());
        telemetry.addData("4", menuItems[3] + S + DELAYS);
        telemetry.update();
    }

    private void initializeDeques() {
        COLORS = new LinkedList<>();
        PARTICLES = new LinkedList<>();
        PARKS = new LinkedList<>();

        COLORS.add("RED"); COLORS.add("BLUE");

        PARTICLES.add("1"); PARTICLES.add("2"); PARTICLES.add("3");

        PARKS.add("CORNER"); PARKS.add("CENTER");
    }
}