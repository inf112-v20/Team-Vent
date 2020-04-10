package inf112.skeleton.app.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import inf112.skeleton.app.model.cards.*;

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

    public int getIndex(){
        String response = sendAndReceiveMessage("GET_I");
        return Integer.parseInt(response);
    }
    public int getNumberOfPlayers(){
        String response = sendAndReceiveMessage("GET_N");
        return Integer.parseInt(response);
    }
    public String getGameStatus() {
        return sendAndReceiveMessage("GET_S");
    }

    // TODO: Implement priorities
    public void setProgrammingSlots(Card[] playerSlots){
        StringBuilder message = new StringBuilder();
        for (Card card : playerSlots){
            if (card == null){
                message.append("_");
            } else {
                message.append(card.toString());
            }
            message.append(',');
        }
        sendAndReceiveMessage("SET_H-" + message.toString());
    }

    public Card[][] getPlayerCards(){
        String response = sendAndReceiveMessage("GET_H");
        Card[][] allCardSlots = new Card[8][5];
        String[] handsStringArray = response.split("-");
        for (int i = 0; i < 8; i++){
            Card[] cardSlots = new Card[5];
            String[] cardsStringArray = handsStringArray[i].split(",");
            if (cardsStringArray[0].equals("*")){ // * indicates all empty programming slots
                continue;
            }
            for (int j = 0; j < 5; j++){
                cardSlots[j] = stringToCard(cardsStringArray[j]);
            }
            allCardSlots[i] = cardSlots;
        }
        return allCardSlots;
    }

    // TODO: This might be break when adding or removing new cards. Come up with a better idea
    public Card stringToCard(String cardString){
        switch (cardString){
            case "BACK UP":
                return Card.BACK_UP;
            case "MOVE ONE":
                return Card.MOVE_ONE;
            case "MOVE TWO":
                return Card.MOVE_TWO;
            case "MOVE THREE":
                return Card.MOVE_THREE;
            case "ROTATE LEFT":
                return Card.ROTATE_LEFT;
            case "ROTATE RIGHT":
                return Card.ROTATE_RIGHT;
            default:
                return null;
        }
    }

    public void setReady() {
        sendAndReceiveMessage("SET_R");
    }

    public Boolean getReady(){
        return Boolean.parseBoolean(sendAndReceiveMessage("GET_R"));
    }

    public void resetReady(){
        sendAndReceiveMessage("RESET_R");
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

    public String sendAndReceiveMessage(String message){
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
