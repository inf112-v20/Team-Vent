package inf112.skeleton.app;

public class GameScreen {
    /*
    private static final int TILES_WIDE = 5;
    private static final int TILES_HIGH = 5;
    private static final int TILE_ID_HOLE = 6;
    private static final int PIXELS_PER_TILE = 100;
    private RoboRally game;
    private TiledMapTileLayer playerLayer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer.Cell playerCell;
    private Robot robot;
    private TiledMapTileLayer tileLayer;
    private TiledMapTileLayer.Cell stillRobotCell;
    private TiledMapTileLayer.Cell redRobotCell;
    private Player player;
    private SpriteBatch batch;
    private BitmapFont font;
    private boolean shiftIsPressed;
    //private Animation walk;
    //private float elapsedTime = 0;
    //private TextureAtlas textureAtlas;

    public GameScreen(RoboRally game) {
        this.game = game;
    }

    private static void log(String message) {
        Gdx.app.log(GameScreen.class.getName(), message);
    }

    @Override
    // DONE
    public void show() {
        // create camera
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, TILES_WIDE + TILES_WIDE / 2f, TILES_HIGH);
        camera.position.x = (float) TILES_WIDE / 2 + TILES_WIDE / 4f;
        camera.position.y = (float) TILES_HIGH / 2;
        camera.update();

        // create map
        TiledMap tiledMap = new TmxMapLoader().load("demo.tmx");
        playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Tile");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1 / PIXELS_PER_TILE);
        mapRenderer.setView(camera);

        // create robot, (robots under CC BY-SA 3.0, we will credit Skorpio in the coming credits
        robot = new Robot(new Location(new RVector2(2, 2), Direction.NORTH));
        stillRobotCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(
                new Texture("Player/Mechs/Mech5.psd")))); // todo: move
        redRobotCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(
                new Texture("Player/Mechs/Mech5.psd"))));
        playerCell = stillRobotCell;
        Gdx.input.setInputProcessor(this);
        //walking animation textureAtlas needs a spritesheet looking into converting psd to spritesheet
        // textureAtlas = new TextureAtlas(Gdx.files.internal("Player/Mechs/Mech5.psd"));
        // walk = new Animation(1/15f, textureAtlas.getRegions());

        player = new Player();
        player.genereateCardHand(); // todo: move
        createFont();
    }

    @Override
    // DONE
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        playerLayer.setCell(robot.getX(), robot.getY(), playerCell);
        mapRenderer.render();
        renderFont();
    }

    @Override
    // DONE
    public void resize(int width, int height) {
        log(String.format("Resized to width=%d, height=%d", width, height));
    }

    @Override
    // DONE
    public void pause() {
        log("Paused");
    }

    @Override
    // DONE
    public void resume() {
        log("Resumed");
    }

    @Override
    // DONE
    public void hide() {
        mapRenderer.dispose();
        batch.dispose();
        font.dispose();
    }

    @Override
    // DONE
    public void dispose() {
    }

    @Override
    // DONE
    public boolean keyUp(int keycode) {
        log(String.format("Input: %s released", Input.Keys.toString(keycode).toUpperCase()));
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = false;
        }
        if (cardKeyCodes(keycode)) {
            return true;  // input has been handled
        }
        switch (keycode) {
            case Input.Keys.LEFT:
                playerLayer.setCell(robot.getX(), robot.getY(), null);
                robot.execute(new RotateLeftCard());
                break;
            case Input.Keys.UP:
                playerLayer.setCell(robot.getX(), robot.getY(), null);
                robot.execute(new MoveForwardCard());
                break;
            case Input.Keys.RIGHT:
                playerLayer.setCell(robot.getX(), robot.getY(), null);
                robot.execute(new RotateRightCard());
                break;
            default:
                return false; // the robot did not move
        }
        update();
        return true; // the robot moved
    }

    @Override
    // DONE
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SHIFT_LEFT) {
            shiftIsPressed = true; // todo: move
            return true;
        }
        return false;
    }

    // DONE?
    private void update() {
        // precondition: the robot has moved
        TiledMapTile tileUnderRobot = tileLayer.getCell(robot.getX(), robot.getY()).getTile();
        if (tileUnderRobot.getId() == TILE_ID_HOLE) {
            log(robot.status());
            robot.takeDamage();
            playerCell = redRobotCell;
        } else {
            playerCell = stillRobotCell;
        }
        playerLayer.setCell(robot.getX(), robot.getY(), playerCell);
        if (!robot.alive()) {
            game.setGameOverScreen();
        }
    }

    // DONE
    private void createFont() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    // DONE
    private void renderFont() {
        batch.begin();
        font.draw(batch, player.generateHandAsString(), 550, 550);
        font.draw(batch, player.generateProgrammingSlotsAsString(), 550, 300);
        batch.end();
    }

    // DONE
    private boolean cardKeyCodes(int keycode) {
        if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_5 && shiftIsPressed) {
            player.undoProgrammingSlotPlacement(keycode - 8);
            return true;
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            player.placeCardFromHandToSlot(keycode - 8);
            return true; // input has been handled, no need to handle further
        } else if (keycode == Input.Keys.G) {
            player.genereateCardHand();
            return true;
        } else if (keycode == Input.Keys.E) {
            endTurn();
            return true;
        }
        return false;
    }

    // DONE
    private void endTurn() {
        for (int i = 0; i < 5; i++) {
            doPhase(i);
        }
        player.clearProgrammingSlots();
        player.genereateCardHand();
    }

    // DONE
    private void doPhase(int i) {
        IProgramCard card = player.getCardInProgrammingSlot(i);
        if (card != null) {
            playerLayer.setCell(robot.getX(), robot.getY(), null);
            robot.execute(card);
        }
    }
     */
}
