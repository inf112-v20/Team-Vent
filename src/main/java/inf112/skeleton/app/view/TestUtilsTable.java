package inf112.skeleton.app.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import inf112.skeleton.app.model.Player;
import inf112.skeleton.app.model.cards.Card;

public class TestUtilsTable extends Table {
    private Label handLabel;
    private Label slotsLabel;
    private Player player;

    public TestUtilsTable(Player player, Skin skin) {
        this.player = player;
        this.defaults().padBottom(10);
        this.add(new Label("TEST UTILS", skin)).left();
        this.row();
        this.handLabel = new Label(handAsString(), skin);
        this.add(handLabel).padBottom(20);
        this.row();
        this.slotsLabel = new Label(programmingSlotsAsString(), skin);
        this.add(slotsLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.handLabel.setText(handAsString());
        this.slotsLabel.setText(programmingSlotsAsString());
    }

    public String handAsString() {
        StringBuilder handAsString = new StringBuilder();
        cardArrayToString(handAsString, player.getHand());
        handAsString.append("\nG  Generate New Hand");
        return handAsString.toString();
    }

    public String programmingSlotsAsString() {
        StringBuilder programmingSlotsAsString = new StringBuilder();
        programmingSlotsAsString.append("PROGRAMMING SLOTS: \n");
        cardArrayToString(programmingSlotsAsString, player.getProgrammingSlots());
        programmingSlotsAsString.append("\nE  Execute (order 66)");
        return programmingSlotsAsString.toString();
    }

    private void cardArrayToString(StringBuilder TargetString, Card[] cardArray) {
        for (int i = 0; i < cardArray.length; i++) {
            TargetString.append(i + 1);
            TargetString.append(".  ");
            if (cardArray[i] != null) {
                TargetString.append(cardArray[i].toString());
            }
            TargetString.append("\n");
        }
    }
}