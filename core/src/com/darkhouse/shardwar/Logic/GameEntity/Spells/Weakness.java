package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class Weakness extends Spell{

    public static class P extends SpellPrototype {

        private int dmgReduction;
        private int duration;

        public P(int dmgReduction, int duration) {
            super("weakness", new TargetData[]{
                    new TargetData(NonTargetType.ALL, FieldTarget.ENEMY, Tower.class)
            });
            this.dmgReduction = dmgReduction;
            this.duration = duration;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("weaknessTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(dmgReduction), 6)
                    + " " + l.getWord("weaknessTooltip2") + " " +
                    FontLoader.colorString(String.valueOf(duration), 5) +
                    " " + l.getWord("weaknessTooltip3");
        }

        @Override
        public Spell createSpell(Player player) {
            return new Weakness(player, this);
        }
    }

    private class WeakEffect extends Effect<Tower>{

//        private int startDmg;
        private int dmgReduction;

        public WeakEffect(int duration, int dmgReduction) {
            super("weakness", false, duration, INone.class);
            this.dmgReduction = dmgReduction;
        }

        @Override
        public void apply() {
//            startDmg = owner.getDmg();
//            owner.setDmg(startDmg/2);
            owner.addBonusDmg(-dmgReduction);
        }

        @Override
        public void dispell() {
//            owner.setDmg(startDmg);
            owner.addBonusDmg(dmgReduction);
        }
    }

    private int duration;
    private int dmgReduction;

    public Weakness(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.dmgReduction = prototype.dmgReduction;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject o:targets.get(0)){
            o.addEffect(new WeakEffect(duration, dmgReduction).setOwner((Tower) o));
        }
    }
}
