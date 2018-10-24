package art.soft.gameObjs.gui;

import art.soft.Loader;
import art.soft.animation.Animation;
import static art.soft.gameObjs.gui.ButtonMenu.keyAnim;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 *
 * @author Артём Святоха
 */
public class KeyButton extends Button {

    public KeyButton(ButtonMenu menu, String text, int x, int y) {
        super(menu, text, x, y);
    }

    private int[] keys;
    private int keyNum;
    private String keyName;
    private int centerX;

    public void setKeys(int[] keys, int keyNum) {
        this.keyNum = keyNum;
        this.keys = keys;
        setKey(keys[keyNum]);
    }

    public void setKey(int key) {
        keys[keyNum] = key;
        if (key != 0) {
            keyName = KeyEvent.getKeyText(key);
            centerX = (56 - Loader.getLoader().stringWidth(keyName)) >> 1;
        } else {
            keyName = "";
            centerX = 28;
        }
    }

    @Override
    public void draw(Graphics g, boolean select) {
        Animation img = keyAnim;
        img.setAnimation(menu.buttonListener == this ? 1 : 0);
        img.play(g, x, y);
        if (menu.buttonListener != this) {
            g.setColor(Color.RED);
            g.drawString(keyName, x + centerX, y);
        }
        x += 68;
        super.draw(g, select);
        x -= 68;
    }

    @Override
    public void keyReleased(int keyCode) {
        if (menu.buttonListener == this) {
            if (keyCode == KeyEvent.VK_ESCAPE) keyCode = 0;
            setKey(keyCode);
            Loader.getLoader().settings.Save();
            menu.buttonListener = null;
            menu.nextButton();
        } else {
            menu.buttonListener = this;
        }
        press = false;
    }
}
