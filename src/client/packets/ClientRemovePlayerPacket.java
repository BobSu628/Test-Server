package client.packets;

import java.io.Serializable;
import java.util.UUID;

public class ClientRemovePlayerPacket implements Serializable {

    private static final long serialVersionUID = 1L;

    public UUID uuid;
    public String name;

    public ClientRemovePlayerPacket(UUID uuid, String name){
        this.uuid = uuid;
        this.name = name;

    }
}
