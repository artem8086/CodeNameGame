package art.soft.objects;

import art.soft.Loader;
import art.soft.level.Layer;
import art.soft.objsData.ObjData;
import art.soft.objsData.Static;
import static art.soft.objsData.Static.FLIP_X_MASK;
import static art.soft.objsData.Static.FLIP_Y_MASK;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class StaticObj extends Decoration {

    public int hp;
    public float vx, vy;
    public int cenX, cenY;
    protected boolean flipX, flipY;
    //
    protected ObjData drop;

    @Override
    public void init(ObjData data, ObjData drop) {
        super.init(data, drop);
        this.drop = drop;
        Static sData = (Static) data;
        setFlipX((sData.flip & FLIP_X_MASK) != 0);
        setFlipY((sData.flip & FLIP_Y_MASK) != 0);
        hp = sData.hp;
        vx = vy = 0;
    }

    public void setFlipX(boolean flipX) {
        cenX = flipX ? data.width - data.cenX : data.cenX;
        this.flipX = flipX;
    }
    
    public void setFlipY(boolean flipY) {
        cenY = flipY ? data.height - data.cenY : data.cenY;
        this.flipY = flipY;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }

    public boolean isAlive() {
        return hp != 0;
    }

    public int getX() {
        return x - cenX;
    }

    public int getY() {
        return y - cenY;
    }

    public int getHP() {
        return hp;
    }

    public int getMaxHP() {
        return ((Static) data).hp;
    }

    public void addHP(int dmg) {
        if (hp >= 0) {
            hp += dmg;
            int max = ((Static) data).hp;
            if (hp > max) hp = max;
            else if (hp <= 0) {
                setDeathAnim();
                hp = 0;
            }
        }
    }

    public boolean contain(int x, int y) {
        int tx = this.x - cenX;
        int ty = this.y - cenY;
        return x >= tx && x <= tx + data.width && y >= ty && y <= ty + data.height;
    }

    public boolean contain(int tx, int ty, int w, int h) {
        int rx = x - cenX;
        int ry = y - cenY;
        return rx + data.width > tx && ry + data.height > ty
                && tx + w > rx && ty + h > ry;
    }

    public void setDeathAnim() {
        anim.setAnimation(((Static) data).deathAnim);
    }

    public int getColision() {
        return hp == 0 ? 0 : ((Static) data).collision;
    }

    public void addForce(float x, float y) {}

    public void setForce(float x, float y) {
        vx = x; vy = y;
    }

    public float getVX() {
        return vx;
    }

    public float getVY() {
        return vx;
    }

    public float getImpulseX() {
        return vx;
    }

    @Override
    public int getCenterX() {
        return cenX;
    }

    @Override
    public int getCenterY() {
        return cenY;
    }

    @Override
    public boolean act(Layer layer) {
        return anim != null ? anim.incAnim(hp != 0) : false;
    }

    @Override
    public void draw(Graphics g) {
        if (anim != null) anim.play(g, x, y, flipX, flipY);
        //
        if (Loader.getLoader().debugStatic) {
            g.setColor(Color.BLUE);
            g.drawRect(x - cenX, y - cenY, data.width, data.height);
        }
    }

    @Override
    public void pool() {
        if (drop != null && hp == 0) {
            drop.createObj(Loader.getLoader().engine.curLayer, x, y, null);
        }
        hp = 0;
        drop = data = null;
    }
    
}
