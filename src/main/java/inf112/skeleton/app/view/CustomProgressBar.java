package inf112.skeleton.app.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CustomProgressBar extends ProgressBar {

    public CustomProgressBar(int min, int max, int width, int height, Color backgroundColor, Color fillColor) {
        super(min, max, 1, false, new ProgressBarStyle());
        getStyle().background = getColoredDrawable(width, height, backgroundColor);
        getStyle().knob = getColoredDrawable(0, height, fillColor);
        getStyle().knobBefore = getColoredDrawable(width, height, fillColor);
        setWidth(width);
        setHeight(height);
        setAnimateDuration(0.5f);  // should be no longer than the delay between steps
    }

    public static Drawable getColoredDrawable(int w, int h, Color color) {
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
    }
}