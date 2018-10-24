package art.soft.model;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import art.soft.animation.Animation;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Texture extends Polygon {

    private final Animation animation;
    private final int vert;

    public Texture(Loader loader, AnimSet set, int frame, int vert) {
        animation = loader.getAnimation();
        animation.setAnimAndSet(set, frame);
        this.vert = vert;
    }

    @Override
    public void reset(){
        animation.reset();
    }

    @Override
    public void draw(Graphics g) {
        animation.incAnim(true);
        animation.play(g, Model.xVerts[vert], Model.yVerts[vert]);
    }
}
