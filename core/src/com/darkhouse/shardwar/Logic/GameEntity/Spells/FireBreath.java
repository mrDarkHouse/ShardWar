package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class FireBreath extends Spell{

    public static class P extends Spell.SpellPrototype{

        private int dmg;

        public P(int dmg) {
            super("firebreath", new TargetData[]{
                    new TargetData(TargetType.HLINE, FieldTarget.ENEMY, Tower.class, Wall.class)
            });
            this.dmg = dmg;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("firebreathTooltip1") + " " + FontLoader.colorString(String.valueOf(dmg), 7)
                    + " " + l.getWord("firebreathTooltip2");
        }

        @Override
        public Spell createSpell(Player player) {
            return new FireBreath(this, player);
        }
    }

    private int dmg;

    public FireBreath(P prototype, Player owner) {
        super(owner, prototype);
        this.dmg = prototype.dmg;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g: targets.get(0)) {
            g.dmg(dmg, this);
        }
    }

}
