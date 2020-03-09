package inf112.skeleton.app.network;

import com.badlogic.gdx.net.Socket;

import java.io.*;

public class Server implements Runnable {
    private Socket connectedSocket;
    private GameHost gameHost;
    private int index;

    public Server(GameHost gameHost, Socket connectedSocket, int index) {
        this.connectedSocket = connectedSocket;
        this.gameHost = gameHost;
        this.index = index;
        gameHost.connectionList[index] = connectedSocket.getRemoteAddress();
    }

    private String[] parseMessage(String message){
        return message.split("-");
    }

    private String PING(){
        System.out.println("Recieve ping from " + connectedSocket.getRemoteAddress());
        return "pong";
    }

    private String REF_P(){
        StringBuilder response = new StringBuilder();
        for(String connection : gameHost.connectionList){
            response.append(connection);
            response.append("-");
        }
        return response.toString();
    }

    @Override
    public void run() {
        InputStream inputStream = connectedSocket.getInputStream();
        BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));
        DataOutputStream outputStream = new DataOutputStream(connectedSocket.getOutputStream());
        String message;
        while (true) {
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
                        response = ""; //TODO
                        break;
                    case "STOP":
                        response = "STOP";
                        gameHost.connectionList[index] = "";
                        connectedSocket.dispose();
                        Thread.currentThread().interrupt();
                        break;
                    default:
                        response = "ERR-Invalid command";
                        break;
                }
                outputStream.writeBytes(response);
                outputStream.flush();
            } catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
    }
}
