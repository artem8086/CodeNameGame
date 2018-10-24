package art.soft.level;

import art.soft.Loader;
import art.soft.Settings;
import art.soft.gameObjs.containers.CenterContainer;
import static art.soft.gameObjs.gui.ButtonMenu.MENU_IN_GAME;
import art.soft.stages.Stage;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class levelStage extends Stage {

    private boolean load, pause;
    
    private CenterContainer inGameMenu;
    
    private final gameEngine engine;

    public levelStage(Loader loader) {
        super(loader);
        engine = loader.engine;
    }

    @Override
    public void init() {
        if (!load) {
            initGameMenu();
            load = true;
        }
        engine.init(loader);
        loader.buttonMenu.setMenu(MENU_IN_GAME);
        loader.buttonMenu.toMainMenu.setMenu(MENU_IN_GAME);
        unpause();
    }

    public void initGameMenu() {
        inGameMenu = loader.getObj(CenterContainer.class);
        inGameMenu.setBacground(new Color(0, 0, 0, 0x77));
        inGameMenu.add(loader.buttonMenu);
        //
        
    }

    @Override
    public void act() {
        if (pause) {
            inGameMenu.act();
        } else {
            engine.act();
        }
    }

    @Override
    public void draw(Graphics g) {
        engine.draw(g);
        if (pause) {
            inGameMenu.draw(g);
        } else {
            // HUD action
        }
    }

    public void pause() {
        setKeyListener(loader.buttonMenu);
        pause = true;
    }

    public void unpause() {
        setKeyListener(engine); // (level);
        pause = false;
    }

    @Override
    public void keyPressed(int keyCode) {
        if (!pause && keyCode == loader.settings.gameKeys[Settings.GAME_PAUSE]) {
            pause();
            return;
        }
        super.keyPressed(keyCode);
    }
}
