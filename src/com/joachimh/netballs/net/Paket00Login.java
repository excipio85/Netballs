package com.joachimh.netballs.net;

import java.io.IOException;

public class Paket00Login extends Packet{

    private String username;

    public Paket00Login(byte[] data) {
        super(00);
        this.username = readData(data);
    }

    public Paket00Login(String username) {
        super(00);
        this.username = username;
    }

    @Override
    public void writeData(GameClient client) {
        try {
            client.sendData(getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username).getBytes();
    }

    public String getUsername(){
        return username;
    }
}
