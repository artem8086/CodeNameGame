package art.soft;

import art.soft.animation.AnimSet;
import art.soft.animation.Animation;
import art.soft.gameObjs.gameObj;
import art.soft.gameObjs.gui.ButtonMenu;
import art.soft.level.gameEngine;
import art.soft.level.levelStage;
import art.soft.model.Model;
import art.soft.stages.Menu;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.HashMap;

/**
 *
 * @author Артём Святоха
 */
public class Loader {

    private static final String FONT_NAME = "Serif";
    private static final int FONT_STYLE = Font.CENTER_BASELINE;
    private static final int FONT_SIZE = 20;

    public Animation animsPool;

    private final HashMap<String, AnimSet> allAnims = new HashMap<>();
    
    private final HashMap<String, Model> allModels = new HashMap<>();

    private gameObj poolObjs;

    public final Game game;
    public final Settings settings;
    
    private final Toolkit tk;
    public final ClassLoader cl;
    private final MediaTracker md;

    public Font font;
    private FontMetrics metrics;

    // Stage data
    public Menu menu;
    public ButtonMenu buttonMenu;
    public levelStage level;
    public gameEngine engine;

    // Настройки отладки
    public boolean debugDecoration;
    public boolean debugPolyModel;
    public boolean debugStatic;
    public boolean debugDynamic;
    public boolean debugDamageRegion;
    public boolean debugCamera;

    Loader(Game game) {
        this.game = game;
        //
        tk = Toolkit.getDefaultToolkit();
	cl = Game.class.getClassLoader();
        md = new MediaTracker(game);
        //
        settings = new Settings(this);
        settings.Load();
                //
            // Скрываем курсор
        if (settings.fullscreen) {
            Image invis = tk.createImage(new MemoryImageSource(16, 16, new int[256], 0, 16));
            game.setCursor(tk.createCustomCursor(invis, new Point(), null));
        }
    }

    void init() {
        font = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        Graphics g = game.getGameGraphics();
        g.setFont(font);
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        metrics = g.getFontMetrics();
        // Stage init
        menu = new Menu(this);
        engine = new gameEngine();
        level = new levelStage(this);
    }

    public int stringWidth(String text) {
        return metrics.stringWidth(text);
    }
    
    public DataInputStream openFile(String file) {
        return new DataInputStream(new BufferedInputStream(
                cl.getResourceAsStream(Game.DATA_DIR + file)));
    }

    private Image loadImage(String im){
        //game.log("Load " + im);
        Image i = tk.getImage(cl.getResource(Game.DATA_DIR + im));
        md.addImage(i, 0);
        try {
            md.waitForID(0);
        } catch (InterruptedException ex) {
            game.log(ex.getMessage());
        }
        md.removeImage(i, 0);
        return i;
    }

    public AnimSet loadAnimation(String path) {
        AnimSet anim = allAnims.get(path);
        if (anim == null) {
            anim = new AnimSet(this, loadImage(path + ".png"), path);
            allAnims.put(path, anim);
        } else {
            anim.loadIndx ++;
        }
        return anim;
    }

    public void removeAnim(String anim) {
        AnimSet a = allAnims.get(anim);
        if (a.loadIndx == 0) {
            allAnims.remove(anim);
        } else {
            a.loadIndx --;
        }
    }

    public Model loadModel(String path) {
        Model model = allModels.get(path);
        if (model == null) {
            model = new Model(this, path);
            allModels.put(path, model);
        } else {
            model.loadIndx ++;
        }
        return model;
    }

    public void removeModel(String model) {
        Model m = allModels.get(model);
        if (m.loadIndx == 0) {
            allModels.remove(model);
            AnimSet[] animSets = m.animSets;
            if (animSets != null) {
                for (AnimSet set : animSets) {
                    set.remove(this);
                }
            }
        } else {
            m.loadIndx --;
        }
    }

    public void poolObj(gameObj obj) {
        obj.pool(this);
        obj.next = poolObjs;
        poolObjs = obj;
    }

    public void poolAllObj(gameObj obj) {
        gameObj next;
        while (obj != null) {
            next = obj.next;
            poolObj(obj);
            obj = next;
        }
    }

    public <T> T getObj(Class<T> type) {
        gameObj obj = poolObjs;
        gameObj pred = null;
        while (obj != null) {
            if (type.equals(obj)) {
                if (pred == null) poolObjs = obj.next;
                else pred.next = obj.next;
                obj.next = null;
                obj.init(this);
                return (T) obj;
            }
            pred = obj;
            obj = obj.next;
        }
        try {
            obj = (gameObj) type.newInstance();
            obj.init(this);
            return (T) obj;
        } catch (InstantiationException | IllegalAccessException ex) {
            game.log(ex.getMessage());
        }
        return null;
    }

    public gameObj getObjTyped(Class<?> type) {
        gameObj obj = poolObjs;
        gameObj pred = null;
        while (obj != null) {
            if (type.equals(obj)) {
                if (pred == null) poolObjs = obj.next;
                else pred.next = obj.next;
                obj.next = null;
                obj.init(this);
                return obj;
            }
            pred = obj;
            obj = obj.next;
        }
        try {
            obj = (gameObj) type.newInstance();
            obj.init(this);
            return obj;
        } catch (InstantiationException | IllegalAccessException ex) {
            game.log(ex.getMessage());
        }
        return null;
    }

    public Animation getAnimation() {
        if (animsPool != null) {
            Animation anim = animsPool;
            animsPool = anim.next;
            anim.next = null;
            return anim;
        }
        return new Animation();
    }
}
