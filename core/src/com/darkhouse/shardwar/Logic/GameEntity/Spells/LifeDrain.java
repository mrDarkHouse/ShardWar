package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class LifeDrain extends Spell {

    public static class P extends SpellPrototype{
        private int value;

        public P(int value) {
            super("lifedrain", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.ENEMY, Tower.class, Wall.class),
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Tower.class, Wall.class)
            });
            this.value = value;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("lifedrainTooltip1") + " " + FontLoader.colorString(String.valueOf(value),4) + " " +
                    l.getWord("lifedrainTooltip2") + System.getProperty("line.separator") +
                    l.getWord("lifedrainTooltip3");
        }

        @Override
        public Spell createSpell(Player player) {
            return new LifeDrain(player, this);
        }
    }

    private int value;

    public LifeDrain(Player owner, P prototype) {
        super(owner, prototype);
        this.value = prototype.value;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        GameObject enemy = targets.get(0).get(0);
        GameObject friendly = targets.get(1).get(0);

        int h = enemy.getHealth();
        int realValue;
        if(h < value) realValue = h;
        else realValue = value;
        enemy.dmg(realValue, this);
        friendly.heal(realValue);
    }
}
