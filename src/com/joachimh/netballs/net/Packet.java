package com.joachimh.netballs.net;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

public abstract class Packet {

    public enum PacketTypes{
        INVALID(-1),
        LOGIN(00),
        DISCONNECT(01);

        private int packedId;
        PacketTypes(int packedId){
            this.packedId = packedId;
        }

        public int getPackedId(){
            return packedId;
        }
    }

    public byte pakedId;
    public Packet(int paketId){
        this.pakedId = (byte)paketId;
    }

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);

    public String readData(byte[] data){
        String message = new String(data).trim();
        return message.substring(2);
    }

    public static PacketTypes lookupPaket(int packetId){
        for (PacketTypes p : PacketTypes.values()){
            if(p.getPackedId() == packetId){
                return p;
            }
        }
        return PacketTypes.INVALID;
    }

    public abstract byte[] getData();

    public static PacketTypes lookupPaket(String paketId){
        try{
            return lookupPaket(Integer.parseInt(paketId));
        }catch (Exception e){
            return PacketTypes.INVALID;
        }
    }

}
