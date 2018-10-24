package art.soft.level;

import art.soft.Loader;
import art.soft.objects.Dynamic;
import art.soft.objects.ObjData;
import art.soft.objects.Static;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class DynamicObj extends StaticObj {

    protected Layer layer;
    public boolean onGround;

    @Override
    public void init(Loader loader, ObjData data, Layer layer) {
        super.init(loader, data, layer);
        this.layer = layer;
        onGround = false;
        vx = vy = 0;
    }

    @Override
    public boolean act() {
        boolean del = super.act();
        gameEngine engine = data.loader.engine;
        Dynamic dynamic = (Dynamic) data;
        vy += engine.gravity;
        vx *= dynamic.acceleratX;
        vy *= dynamic.acceleratY;
        int max = dynamic.max_speed;
        if (vx > max) vx = max; else if (vx < - max) vx = - max;
        if (vy > max) vy = max; else if (vy < - max) vy = - max;
        int old_x = x;
        int old_y = y;
        x += vx;
        if (engine.flipGravity) {
            setFlipY(true);
            y -= vy;
        } else {
            setFlipY(false);
            y += vy;
        }
        // Обнаружение коллизий
        
        StaticObj obj = (StaticObj) layer.objs;
        StaticObj collObj = null;
        onGround = false;
        boolean collX = false;
        while (obj != null) {
            if (obj != this) {
                if ((obj.getColision() & dynamic.coll_mask) != 0) {
                    boolean coll = false;
                    if (vy != 0) {
                        if (collisionDetect(old_x, y, obj, true)) {
                            if (vy > 0) onGround = true;
                            coll = true;
                        }
                    }
                    if (!coll && vx != 0) {
                        if (collisionDetect(x, old_y, obj, false)) {
                            coll = collX = true;
                        }
                    }
                    
                    if (coll) collObj = obj;
                }
            }
            obj = (StaticObj) obj.next;
        }
        if (collObj != null) {
            // Обработка столкновения
            float friction = ((Static) collObj.data).friction;
            addForce(collX ? 0 : collObj.vx, !collX ? 0 : collObj.vy);
            collObj.addForce(collX ? vx * friction : 0, !collX ? vy * friction : 0);
            if (collX) {
                vx = 0;
                //vy -= vy * friction;
            } else {
                vy = 0;
                vx -= vx * friction;
            }
        } else {
            vx -= vx * engine.envirFrictionX;
            vy -= vy * engine.envirFrictionY;
        }
        return del;
    }

    @Override
    public void addForce(float x, float y) {
        vx += x; vy += y;
    }

    public boolean collisionDetect(int x, int y, StaticObj obj, boolean vert) {
        Static thisData = (Static) data;
        Static objData = (Static) obj.data;
        //
        int rx = x - cenX;
        int ry = y - cenY;
        int rw = rx + thisData.width;
        int rh = ry + thisData.height;
        int tx = obj.x - obj.cenX;
        int ty = obj.y - obj.cenY;
        int tw = tx + objData.width;
        int th = ty + objData.height;
        //
        boolean coll = rw > tx && rh > ty && tw > rx && th > ry;
        //
        if (coll) {
            if (vert) {
                if (ry < ty) this.y = ty - thisData.height + cenY; 
                else this.y = th + cenY;
            } else {
                if (rx < tx) this.x = tx - thisData.width + cenX;
                else this.x = tw + cenX;
            }
        }
        return coll;
    }

    @Override
    public void draw(Graphics g) {
        if (anim != null) anim.play(g, x, y, flipX, flipY);
        //
        //g.setColor(Color.RED);
        //g.drawRect(x - cenX, y - cenY, data.width, data.height);
    }

    @Override
    public void pool(Loader loader) {
        super.pool(loader);
        layer = null;
    }
}
