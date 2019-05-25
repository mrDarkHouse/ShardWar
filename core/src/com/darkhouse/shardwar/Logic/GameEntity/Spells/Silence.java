package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class Silence extends Spell {

    public static class P extends SpellPrototype{

        private int duration;

        public P(int duration) {
            super("silence", new TargetData[]{
                    new TargetData(NonTargetType.PLAYER, FieldTarget.ENEMY, Player.class)
            });
            this.duration = duration;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("silenceTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(duration), 2) + " " +
                    l.getWord("silenceTooltip2");
        }

        @Override
        public Spell createSpell(Player player) {
            return new Silence(player, this);
        }
    }

    private class SilenceEffect extends Effect<Player>{

        public SilenceEffect(int duration) {
            super("silence", false, duration, INone.class);
        }

        @Override
        public void apply() {
            owner.setSilenced(true);
        }

        @Override
        public void dispell() {
            owner.setSilenced(false);
        }
    }

    private int duration;

    public Silence(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            Player p = ((Player) g);
            p.addEffect(new SilenceEffect(duration).setOwner(p));
        }
    }
}
