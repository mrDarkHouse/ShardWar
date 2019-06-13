package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Shotgun;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;

import java.util.ArrayList;

public class PowerfulAmmo extends Spell {

    public static class P extends SpellPrototype{
        private int duration;
        private int bonusDmg;

        public P(int duration, int bonusDmg) {
            super("powerfulammo", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Shotgun.class)
            });
            this.duration = duration;
            this.bonusDmg = bonusDmg;
        }

        @Override
        public String getTooltip() {
            return "";
        }

        @Override
        public Spell createSpell(Player player) {
            return new PowerfulAmmo(player, this);
        }
    }

    private class PowerfulAmmoEffect extends Effect<Tower> implements Effect.INone {

        private int bonusDmg;

        public PowerfulAmmoEffect(int duration, int bonusDmg) {
            super("powerfulammo", true, duration, INone.class);
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

    public PowerfulAmmo(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.bonusDmg = prototype.bonusDmg;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new PowerfulAmmoEffect(duration, bonusDmg).setOwner(((Tower) g)));
        }
    }
}
