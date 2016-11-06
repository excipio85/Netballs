package com.joachimh.netballs;

import javax.swing.*;

/**
 * Created by Joachim on 06.11.2016.
 */
public class Game {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Netballs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GamePanel());
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
