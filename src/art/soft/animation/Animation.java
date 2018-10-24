package art.soft.animation;

import art.soft.Loader;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Animation {
    
    private AnimSet animSet;
    
    private AnimData current;
    
    private int time, nFrame;
    
    public Animation next;

    public Animation() {}

    public Animation(AnimSet animSet) {
        this.animSet = animSet;
    }

    public void setAnimSet(AnimSet set) {
        animSet = set;
    }

    public AnimSet getAnimSet() {
        return animSet;
    }

    public void setAnimation(int num) {
        AnimData anim = animSet.data[num];
        if (anim != current) {
            current = anim;
            time = anim.frameTime;
            nFrame = 0;
        }
    }

    public void setAnimAndSet(AnimSet set, int num) {
        animSet = set;
        AnimData anim = animSet.data[num];
        if (anim != current) {
            current = anim;
            time = anim.frameTime;
            nFrame = 0;
        }
    }

    public void reset() {
        time = current.frameTime;
        nFrame = 0;
    }

    public void play(Graphics g, int x, int y) {
        AnimFrame frame = current.frames[nFrame];
        if (frame != null) frame.draw(g, animSet.animPack, x, y);
        if (next != null) next.play(g, x, y);
    }

    public void play(Graphics g, int x, int y, boolean flipX, boolean flipY) {
        AnimFrame frame = current.frames[nFrame];
        if (frame != null) frame.draw(g, animSet.animPack, x, y, flipX, flipY);
        if (next != null) next.play(g, x, y, flipX, flipY);
    }

    public void pool() {
        if (next != null) next.pool();
        animSet = null;
        current = null;
        Loader loader = Loader.getLoader();
        next = loader.animsPool;
        loader.animsPool = this;
    }

    public boolean incAnim(boolean cycle) {
        if (next != null) next.incAnim(cycle);
        time --;
        if (time <= 0) {
            time = current.frameTime;
            nFrame ++;
            if (nFrame >= current.frames.length) {
                nFrame = cycle ? 0 : current.frames.length - 1;
                return !cycle;
            }
        }
        return false;
    }
}
