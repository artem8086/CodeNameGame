package art.soft.objects.items;

import art.soft.level.Player;
import art.soft.objsData.ItemData;
import art.soft.objsData.UnitData;

/**
 *
 * @author Артём Святоха
 */
public class Skin extends Item {

    @Override
    public boolean playerGet(Player player) {
        UnitData sets = player.unit.mData;
        sets.animsData[ammo].remove(data.loader);
        sets.animsData[ammo] = ((ItemData) data).animData;
        ((ItemData) data).animData.loadIndx ++;
        player.setSet(sets);
        return false;
    }
}
