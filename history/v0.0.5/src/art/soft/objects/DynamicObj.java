package art.soft.objects;

import art.soft.Loader;
import art.soft.level.Layer;
import art.soft.level.gameEngine;
import art.soft.objsData.Dynamic;
import art.soft.objsData.ObjData;
import art.soft.objsData.Static;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class DynamicObj extends StaticObj {

    protected static StaticObj hCollObj, vCollObj;

    public boolean onGround;
    protected float friction, impulseX;

    @Override
    public void init(Loader loader, ObjData data, ObjData drop) {
        super.init(loader, data, drop);
        onGround = false;
        friction = 1f;
        impulseX = 0;
    }

    @Override
    public boolean act(Layer layer) {
        boolean del = super.act(layer);
        gameEngine engine = data.loader.engine;
        Dynamic dynamic = (Dynamic) data;
        if (onGround) vx *= friction;
        modifyForce();
        if (!onGround) {
            vx *= engine.envirFrictionX;
            vy *= engine.envirFrictionY;
        }
        vy += engine.gravity * dynamic.mass;
        int max = dynamic.max_speedX;
        if (vx > max) vx = max; else if (vx < - max) vx = - max;
        max = dynamic.max_speedY;
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
        hCollObj = vCollObj = null;
        onGround = false;
        while (obj != null) {
            if (obj != this) {
                dealsDamage(obj);
                if ((obj.getColision() & dynamic.coll_mask) != 0) {
                    if (vy != 0 && collisionDetect(old_x, old_y, old_x, y, obj, true)) {
                        vCollObj = obj;
                    } else
                    if (vx != 0 && collisionDetect(old_x, old_y, x, old_y, obj, false)) {
                        hCollObj = obj;
                    }
                }
            }
            obj = (StaticObj) obj.next;
        }
        // Обработка столкновения
        if (hCollObj != null) collisonProcessing(hCollObj, true);
        if (vCollObj != null) collisonProcessing(vCollObj, false);
        return del;
    }

    protected void dealsDamage(StaticObj obj) {}

    protected void modifyForce() {}

    protected void collisonProcessing(StaticObj collObj, boolean collX) {
        Dynamic dynamic = (Dynamic) data;
        // Просчёт урона
        float x = vx * dynamic.damageX_reduct;
        float y = vy * dynamic.damageY_reduct;
        float damage = (float) Math.sqrt(x * x + y * y) - dynamic.min_damage;
        if (damage > 0) {
            damage *= dynamic.mass;
            //data.loader.game.log(data.type + " get damage = " + (int) damage + ", hp = " + hp);
            collObj.addHP(- (int) damage);
            this.addHP(- (int) damage);
        }
        //
        collObj.addForce(collX ? vx : 0, collX ? 0 : vy > 0 ? vy : vy / dynamic.mass);
        if (collX) {
            impulseX = vx = 0;
            //vy -= vy * friction;
        } else {
            vx += collObj.getImpulseX();
            impulseX = vx;
            friction = ((Static) collObj.data).friction + dynamic.friction;
            friction /= dynamic.mass * 2;
            vy = 0;
        }
    }

    @Override
    public float getImpulseX() {
        return impulseX;
    }

    @Override
    public void addForce(float x, float y) {
        vx += x; vy += y;
    }

    public boolean collisionDetect(int old_x, int old_y, int x, int y, StaticObj obj, boolean vert) {
        Static thisData = (Static) data;
        Static objData = (Static) obj.data;
        //
        int rx = old_x - cenX;
        int ry = old_y - cenY;
        int rw = rx + thisData.width;
        int rh = ry + thisData.height;
        int t = old_x - x;
        if (t >= 0) rx -= t; else rw -= t;
        t = old_y - y;
        if (t < 0) rh -= t; else ry -= t;
        int tx = obj.x - obj.cenX;
        int ty = obj.y - obj.cenY;
        int tw = tx + objData.width;
        int th = ty + objData.height;
        //
        if (rw > tx && rh > ty && tw > rx && th > ry) {
            if (vert) {
                if (data.loader.engine.flipGravity) {
                    if (rh < th) {
                        if (vy <= 0) this.y = ty - thisData.height + cenY;
                    } else {
                        if (vy >= 0) {
                            this.y = th + cenY;
                            onGround = true;
                        }
                    }
                } else
                if (ry < ty) {
                    if (vy >= 0) {
                        this.y = ty - thisData.height + cenY;
                        onGround = true;
                    }
                } else {
                    if (vy <= 0) this.y = th + cenY;
                }
            } else {
                if (rx < tx) {
                    if (vx >= 0) this.x = tx - thisData.width + cenX;
                } else {
                    if (vx <= 0) this.x = tw + cenX;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        if (anim != null) anim.play(g, x, y, flipX, flipY);
        //
        if (data.loader.debugDynamic) {
            g.setColor(Color.RED);
            g.drawRect(x - cenX, y - cenY, data.width, data.height);
        }
    }
}
