package art.soft.level;

import art.soft.Loader;
import art.soft.gameObjs.gameObj;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Layer extends gameObj {

    public boolean visible = true;
    
    public float paralaxX = 1f, paralaxY = 1f;

    public int xOffs, yOffs;

    private int x, y;

    public objPos objsPos;
    
    public LayerObj objs;

    public Loader loader;

    private objPos curObj;
    
    // Json data
    public objPos objects[];
    
    void initJson() {
        objPos last = null;
        for (objPos obj : objects) {
            if (last == null) {
                objsPos = last = obj;
            } else {
                last.next = obj;
                last = obj;
            }
            obj.x *= paralaxX;
            obj.y *= paralaxY;
        }
    }
    // Json end

    @Override
    public void init(Loader loader) {
        this.loader = loader;
        xOffs = yOffs = 0;
        curObj = objsPos;
    }

    @Override
    public boolean act() {
        gameEngine engine = loader.engine;
        x = (int) ((engine.camX + xOffs) * paralaxX) - (loader.game.width >> 1);
        y = (int) ((engine.camY + yOffs) * paralaxY) - (loader.game.height >> 1);
        //
        Camera camera = engine.curCamera;
        while (curObj != null && camera.contain(curObj.data,
                curObj.data.getCenterX(), curObj.data.getCenterY(),
                curObj.x, curObj.y, x, y, camera.add_mask)) {
            curObj.addObj(loader, this);
            curObj = curObj.next;
        }
        //
        gameObj pred = null;
        gameObj nextObj;
        LayerObj obj = objs;
        while (obj != null) {
            nextObj = obj.next;
            if (!camera.contain(obj.data, obj.getCenterX(), obj.getCenterY(),
                    obj.x, obj.y, x, y, camera.del_mask)
                    || obj.act()) {
                if (pred == null) objs = (Decoration) nextObj;
                else pred.next = nextObj;
                obj.pool(loader);
            } else {
                pred = obj;
            }
            obj = (Decoration) nextObj;
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        if (visible) {
            g.translate(- x, - y);
            //
            LayerObj obj = objs;
            while (obj != null) {
                obj.draw(g);
                obj = (Decoration) obj.next;
            }
            //
            g.translate(x, y);
        }
    }

    public void add(LayerObj obj) {
        obj.next = objs;
        objs = obj;
    }

    /*
     * Удаляет все добавленные обьекты
     */
    public void removeAll() {
        loader.poolAllObj(objs);
        objs = null;
    }

    @Override
    public void pool(Loader loader) {
        removeAll();
        curObj = objsPos = null;
        loader = null;
    }
}
