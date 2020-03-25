package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.*;

public class GameClient {
    private BufferedReader input;
    private PrintWriter output;

    public GameClient(String hostName) {
        SocketHints socketHints = new SocketHints();
        int port = 10243;
        Socket clientSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, hostName, port, socketHints);

        output = new PrintWriter(clientSocket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String[] getPlayersInLobby(){
        String response = sendAndReceiveMessage("REF_P");
        return response.split("-");
    }

    public String getGameStatus() {
        String reponse = sendAndReceiveMessage("GET_S");
        return reponse;
    }

    public void setGameStatus(String status) {
        sendAndReceiveMessage(String.format("SET_S-%s", status));
    }

    public void closeConnection(){
        sendAndReceiveMessage("STOP_C");
    }

    public void stopHost(){
        sendAndReceiveMessage("STOP_H");
    }

    private String sendAndReceiveMessage(String message){
        output.println(message);
        String response;
        try{
            response = input.readLine();
        } catch (java.net.SocketException e){
            response = "CLOSE";
        } catch (IOException e) {
            response = "";
            e.printStackTrace();
        }
        return response;
    }
}
