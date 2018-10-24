package art.soft.level;

import art.soft.Loader;
import art.soft.objects.StaticObj;
import art.soft.objsData.ObjData;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Артём Святоха
 */
public class bulletPos {

    public boolean flipY = false;

    public int x, y;

    public ObjData data;

    // Json data
    public String name;

    public void loadJson() {
        data = Loader.getLoader().engine.getObj(name);
    }

    public void writeBulletPos(DataOutputStream dos, ArrayList<String> bulls) throws IOException {
        dos.writeBoolean(flipY);
        dos.writeShort(x);
        dos.writeShort(y);
        dos.writeByte(bulls.indexOf(name));
    }
    // Json end

    public void readBulletPos(DataInputStream dis, ObjData bullets[]) throws IOException {
        flipY = dis.readBoolean();
        x = dis.readShort();
        y = dis.readShort();
        data = bullets[dis.readUnsignedByte()];
    }

    public LayerObj addObj(StaticObj owner, Layer layer) {
        Loader loader = Loader.getLoader();
        StaticObj obj = (StaticObj) loader.getObj(data.typeObj);
        if (obj != null) {
            obj.init(data, null);
            obj.setFlipY(flipY);
            obj.setPos(owner.x + (owner.isFlipX() ? - x : x), owner.y + (owner.isFlipY() ? - y : y));
            if (owner.isFlipX()) obj.setFlipX(!obj.isFlipX());
            if (owner.isFlipY()) obj.setFlipY(!obj.isFlipY());
            layer.add(obj);
        } else
            loader.game.log("Cann't create object " + data.typeObj);
        return obj;
    }
}
