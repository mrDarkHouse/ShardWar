package com.darkhouse.shardwar.Logic;

import com.badlogic.gdx.graphics.Texture;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

public class WallSlot extends Slot<Wall.WallPrototype, Wall> {
    public WallSlot(boolean player, FightScreen owner, Player user, int line, int row) {
        super(ShardWar.main.getAssetLoader().get("wallSlot.png", Texture.class), player, owner, user, line, row);
    }

    protected void init(){
        tooltip = new BuyWindow.WallWindow(this);
        super.init();
    }
}
