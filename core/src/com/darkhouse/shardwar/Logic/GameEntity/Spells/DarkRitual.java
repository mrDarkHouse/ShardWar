package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class DarkRitual extends Spell{

    public static class P extends SpellPrototype{

        private float multiplayer;

        public P(float multiplayer) {
            super("darkritual", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Tower.class),
                    new TargetData(NonTargetType.ALL, FieldTarget.ENEMY, Tower.class, Wall.class)
            });
            this.multiplayer = multiplayer;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("darkritualTooltip1") + System.getProperty("line.separator") +
                    l.getWord("darkritualTooltip2") + " " +
                    FontLoader.colorString((int)multiplayer + "x", 4) + " " +
                    l.getWord("darkritualTooltip3") + " " +
                    l.getWord("darkritualTooltip4") + System.getProperty("line.separator") +
                    l.getWord("darkritualTooltip5");
        }

        @Override
        public Spell createSpell(Player player) {
            return new DarkRitual(player, this);
        }
    }


    private float multiplayer;

    public DarkRitual(Player owner, P prototype) {
        super(owner, prototype);
        this.multiplayer = prototype.multiplayer;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        GameObject friendly = targets.get(0).get(0);
        if(friendly.getHealth() != friendly.getMaxHealth()) return;
        int dmg = ((int) Math.floor(((Tower) friendly).getDmg() * multiplayer));
        if(dmg < 0) dmg = 0;
        for(GameObject g:targets.get(1)){
            g.dmg(dmg, this, true);
        }
        friendly.die(this, false, true);
    }
}
