package com.darkhouse.shardwar.Logic.Slot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkhouse.shardwar.Logic.BuyWindow;
import com.darkhouse.shardwar.Logic.GameEntity.Empty;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

public class WallSlot extends Slot<Wall.WallPrototype, Wall> {
    public WallSlot(boolean player, FightScreen owner, Player user, int line, int row) {
        super(ShardWar.main.getAssetLoader().get("wallSlot.png", Texture.class),
                player, true, 0, owner, user, line, row);
    }

    public void init(){
        buyWindow = new BuyWindow.WallWindow(this);
        TextureRegion r = new TextureRegion(ShardWar.main.getAssetLoader().get("wallSlotSelect.png", Texture.class));
        if(!player)r.flip(false, true);
        choose = r;

        TextureRegion m = new TextureRegion(ShardWar.main.getAssetLoader().get("wallSlotReserve.png", Texture.class));
        if(!player)m.flip(false, true);
        reserve = m;

        TextureRegion d = new TextureRegion(ShardWar.main.getAssetLoader().get("wallSlotDisabled.png", Texture.class));
        if(!player)d.flip(false, true);
        disableTexture = d;

        super.init();
    }
}
