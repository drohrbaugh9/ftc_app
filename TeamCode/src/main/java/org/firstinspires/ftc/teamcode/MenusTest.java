package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Deque;

@TeleOp(name="MenusTest", group="Test")
//@Disabled

public class MenusTest extends OpMode {

    Deque<String> COLORS;
    Deque<String> PARTICLES;
    Deque<String> PARKS;
    int DELAYS = 0; final int DELAY_MAX = 20;

    String[] menuItems = {"COLOR", "balls", " park", "delay"};
    String S = ": ";

    int activeItem = 0, ACTIVE_ITEM_MAX = 3;

    public void init() {
        initializeDeques();
        displayMenu();
    }

    public void loop() {
        if (gamepad1.dpad_down) {
            menuItems[activeItem] = menuItems[activeItem].toLowerCase();
            activeItem++;
            if (activeItem > ACTIVE_ITEM_MAX) activeItem = 0;
            if (activeItem < 0) activeItem = ACTIVE_ITEM_MAX;
            menuItems[activeItem] = menuItems[activeItem].toUpperCase();
        } else if (gamepad1.dpad_up) {
            menuItems[activeItem] = menuItems[activeItem].toLowerCase();
            activeItem--;
            if (activeItem < 0) activeItem = ACTIVE_ITEM_MAX;
            if (activeItem > ACTIVE_ITEM_MAX) activeItem = 0;
            menuItems[activeItem] = menuItems[activeItem].toUpperCase();
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
                PARKS.addLast(PARTICLES.pollFirst());
            }
        } else if (gamepad1.dpad_left) {
            if (activeItem == 3) {
                DELAYS--;
                if (DELAYS < 0) DELAYS = DELAY_MAX;
                if (DELAYS > DELAY_MAX) DELAYS = 0;
            }
            else if (activeItem == 0) COLORS.addFirst(COLORS.pollLast());
            else if (activeItem == 1) PARTICLES.addFirst(PARTICLES.pollLast());
            else if (activeItem == 2) PARKS.addFirst(PARKS.pollLast());
        }

        displayMenu();
    }

    private void displayMenu() {
        telemetry.addData("color", menuItems[0] + S + COLORS.peekFirst());
        telemetry.addData("balls", menuItems[1] + S + PARTICLES.peekFirst());
        telemetry.addData("park", menuItems[2] + S + PARKS.peekFirst());
        telemetry.addData("delay", menuItems[3] + S + DELAYS);
        telemetry.update();
    }

    private void initializeDeques() {
        COLORS.add("RED"); COLORS.add("BLUE");

        PARTICLES.add("1"); PARTICLES.add("2"); PARTICLES.add("3");

        PARKS.add("CORNER"); PARKS.add("CENTER");
    }
}