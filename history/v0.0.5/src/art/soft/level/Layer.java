package art.soft.level;

import art.soft.Loader;
import art.soft.gameObjs.gameObj;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class Layer extends gameObj {

    public boolean visible = true;
    private boolean setVisible;
    
    public float paralaxX = 1f, paralaxY = 1f;

    public int xOffs, yOffs;

    public int x, y;

    public objPos objsPos;
    
    public LayerObj objs;

    public Loader loader;

    private objPos curObj;
    
    // Json data
    public boolean autoParalax = true;

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
            if (autoParalax) {
                obj.x *= paralaxX;
                obj.y *= paralaxY;
            }
        }
    }

    public void writeLayer(DataOutputStream dos) throws IOException {
        dos.writeBoolean(visible);
        dos.writeFloat(paralaxX);
        dos.writeFloat(paralaxY);
        dos.writeShort(objects.length);
        for (objPos obj : objects) {
            obj.writeObjPos(dos, loader);
        }
    }
    // Json end

    public void readLayer(DataInputStream dis, Loader loader) throws IOException {
        this.loader = loader;
        visible = dis.readBoolean();
        paralaxX = dis.readFloat();
        paralaxY = dis.readFloat();
        int n = dis.readUnsignedShort();
        objPos last = null, obj;
        for (n --; n >= 0; n --) {
            obj = new objPos();
            obj.readObjPos(dis, loader);
            //
            if (last == null) {
                objsPos = last = obj;
            } else {
                last.next = obj;
                last = obj;
            }
        }
        curObj = objsPos;
    }

    @Override
    public void init(Loader loader) {
        this.loader = loader;
        xOffs = yOffs = 0;
        curObj = objsPos;
        setVisible = visible;
        removeAll();
    }

    public boolean isVisible() {
        return setVisible;
    }

    public void setVisible(boolean visible) {
        setVisible = visible;
    }

    @Override
    public boolean act() {
        if (curObj != null || objs!= null) {
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
                        || obj.act(this)) {
                    if (pred == null) objs = (LayerObj) nextObj;
                    else pred.next = nextObj;
                    obj.pool(loader);
                } else {
                    pred = obj;
                }
                obj = (LayerObj) nextObj;
            }
        } else setVisible = false;
        return false;
    }

    @Override
    public void draw(Graphics g) {
        if (setVisible) {
            g.translate(- x, - y);
            //
            LayerObj obj = objs;
            while (obj != null) {
                obj.draw(g);
                obj = (LayerObj) obj.next;
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
