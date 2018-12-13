package client.packets;

import java.io.Serializable;

public class ServerPlayerUpdatePacket implements Serializable {

    public UpdateParameters parameters;

    public ServerPlayerUpdatePacket(UpdateParameters parameters){
        this.parameters = parameters;
    }

}
