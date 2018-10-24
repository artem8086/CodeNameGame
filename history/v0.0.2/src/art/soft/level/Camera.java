package art.soft.level;

import art.soft.Loader;
import art.soft.objects.ObjData;
import java.awt.Rectangle;

/**
 *
 * @author Артём Святоха
 */
public class Camera extends Rectangle {

    public static final int MASK_UP = 0x1;
    public static final int MASK_DOWN = 0x2;
    public static final int MASK_LEFT = 0x4;
    public static final int MASK_RIGHT = 0x8;

    public int add_mask;
    public int del_mask;
    public int move_mask;
    
    private Loader loader;
    
    public Camera next;

    public void init(Loader loader) {
        this.loader = loader;
    }

    public boolean contain(ObjData data, int cenX, int cenY, int objX, int objY, int x1, int y1, int mask) {
        if ((mask & MASK_UP) != 0) {
            if (objY - cenY < y1) return false;
        }
        if ((mask & MASK_LEFT) != 0) {
            if (objX - cenX + data.width < x1) return false;
        }
        if ((mask & MASK_DOWN) != 0) {
            int y2 = loader.game.height + y1;
            if (objY + cenY - data.height > y2) return false;
        }
        if ((mask & MASK_RIGHT) != 0) {
            int x2 = loader.game.width + x1;
            if (objX + cenX - data.width > x2) return false;
        }
        return true;
    }
}
