package client.game;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public class NetPlayer implements Serializable {

    private UUID uuid;
    private float x, y;
    private int width, height;
    private ID id;
    private String name;
    private float velX, velY;

    public NetPlayer(float x, float y, UUID uuid, ID id, String name){
        //super(x, y, id, playerHandler);
        this.x = x; this.y = y; this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.velX = 0; this.velY = 0;
        this.width = 32; this.height = 32;
    }

    public void tick(){
        x += velX;
        y += velY;
    }

    public void render(Graphics g){
        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y, width, height);
        g.setColor(Color.white);
        g.drawString(name, (int)x+5, (int)y+10);

    }


    public ID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
