package com.joachimh.netballs.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.joachimh.netballs.GamePanel;
import com.joachimh.netballs.PlayerMP;

public class GameServer extends Thread {

    private DatagramSocket socket;
    private GamePanel gamePanel;
    private List<PlayerMP> connectedPlayers = new ArrayList<>();

    public GameServer(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        try {
            this.socket = new DatagramSocket(1331);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            System.out.println("hi");
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            if(message.trim().equalsIgnoreCase("ping")){
                System.out.println("Client > " + message);
                try {
                    sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress ipAddress, int port){
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPaket(message.substring(0, 2));
        switch (type){
            default:
            case INVALID:
                break;
            case LOGIN:
                Paket00Login paket = new Paket00Login(data);
                System.out.println(ipAddress.getHostAddress() + ":" + port + " " + paket.getUsername() + "has connected");
                PlayerMP player = null;
                if(ipAddress.getHostAddress().equalsIgnoreCase("127.0.0.1")){
                    //
                }
                player = new PlayerMP(paket.getUsername(), ipAddress, port);
                if(player != null){
                    this.connectedPlayers.add(player);
                    gamePanel.player = player;
                }
                gamePanel.players.add(player);
                break;
            case DISCONNECT:
                break;
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        socket.send(packet);
    }

    public void sendDataToAllClients(byte[] data) {
        for(PlayerMP p : connectedPlayers){
            try {
                sendData(data, p.ipAddress, p.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
