package inf112.skeleton.app.network;

import com.badlogic.gdx.net.Socket;

import java.io.*;
import java.net.SocketException;

public class Server implements Runnable {
    private Socket connectedSocket;
    private GameHost gameHost;
    private int index;
    private String clientAddress;
    private boolean connected = true;

    public Server(GameHost gameHost, Socket connectedSocket, int index) {
        this.connectedSocket = connectedSocket;
        this.gameHost = gameHost;
        this.index = index;
        clientAddress = connectedSocket.getRemoteAddress();
        gameHost.connectionList[index] = clientAddress;
    }

    private String[] parseMessage(String message){
        return message.split("-");
    }

    private String PING(){
        return "PONG";
    }

    private String REF_P(){
        StringBuilder response = new StringBuilder();
        for(String connection : gameHost.connectionList){
            response.append(connection);
            response.append(" -");
        }
        return response.toString();
    }

     public void closeConnection(){
        gameHost.connectionList[index] = "";
        System.out.println("Closed connection with: " + clientAddress);
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
                        response = PING();
                        break;
                    case "SET_H":
                        response = ""; //TODO
                        break;
                    case "GET_H":
                        response = ""; //TODO
                        break;
                    case "REF_P":
                        response = REF_P();
                        break;
                    case "STOP_C":
                        response = "STOP_C";
                        closeConnection();
                        break;
                    case "STOP_H":
                        response = "STOP_H";
                        stopHost();
                        break;
                    default:
                        response = "ERR-Invalid command";
                        break;
                }
                System.out.println("Got " + command + " command from " + clientAddress);
                System.out.println("Response: " + response);
                outputStream.writeBytes(response + "\r");
                outputStream.flush();
            } catch (IOException e){
                if (e instanceof SocketException){
                    closeConnection();
                } else {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
