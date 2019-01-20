package client.packets;

import client.game.NetPlayer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class ClientPlayerAcceptedPacket implements Serializable {
    public HashMap<UUID, UpdateParameters> players;
    public HashMap<UUID, String> playerNames;
    //public long time;

    public ClientPlayerAcceptedPacket(HashMap<UUID, UpdateParameters> players, HashMap<UUID, String> playerNames){
        this.players = players;
        this.playerNames = playerNames;
        //this.time = time;
    }
}
