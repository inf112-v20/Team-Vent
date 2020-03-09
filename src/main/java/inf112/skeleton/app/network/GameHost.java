package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.util.Arrays;

public class GameHost {
    private String hostName;
    private int port = 10243;
    private SocketHints socketHints;
    private ServerSocketHints serverHints;
    private final ServerSocket serverSocket;
    public String[] connectionList;

    public GameHost(String hostName) {
        this.hostName = hostName;
        SocketHints socketHints = new SocketHints();
        ServerSocketHints serverHints = new ServerSocketHints();
        // Accepting connections never time out. TODO: Probably a good idea to find another solution
        serverHints.acceptTimeout = 0;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, hostName, port, serverHints);
        connectionList = new String[8];
        Arrays.fill(connectionList, "");
        while(true){
            // Waits until a new connection is established
            Socket connectedSocket;
            synchronized (serverSocket){
                connectedSocket = serverSocket.accept(null);
            }
            System.out.print("Connection established: ");
            System.out.println(connectedSocket.getRemoteAddress());
            for(int i = 0; i < 8; i++){
                if(connectionList[i].equals("")){
                    Thread thread = new Thread(new Server(this, connectedSocket, i));
                    thread.start();
                    break;
                } else if (i == 7){
                    // TODO: Send close command to client and then close the connection
                    System.out.println("Refused connection. Lobby is full");
                }
            }
        }
    }
}