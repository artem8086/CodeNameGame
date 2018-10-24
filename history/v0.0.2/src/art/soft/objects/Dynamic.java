package art.soft.objects;

/**
 *
 * @author Артём Святоха
 */
public class Dynamic extends Static {

    public int max_speed; // Максимальная скорость обьекта
    public float acceleratX = 1; // Ускорение по оси X
    public float acceleratY = 1; // Ускорение по оси Y
    public int coll_mask;

    public Dynamic() {
        type = objType.dynamic;
    }
}
