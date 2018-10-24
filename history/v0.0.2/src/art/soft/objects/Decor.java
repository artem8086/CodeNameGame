package art.soft.objects;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class Decor extends ObjData {

    public AnimSet animData;
    public int anim;

    // Json data
    public String animation;
    
    @Override
    public void loadJson(Loader loader) {
        super.loadJson(loader);
        if (animation != null) {
            animData = loader.loadAnimation(animation);
        }
    }
    // Json end

    @Override
    public void readData(Loader loader, DataInputStream dis) throws IOException {
        super.readData(loader, dis);
        String animName = dis.readUTF();
        anim = dis.readUnsignedByte();
        // Load
        animData = loader.loadAnimation(animName);
    }

    @Override
    public void remove() {
        animData.remove(loader);
    }
}
