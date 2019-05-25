package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.Empty;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class DisableField extends Spell{

    public static class P extends SpellPrototype{

        private int duration;

        public P(int duration){
            super("disablefield", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.ENEMY, Empty.class)
            });
            this.duration = duration;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("disablefieldTooltip1") + System.getProperty("line.separator") +
                    l.getWord("disablefieldTooltip2") + " " +
                    FontLoader.colorString(String.valueOf(duration), 2) + " " +
                    l.getWord("disablefieldTooltip3");
        }

        @Override
        public Spell createSpell(Player player) {
            return new DisableField(player, this);
        }
    }

    public static class DisableFieldEffect extends Effect<Empty>{

        public DisableFieldEffect(int duration) {
            super("disablefield", false, duration, INone.class);
        }

        @Override
        public void apply() {
            owner.getSlot().setDisable(true);
        }

        @Override
        public void dispell() {
            owner.getSlot().setDisable(false);
        }
    }


    private int duration;

    public DisableField(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new DisableFieldEffect(duration).setOwner((Empty) g));
        }
    }

}
