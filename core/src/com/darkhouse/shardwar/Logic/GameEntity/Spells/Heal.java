package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class Heal extends Spell{

    public static class P extends SpellPrototype{

        private int healAmount;

        public P(int healAmount) {
            super("heal", new TargetData[]{
                    new TargetData(NonTargetType.PLUS, FieldTarget.FRIENDLY, Tower.class, Wall.class)
            });
            this.healAmount = healAmount;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("healTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(healAmount), 3)
                    + " " + l.getWord("healTooltip2");
        }

        @Override
        public Spell createSpell(Player player) {
            return new Heal(player, this);
        }
    }

    private int healAmount;

    public Heal(Player owner, P prototype) {
        super(owner, prototype);
        healAmount = prototype.healAmount;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.heal(healAmount);
        }
    }
}
