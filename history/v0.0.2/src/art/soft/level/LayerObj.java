package art.soft.level;

import art.soft.Loader;
import art.soft.gameObjs.gameObj;
import art.soft.objects.ObjData;

/**
 *
 * @author Артём Святоха
 */
public abstract class LayerObj extends gameObj {

    public ObjData data;
    public int x, y;

    @Override
    public void init(Loader loader) {}

    public abstract void init(Loader loader, ObjData data, Layer layer);

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
}
