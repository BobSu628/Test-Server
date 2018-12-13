package client.game;

import client.main.PlayerHandler;
import client.packets.UpdateParameters;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.UUID;

public class Game extends Canvas{

    public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;

    private PlayerHandler playerHandler;

    public Game() {
        playerHandler = new PlayerHandler();
        KeyInput keyInput = new KeyInput(playerHandler);
        this.addKeyListener(keyInput);

    }

    public void tick(){
        playerHandler.tick();
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        ////////////////////////

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        playerHandler.render(g);

        ////////////////////////

        g.dispose();
        bs.show();
    }

    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }

    public void initOtherPlayers(HashMap<UUID, UpdateParameters> players, HashMap<UUID, String> names){
        playerHandler.initOtherPlayers(players, names);
    }

}
