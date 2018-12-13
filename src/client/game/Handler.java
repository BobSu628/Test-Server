package client.game;

import java.awt.*;
import java.util.LinkedList;

public class Handler {
    NetPlayer player;
    LinkedList<GameObject> object = new LinkedList<GameObject>();

    public void tick() {
        player.tick();
        for(int i = 0; i < object.size(); i ++) {
            GameObject tmpObject = object.get(i);

            tmpObject.tick();
        }
    }

    public void render(Graphics g) {
        player.render(g);
        for(int i = 0; i < object.size(); i ++) {
            GameObject tmpObject = object.get(i);

            tmpObject.render(g);
        }
    }

    public void addObject(GameObject object) {
        this.object.add(object);
    }

    public void removeObject(GameObject object) {
        this.object.remove(object);
    }

}
