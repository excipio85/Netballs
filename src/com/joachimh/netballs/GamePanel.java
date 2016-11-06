package com.joachimh.netballs;

import com.joachimh.netballs.net.GameClient;
import com.joachimh.netballs.net.GameServer;
import com.joachimh.netballs.net.Paket00Login;
import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Joachim on 06.11.2016.
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

    public static int WIDTH = 400;
    public static int HEIGHT = 400;

    private Thread thread;
    private boolean running;

    private BufferedImage image;
    private Graphics2D g;

    private int FPS = 30;
    private double averageFPS;

    public Player player;
    public static ArrayList<Player> players = new ArrayList<>();

    private GameClient socketClient;
    private GameServer socketServer;

    public GamePanel(){
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify(){
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }

    @Override
    public void run(){
        running = true;
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D)image.getGraphics();

        //players.add(new Player("Joachim"));

        if(JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0){
            socketServer = new GameServer(this);
            socketServer.start();
        }

        socketClient = new GameClient(this, "localhost");
        socketClient.start();
        try {
            socketClient.sendData("ping".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Paket00Login loginPaket = new Paket00Login(JOptionPane.showInputDialog(this, "Please enter Username: "));
        loginPaket.writeData(socketClient);
        if(player != null)
            players.add(player);

        long startTime;
        long urdTimeMillis;
        long waitTime;
        long totalTime = 0;

        int frameCount = 0;
        int maxFrameCount = 30;

        long targetTime = 1000 / FPS;

        while(running){

            startTime = System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            urdTimeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - urdTimeMillis;

            try{
                Thread.sleep(waitTime);
            }catch (Exception e){

            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == maxFrameCount){
                averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    private void gameUpdate(){
        players.forEach((p) -> p.update());
    }

    private void gameRender(){
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        players.forEach((p) -> p.draw(g));
    }

    private void gameDraw(){
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            player.setLeft(true);
        }
        if(keyCode == KeyEvent.VK_RIGHT){
            player.setRight(true);
        }
        if(keyCode == KeyEvent.VK_UP){
            player.setUp(true);
        }
        if(keyCode == KeyEvent.VK_DOWN){
            player.setDown(true);
        }
        if(keyCode == KeyEvent.VK_X){
            players.add(new Player("Sven"));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode == KeyEvent.VK_LEFT){
            player.setLeft(false);
        }
        if(keyCode == KeyEvent.VK_RIGHT){
            player.setRight(false);
        }
        if(keyCode == KeyEvent.VK_UP){
            player.setUp(false);
        }
        if(keyCode == KeyEvent.VK_DOWN){
            player.setDown(false);
        }

    }
}
