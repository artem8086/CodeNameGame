package art.soft.gameObjs.gui;

/**
 *
 * @author Артём Святоха
 */
public class MenuSelector implements ButtonListener {

    private int menu;

    public MenuSelector(int menu) {
        this.menu = menu;
    }

    public void setMenu(int menu) {
        this.menu = menu;
    }

    @Override
    public void pressed(Button button) {
        button.menu.setMenu(menu);
    }
}
