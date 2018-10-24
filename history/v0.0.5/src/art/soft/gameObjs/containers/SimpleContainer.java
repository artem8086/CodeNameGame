package art.soft.gameObjs.containers;

import art.soft.Loader;
import art.soft.gameObjs.gameObj;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class SimpleContainer extends gameObj {

    public gameObj objects;
    public Loader loader;

    @Override
    public void init(Loader loader) {
        this.loader = loader;
    }

    @Override
    public boolean act() {
        gameObj obj = objects;
        while (obj != null) {
            obj.act();
            obj = obj.next;
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        gameObj obj = objects;
        while (obj != null) {
            obj.draw(g);
            obj = obj.next;
        }
    }

    @Override
    public void pool(Loader loader) {
        removeAll();
        this.loader = null;
    }

    /*
     * Добавление обьекта на сцену
     */
    public void add(gameObj obj) {
        obj.next = objects;
        objects = obj;
    }

    /*
     * Удаляет все добавленные обьекты
     */
    public void removeAll() {
        loader.poolAllObj(objects);
        objects = null;
    }
}
