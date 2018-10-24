package art.soft.level;

import art.soft.Loader;
import art.soft.objsData.ObjData;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class objPos {

    public int x, y;

    public ObjData data, drop;

    public objPos next;

    // Json data
    public String dropName;

    public String name;

    public void loadJson(Loader loader) {
        data = loader.engine.getObj(name);
        if (dropName != null) {
            drop = loader.engine.getObj(dropName);
        }
    }

    public void writeObjPos(DataOutputStream dos, Loader loader) throws IOException {
        gameEngine engine = loader.engine;
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeShort(engine.getIndex(name));
        dos.writeShort(engine.getIndex(dropName));
        name = dropName = null;
    }
    // Json end

    public void readObjPos(DataInputStream dis, Loader loader) throws IOException {
        gameEngine engine = loader.engine;
        x = dis.readInt();
        y = dis.readInt();
        data = engine.getObj(dis.readUnsignedShort());
        drop = engine.getObj(dis.readUnsignedShort());
    }

    public LayerObj addObj(Loader loader, Layer layer) {
        return data.createObj(loader, layer, x, y, drop);
    }
}
