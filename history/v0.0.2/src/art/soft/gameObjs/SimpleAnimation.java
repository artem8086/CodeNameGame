package art.soft.gameObjs;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import art.soft.animation.Animation;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class SimpleAnimation extends gameObj {

    public Animation animation;
    public int x, y;
    public boolean cycle = true;

    public void setAnim(Animation animation) {
        this.animation = animation;
    }

    public void setAnimSet(Loader loader, AnimSet set) {
        if (animation != null) {
            animation.pool(loader);
        }
        animation = loader.getAnimation();
        animation.setAnimSet(set);
    }

    public void setAnimNum(int num) {
        animation.setAnimation(num);
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public void setPos(int x, int y) {
        this.x = x; this.y = y;
    }

    @Override
    public void init(Loader loader) {
        cycle = true;
    }

    @Override
    public boolean act() {
        return animation.incAnim(cycle);
    }

    @Override
    public void draw(Graphics g) {
        animation.play(g, x, y);
    }

    @Override
    public void pool(Loader loader) {
        animation.pool(loader);
        animation = null;
    }
}
