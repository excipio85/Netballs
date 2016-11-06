package com.joachimh.netballs.net;

import com.joachimh.netballs.Game;
import com.joachimh.netballs.GamePanel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GameClient extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private GamePanel gamePanel;

    public GameClient(GamePanel gamePanel, String ipAddress){
        this.gamePanel = gamePanel;
        try {
            this.ipAddress = InetAddress.getByName(ipAddress);
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server > " + new String(packet.getData()));

        }
    }

    public void sendData(byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        socket.send(packet);
    }
}
