package com.darkhouse.shardwar.Logic.Slot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

public class PlayerSlot extends Slot<Player.PlayerPrototype, Player> {

    public PlayerSlot(boolean player, FightScreen owner, Player user, int column, int row) {
        super(ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class),
                player, false, 2, owner, user, column, row);
    }

    @Override
    protected void init() {
        choose = new TextureRegion(ShardWar.main.getAssetLoader().get("towerSlotSelect.png", Texture.class));
        reserve = new TextureRegion(ShardWar.main.getAssetLoader().get("towerSlotReserve.png", Texture.class));
        super.init();
    }

    @Override
    protected void fadeIn() {
//        super.fadeIn();
    }

    @Override
    protected void fadeOut() {
//        super.fadeOut();
    }

    @Override
    public void initTooltip() {
//        super.initTooltip();
    }

    @Override
    public void hideTooltip() {
//        super.hideTooltip();
    }
}
