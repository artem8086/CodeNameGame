package art.soft.objects;

import art.soft.Loader;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public abstract class ObjData {

    public enum objType {
        decoration, static_obj, dynamic, unit_obj
    }

    public Loader loader;

    public objType type = objType.decoration;

    public int cenX, cenY, width, height;

    public void readData(Loader loader, DataInputStream dis) throws IOException {
        this.loader = loader;
        cenX = dis.readShort();
        cenY = dis.readShort();
        width = dis.readUnsignedShort();
        height = dis.readUnsignedShort();
    }

    // Json data    
    public void loadJson(Loader loader) {
        this.loader = loader;
    }
    // Json end

    public int getCenterX() {
        return cenX;
    }

    public int getCenterY() {
        return cenY;
    }

    public abstract void remove();
}
