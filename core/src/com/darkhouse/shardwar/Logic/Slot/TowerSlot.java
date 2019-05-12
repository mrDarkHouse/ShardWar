package com.darkhouse.shardwar.Logic.Slot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkhouse.shardwar.Logic.BuyWindow;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

public class TowerSlot extends Slot<Tower.TowerPrototype, Tower> {

    public TowerSlot(boolean player, FightScreen owner, Player user, int line, int row) {
        super(ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class),
                player, true, 2, owner, user, line, row);
    }

    protected void init(){
        tooltip = new BuyWindow.TowerWindow(this);
        choose = new TextureRegion(ShardWar.main.getAssetLoader().get("towerSlotSelect.png", Texture.class));
        disableTexture = new TextureRegion(ShardWar.main.getAssetLoader().get("towerSlotDisabled.png", Texture.class));
        super.init();
    }
}
