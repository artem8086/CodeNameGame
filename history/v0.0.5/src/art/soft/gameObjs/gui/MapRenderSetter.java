package art.soft.gameObjs.gui;

import art.soft.Settings;

/**
 *
 * @author Артём Святоха
 */
public class MapRenderSetter implements ButtonListener {

    private int map;

    public MapRenderSetter(int map) {
        this.map = map;
    }

    @Override
    public void pressed(Button button) {
        CheckButton checkBut = (CheckButton) button;
        Settings settings = button.menu.loader.settings;
        checkBut.check ^= true;
        settings.mapRender[map] = checkBut.check ? 1 : 0;
        settings.applyRender(button.menu.loader.game.getGameGraphics());
        settings.Save();
    }
}
