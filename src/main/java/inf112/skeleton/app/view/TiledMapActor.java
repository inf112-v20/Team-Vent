package inf112.skeleton.app.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import inf112.skeleton.app.model.GameModel;

public class TiledMapActor extends Actor {
    private OrthogonalTiledMapRenderer renderer;

    public TiledMapActor(GameModel gameModel, float unitScale) {
        TiledMap map = gameModel.getMapHandler().getMap();
        setWidth((int) map.getProperties().get("tilewidth") * (int) map.getProperties().get("width") * unitScale);
        setHeight((int) map.getProperties().get("tileheight") * (int) map.getProperties().get("height") * unitScale);
        setPosition(0, Gdx.graphics.getHeight() - getHeight());  // (x, y) or the left corner of the map
        renderer = new BoardRenderer(gameModel, unitScale);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        OrthographicCamera cam = (OrthographicCamera) getStage().getCamera();
        cam.translate(-getX(), -getY(), 0);
        cam.update();
        renderer.setView(cam);
        renderer.render();
        cam.translate(getX(), getY(), 0);
        cam.update();
        batch.begin();
    }
}
