package inf112.skeleton.app.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PopImage {
    private final Sprite sprite;
    private Boolean show;
    private Boolean decreaseTransparancy;

    public PopImage(Texture texture) {
        sprite = new Sprite(texture);
        sprite.setPosition(200, 400);
        sprite.setAlpha(0f);
        show = false;
        decreaseTransparancy = false;
    }

    public boolean getShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
        if (!show) {
            sprite.setAlpha(0f);
            decreaseTransparancy = false;
        }
    }

    public Sprite getSprite() {
        if (sprite.getColor().a <= 0.98f) {
            sprite.setAlpha(sprite.getColor().a + 0.02f);
        } else {
            decreaseTransparancy = true;
        }
        if (decreaseTransparancy) {
            sprite.setAlpha(sprite.getColor().a - 0.03f);
        }
        return sprite;
    }
}
