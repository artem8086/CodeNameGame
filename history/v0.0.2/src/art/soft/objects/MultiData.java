package art.soft.objects;

import art.soft.Loader;

/**
 *
 * @author Артём Святоха
 */
public class MultiData extends ObjData {

    public Dynamic forms[];
    
    public FrameData frames[];
    
    public int inAirStatus = 12;

    public int startFrame, startStatus;

    public float jumpX, jumpY;

    public int frameStatus[];

    public MultiData() {
        type = objType.unit_obj;
    }

    // Json data    
    @Override
    public void loadJson(Loader loader) {
        super.loadJson(loader);
        for (Dynamic form : forms) {
            form.loadJson(loader);
        }
    }
    // Json end

    @Override
    public void remove() {
    }    
}
