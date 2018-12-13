package client.packets;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class ClientPlayerUpdatePacket implements Serializable {

    public UUID uuid;
    public UpdateParameters parameters;

    public ClientPlayerUpdatePacket(UUID uuid, UpdateParameters parameters){
        this.uuid = uuid;
        this.parameters = parameters;
    }

}
