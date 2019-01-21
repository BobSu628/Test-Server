package client.main;

import client.game.Game;
import client.game.ID;
import client.game.NetPlayer;
import client.game.Window;
import client.packets.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.UUID;

public class Client implements Runnable{

    private String host;
    private int port;
    private UUID uuid;
    private String playerName;
    private NetPlayer player;
    //private long time;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;
    private EventListener listener;
    private Game game;
    private Window window;

    public Client(int port){
        this.port = port;

        game = new Game();
        window = new Window(Game.WIDTH, Game.HEIGHT, "Test", game);

    }

    //connect to the client
    public void connect(){
        //boolean start = false;
        try{
            //get host
            host = getServerAddress();
            socket = new Socket(host, port);
            //socket.setSoTimeout(100);

            //io setup
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            listener = new EventListener(game.getPlayerHandler());

            //set player name
            while(true) {
                try {
                    Object receivedPacket = in.readObject();
                    if(receivedPacket instanceof ClientSubmitNamePacket) {
                        submitPlayer();
                    }else if(receivedPacket instanceof ClientPlayerAcceptedPacket){
                        ClientPlayerAcceptedPacket packet = (ClientPlayerAcceptedPacket) receivedPacket;
                        game.initOtherPlayers(packet.players, packet.playerNames);
                        //this.time = packet.time;
                        break;
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }catch (ClassNotFoundException e){ }

            }
            //start running
            new Thread(this).start();
        }catch (ConnectException e){
            System.out.println("Unable to connect to the server");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void submitPlayer(){
        playerName = getName();
        uuid = UUID.randomUUID();
        player = new NetPlayer(0, 0, uuid, ID.Player, playerName);
        game.getPlayerHandler().myPlayer = player;

        sendObject(new ServerAddPlayerPacket(uuid, playerName, extractParameters(player)));
    }

    //close the connection
    public void close(){
        try{
            running = false;

            //tell the server that we disconnected
            ServerRemovePlayerPacket packet = new ServerRemovePlayerPacket();
            sendObject(packet);

            in.close();
            out.close();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //send data to the server
    public void sendObject(Object packet){
        try{
            out.writeObject(packet);
            //out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        running = true;

        while (running){
            //int frames = 0;
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                //receive
                try{
                    Object data = in.readObject();
                    listener.received(data);
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }catch (SocketException e){
                    close();
                }catch (IOException e){
                    e.printStackTrace();
                }

                //send
                sendObject(new ServerPlayerUpdatePacket(extractParameters(player)));
                tick();
                delta--;
            }

            if (running) {
                render();
                //System.out.println("ok");
            }
        }

    }

    private void tick(){
        this.game.tick();
    }

    private void render(){
        this.game.render();
    }

    private String getServerAddress() {
        return JOptionPane.showInputDialog(
                window.getFrame(),
                "Enter IP Address of the Server:",
                "Welcome to the game",
                JOptionPane.QUESTION_MESSAGE);
    }


    private String getName() {
        return JOptionPane.showInputDialog(
                window.getFrame(),
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    public static UpdateParameters extractParameters(NetPlayer player){
        return new UpdateParameters(player.getX(), player.getY());
    }

    public static void main(String[] args) {
        Client client = new Client(9001);
        client.connect();

    }

}
