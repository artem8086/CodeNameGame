package art.soft.objsData;

import art.soft.Loader;
import art.soft.level.bulletPos;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Артём Святоха
 */
public class FrameData {

    public int form;
    
    public int count = 0;

    public int next = -1;
    
    public short anims[];

    public DamageRegion dmgRegs[];

    public bulletPos bullets[];
    
    // Json data
    public String indx;

    public void loadJson() {
        if (bullets != null) {
            for (bulletPos bullet : bullets) {
                bullet.loadJson();
            }
        }
    }

    public void writeFrame(DataOutputStream dos, ArrayList<String> bulls) throws IOException {
        dos.writeByte(form);
        dos.writeShort(count);
        dos.writeShort(next);
        //
        if (anims != null) {
            dos.writeByte(anims.length);
            for (short anim : anims) {
                dos.writeShort(anim);
            }
        } else dos.writeByte(0);
        //
        if (dmgRegs != null) {
            dos.writeByte(dmgRegs.length);
            for (DamageRegion dmgReg : dmgRegs) {
                dmgReg.writeDmgReg(dos);
            }
        } else dos.writeByte(0);
        //
        if (bullets != null) {
            dos.writeByte(bullets.length);
            for (bulletPos bullet : bullets) {
                bullet.writeBulletPos(dos, bulls);
            }
        } else dos.writeByte(0);
    }
    // Json end

    public void readFrame(DataInputStream dis, ObjData bulls[]) throws IOException {
        form = dis.readUnsignedByte();
        count = dis.readUnsignedShort();
        next = dis.readUnsignedShort();
        //
        int i;
        int n = dis.readUnsignedByte();
        anims = new short[n];
        for (i = 0; i < n; i ++) {
            anims[i] = dis.readShort();
        }
        //
        n = dis.readUnsignedByte();
        if (n != 0) {
            dmgRegs = new DamageRegion[n];
            for (i = 0; i < n; i ++) {
                dmgRegs[i] = new DamageRegion();
                dmgRegs[i].readDmgReg(dis);
            }
        }
        //
        n = dis.readUnsignedByte();
        if (n != 0) {
            bullets = new bulletPos[n];
            for (i = 0; i < n; i ++) {
                bullets[i] = new bulletPos();
                bullets[i].readBulletPos(dis, bulls);
            }
        }
    }
}
