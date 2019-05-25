package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class Greed extends Spell {

    public static class P extends SpellPrototype{

        private int bounty;

        public P(int bounty) {
            super("greed", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Tower.class, Wall.class)
            });
            this.bounty = bounty;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("greedTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(bounty), 2) + " " +
                    l.getWord("greedTooltip2");
        }

        @Override
        public Spell createSpell(Player player) {
            return new Greed(player, this);
        }
    }

    private int bounty;

    public Greed(Player owner, P prototype) {
        super(owner, prototype);
        this.bounty = prototype.bounty;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
//            g.dmg(g.getHealth(), this);
            g.kill(this, false);
            getOwnerPlayer().addShards(bounty);
        }
    }
}
