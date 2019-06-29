package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Assault;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.MachineGun;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class FireBullets extends Spell{

    public static class P extends SpellPrototype{
        private int duration;
        private int burnDuration;
        private int burnDmg;

        public P(int duration, int burnDuration, int burnDmg) {
            super("firebullets", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Assault.class, MachineGun.class)
            });
            this.duration = duration;
            this.burnDuration = burnDuration;
            this.burnDmg = burnDmg;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("firebulletsTooltip1") + " " + FontLoader.colorString(String.valueOf(duration), 5) + " " +
                    l.getWord("firebulletsTooltip2") + System.getProperty("line.separator") +
                    l.getWord("firebulletsTooltip3") + " " +
                    FontLoader.colorString(String.valueOf(burnDuration), 5) + " " +
                    l.getWord("firebulletsTooltip4") + System.getProperty("line.separator") +
                    l.getWord("firebulletsTooltip5") + " " +
                    FontLoader.colorString(String.valueOf(burnDmg), 4) + " " +
                    l.getWord("firebulletsTooltip6");
        }

        @Override
        public Spell createSpell(Player player) {
            return new FireBullets(player, this);
        }
    }

    private static class FireBulletsEffect extends Effect<Tower> implements Effect.IAfterAttack {

        private class FireBulletsHitEffect extends Effect {
            private int burnDmg;

            public FireBulletsHitEffect(int duration, int burnDmg) {
                super("firebullets", false, duration, INone.class);
                this.burnDmg = burnDmg;
            }

            @Override
            public void apply() {

            }
            @Override
            public void dispell() {

            }

            @Override
            protected void act() {
                owner.dmg(burnDmg, FireBulletsEffect.this.owner, true);
            }
        }

        private int burnDuration;
        private int burnDmg;

        public FireBulletsEffect(int duration, int burnDuration, int burnDmg) {
            super("firebullets", true, duration, IAfterAttack.class);
            this.burnDuration = burnDuration;
            this.burnDmg = burnDmg;
        }

        @Override
        public void apply() {

        }

        @Override
        public void dispell() {

        }

        @Override
        public void afterAttack(GameObject target, int dmg) {
            target.addEffect(new FireBulletsHitEffect(burnDuration, burnDmg).setOwner(target));
        }
    }



    private int duration;
    private int burnDuration;
    private int burnDmg;

    public FireBullets(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.burnDuration = prototype.burnDuration;
        this.burnDmg = prototype.burnDmg;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new FireBulletsEffect(duration, burnDuration, burnDmg).setOwner(((Tower) g)));
        }
    }
}
