package art.soft.stages;

import art.soft.Loader;
import art.soft.gameObjs.GameKeyListener;
import art.soft.gameObjs.gameObj;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public abstract class Stage {

    public gameObj objects;
    protected final Loader loader;
    protected int tx, ty;
    protected GameKeyListener keyListener;

    public Stage(Loader loader) {
        this.loader = loader;
    }

    /*
     * Установка слушателя клавиатуры
     * @param keyListener слушатель клавиатуры
     */
    public void setKeyListener(GameKeyListener keyListener) {
        this.keyListener = keyListener;
    }

    /*
     * Вызывается при установке сцены
     */
    public abstract void init(); 

    /*
     * Вызывается для обработки игровой логики
     */
    public void act() {
        gameObj obj = objects;
        while (obj != null) {
            obj.act();
            obj = obj.next;
        }
    }

    /*
     * Установка смещения рисования сцены
     * @param x смещение по координате X
     * @param y смещение по координате Y
     */
    public void setTranslate(int x, int y) {
        tx = x; ty = y;
    }

    /*
     * Вызывается для обработки сцены
     * @g графический контекст
     */
    public void draw(Graphics g) {
        int ttx = tx;
        int tty = ty;
        g.translate(ttx, tty);
        //
        gameObj obj = objects;
        while (obj != null) {
            obj.draw(g);
            obj = obj.next;
        }
        //
        g.translate(- ttx, - tty);
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

    /*
     * Вызывается при нажатии клавиши
     * @keyCode код клавиши
     */
    public void keyPressed(int keyCode) {
        if (keyListener != null) {
            keyListener.keyPressed(keyCode);
        }
    }

    /*
     * Вызывается при отпускании клавиши
     * @keyCode код клавиши
     */
    public void keyReleased(int keyCode) {
        if (keyListener != null) {
            keyListener.keyReleased(keyCode);
        }
    }
}
