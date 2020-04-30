package inf112.skeleton.app.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PopImage {
    private Sprite sprite;
    private Boolean show;

    public PopImage(Texture texture) {
        sprite = new Sprite(texture);
        sprite.setPosition(200, 400);
        sprite.setAlpha(0f);
        show = false;
    }

    public void setShow (boolean show) {
        this.show = show;
    }

    public boolean getShow() {return show;}

    public Sprite getSprite() {
        if (sprite.getColor().a <= 0.98f) {
            sprite.setAlpha(sprite.getColor().a + 0.02f);
        }
        return sprite;
    }
}
