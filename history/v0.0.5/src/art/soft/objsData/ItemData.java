package art.soft.objsData;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import art.soft.objects.items.Item;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class ItemData extends Dynamic {

    public String setName;
    
    public AnimSet icon;

    public int ammo, iconAnim;

    public ItemData() {
        typeObj = Item.class;
        isItem = true;
    }

    // Json data
    public String iconName;
    
    @Override
    public void loadJson(Loader loader) {
        super.loadJson(loader);
        if (iconName != null) {
            icon = loader.loadAnimation(iconName);
        }
    }

    @Override
    protected void writeData(DataOutputStream dos) throws IOException {
        super.writeData(dos);
        dos.writeUTF(setName != null ? setName : "");
        if (iconName != null) {
            dos.writeUTF(iconName);
            dos.writeByte(iconAnim);
        } else dos.writeUTF("");
        dos.writeShort(ammo);
    }
    // Json end

    @Override
    protected void readData(Loader loader, DataInputStream dis) throws IOException {
        super.readData(loader, dis);
        String set = dis.readUTF();
        setName = set.length() != 0 ? set : null;
        //
        String iconPath = dis.readUTF();
        // Load
        if (iconPath.length() > 0) {
            iconAnim = dis.readUnsignedByte();
            icon = loader.loadAnimation(iconPath);
        }
        ammo = dis.readUnsignedShort();
        if (ammo == 0xFFFF) ammo = -1;
    }
}
