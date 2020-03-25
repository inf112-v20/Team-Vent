package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import org.lwjgl.Sys;

import java.util.Arrays;

public class GameHost {
    private String hostName;
    private int port = 10243;
    private SocketHints socketHints;
    private ServerSocketHints serverHints;
    private final ServerSocket serverSocket;
    private Boolean running = true;
    private Server[] servers;
    private String GameStatus;
    public String[] connectionList;

    public GameHost(String hostName) {
        this.hostName = hostName;
        SocketHints socketHints = new SocketHints();
        ServerSocketHints serverHints = new ServerSocketHints();
        // Accepting connections never time out. TODO: Probably a good idea to find another solution
        serverHints.acceptTimeout = 0;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, hostName, port, serverHints);
        connectionList = new String[8];
        servers = new Server[8];
        GameStatus = "LOBBY WAITING";
        Arrays.fill(connectionList, "");
        while(running){
            // Waits until a new connection is established
            Socket connectedSocket;
            synchronized (serverSocket) {
                try {
                    connectedSocket = serverSocket.accept(null);
                    System.out.print("Connection established: ");
                    System.out.println(connectedSocket.getRemoteAddress());
                } catch (com.badlogic.gdx.utils.GdxRuntimeException e) {
                    // For catching "Error accepting socket" exception when stopping the Game Host Server
                    running = false;
                    break;
                }
            }

            for(int i = 0; i < 8; i++){
                if(connectionList[i].equals("")){
                    Server server = new Server(this, connectedSocket, i);
                    Thread thread = new Thread(server);
                    servers[i] = server;
                    thread.setName(String.format("Server %d", i));
                    thread.start();
                    break;
                } else if (i == 7){
                    // TODO: Send close command to client and then close the connection
                    System.out.println("Refused connection. Lobby is full");
                }
            }
        }
        System.out.println("Shutting down GameHost Server");
    }

    public String getGameStatus() {
        return GameStatus;
    }

    public void stop(){
        for(Server s : servers){
            if(s != null){
                s.closeConnection();
            }
        }
        serverSocket.dispose();
        running = false;
    }
}