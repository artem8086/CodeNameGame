package art.soft.model;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Polygon {

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
        int indx;
        for (int i = n - 1; i >= 0; i --) {
            indx = (int) verts[i] & 0xFFFF;
            Model.xPoly[i] = Model.xVerts[indx];
            Model.yPoly[i] = Model.yVerts[indx];
        }
        if (n > 2) {
            g.fillPolygon(Model.xPoly, Model.yPoly, n);
        } else
        if (n == 2) {
            g.drawLine(Model.xPoly[0], Model.yPoly[0], Model.xPoly[1], Model.yPoly[1]);
        } else {
            g.fillRect(Model.xPoly[0], Model.yPoly[0], 1, 1);
        }
    }
}
