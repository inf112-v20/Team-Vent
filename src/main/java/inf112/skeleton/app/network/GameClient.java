package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.*;

public class GameClient {
    private String hostName;
    private int port = 10243;
    private BufferedReader input;
    private PrintWriter output;

    public GameClient(String hostName) {
        this.hostName = hostName;
        SocketHints socketHints = new SocketHints();
        Socket clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, hostName, port, socketHints);

        output = new PrintWriter(clientSocket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    public String sendAndReceiveMessage(String message){
        System.out.println(message);
        output.println(message);
        String response;
        try{
            response = input.readLine();
        } catch (IOException e) {
            response = null;
            e.printStackTrace();
        }
        return response;
    }
}
