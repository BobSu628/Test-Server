package client.main;

import client.game.ID;
import client.game.NetPlayer;
import client.packets.ClientAddPlayerPacket;
import client.packets.ClientPlayerUpdatePacket;
import client.packets.ClientRemovePlayerPacket;
import client.packets.UpdateParameters;

import java.util.*;

public class EventListener {

    public PlayerHandler playerHandler;

    public EventListener(PlayerHandler playerHandler){
        this.playerHandler = playerHandler;
    }

    public void received(Object p){
        // another player joined
        if(p instanceof ClientAddPlayerPacket){
            ClientAddPlayerPacket packet = (ClientAddPlayerPacket) p;
            playerHandler.addPlayer(new NetPlayer(0, 0, packet.uuid, ID.Player, packet.name));
            System.out.println(packet.name + " has joined the game");
        }
        // another player left
        else if(p instanceof ClientRemovePlayerPacket){
            ClientRemovePlayerPacket packet = (ClientRemovePlayerPacket)p;
            System.out.println(packet.name + " has left the game");
            playerHandler.removePlayer(packet.uuid);
        }
        //received player updates
        else if(p instanceof ClientPlayerUpdatePacket){
            ClientPlayerUpdatePacket packet = (ClientPlayerUpdatePacket) p;

            if(!playerHandler.myPlayer.getUUID().equals(packet.uuid)){
                try{
                    playerHandler.updatePlayer(packet.uuid, packet.parameters);
                    //System.out.println(parameters.x+", "+parameters.y);
                    //System.out.println("ok");
                }catch (NullPointerException e){
                    //e.printStackTrace();
                }
            }

        }

    }

}
