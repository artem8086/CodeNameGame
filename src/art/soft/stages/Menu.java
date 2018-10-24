package art.soft.stages;

import art.soft.Loader;
import art.soft.gameObjs.SimpleAnimation;
import art.soft.gameObjs.containers.CenterContainer;
import art.soft.gameObjs.gui.ButtonMenu;
import static art.soft.gameObjs.gui.ButtonMenu.MENU_START;
import art.soft.gameObjs.gui.SimpleText;
import java.awt.Color;

/**
 *
 * @author Артём Святоха
 */
public class Menu extends Stage {

    public boolean load = false;

    @Override
    public void init() {
        Loader loader = Loader.getLoader();
        if (!load) {
            loader.menu = this;
            CenterContainer center = (CenterContainer) loader.getObj(CenterContainer.class);
            add(center);
            //
            ButtonMenu menu = new ButtonMenu();
            loader.buttonMenu = menu;
            menu.load();
            setKeyListener(menu);
            center.add(menu);
            //
            center.setBacground(Color.GRAY);
            SimpleAnimation artsoft = (SimpleAnimation) loader.getObj(SimpleAnimation.class);
            artsoft.setAnimSet(loader.loadAnimation("artsoft"));
            artsoft.setCycle(false);
            artsoft.setPos(400, 220);
            artsoft.setAnimNum(1);
            center.add(artsoft);
            //
            SimpleText text = (SimpleText) loader.getObj(SimpleText.class);
            text.setColor(Color.WHITE);
            text.setText("Автор: Святоха Артём", 100, 580);
            center.add(text);
            //
            text = (SimpleText) loader.getObj(SimpleText.class);
            text.setColor(Color.WHITE);
            text.setText("ArtSoft© год 2к16 - 2017", 100, 610);
            center.add(text);
            //
            SimpleAnimation plus18 = (SimpleAnimation) loader.getObj(SimpleAnimation.class);
            plus18.setAnimSet(loader.loadAnimation("plus18"));
            plus18.setPos(700, 560);
            plus18.setAnimNum(0);
            center.add(plus18);
            //
            load = true;
        }
        loader.buttonMenu.setMenu(MENU_START);
        loader.buttonMenu.toMainMenu.setMenu(MENU_START);
    }
}
