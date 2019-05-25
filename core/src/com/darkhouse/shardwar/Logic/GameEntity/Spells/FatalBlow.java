package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class FatalBlow extends Spell{

    public static class P extends SpellPrototype{

        private int dmg;

        public P(int dmg) {
            super("fatalblow", new TargetData[]{
                    new TargetData(NonTargetType.PLAYER, FieldTarget.ENEMY, Player.class)
            });
            this.dmg = dmg;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("fatalblowTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(dmg), 7) + " " +
                    l.getWord("fatalblowTooltip2");
        }

        @Override
        public Spell createSpell(Player player) {
            return new FatalBlow(player, this);
        }
    }

    private int dmg;

    public FatalBlow(Player owner, P prototype) {
        super(owner, prototype);
        dmg = prototype.dmg;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.dmg(dmg, this);
        }
    }
}
