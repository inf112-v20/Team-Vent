package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import inf112.skeleton.app.model.cards.Card;
import inf112.skeleton.app.view.StatsTable;
import inf112.skeleton.app.view.TiledMapActor;

import java.util.HashMap;

public class GameScreen extends ScreenAdapter {
    private final GameModel gameModel;

    private Viewport viewport;
    private Stage stage;
    private ImageButton[] programmingSlotButtons;
    private ImageButton[] handSlotButtons;
    private HashMap<String, TextureRegionDrawable> cardTextures;
    private String time;

    private SpriteBatch popImages;
    public PopImage[] phasesImages;
    public PopImage win;
    public PopImage lose;

    private Label timeLabel;

    public GameScreen(GameModel gameModel, InputMultiplexer inputMultiplexer) {
        time = "";
        this.gameModel = gameModel;
        viewport = new ScreenViewport();
        stage = new Stage(viewport);
        inputMultiplexer.addProcessor(stage);
    }

    public void show() {

        float unitScale = 0.5f; // the unit scale determines the size of the map
        Skin skin = new Skin(Gdx.files.internal(("Skin/shade/skin/uiskin.json")));
        TextureAtlas cardsAtlas = new TextureAtlas(Gdx.files.internal("Cards.atlas"));

        popImages = new SpriteBatch();
        phasesImages = new PopImage[5];
        phasesImages[0] = new PopImage(new Texture("PopUpImages/Phase1.png"));
        phasesImages[1] = new PopImage(new Texture("PopUpImages/Phase2.png"));
        phasesImages[2] = new PopImage(new Texture("PopUpImages/Phase3.png"));
        phasesImages[3] = new PopImage(new Texture("PopUpImages/Phase4.png"));
        phasesImages[4] = new PopImage(new Texture("PopUpImages/Phase5.png"));
        win = new PopImage(new Texture("PopUpImages/Win.png"));
        lose = new PopImage(new Texture("PopUpImages/lose.png"));

        // load card textures
        cardTextures = new HashMap<>();
        cardTextures.put("MOVE ONE", new TextureRegionDrawable(cardsAtlas.findRegion("card_move", 1)));
        cardTextures.put("MOVE TWO", new TextureRegionDrawable(cardsAtlas.findRegion("card_move", 2)));
        cardTextures.put("MOVE THREE", new TextureRegionDrawable(cardsAtlas.findRegion("card_move", 3)));
        cardTextures.put("BACK UP", new TextureRegionDrawable(cardsAtlas.findRegion("card_back_up")));
        cardTextures.put("ROTATE LEFT", new TextureRegionDrawable(cardsAtlas.findRegion("card_rotate_left")));
        cardTextures.put("ROTATE RIGHT", new TextureRegionDrawable(cardsAtlas.findRegion("card_rotate_right")));
        cardTextures.put("U TURN", new TextureRegionDrawable(cardsAtlas.findRegion("card_u_turn")));
        cardTextures.put("NULL", new TextureRegionDrawable(cardsAtlas.findRegion("card_null")));

        // beside the board: stats table
        Table sideTable = new Table();
        sideTable.defaults().left();
        sideTable.add(new StatsTable(gameModel, skin));
        sideTable.row();

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
        for (int i = 0; i < 5; i++) {
            ImageButton programmingSlot = new ImageButton(nullTexture);
            cardTable.add(programmingSlot).prefHeight(10).prefWidth(60);
            programmingSlotButtons[i] = programmingSlot;
        }
        // player's card hand
        cardTable.add().padLeft(50);
        for (int i = 0; i < 9; i++) {
            ImageButton handSlot = new ImageButton(nullTexture);
            cardTable.add(handSlot).prefHeight(10).prefWidth(60);
            handSlotButtons[i] = handSlot;
        }
        addCardButtonsFunctionality();
        timeLabel = new Label("Time: " + time, skin);
        cardTable.add(timeLabel).left().padLeft(20);
        cardTable.row();
        // 1-5 labels under programming slots
        for (int i = 1; i < 6; i++) {
            cardTable.add(new Label(Integer.toString(i), skin));
        }

        // root table
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().padTop(10).padLeft(10);
        rootTable.add(new TiledMapActor(gameModel, unitScale)).left();
        rootTable.add(sideTable).expandX().top().left().padLeft(50);
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
        updateCards();
        timeLabel.setText(time);
        renderPopups();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void updateTime(String time) {
        this.time = time;
    }

    private void addCardButtonsFunctionality() {
        for (int i = 0; i < handSlotButtons.length; i++) {
            int slotIndex = i;
            handSlotButtons[i].addListener(new InputListener() {
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

        for (int i = 0; i < programmingSlotButtons.length; i++) {
            int slotIndex = i;
            programmingSlotButtons[i].addListener(new InputListener() {
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

    private void updateCards() {
        // updates all of the cards with new textures
        for (int i = 0; i < 5; i++) {
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            Card card = gameModel.getMyPlayer().getCardInProgrammingSlot(i);
            String cardName = card == null ? "NULL" : card.toString();
            style.imageUp = cardTextures.get(cardName);
            programmingSlotButtons[i].setStyle(style);
        }

        for (int i = 0; i < 9; i++) {
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            Card card = gameModel.getMyPlayer().getCardinHand(i);
            String cardName = card == null ? "NULL" : card.toString();
            style.imageUp = cardTextures.get(cardName);
            handSlotButtons[i].setStyle(style);
        }
    }

    private void renderPopups() {
        popImages.begin();
        popImages.enableBlending();
        for (PopImage phaseImage : phasesImages) {
            if (phaseImage.getShow()) {
                phaseImage.getSprite().draw(popImages);
            }
        }
        if (win.getShow()) {
            win.getSprite().draw(popImages);
        }
        else if (lose.getShow()) {
            lose.getSprite().draw(popImages);
        }
        popImages.end();
    }
}
