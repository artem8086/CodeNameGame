package art.soft.gameObjs.gui;

import art.soft.Loader;
import art.soft.gameObjs.gameObj;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class SimpleText extends gameObj {

    private Color color;
    private String text;
    public int x, y;

    public void setColor(Color color) {
        this.color = color;
    }

    public void setText(String text, int x, int y) {
        this.x = x; this.y = y;
        this.text = text;
    }

    @Override
    public void init() {}

    @Override
    public boolean act() {
        return false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawString(text, x, y);
    }

    @Override
    public void pool() {
        color = null;
        text = null;
    }
}
