package server;

import client.packets.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class Sender implements Runnable{
    public static HashMap<UUID, PlayerData> players; //store sever-controlled data of all players
    private static Queue<PlayerData> addedPlayers; //store data of all players to be added
    private static Queue<PlayerData> removedPlayers; //store data of all players to be removed

    public Sender(){
        players = new HashMap<>();
        addedPlayers = new LinkedList<>();
        removedPlayers = new LinkedList<>();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (Server.running){
            //int frames = 0;
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                sync();
                delta--;
            }
        }
    }

    private void sync(){
        //add player from buffer
        while(!addedPlayers.isEmpty()){
            PlayerData player = addedPlayers.poll();
            players.put(player.getUuid(), player);
            try {

                player.send(new ClientPlayerAcceptedPacket(extractParameters(player.getUuid()), extractNames(player.getUuid())));
                broadcast(player.getUuid(), new ClientAddPlayerPacket(player.getUuid(), player.getName()));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        //remove player from buffer
        while(!removedPlayers.isEmpty()){
            PlayerData player = removedPlayers.poll();
            players.remove(player.getUuid());
            broadcast(player.getUuid(), new ClientRemovePlayerPacket(player.getUuid(), player.getName()));
        }

        ClientPlayerUpdatePacket output = new ClientPlayerUpdatePacket();
        //pack update data of all players into output
        for(UUID id: players.keySet()){
            PlayerData player = players.get(id);
            UpdateParameters parameters = player.extractParameters();
            output.addPlayer(id, parameters);

        }

        //send output excluding current player's data to each player

        for(UUID id: players.keySet()){
            PlayerData player = players.get(id);
            try {
                player.send(output);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void addPlayer(PlayerData player){
        addedPlayers.add(player);
    }

    public static void removePlayer(PlayerData player){
        removedPlayers.add(player);
    }

    public static HashMap<UUID, UpdateParameters> extractParameters(UUID uuid){
        HashMap<UUID, UpdateParameters> ret = new HashMap<>();
        for(UUID id: players.keySet()){
            if(!id.equals(uuid)){
                ret.put(id, players.get(id).extractParameters());
            }
        }
        return ret;
    }

    public static HashMap<UUID, String> extractNames(UUID uuid){
        HashMap<UUID, String> ret = new HashMap<>();
        for(UUID id: players.keySet()){
            if (!id.equals(uuid)) {
                ret.put(id, players.get(id).getName());
            }
        }
        return ret;
    }

    public static void broadcast(UUID uuid, Object packet){
        for(UUID id: players.keySet()){

            if(!uuid.equals(id)) {
                try{
                    players.get(id).send(packet);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
