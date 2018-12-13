package client.game;

import client.main.PlayerHandler;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable {

    protected float x, y;
    protected int width, height;
    protected ID id;
    protected PlayerHandler playerHandler;

    public GameObject(float x, float y, ID id, PlayerHandler playerHandler){
        this.x = x;
        this.y = y;
        this.id = id;
        this.playerHandler = playerHandler;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    //public abstract Rectangle getBounds();

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
