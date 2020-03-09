package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import inf112.skeleton.app.network.GameClient;
import inf112.skeleton.app.network.GameHost;

public class LobbyScreen extends ScreenAdapter {

    public LobbyScreen(Boolean isHost, String hostAddress) {
        if(isHost){
            Thread gameHostThread = new Thread(() -> {
                GameHost gameHost = new GameHost(hostAddress);
            });
        }
        GameClient gameClient = new GameClient(hostAddress);

        Skin skin = new Skin(Gdx.files.internal("skin/shade/skin/uiskin.json"));
        Stage stage = new Stage();
        Table table  = new Table();
    }
}
