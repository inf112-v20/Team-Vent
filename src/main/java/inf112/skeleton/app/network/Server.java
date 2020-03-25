package inf112.skeleton.app.network;

import com.badlogic.gdx.net.Socket;

import java.io.*;
import java.net.SocketException;

public class Server implements Runnable {
    private Socket connectedSocket;
    private GameHost gameHost;
    private int index;
    private boolean connected = true;

    public Server(GameHost gameHost, Socket connectedSocket, int index) {
        this.connectedSocket = connectedSocket;
        this.gameHost = gameHost;
        this.index = index;
        String clientAddress = connectedSocket.getRemoteAddress();
        gameHost.connectionList[index] = clientAddress;
    }

    private String[] parseMessage(String message){
        return message.split("-");
    }

    private String ping(){
        return "PONG";
    }

    private String getConnectedPlayers(){
        StringBuilder response = new StringBuilder();
        for(String connection : gameHost.connectionList){
            response.append(connection);
            response.append(" -");
        }
        return response.toString();
    }

    private String getGameStatus(){
        return gameHost.getGameStatus();
    }

    private String setGameStatus(String gameStatus) {
        gameHost.setGameStatus(gameStatus);
        return "";
    }

     public void closeConnection(){
        gameHost.connectionList[index] = "";
        connectedSocket.dispose();
        Thread.currentThread().interrupt();
        connected = false;
    }

    private void stopHost(){
        gameHost.stop();
        closeConnection();
    }

    @Override
    public void run()  {
        InputStream inputStream = connectedSocket.getInputStream();
        BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));
        DataOutputStream outputStream = new DataOutputStream(connectedSocket.getOutputStream());
        String message;
        while (connected) {
            try {
                message = bufferedInputStream.readLine();
                String[] commands = parseMessage(message);
                String command = commands[0];
                String response;
                switch (command){
                    case "PING":
                        response = ping();
                        break;
                    case "SET_H":
                        response = ""; //TODO
                        break;
                    case "GET_H":
                        response = ""; //TODO
                        break;
                    case "REF_P":
                        response = getConnectedPlayers();
                        break;
                    case "GET_S":
                        response = getGameStatus();
                        break;
                    case "SET_S":
                        response = setGameStatus(commands[1]);
                        break;
                    case "STOP_C":
                        response = "CLOSING CONNECTION";
                        closeConnection();
                        break;
                    case "STOP_H":
                        response = "STOPPING HOST";
                        stopHost();
                        break;
                    default:
                        response = "ERR-Invalid command";
                        break;
                }
                outputStream.writeBytes(response + "\r");
                outputStream.flush();
            } catch (SocketException e){
                closeConnection();
            } catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
    }
}
