package art.soft.level;

import art.soft.Loader;
import art.soft.objects.FrameData;
import art.soft.objects.MultiData;
import art.soft.objects.ObjData;

/**
 *
 * @author Артём Святоха
 */
public class UnitObj extends DynamicObj {

    protected MultiData mData;
    
    protected FrameData curFrame;
    protected int count, status, oldStatus;
    protected boolean jump;

    @Override
    public void init(Loader loader, ObjData data, Layer layer) {
        jump = false;
        mData = (MultiData) data;
        oldStatus = status = mData.startStatus;
        setFrame(mData.startFrame);
        super.init(loader, this.data, layer);
    }

    public void setStatus(int status) {
        if (!onGround) status += mData.inAirStatus;
        if (this.status != status) {
            oldStatus = this.status;
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
            setAnim(frame.anims);
        }
        count = frame.count;
    }

    private void setAnim(int[] anims) {
        
    }

    @Override
    public boolean act() {
        if (jump && onGround) {
            addForce(flipX ? - mData.jumpX : mData.jumpX, mData.jumpY);
        }
        addForce(flipX ? - curFrame.forceX : curFrame.forceX, curFrame.forceY);
        boolean del = super.act();
        //
        if (count > 0) count --;
        else setFrame(curFrame.next);
        //
        int stat = status % mData.inAirStatus;
        setStatus(stat);
        return del;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
