package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.Empty;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;

import java.util.ArrayList;

public class Dispel extends Spell {

    public static class P extends SpellPrototype{

        public P() {
            super("dispel", new TargetData[]{
                    new TargetData(TargetType.ALL, FieldTarget.ANY, Tower.class, Wall.class, Empty.class, Player.class)
            });
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("dispelTooltip1") + System.getProperty("line.separator") +
                    l.getWord("dispelTooltip2") + System.getProperty("line.separator") +
                    l.getWord("dispelTooltip3");
        }

        @Override
        public Spell createSpell(Player player) {
            return new Dispel(player, this);
        }
    }


    public Dispel(Player owner, P prototype) {
        super(owner, prototype);
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (ArrayList<GameObject> a:targets) {
            for (GameObject g:a){
                if(ShardWar.fightScreen.getField(getOwnerPlayer()).getGameObjects().contains(g)){//friendly
                    g.dispell(false);
                }else {
                    g.dispell(true);
                }
            }
        }
    }
}
