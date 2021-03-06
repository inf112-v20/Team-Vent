package inf112.skeleton.app.controller;

import inf112.skeleton.app.network.GameClient;

import java.util.Timer;
import java.util.TimerTask;

public class HostController {
    private final GameClient gameClient;

    public HostController(GameClient gameClient) {
        this.gameClient = gameClient;
        this.gameClient.setGameStatus("START");
        Timer timer = new Timer(true);
        timer.schedule(listenToServer(), 0, 200);
    }

    private TimerTask listenToServer() {
        return new TimerTask() {
            @Override
            public void run() {
                actOnAllReady();
            }
        };
    }

    public void actOnAllReady() {
        Boolean allReady = gameClient.getReady();
        if (!allReady) {
            return;
        }
        gameClient.resetReady();
        String status = gameClient.getGameStatus();
        switch (status) {
            case "START":
            case "START ROUND":
                startProgrammingPhase();
                break;
            case "PROGRAMMING":
                endProgrammingPhase();
                break;
            default:
                break;
        }
    }

    public void startProgrammingPhase() {
        gameClient.setGameStatus("PROGRAMMING");
    }

    public void endProgrammingPhase() {
        gameClient.setGameStatus("START ROUND");
    }
}
