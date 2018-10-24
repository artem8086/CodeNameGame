package art.soft.model;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Polygon {

    private final static int[] xPoly = new int[3], yPoly = new int[3];

    private Color color;
    short verts[];

    Polygon() {}

    public Polygon(int color) {
        this.color = new Color(color, true);
    }

    public void reset(){}

    public void draw(Graphics g) {
        g.setColor(color);
        int n = verts.length;
        if (n > 2) {
            int indx;
            for (int i = n - 3; i >= 0; i --) {
                indx = (int) verts[i] & 0xFFFF;
                xPoly[0] = Model.xVerts[indx];
                yPoly[0] = Model.yVerts[indx];
                indx = (int) verts[i + 1] & 0xFFFF;
                xPoly[1] = Model.xVerts[indx];
                yPoly[1] = Model.yVerts[indx];
                indx = (int) verts[i + 2] & 0xFFFF;
                xPoly[2] = Model.xVerts[indx];
                yPoly[2] = Model.yVerts[indx];
                g.fillPolygon(xPoly, yPoly, 3);
            }
            
        } else
        if (n == 2) {
            int indx1 = (int) verts[0] & 0xFFFF;
            int indx2 = (int) verts[1] & 0xFFFF;
            g.drawLine(Model.xVerts[indx1], Model.yVerts[indx1],
                    Model.xVerts[indx2], Model.yVerts[indx2]);
        } else {
            int indx = (int) verts[0] & 0xFFFF;
            g.fillRect(Model.xVerts[indx], Model.yVerts[indx], 1, 1);
        }
    }
}
