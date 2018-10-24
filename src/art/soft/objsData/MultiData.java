package art.soft.objsData;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import art.soft.level.bulletPos;
import art.soft.objects.InteractiveObj;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Артём Святоха
 */
public class MultiData extends ObjData {

    public AnimSet animsData[];

    public Static forms[];
    
    public FrameData frames[];

    public int startFrame, startStatus, deathStatus;

    public short frameStatus[];

    public MultiData() {
        typeObj = InteractiveObj.class;
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
        dos.writeByte(anims.length);
        for (String anim : anims) {
            dos.writeUTF(anim);
        }
        //
        dos.writeByte(forms.length);
        for (Static form : forms) {
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
        dos.writeShort(startFrame);
        dos.writeShort(startStatus);
        dos.writeShort(deathStatus);
        //
        dos.writeShort(frameStatus.length);
        for (short status : frameStatus) {
            dos.writeShort(status);
        }
    }
    // Json end

    @Override
    protected void readData(DataInputStream dis) throws IOException {
        super.readData(dis);
        int i;
        int n = dis.readUnsignedByte();
        animsData = new AnimSet[n];
        for (i = 0; i < n; i ++) {
            animsData[i] = Loader.getLoader().loadAnimation(dis.readUTF());
        }
        //
        n = dis.readUnsignedByte();
        forms = new Static[n];
        for (i = 0; i < n; i ++) {
            forms[i] = new Static();
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
        startFrame = dis.readUnsignedShort();
        startStatus = dis.readUnsignedShort();
        deathStatus = dis.readUnsignedShort();
        //
        n = dis.readUnsignedShort();
        frameStatus = new short[n];
        for (i = 0; i < n; i ++) {
            frameStatus[i] = dis.readShort();
        }
    }

    @Override
    public void remove() {
        if (forms != null) {
            for (Static form : forms) {
                form.remove();
            }
        }
    }    
}
