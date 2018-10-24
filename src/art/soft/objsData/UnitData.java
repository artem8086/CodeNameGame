package art.soft.objsData;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import art.soft.level.bulletPos;
import art.soft.objects.UnitObj;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Артём Святоха
 */
public class UnitData extends ObjData {

    public String setName;

    public AnimSet animsData[];

    public Dynamic forms[];

    public FrameData frames[];

    public int inAirStatus = 13;

    public int startFrame, startStatus, deathStatus;

    public float jumpX, jumpY;

    public short frameStatus[];

    public float[] forceX, forceY;

    public UnitData() {
        typeObj = UnitObj.class;
    }

    // Json data
    public String anims[];
    
    @Override
    public void loadJson() {
        super.loadJson();
        int i = anims.length;
        animsData = new AnimSet[i];
        for (i --; i >= 0; i --) {
            animsData[i] = Loader.getLoader().loadAnimation(anims[i]);
        }
        i = 0;
        for (FrameData frame : frames) {
            i ++;
            if (frame.next < 0) frame.next = i;
            frame.loadJson();
        }
    }

    @Override
    protected void writeData(DataOutputStream dos) throws IOException {
        super.writeData(dos);
        dos.writeUTF(setName != null ? setName : "");
        //
        dos.writeByte(anims.length);
        for (String anim : anims) {
            dos.writeUTF(anim);
        }
        //
        dos.writeByte(forms.length);
        for (Dynamic form : forms) {
            form.writeData(dos);
        }
        //
        ArrayList<String> bullets = new ArrayList<>();
        for (FrameData frame : frames) {
            if (frame.bullets != null) {
                for (bulletPos bullet : frame.bullets) {
                    if (!bullets.contains(bullet.name)) {
                        bullets.add(bullet.name);
                    }
                }
            }
        }
        dos.writeByte(bullets.size());
        for (int i = 0; i < bullets.size(); i ++) {
             dos.writeUTF(bullets.get(i));
        }
        //
        dos.writeShort(frames.length);
        for (FrameData frame : frames) {
            frame.writeFrame(dos, bullets);
        }
        //
        dos.writeShort(inAirStatus);
        dos.writeShort(startFrame);
        dos.writeShort(startStatus);
        dos.writeShort(deathStatus);
        dos.writeFloat(jumpX);
        dos.writeFloat(jumpY);
        //
        dos.writeShort(frameStatus.length);
        for (short status : frameStatus) {
            dos.writeShort(status);
        }
        //
        dos.writeShort(forceX.length);
        for (int i = 0; i < forceX.length; i ++) {
            dos.writeFloat(forceX[i]);
            dos.writeFloat(forceY[i]);
        }
    }
    // Json end

    @Override
    protected void readData(DataInputStream dis) throws IOException {
        super.readData(dis);
        setName = dis.readUTF();
        if (setName.length() == 0) setName = null;
        //
        int i;
        int n = dis.readUnsignedByte();
        animsData = new AnimSet[n];
        for (i = 0; i < n; i ++) {
            animsData[i] = Loader.getLoader().loadAnimation(dis.readUTF());
        }
        //
        n = dis.readUnsignedByte();
        forms = new Dynamic[n];
        for (i = 0; i < n; i ++) {
            forms[i] = new Dynamic();
            forms[i].readData(dis);
        }
        //
        n = dis.readUnsignedByte();
        ObjData bullets[] = new ObjData[n];
        for (i = 0; i < n; i ++) {
            bullets[i] = Loader.getLoader().engine.getObj(dis.readUTF());
        }
        //
        n = dis.readUnsignedShort();
        frames = new FrameData[n];
        for (i = 0; i < n; i ++) {
            frames[i] = new FrameData();
            frames[i].readFrame(dis, bullets);
        }
        //
        inAirStatus = dis.readUnsignedShort();
        startFrame = dis.readUnsignedShort();
        startStatus = dis.readUnsignedShort();
        deathStatus = dis.readUnsignedShort();
        jumpX = dis.readFloat();
        jumpY = dis.readFloat();
        //
        n = dis.readUnsignedShort();
        frameStatus = new short[n];
        for (i = 0; i < n; i ++) {
            frameStatus[i] = dis.readShort();
        }
        //
        n = dis.readUnsignedShort();
        forceX = new float[n];
        forceY = new float[n];
        for (i = 0; i < n; i ++) {
            forceX[i] = dis.readFloat();
            forceY[i] = dis.readFloat();
        }
    }

    @Override
    public void remove() {
        for (AnimSet anim : animsData) {
            anim.remove();
        }
    }
}
