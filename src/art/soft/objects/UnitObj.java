package art.soft.objects;

import art.soft.Loader;
import art.soft.animation.Animation;
import art.soft.level.Layer;
import art.soft.level.Player;
import art.soft.level.bulletPos;
import art.soft.objsData.DamageRegion;
import art.soft.objsData.FrameData;
import art.soft.objsData.UnitData;
import art.soft.objsData.ObjData;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class UnitObj extends DynamicObj {

    public UnitData mData;
    
    public Player owner;

    protected FrameData curFrame;
    protected int count, status;
    protected boolean jump;

    @Override
    public void init(ObjData data, ObjData drop) {
        jump = false;
        mData = (UnitData) data;
        status = mData.startStatus;
        FrameData frame = mData.frames[mData.startFrame];
        super.init(mData.forms[frame.form], drop);
        anim = Loader.getLoader().getAnimation();
        setFrame(mData.startFrame);
    }

    public void setData(UnitData mData) {
        this.mData = mData;
        if (owner != null) {
            status = owner.getStatus();
        } else status = 0;
        setStatusImm(status);
    }

    public void setStatus(int status) {
        if (!onGround) status += mData.inAirStatus;
        if (this.status != status) {
            setFrame(mData.frameStatus[status]);
            this.status = status;
        }
    }

    public void setStatusImm(int status) {
        if (!onGround) status += mData.inAirStatus;
        setFrame(mData.frameStatus[status]);
        this.status = status;
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
        Loader loader = Loader.getLoader();
        int len = anims.length - 1;
        for (int i = 0; i <= len; i ++) {
            int a = anims[i];
            animation.setAnimAndSet(mData.animsData[(a >> 8) & 0xFF], a & 0xFF);
            if (i != len) {
                if (animation.next == null) {
                    animation.next = loader.getAnimation();
                }
                animation = animation.next;
            } else {
                if (animation.next != null) {
                    animation.next.pool();
                    animation.next = null;
                }
            }
        }
    }

    @Override
    public void setDeathAnim() {
        if (owner != null) owner.setStdSet();
        setStatus(mData.deathStatus);
    }

    @Override
    protected void modifyForce() {
        if (jump && onGround && hp != 0) {
            addForce(flipX ? - mData.jumpX : mData.jumpX, mData.jumpY);
        }
        float forceX = mData.forceX[status];
        float forceY = mData.forceY[status];
        addForce(flipX ? - forceX : forceX, Loader.getLoader().engine.flipGravity ? - forceY : forceY);
    }

    @Override
    public boolean act(Layer layer) {
        super.act(layer);
        //
        if (curFrame.bullets != null) {
            for (bulletPos bullet : curFrame.bullets) {
                bullet.addObj(this, layer);
            }
            if (owner != null && owner.item != null) {
                owner.item.rangeFire(curFrame.bullets.length);
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
        int stat = status % mData.inAirStatus;
        setStatus(stat);
        return false;
    }

    @Override
    protected void dealsDamage(StaticObj obj) {
        if (curFrame.dmgRegs != null) {
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
            if (owner != null && owner.item != null) {
                owner.item.meleeFire(curFrame.dmgRegs.length);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        //
        if (Loader.getLoader().debugDamageRegion) {
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

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    @Override
    public void pool() {
        super.pool();
        mData = null;
    }
}
