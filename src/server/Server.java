package server;

import client.packets.UpdateParameters;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class Server implements Runnable{

    private static int PORT = 9001;
    //private int port;
    private ServerSocket serverSocket;
    private boolean running = false;

    static HashMap<UUID, String> playerNames = new HashMap<>();
    static HashMap<UUID, UpdateParameters> players = new HashMap<>();
    static HashMap<UUID, ObjectOutputStream> outputStreams = new HashMap<>();

    public Server(int port){
        //this.port = port;

        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void start(){
        new Thread(this).start();
        running = true;
    }

    @Override
    public void run() {
        while (running){

            try{
                Socket socket = serverSocket.accept(); // attempt to accept a client
                initSocket(socket);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        shutdown();
    }

    private void initSocket(Socket socket){
        Connection connection = new Connection(socket); // establish client connection
        new Thread(connection).start();
    }

    public void shutdown(){
        running = false;

        try{
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int portNumber = PORT;
        new Server(portNumber).start();
    }

}
