package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import inf112.skeleton.app.model.GameModel;
import inf112.skeleton.app.view.StatsTable;
import inf112.skeleton.app.view.TestUtilsTable;
import inf112.skeleton.app.model.Robot;
import inf112.skeleton.app.model.cards.Card;
import inf112.skeleton.app.view.TiledMapActor;

import java.util.*;
import java.util.List;

public class GameScreen extends ScreenAdapter {
    private final GameModel gameModel;

    private Viewport viewport;
    private Stage stage;

    private ImageButton[] programmingSlotButtons;
    private ImageButton[] handSlotButtons;
    private HashMap<String, TextureRegionDrawable> cardTextures;
    private HashMap<Robot, Label> robotStatuses;
    private Label programmingSlotsLabel;
    private Label handLabel;


    public GameScreen(GameModel gameModel, InputMultiplexer inputMultiplexer) {
        this.gameModel = gameModel;
        robotStatuses = new HashMap<>();
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        inputMultiplexer.addProcessor(stage);
    }

    public void show() {

        float unitScale = 0.5f; // the unit scale determines the size of the map
        Skin skin = new Skin(Gdx.files.internal(("Skin/shade/skin/uiskin.json")));
        TextureAtlas cardsAtlas = new TextureAtlas(Gdx.files.internal("Cards.atlas"));

        // load card textures
        cardTextures = new HashMap<>();
        cardTextures.put("MOVE ONE", new TextureRegionDrawable(cardsAtlas.findRegion("card_move", 1)));
        cardTextures.put("MOVE TWO", new TextureRegionDrawable(cardsAtlas.findRegion("card_move", 2)));
        cardTextures.put("MOVE THREE", new TextureRegionDrawable(cardsAtlas.findRegion("card_move", 3)));
        cardTextures.put("BACK UP", new TextureRegionDrawable(cardsAtlas.findRegion("card_back_up")));
        cardTextures.put("ROTATE LEFT", new TextureRegionDrawable(cardsAtlas.findRegion("card_rotate_left")));
        cardTextures.put("ROTATE RIGHT", new TextureRegionDrawable(cardsAtlas.findRegion("card_rotate_right")));
        cardTextures.put("NULL", new TextureRegionDrawable(cardsAtlas.findRegion("card_null")));

        // inside sidebar: stats
        Table stats = new Table();
        stats.defaults().left();
        List<Robot> sortedRobots = new LinkedList<>(gameModel.getRobots());
        Collections.swap(sortedRobots, 0, sortedRobots.indexOf(gameModel.getMyPlayer().getRobot()));
        for (Robot robot : gameModel.getRobots()) {
            Label robotLabel = new Label(formatRobotState(robot), skin);
            robotStatuses.put(robot, robotLabel);
            stats.add(robotLabel);
            stats.row();
        }

        // inside sidebar: test utils
        Table utils = new Table();
        handLabel = new Label(gameModel.getMyPlayer().handAsString(), skin);
        utils.add(handLabel);
        utils.row();
        programmingSlotsLabel = new Label(gameModel.getMyPlayer().programmingSlotsAsString(), skin);
        utils.add(programmingSlotsLabel);

        // beside the board: stats table
        Table sideTable = new Table();
        sideTable.defaults().left();
        sideTable.add(stats);
        sideTable.row();
        sideTable.add(utils).bottom().padTop(10);

        // below the board: card table
        Table cardTable = new Table();
        Label programmingSlotLabel = new Label("Programming Slots:", skin);
        Label cardHandLabel = new Label("Hand:", skin);
        cardTable.add(programmingSlotLabel).colspan(6).left();
        cardTable.add(cardHandLabel).colspan(9).left();
        cardTable.row();

        TextureRegionDrawable nullTexture = cardTextures.get("NULL");
        programmingSlotButtons = new ImageButton[5];
        handSlotButtons = new ImageButton[9];
        // programming slots
        for (int i = 0; i < 5; i++){
            ImageButton programmingSlot = new ImageButton(nullTexture);
            cardTable.add(programmingSlot).prefHeight(10).prefWidth(60);
            programmingSlotButtons[i] = programmingSlot;
        }
        // player's card hand
        cardTable.add().padLeft(50);
        for (int i = 0; i < 9; i++){
            ImageButton handSlot = new ImageButton(nullTexture);
            cardTable.add(handSlot).prefHeight(10).prefWidth(60);
            handSlotButtons[i] = handSlot;
        }
        addCardButtonsFunctionality();
        cardTable.row();
        // 1-5 labels under programming slots
        for (int i = 1; i < 6; i++){
            cardTable.add(new Label(Integer.toString(i), skin));
        }



        // root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(15);
        rootTable.add(new TiledMapActor(gameModel, unitScale)).left();
        rootTable.add(sideTable).expandX().top().left();
        rootTable.row();
        rootTable.add(cardTable).colspan(2).expandY().left();

        stage.addActor(rootTable);
        stage.setDebugAll(false);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        updateText();
        updateCards();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void updateText(){
        for (Robot robot : gameModel.getRobots()) {
            robotStatuses.get(robot).setText(formatRobotState(robot));
        }
        handLabel.setText(gameModel.getMyPlayer().handAsString());
        programmingSlotsLabel.setText(gameModel.getMyPlayer().programmingSlotsAsString());
    }

    private String formatRobotState(Robot robot) {
        // note: because of a problem with the the font the string there is no way to align the strings perfectly
        return String.format("%-8s  %s %d  %s %d  %s  %s%n",
                robot.toString(),
                "HP:", robot.getState().getHp(),
                "LIVES:", robot.getState().getLives(),
                "FLAGS:", String.format("%d/%d", robot.getState().getCapturedFlags(),
                        gameModel.getMapHandler().getNumberOfFlags()));
    }

    private void addCardButtonsFunctionality(){
        for (int i = 0; i < handSlotButtons.length; i++){
            int slotIndex = i;
            handSlotButtons[i].addListener(new InputListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    gameModel.getMyPlayer().placeCardFromHandToSlot(slotIndex);
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
        }

        for (int i = 0; i < programmingSlotButtons.length; i++){
            int slotIndex = i;
            programmingSlotButtons[i].addListener(new InputListener(){
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    gameModel.getMyPlayer().undoProgrammingSlotPlacement(slotIndex);
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
        }
    }

    private void updateCards(){
        // updates all of the cards with new textures
        for (int i = 0; i < 5; i++) {
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            Card card = gameModel.getMyPlayer().getCardInProgrammingSlot(i);
            String cardName = card == null ? "NULL" : card.toString();
            style.imageUp = cardTextures.get(cardName);
            programmingSlotButtons[i].setStyle(style);
        }

        for (int i = 0; i < 9; i++){
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            Card card = gameModel.getMyPlayer().getCardinHand(i);
            String cardName = card == null ? "NULL" : card.toString();
            style.imageUp = cardTextures.get(cardName);
            handSlotButtons[i].setStyle(style);
        }
    }
}
