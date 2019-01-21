package client.packets;

import java.io.Serializable;

public class UpdateParameters implements Serializable {

    public float x, y;

    public UpdateParameters(float x, float y){
        this.x = x;
        this.y = y;
    }

}
