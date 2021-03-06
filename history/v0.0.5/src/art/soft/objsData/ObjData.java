package art.soft.objsData;

import art.soft.Loader;
import art.soft.level.Layer;
import art.soft.level.LayerObj;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public abstract class ObjData {

        // *** добовлять только в конец массива ***
    private final static Class<?> TYPE_CODE[] = {
        Decor.class,
        PolyModel.class,
        Static.class,
        MultiData.class,
        Dynamic.class,
        UnitData.class,
        Bullet.class,
        ItemData.class
    };

    private final static String OBJS_PATH = "objects/";

    private final static String OBJECTS_CLASS_PATH = "art.soft.objects.";

    public boolean isItem = false;

    public String name;

    public Loader loader;

    public Class<?> typeObj;

    public int cenX, cenY, width, height;

    public static ObjData loadObj(Loader loader, String name) {
        //loader.game.log("Load object - " + name);
        DataInputStream dis = loader.openFile(OBJS_PATH + name + ".obj");
        ObjData object = null;
        try {
            int type = dis.readUnsignedByte();
            try {
                object = (ObjData) TYPE_CODE[type].newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                loader.game.log("Cann't create object (" + name + ") from class:\n" + ex.getMessage());
            }
            if (object != null) {
                object.name = name;
                object.readData(loader, dis);
            }
            dis.close();
        } catch (IOException ex) {
            loader.game.log("Cann't load object (" + name + ") :\n" + ex.getMessage());
        }
        return object;
    }

    protected void readData(Loader loader, DataInputStream dis) throws IOException {
        this.loader = loader;
        String type = dis.readUTF();
        if (type.length() != 0) {
            try {
                typeObj = loader.cl.loadClass(OBJECTS_CLASS_PATH + type);
            } catch (ClassNotFoundException ex) {
                loader.game.log("Cann't load class:\n" + ex.getMessage());
            }
        }
        cenX = dis.readShort();
        cenY = dis.readShort();
        width = dis.readUnsignedShort();
        height = dis.readUnsignedShort();
    }

    // Json data
    private static final String OBJECT_FULL_PATH = "G:/java/Projects/CodeNameGame/data/data/objects/";
    public String type;

    public void loadJson(Loader loader) {
        this.loader = loader;
        //
        if (type != null) {
            try {
                typeObj = loader.cl.loadClass(OBJECTS_CLASS_PATH + type);
            } catch (ClassNotFoundException ex) {
                loader.game.log("Cann't load class:\n" + ex.getMessage());
            }
        }
    }

    public void saveToBin() {
        File file = new File(OBJECT_FULL_PATH + name + ".obj");
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));
            //
            int type = 0;
            Class<? extends ObjData> thisClass = this.getClass();
            for (type = TYPE_CODE.length - 1; type >= 0; type --) {
                if (thisClass.equals(TYPE_CODE[type])) break;
            }
            dos.writeByte(type);
            //
            writeData(dos);
            dos.close();
        } catch (IOException ex) {
            loader.game.log("Cann't save obj file:\n" + ex.getMessage());
        }
    }

    protected void writeData(DataOutputStream dos) throws IOException {
        dos.writeUTF(type != null ? type : "");
        dos.writeShort(cenX);
        dos.writeShort(cenY);
        dos.writeShort(width);
        dos.writeShort(height);
    }
    // Json end

    public LayerObj createObj(Loader loader, Layer layer, int x, int y, ObjData drop) {
        LayerObj obj = (LayerObj) loader.getObjTyped(typeObj);
        if (obj != null) {
            obj.init(loader, this, drop);
            obj.setPos(x, y);
            layer.add(obj);
        } else
            loader.game.log("Cann't create object " + typeObj);
        return obj;
    }

    public int getCenterX() {
        return cenX;
    }

    public int getCenterY() {
        return cenY;
    }

    public abstract void remove();
}
