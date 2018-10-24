package art.soft.level;

import art.soft.Loader;
import art.soft.gameObjs.gameObj;
import art.soft.objsData.ObjData;

/**
 *
 * @author Артём Святоха
 */
public abstract class LayerObj extends gameObj {

    public ObjData data;
    public int x, y;

    @Override
    public boolean act() {
        return false;
    }

    public abstract boolean act(Layer layer);

    @Override
    public void init() {}

    public abstract void init(ObjData data, ObjData drop);

    public int getCenterX() {
        return data.cenX;
    }

    public int getCenterY() {
        return data.cenY;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void pool() {
        data = null;
    }
}
