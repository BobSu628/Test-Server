package client.packets;

import java.io.Serializable;
import java.util.UUID;

public class ServerAddPlayerPacket implements Serializable {

    public UUID uuid;
    public String name;
    public UpdateParameters parameters;

    public ServerAddPlayerPacket(UUID uuid, String name, UpdateParameters parameters){
        this.uuid = uuid;
        this.name = name;
        this.parameters = parameters;
    }

}
