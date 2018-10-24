package art.soft.objects;

import art.soft.Loader;
import art.soft.animation.Animation;
import art.soft.level.Layer;
import art.soft.level.bulletPos;
import art.soft.objsData.DamageRegion;
import art.soft.objsData.FrameData;
import art.soft.objsData.MultiData;
import art.soft.objsData.ObjData;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class InteractiveObj extends StaticObj {

    protected MultiData mData;
    
    protected FrameData curFrame;
    protected int count, status;

    @Override
    public void init(Loader loader, ObjData data, ObjData drop) {
        mData = (MultiData) data;
        status = mData.startStatus;
        FrameData frame = mData.frames[mData.startFrame];
        super.init(loader, mData.forms[frame.form], drop);
        setFrame(mData.startFrame);
    }

    public void setData(MultiData mData) {
        this.mData = mData;
        setStatus(status);
    }

    public void setStatus(int status) {
        if (this.status != status) {
            setFrame(mData.frameStatus[status]);
            this.status = status;
        }
    }

    public int getStatus() {
        return status;
    }

    private void setFrame(int frameNum) {
        FrameData frame = mData.frames[frameNum];
        if (frame != curFrame) {
            curFrame = frame;
            data = mData.forms[frame.form];
            if (frame.anims != null) setAnim(frame.anims);
        }
        count = frame.count;
    }

    private void setAnim(short[] anims) {
        Animation animation = anim;
        Loader loader = mData.loader;
        int len = anims.length - 1;
        for (int i = 0; i <= len;  i++) {
            int a = anims[i];
            animation.setAnimAndSet(mData.animsData[(a >> 8) & 0xFF], a & 0xFF);
            if (i < len) {
                if (animation.next == null) {
                    animation.next = loader.getAnimation();
                }
                animation = animation.next;
            } else {
                if (animation.next != null) {
                    animation.next.pool(loader);
                    animation.next = null;
                }
            }
        }
    }

    @Override
    public void setDeathAnim() {
        setStatus(mData.deathStatus);
    }

    @Override
    public boolean act(Layer layer) {
        super.act(layer);
        //
        if (curFrame.bullets != null) {
            for (bulletPos bullet : curFrame.bullets) {
                bullet.addObj(data.loader, this, layer);
            }
        }
        //
        if (curFrame.dmgRegs != null) {
            StaticObj obj = (StaticObj) layer.objs;
            while (obj != null) {
                if (obj != this) dealsDamage(obj);
                obj = (StaticObj) obj.next;
            }
        }
        //
        if (count > 0) count --;
        else setFrame(curFrame.next);
        //
        if (hp == 0) {
            setStatus(mData.deathStatus);
            return curFrame == mData.frames[curFrame.next];
        }
        return false;
    }

    protected void dealsDamage(StaticObj obj) {
        for (DamageRegion region : curFrame.dmgRegs) {
            if ((obj.getColision() & region.mask) != 0) {
                int w = region.w;
                int h = region.h;
                int x1 = flipX ? x - region.x - w : x + region.x;
                int y1 = flipY ? y - region.y - h : y + region.y;
                if (obj.contain(x1, y1, w, h)) {
                    obj.addForce(flipX ? - region.forceX : region.forceX,
                            flipY ? - region.forceY : region.forceY);
                    obj.addHP(region.damage);
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        //
        if (data.loader.debugDamageRegion) {
            g.setColor(Color.YELLOW);
            if (curFrame.dmgRegs != null) {
                for (DamageRegion region : curFrame.dmgRegs) {
                    int w = region.w;
                    int h = region.h;
                    int x1 = flipX ? x - region.x - w : x + region.x;
                    int y1 = flipY ? y - region.y - h : y + region.y;
                    g.drawRect(x1, y1, w, h);
                }
            }
        }
    }

    @Override
    public void pool(Loader loader) {
        super.pool(loader);
        mData = null;
    }
}
