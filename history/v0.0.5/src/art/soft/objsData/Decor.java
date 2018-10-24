package art.soft.objsData;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import art.soft.objects.Decoration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class Decor extends ObjData {

    public AnimSet animData;
    public int anim;

    public Decor() {
        typeObj = Decoration.class;
    }

    // Json data
    public String animation;
    
    @Override
    public void loadJson(Loader loader) {
        super.loadJson(loader);
        if (animation != null) {
            animData = loader.loadAnimation(animation);
        }
    }

    @Override
    protected void writeData(DataOutputStream dos) throws IOException {
        super.writeData(dos);
        if (animation != null) {
            dos.writeUTF(animation);
            dos.writeByte(anim);
        } else dos.writeUTF("");
    }
    // Json end

    @Override
    protected void readData(Loader loader, DataInputStream dis) throws IOException {
        super.readData(loader, dis);
        String animName = dis.readUTF();
        // Load
        if (animName.length() != 0) {
            anim = dis.readUnsignedByte();
            //loader.game.log("Load decor (" + name + ") anim = " + animName);
            animData = loader.loadAnimation(animName);
        }
    }

    @Override
    public void remove() {
        animData.remove(loader);
    }
}
