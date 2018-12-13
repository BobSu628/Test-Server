package client.game;

import client.main.PlayerHandler;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    PlayerHandler playerHandler;

    public KeyInput(PlayerHandler playerHandler){
        this.playerHandler = playerHandler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W){
            playerHandler.myPlayer.setVelY(-3);
        }
        if(key == KeyEvent.VK_S){
            playerHandler.myPlayer.setVelY(3);
        }
        if(key == KeyEvent.VK_A){
            playerHandler.myPlayer.setVelX(-3);
        }
        if(key == KeyEvent.VK_D){
            playerHandler.myPlayer.setVelX(3);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_W){
            playerHandler.myPlayer.setVelY(0);
        }
        if(key == KeyEvent.VK_S){
            playerHandler.myPlayer.setVelY(0);
        }
        if(key == KeyEvent.VK_A){
            playerHandler.myPlayer.setVelX(0);
        }
        if(key == KeyEvent.VK_D){
            playerHandler.myPlayer.setVelX(0);
        }

    }
}
