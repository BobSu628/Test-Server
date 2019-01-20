package server;

import client.packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class Connection implements Runnable {

    private UUID uuid = null;
    private String playerName;
    private UpdateParameters parameters;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    //private UUID id;

    public Connection(Socket socket){
        this.socket = socket;

        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            do{
                sendObject(out, new ClientSubmitNamePacket());
                try {
                    Object packet = in.readObject();
                    if(packet instanceof ServerAddPlayerPacket){
                        uuid = ((ServerAddPlayerPacket) packet).uuid;
                        parameters = ((ServerAddPlayerPacket) packet).parameters;
                        playerName = ((ServerAddPlayerPacket) packet).name;
                    }

                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }

                if(uuid != null && !Server.players.containsKey(uuid)) {
                    Server.playerNames.put(uuid, playerName);
                    Server.players.put(uuid, parameters);
                    Server.outputStreams.put(uuid, this.out);
                }

            }while(uuid == null || !Server.players.containsKey(uuid));

            sendObject(out, new ClientPlayerAcceptedPacket(Server.players, Server.playerNames));
            broadcast(new ClientAddPlayerPacket(uuid, playerName));


            long lastTime = System.nanoTime();
            double amountOfTicks = 60.0;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;
            long timer = System.currentTimeMillis();

            while(socket.isConnected()){
                //int frames = 0;
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                //
                while (delta >= 1) {
                    //send
                    //sendObject(out, new ClientPlayerUpdatePacket(Server.players));
                    for(Map.Entry<UUID, UpdateParameters> entry: Server.players.entrySet()){
                        sendObject(out, new ClientPlayerUpdatePacket(entry.getKey(), entry.getValue()));
                        System.out.print(entry.getValue().x+", "+entry.getValue().y + "  ");
                    }
                    System.out.println();
                    //System.out.println(Server.players.get(uuid).x+", "+Server.players.get(uuid).y);

                    //receive
                    try{
                        Object data = in.readObject();
                        if(data instanceof ServerRemovePlayerPacket){
                            break;
                        }else if(data instanceof ServerPlayerUpdatePacket){

                            ServerPlayerUpdatePacket playerPacket = ((ServerPlayerUpdatePacket) data);
                            parameters = playerPacket.parameters;

                            Server.players.put(uuid, parameters);
                            //System.out.println(Server.players.get(uuid).x+", "+Server.players.get(uuid).y);
                        }

                    }catch (ClassNotFoundException e){
                        e.printStackTrace();
                    }

                    delta--;
                }
                //frames ++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    //System.out.println("FPS: " + frames);
                    //frames = 0;
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

    private void close(){

        if(uuid != null) {
            Server.players.remove(uuid);
            Server.playerNames.remove(uuid);
            Server.outputStreams.remove(uuid);
        }

        broadcast(new ClientRemovePlayerPacket(uuid, playerName));

        try{
            in.close();
            out.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendObject(ObjectOutputStream out, Object packet){
        try{
            out.writeObject(packet);
            out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void broadcast(Object packet){
        for(Map.Entry<UUID, ObjectOutputStream> entry: Server.outputStreams.entrySet()){

            if(!uuid.equals(entry.getKey())) {
                //System.out.println("sent to" + output);
                sendObject(entry.getValue(), packet);
            }
        }
    }

}
