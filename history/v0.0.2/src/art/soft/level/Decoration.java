package art.soft.level;

import art.soft.Loader;
import art.soft.animation.Animation;
import art.soft.objects.Decor;
import art.soft.objects.ObjData;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Decoration extends LayerObj {

    protected Animation anim;
    
    @Override
    public void init(Loader loader, ObjData data, Layer layer) {
        this.data = data;
        Decor decor = (Decor) data;
        if (decor.animData != null) {
            anim = loader.getAnimation();
            anim.setAnimSet(decor.animData);
            anim.setAnimation(decor.anim);
        }
    }

    @Override
    public void draw(Graphics g) {
        anim.play(g, x, y);
        //
        //g.setColor(Color.GREEN);
        //g.drawRect(x - data.cenX, y - data.cenY, data.width, data.height);
    }

    @Override
    public boolean act() {
        anim.incAnim(false);
        return false;
    }

    @Override
    public void pool(Loader loader) {
        anim.pool(loader);
        anim = null;
    }
}
