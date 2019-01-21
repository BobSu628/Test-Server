package client.packets;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class ClientPlayerUpdatePacket implements Serializable {

    public HashMap<UUID, UpdateParameters> players;

    public ClientPlayerUpdatePacket(){
        players = new HashMap<>();
    }

    /*
    public ClientPlayerUpdatePacket(HashMap<UUID, UpdateParameters> data){
        this.players = data;
    }
    */

    public void addPlayer(UUID uuid, UpdateParameters parameters){
        this.players.put(uuid, parameters);
    }

    public void removePlayer(UUID uuid){
        this.players.remove(uuid);
    }

    /*
    public ClientPlayerUpdatePacket exclone(UUID uuid){
        ClientPlayerUpdatePacket ret = new ClientPlayerUpdatePacket(this.players);
        ret.removePlayer(uuid);
        return ret;
    }
    */

}
