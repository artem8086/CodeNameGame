package art.soft.objects;

import art.soft.Game;
import art.soft.Loader;
import art.soft.level.Layer;
import art.soft.level.LayerObj;
import art.soft.objsData.ObjData;
import art.soft.objsData.PolyModel;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class ModelObj extends LayerObj {

    Layer layer;

    @Override
    public void init(Loader loader, ObjData data, ObjData drop) {
        this.data = data;
        ((PolyModel) data).model.reset();
    }

    @Override
    public boolean act(Layer layer) {
        this.layer = layer;
        return false;
    }

    @Override
    public void draw(Graphics g) {
        PolyModel poly = (PolyModel) data;
        Game game = poly.loader.game;
        poly.model.draw(g, x, y, poly.zoom,
                layer.x + (game.width >> 1), layer.y + (game.height >> 1));
        //
        if (poly.loader.debugPolyModel) {
            g.setColor(Color.ORANGE);
            g.drawRect(x - data.cenX, y - data.cenY, data.width, data.height);
        }
    }

    @Override
    public void pool(Loader loader) {
        super.pool(loader);
        layer = null;
    }
}
