package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.util.ArrayList;

public class GameServer {
    private String hostName = "127.0.0.1";
    private int port = 10243;
    private SocketHints socketHints;
    private ServerSocketHints serverHints;
    private ServerSocket serverSocket;
    public ArrayList<String> connectionList;

    public GameServer() {
        SocketHints socketHints = new SocketHints();
        ServerSocketHints serverHints = new ServerSocketHints();
        // Accepting connections never time out. TODO: Probably a good idea to find another solution
        serverHints.acceptTimeout = 0;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, hostName, port, serverHints);
        connectionList = new ArrayList<>();
        Thread connectionListenerThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    Socket socket = serverSocket.accept(null);
                    String address = socket.getRemoteAddress();
                    connectionList.add(address);
                    System.out.println(address);
                }
            }
        });
        connectionListenerThread.start();

    }
}
