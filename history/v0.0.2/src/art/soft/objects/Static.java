package art.soft.objects;

import art.soft.level.StaticObj;

/**
 *
 * @author Артём Святоха
 */
public class Static extends Decor {

    public static final int FLIP_X_MASK = 1;
    public static final int FLIP_Y_MASK = 2;

    public int hp = -1; // Immortal
    public float friction; // Трение обьекта
    public int collision;
    public int destroy_anim;
    public int flip;

    public Static() {
        type = objType.static_obj;
    }

    public boolean act(StaticObj obj) {
        return false;
    }

    @Override
    public void remove() {}
}
