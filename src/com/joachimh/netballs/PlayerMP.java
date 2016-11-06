package com.joachimh.netballs;

import java.net.InetAddress;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

    public PlayerMP(String name, InetAddress ipAddress, int port) {
        super(name);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void update(){

    }
}
