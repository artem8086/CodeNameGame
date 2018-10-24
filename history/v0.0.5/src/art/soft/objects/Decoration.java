package art.soft.objects;

import art.soft.Loader;
import art.soft.animation.Animation;
import art.soft.level.Layer;
import art.soft.level.LayerObj;
import art.soft.objsData.Decor;
import art.soft.objsData.ObjData;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Decoration extends LayerObj {

    protected Animation anim;
    
    @Override
    public void init(Loader loader, ObjData data, ObjData drop) {
        this.data = data;
        Decor decor = (Decor) data;
        if (decor.animData != null) {
            anim = loader.getAnimation();
            anim.setAnimAndSet(decor.animData, decor.anim);
        }
    }

    @Override
    public void draw(Graphics g) {
        anim.play(g, x, y);
        //
        if (data.loader.debugDecoration) {
            g.setColor(Color.GREEN);
            g.drawRect(x - data.cenX, y - data.cenY, data.width, data.height);
        }
    }

    @Override
    public boolean act(Layer layer) {
        anim.incAnim(false);
        return false;
    }

    @Override
    public void pool(Loader loader) {
        super.pool(loader);
        anim.pool(loader);
        anim = null;
    }
}
