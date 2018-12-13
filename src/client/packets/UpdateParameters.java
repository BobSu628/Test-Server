package client.packets;

import client.game.NetPlayer;

import java.io.Serializable;

public class UpdateParameters implements Serializable {

    public float x, y, velX, velY;

    public UpdateParameters(float x, float y, float velX, float velY){
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
    }

}
