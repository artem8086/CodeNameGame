package art.soft.objects;

import art.soft.Loader;
import art.soft.level.Layer;
import art.soft.objsData.Bullet;
import art.soft.objsData.ObjData;

/**
 *
 * @author Артём Святоха
 */
public class BulletObj extends StaticObj {

    public int range;

    @Override
    public void init(ObjData data, ObjData drop) {
        super.init(data, drop);
        range = ((Bullet) data).range;
    }

    @Override
    public boolean act(Layer layer) {
        boolean del = super.act(layer);
        if (range == 0) return true;
        if (range > 0) range --;
        //
        Bullet bullet = (Bullet) data;
        x += flipX ? - bullet.velX : bullet.velX;
        y += flipY ? - bullet.velY : bullet.velY;
        StaticObj obj = (StaticObj) layer.objs;
        int xt = x - cenX;
        int yt = y - cenY;
        int w = data.width;
        int h = data.height;
        while (obj != null) {
            if (obj != this) {
                if ((obj.getColision() & bullet.coll_mask) != 0 && obj.contain(xt, yt, w, h)) {
                    obj.addForce(flipX ? - bullet.forceX : bullet.forceX,
                                 flipY ? - bullet.forceY : bullet.forceY);
                    obj.addHP(bullet.damage);
                    return true;
                }
            }
            obj = (StaticObj) obj.next;
        }
        return del;
    }
}
