package art.soft.gameObjs.gui;

import art.soft.Loader;
import static art.soft.gameObjs.gui.ButtonMenu.MENU_IN_GAME;

/**
 *
 * @author Артём Святоха
 */
public class StartGame implements ButtonListener {

    private final int playersNum;
    
    public StartGame(int num) {
        playersNum = num;
    }
    
    @Override
    public void pressed(Button button) {
        Loader loader = button.menu.loader;
        loader.engine.numPlayers = playersNum;
        loader.game.setStage(loader.level);
    }
}
