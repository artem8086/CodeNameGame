package art.soft.stages;

import art.soft.Loader;
import art.soft.gameObjs.SimpleAnimation;
import art.soft.gameObjs.containers.CenterContainer;
import java.awt.Color;

/**
 *
 * @author Артём Святоха
 */
public class Intro extends Stage {

    private static final int INTRO_TIME = 120;
    
    private int time;

    @Override
    public void init() {
        Loader loader = Loader.getLoader();
        time = INTRO_TIME;
        //
        CenterContainer center = (CenterContainer) loader.getObj(CenterContainer.class);
        add(center);
        center.setBacground(Color.GRAY);
        SimpleAnimation artsoft = (SimpleAnimation) loader.getObj(SimpleAnimation.class);
        artsoft.setAnimSet(loader.loadAnimation("artsoft"));
        artsoft.setCycle(false);
        artsoft.setPos(400, 220);
        artsoft.setAnimNum(0);
        center.add(artsoft);
    }

    @Override
    public void act() {
        if (time <= 0) {
            removeAll();
            Loader loader = Loader.getLoader();
            loader.game.setStage(loader.menu);
        } else
            time --;
        super.act();
    }

    @Override
    public void keyReleased(int keyCode) {
        time = 0;
    }

    
}
