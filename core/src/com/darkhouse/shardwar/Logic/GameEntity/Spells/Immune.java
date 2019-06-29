package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.DoubleBarrel;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Musket;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Shotgun;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class Immune extends Spell{

    public static class P extends SpellPrototype{
        private int duration;

        public P(int duration) {
            super("immune", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY,
                            Shotgun.class, DoubleBarrel.class, Musket.class)
            });
            this.duration = duration;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("immuneTooltip1") + System.getProperty("line.separator") +
                    l.getWord("immuneTooltip2") + System.getProperty("line.separator") +
                    l.getWord("immuneTooltip3") + System.getProperty("line.separator") +
                    l.getWord("immuneTooltip4") + " " +
                    FontLoader.colorString(String.valueOf(duration), 5) + " " +
                    l.getWord("immuneTooltip5");
        }

        @Override
        public Spell createSpell(Player player) {
            return new Immune(player, this);
        }
    }
    public static class ImmuneEffect extends Effect<Tower>{

        public ImmuneEffect(int duration) {
            super("immune", true, duration, INone.class);
        }

        @Override
        public void apply() {
            owner.dispell(false);
            owner.setImmune(true);
        }

        @Override
        public void dispell() {
            owner.setImmune(false);
        }
    }

    private int duration;

    public Immune(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new ImmuneEffect(duration).setOwner(((Tower) g)));
        }
    }
}
