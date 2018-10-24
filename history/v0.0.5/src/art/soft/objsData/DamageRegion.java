package art.soft.objsData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class DamageRegion {

    public int x, y, w, h;

    public int damage, mask;

    public float forceX, forceY;

    // Json data
    public void writeDmgReg(DataOutputStream dos) throws IOException {
        dos.writeShort(x);
        dos.writeShort(y);
        dos.writeShort(w);
        dos.writeShort(h);
        dos.writeShort(damage);
        dos.writeShort(mask);
        dos.writeFloat(forceX);
        dos.writeFloat(forceY);
    }
    // Json end

    public void readDmgReg(DataInputStream dis) throws IOException {
        x = dis.readShort();
        y = dis.readShort();
        w = dis.readUnsignedShort();
        h = dis.readUnsignedShort();
        damage = dis.readShort();
        mask = dis.readUnsignedShort();
        forceX = dis.readFloat();
        forceY = dis.readFloat();
    }
}
