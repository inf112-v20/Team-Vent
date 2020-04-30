package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.util.Arrays;

public class GameHost {
    private final ServerSocket serverSocket;
    private Boolean running = true;
    private Server[] servers;
    private String gameStatus;
    private String[] playerHands;
    public String[] connectionList;
    public Boolean[] playersDone;

    public GameHost(String hostName) {
        ServerSocketHints serverHints = new ServerSocketHints();
        // Accepting connections never time out
        serverHints.acceptTimeout = 0;
        int port = 10243;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, hostName, port, serverHints);
        connectionList = new String[8];
        servers = new Server[8];
        gameStatus = "LOBBY WAITING";
        playerHands = new String[8];
        playersDone = new Boolean[8];
        Arrays.fill(connectionList, " ");
        Arrays.fill(playerHands, "*");
        Arrays.fill(playersDone, false);
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
                if(connectionList[i].equals(" ")){
                    Server server = new Server(this, connectedSocket, i);
                    Thread thread = new Thread(server);
                    servers[i] = server;
                    thread.setName(String.format("Server %d", i));
                    thread.start();
                    break;
                } else if (i == 7){
                    System.out.println("Refused connection. Lobby is full");
                }
            }
        }
        System.out.println("Shutting down GameHost Server");
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setPlayerHand(String hand, int playerIndex){
        playerHands[playerIndex] = hand;
    }

    public String[] getPlayerHands() {
        return playerHands;
    }

    public int numberPlayersDone(){
        int players = 0;
        for (Boolean done : playersDone){
            if (done) {
                players++;
            }
        }
        return players;
    }

    public void setDone(int playerIndex){
        playersDone[playerIndex] = true;
    }

    public void unreadyAll(){
        Arrays.fill(playersDone, false);
    }

    public int numberConnectedPlayers(){
        int n = 0;
        for (String player : connectionList){
            if (!" ".equals(player)){
                n++;
            }
        }
        return n;
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