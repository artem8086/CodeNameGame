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

    public Intro(Loader loader) {
        super(loader);
    }

    @Override
    public void init() {
        time = INTRO_TIME;
        //
        CenterContainer center = loader.getObj(CenterContainer.class);
        add(center);
        center.setBacground(Color.GRAY);
        SimpleAnimation artsoft = loader.getObj(SimpleAnimation.class);
        artsoft.setAnimSet(loader, loader.loadAnimation("artsoft"));
        artsoft.setCycle(false);
        artsoft.setPos(400, 220);
        artsoft.setAnimNum(0);
        center.add(artsoft);
    }

    @Override
    public void act() {
        if (time <= 0) {
            removeAll();
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
