package art.soft.gameObjs.gui;

import art.soft.Loader;
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
        Loader loader = Loader.getLoader();
        Settings settings = loader.settings;
        checkBut.check ^= true;
        settings.mapRender[map] = checkBut.check ? 1 : 0;
        settings.applyRender(loader.game.getGameGraphics());
        settings.Save();
    }
}
