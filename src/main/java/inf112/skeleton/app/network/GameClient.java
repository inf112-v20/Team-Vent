package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class GameClient {
    private String hostName = "127.0.0.1";
    private int port = 10243;
    private SocketHints socketHints;
    private Socket clientSocket;

    public GameClient() {
        socketHints = new SocketHints();
        clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, hostName, port, socketHints);
    }
}
