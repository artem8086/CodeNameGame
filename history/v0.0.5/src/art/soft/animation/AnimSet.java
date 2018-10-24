package art.soft.animation;

import art.soft.Loader;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class AnimSet {

    public int loadIndx;

    private String path;
    
    Image animPack;
    
    AnimData data[];

    public AnimSet() {}

    public AnimSet(Loader loader, Image img, String path) {
        this.path = path;
        animPack = img;
        
        DataInputStream dis = loader.openFile(path + ".pak");
        try {
            int num = dis.readUnsignedByte();
            data = new AnimData[num];
            for (int i = 0; i < num; i ++) {
                AnimData aData = data[i] = new AnimData();
                aData.frameTime = dis.readUnsignedByte();
                int nFrames = dis.readShort();
                aData.frames = new AnimFrame[nFrames];
                for (int j = 0; j < nFrames; j ++) {
                    AnimFrame frame = aData.frames[j] = new AnimFrame();
                    frame.x1 = dis.readShort();
                    frame.y1 = dis.readShort();
                    frame.x2 = dis.readShort();
                    frame.y2 = dis.readShort();
                    frame.centerX = dis.readShort();
                    frame.centerY = dis.readShort();
                    frame.width = dis.readShort();
                    frame.height = dis.readShort();
                    if (frame.width <= 0 || frame.height <= 0) {
                        aData.frames[j] = null;
                    }
                }
            }
            dis.close();
        } catch (IOException ex) {
            loader.game.log("Error while read from settings file!");
            loader.game.log(ex.getMessage());
        }
    }

    public void remove(Loader loader) {
        loader.removeAnim(path);
    }
}
