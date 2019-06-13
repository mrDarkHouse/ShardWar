package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Sniper;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class OpticalSight extends Spell {

    public static class P extends SpellPrototype{

        private int duration;
        private int bonusDmg;
        
        public P(int duration, int bonusDmg) {
            super("opticalsight", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Sniper.class)
            });
            this.duration = duration;
            this.bonusDmg = bonusDmg;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("opticalsightTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(bonusDmg), 6) + " " +
                    l.getWord("opticalsightTooltip2") + " " +
                    FontLoader.colorString(String.valueOf(duration), 5) + " " +
                    l.getWord("opticalsightTooltip3");
        }

        @Override
        public Spell createSpell(Player player) {
            return new OpticalSight(player, this);
        }
    }
    
    private class OpticalSightEffect extends Effect<Tower> {
        
        private int bonusDmg;

        public OpticalSightEffect(int duration, int bonusDmg) {
            super("opticalsight", true, duration, INone.class);
            this.bonusDmg = bonusDmg;
        }

        @Override
        public void apply() {
            owner.setDmg(owner.getDmg() + bonusDmg);
        }

        @Override
        public void dispell() {
            owner.setDmg(owner.getDmg() - bonusDmg);
        }

    }

    private int duration;
    private int bonusDmg;

    public OpticalSight(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.bonusDmg = prototype.bonusDmg;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new OpticalSightEffect(duration, bonusDmg).setOwner(((Tower) g)));
        }
    }
}
