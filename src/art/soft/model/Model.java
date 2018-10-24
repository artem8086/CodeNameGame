package art.soft.model;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class Model {

    static int[] xVerts = new int[0], yVerts = new int[0];

    public int loadIndx;

    private String path;
    
    public AnimSet animSets[];

    short[] x, y;
    float z[];

    Polygon[] polygons;

    public Model(String file) {
        Loader loader = Loader.getLoader();
        path = file;
        DataInputStream is = loader.openFile(file + ".vec");
        try {
            int n = is.readUnsignedByte();
            if (n != 0) {
                animSets = new AnimSet[n];
                for (int i = 0; i < n; i ++) {
                    animSets[i] = loader.loadAnimation(is.readUTF());
                }
            }
            //
            n = is.readUnsignedShort();
            if (n > xVerts.length) {
                xVerts = new int[n];
                yVerts = new int[n];
            }
            x = new short[n];
            y = new short[n];
            z = new float[n];
            for (int i = 0; i < n; i ++) {
                x[i] = is.readShort();
                y[i] = is.readShort();
                z[i] = is.readFloat();
            }
            n = is.readUnsignedShort();
            polygons = new Polygon[n];
            for (int i = 0; i < n; i ++) {
                int vn = is.readUnsignedByte();
                if (vn != 0) {
                    int color = is.readInt();
                    Polygon poly = new Polygon(color);
                    polygons[i] = poly;
                    poly.verts = new short[vn];
                    for (int j = 0; j < vn; j ++) {
                        poly.verts[j] = is.readShort();
                    }
                } else {
                    //float paralax = is.readFloat();
                    int anim = is.readUnsignedByte();
                    int frame = is.readUnsignedByte();
                    int vert = is.readUnsignedShort();
                    Texture tex = new Texture(loader, animSets[anim], frame, vert);
                    polygons[i] = tex;
                }
            }
            is.close();
        } catch (IOException ex) {
            loader.game.log("Error while loading vector model file!");
            loader.game.log(ex.getMessage());
        }
    }

    public void draw(Graphics g, int x, int y, float zoom, int xl, int yl) {
        x -= xl; y -= yl;
        // Расчет вершин
        float z;
        for (int i = this.x.length - 1; i >= 0; i --) {
            z = this.z[i] * zoom;
            xVerts[i] = (int) ((this.x[i] + x) * z) + xl;
            yVerts[i] = (int) ((this.y[i] + y) * z) + yl;
        }
        //
        for (Polygon poly : polygons) {
            poly.draw(g);
        }
    }

    public void reset() {
        if (animSets != null) {
            for (Polygon poly : polygons) {
                poly.reset();
            }
        }
    }

    public void remove() {
        Loader.getLoader().removeModel(path);
    }
}
