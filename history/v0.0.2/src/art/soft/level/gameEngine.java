package art.soft.level;

import art.soft.Loader;
import art.soft.gameObjs.GameKeyListener;
import art.soft.gameObjs.gameObj;
import art.soft.objects.ObjData;
import com.esotericsoftware.jsonbeans.Json;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author Артём Святоха
 */
public class gameEngine extends gameObj implements GameKeyListener {

    private final static String OBJS_PATH = "objects/";

    public ObjData[] objects;

    public Layer layers;
    
    public int camX, camY; // Текущие координаты камеры
    
    public float gravity; // Сила притяжения
    public boolean flipGravity = false; // Гравитация действует в обратном направлении
                    // при этом все динамические обьекты переварачивются
    public float envirFrictionX; // сопративление среды по оси X
    public float envirFrictionY; // сопративление среды по оси Y
    
    public int numPlayers;

    public Player players[];
    
    public Camera cameras;
    
    public Camera curCamera;
    
    private Color background = Color.BLUE;
    
    public Loader loader;

    // Json data
    private final static String JSON_PATH = "level/objs/";
    private final static String LEVEL_PATH = "level/level.json";

    private final Json json = new Json();

    private final HashMap<String, ObjData> objs = new HashMap<>();
    
    private int temp;

    public void jsonLoad() {
        //loader.poolAllObj(layers);
        objs.clear();
        File f = new File(LEVEL_PATH);
        if (f.exists()) {
            jsonLoader lev = json.fromJson(jsonLoader.class, f);
            Camera lastCam = null;
            for (Camera camera : lev.cameras) {
                if (lastCam == null) {
                    lastCam = cameras = camera;
                } else {
                    lastCam.next = camera;
                    lastCam = camera;
                }
            }
            background = new Color(lev.color);
            Layer layerEnd = null;
            for (Layer layer : lev.layers) {
                if (layerEnd == null) {
                    layerEnd = layers = layer;
                } else {
                    layerEnd.next = layer;
                    layerEnd = layer;
                }
                layer.initJson();
                layer.init(loader);
                objPos obj = layer.objsPos;
                while (obj != null) {
                    obj.data = getObj(obj.name);
                    obj = obj.next;
                }
            }
            layerEnd.next = null;
            Camera camera = cameras;
            while (camera != null) {
                camera.init(loader);
                camera = camera.next;
            }
            players = lev.players;
            if (players != null)
            for (int i = 0; i < numPlayers; i ++) {
                players[i].init(loader, loader.settings.playerKeys[i]);
                players[i].jsonInit(lev.layers);
            }
        }
    }

    public void postInit() {
        objects = new ObjData[objs.size()];
        temp = 0;
        objs.forEach((name, obj) -> objects[temp++] = obj);
    }

    public ObjData getObj(String objName) {
        ObjData obj = objs.get(objName);
        if (obj == null) {
            obj = json.fromJson(ObjData.class, new File(JSON_PATH + objName + ".json"));
            obj.loadJson(loader);
            objs.put(objName, obj);
        }
        return obj;
    }
    // Json end

    @Override
    public void init(Loader loader) {
        this.loader = loader;
        background = Color.BLUE;
        jsonLoad();
        camX = camY = 0;
        gravity = 0.98f;
        envirFrictionX = 0.1f;
        envirFrictionY = 0;
        curCamera = cameras;
    }

    private int deltaX;

    @Override
    public boolean act() {
        camX += deltaX;
        //
        Layer layer = layers;
        while (layer != null) {
            layer.act();
            layer = (Layer) layer.next;
        }
        return false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, loader.game.width, loader.game.height);
        //
        Layer layer = layers;
        while (layer != null) {
            layer.draw(g);
            layer = (Layer) layer.next;
        }
    }

    @Override
    public void pool(Loader loader) {
    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_D) {
            deltaX = 10;
        }
        if (keyCode == KeyEvent.VK_A) {
            deltaX = - 10;
        }
        if (keyCode == KeyEvent.VK_F5) jsonLoad();
        //
        if (players != null) {
            for (int i = numPlayers - 1; i >= 0; i --) {
                players[i].keyPressed(keyCode);
            }
        }
    }

    @Override
    public void keyReleased(int keyCode) {
        deltaX = 0;
        if (players != null)
        for (Player player : players) {
            player.keyPressed(keyCode);
        }
        //
        if (players != null) {
            for (int i = numPlayers - 1; i >= 0; i --) {
                players[i].keyReleased(keyCode);
            }
        }
    }
}
