package art.soft.level;

import art.soft.Loader;
import art.soft.objects.ObjData;

/**
 *
 * @author Артём Святоха
 */
public class objPos {
    
    public int x, y;
    
    public ObjData data;
    
    public objPos next;

    // Json data

    public String name;
    // Json end

    public LayerObj addObj(Loader loader, Layer layer) {
        LayerObj obj = null;
        switch (data.type) {
            case decoration:
                obj = loader.getObj(Decoration.class);
                break;
            case static_obj:
                obj = loader.getObj(StaticObj.class);
                break;
            case dynamic:
                obj = loader.getObj(DynamicObj.class);
                break;
            case unit_obj:
                obj = loader.getObj(UnitObj.class);
                break;
        }
        obj.init(loader, data, layer);
        obj.setPos(x, y);
        layer.add(obj);
        return obj;
    }
}
