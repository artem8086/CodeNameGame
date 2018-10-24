package art.soft.level;

import art.soft.Loader;
import art.soft.objects.ObjData;
import art.soft.objects.Static;
import static art.soft.objects.Static.FLIP_X_MASK;
import static art.soft.objects.Static.FLIP_Y_MASK;
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

    @Override
    public void init(Loader loader, ObjData data, Layer layer) {
        super.init(loader, data, layer);
        Static sData = (Static) data;
        setFlipX((sData.flip & FLIP_X_MASK) != 0);
        setFlipY((sData.flip & FLIP_Y_MASK) != 0);
        hp = sData.hp;
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

    public int getHP() {
        return hp;
    }

    public void addHP(int hp) {
        if (hp >= 0) {
            this.hp += hp;
            int max = ((Static) data).hp;
            if (this.hp > max) this.hp = max;
            else if (hp < 0) hp = 0;
        }
    }

    public int getColision() {
        return ((Static) data).collision;
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

    @Override
    public int getCenterX() {
        return cenX;
    }

    @Override
    public int getCenterY() {
        return cenY;
    }

    @Override
    public boolean act() {
        if (anim != null) anim.incAnim(false);
        return ((Static) data).act(this);
    }

    @Override
    public void draw(Graphics g) {
        if (anim != null) anim.play(g, x, y, flipX, flipY);
        //
        g.setColor(Color.BLUE);
        g.drawRect(x - cenX, y - cenY, data.width, data.height);
    }

    @Override
    public void pool(Loader loader) {
        data = null;
    }
    
}
