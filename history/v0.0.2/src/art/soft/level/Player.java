package art.soft.level;

import art.soft.Loader;
import static art.soft.Settings.PLAYER_JUMP;
import static art.soft.Settings.PLAYER_LEFT;
import static art.soft.Settings.PLAYER_RIGHT;

/**
 *
 * @author Артём Святоха
 */
public class Player {

    private static final int STATUS_STAND = 0;

    private static final int STATUS_UP = 1;
    private static final int STATUS_DOWN = 2;
    private static final int STATUS_FIRE = 3;
    private static final int STATUS_RUN = 6;

    private static final int MASK_UP =   0x01;
    private static final int MASK_DOWN = 0x02;
    private static final int MASK_RUN =  0x04;
    private static final int MASK_FIRE = 0x08;

    private static final int[] MASK_KEYS = {
        MASK_UP, MASK_DOWN, MASK_RUN, MASK_RUN, MASK_FIRE
    };

    private int curMask;

    public Loader loader;

    public int layer;

    public objPos pos;

    public UnitObj unit;    

    private int[] keys;
    
    private boolean canControl;

    // Json data
    public void jsonInit(Layer layers[]) {
        pos.data = loader.engine.getObj(pos.name);
        unit = (UnitObj) pos.addObj(loader, layers[layer]);
    }
    // Json end

    public void init(Loader loader, int[] keys) {
        this.loader = loader;
        this.keys = keys;
        canControl = true;
        //
        //pos.addObj(loader, );
    }

    public void setCanControl(boolean control) {
        canControl = true;
        curMask = 0;
        unit.setStatus(STATUS_STAND);
    }

    public boolean getCanControl() {
        return canControl;
    }

    private int mask2status(int mask) {
        int status = STATUS_STAND;
        if ((mask & MASK_FIRE) != 0) status += STATUS_FIRE;
        if ((mask & MASK_RUN) != 0) status += STATUS_RUN;
        if ((mask & MASK_DOWN) != 0) status += STATUS_DOWN;
        else if ((mask & MASK_UP) != 0) status += STATUS_UP;
        return status;
    }

    /*
     * Вызывается при нажатии клавиши
     * @key код клавиши
     */
    public void keyPressed(int key) {
        if (canControl) {
            int newMask = curMask;
            for (int i = MASK_KEYS.length - 1; i >= 0; i --) {
                if (key == keys[i]) {
                    newMask |= MASK_KEYS[i];
                    break;
                }
            }
            if (newMask != curMask) {
                if (key == keys[PLAYER_LEFT]) unit.setFlipX(true);
                else if (key == keys[PLAYER_RIGHT]) unit.setFlipX(false);
                curMask = newMask;
                unit.setStatus(mask2status(newMask));
            }
            if (key == keys[PLAYER_JUMP]) unit.setJump(true);
        }
    }

    /*
     * Вызывается при отпускании клавиши
     * @key код клавиши
     */
    public void keyReleased(int key) {
        if (canControl) {
            /*if (key == keys[PLAYER_TAKE]) {
            
            }*/
            int newMask = curMask;
            for (int i = MASK_KEYS.length - 1; i >= 0; i --) {
                if (key == keys[i]) {
                    newMask &= ~ MASK_KEYS[i];
                    break;
                }
            }
            if (newMask != curMask) {
                curMask = newMask;
                unit.setStatus(mask2status(newMask));
            }
            if (key == keys[PLAYER_JUMP]) unit.setJump(false);
        }
    }
}
