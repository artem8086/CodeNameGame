package art.soft.gameObjs.gui;

import art.soft.gameObjs.GameKeyListener;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class Button implements GameKeyListener {

    static final Color SELECT_COLOR = Color.WHITE;
    static final Color PRESSED_COLOR = Color.YELLOW;
    static final Color STANDART_COLOR = Color.LIGHT_GRAY;

    int x, y;
    String text;
    ButtonMenu menu;
    boolean press = false;
    ButtonListener listener;

    public Button(ButtonMenu menu, String text, int x, int y) {
        this.menu = menu;
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public Button(ButtonMenu menu, String text, int x, int y, ButtonListener listener) {
        this.listener = listener;
        this.menu = menu;
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public void setListener(ButtonListener listener) {
        this.listener = listener;
    }

    public void draw(Graphics g, boolean sel) {
        if (press) {
            g.setColor(PRESSED_COLOR);
            g.drawString(text, x + 2, y + 2);
        } else {
            g.setColor(sel ? SELECT_COLOR : STANDART_COLOR);
            g.drawString(text, x, y);
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        press = true;
    }

    @Override
    public void keyReleased(int keyCode) {
        if (press) {
            press = false;
            if (listener != null) {
                listener.pressed(this);
            }
        }
    }
}
