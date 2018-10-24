package art.soft.objsData;

import art.soft.Loader;
import art.soft.model.Model;
import art.soft.objects.ModelObj;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class PolyModel extends ObjData {

    public Model model;
    public float zoom = 1f;

    public PolyModel() {
        typeObj = ModelObj.class;
    }

    // Json data
    public String modelPath;
    
    @Override
    public void loadJson(Loader loader) {
        super.loadJson(loader);
        model = loader.loadModel(modelPath);
    }

    @Override
    protected void writeData(DataOutputStream dos) throws IOException {
        super.writeData(dos);
        dos.writeUTF(modelPath);
        dos.writeFloat(zoom);
    }
    // Json end

    @Override
    protected void readData(Loader loader, DataInputStream dis) throws IOException {
        super.readData(loader, dis);
        model = loader.loadModel(dis.readUTF());
        zoom = dis.readFloat();
    }

    @Override
    public void remove() {
        model.remove(loader);
    }
}
