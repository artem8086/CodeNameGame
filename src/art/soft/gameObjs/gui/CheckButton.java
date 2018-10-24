package art.soft.gameObjs.gui;

import art.soft.animation.Animation;
import static art.soft.gameObjs.gui.ButtonMenu.checkAnim;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class CheckButton extends Button {

    boolean check;

    public CheckButton(ButtonMenu menu, String text, int x, int y, boolean check) {
        super(menu, text, x, y);
        this.check = check;
    }

    public CheckButton(ButtonMenu menu, String text, int x, int y,
            boolean check, ButtonListener listener) {
        super(menu, text, x, y, listener);
        this.check = check;
    }

    @Override
    public void draw(Graphics g, boolean select) {
        Animation img = checkAnim;
        img.setAnimation(check ? 1 : 0);
        img.play(g, x, y);
        x += 24;
        super.draw(g, select);
        x -= 24;
    }
}
