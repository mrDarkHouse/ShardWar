package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Effects.Effect;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.DoubleRocket;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Rocket;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.Projectile;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AbilityHelper;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;

public class AdditionalRocket extends Spell {

    public static class P extends SpellPrototype{
        private int duration;
        private int rocketDamage;

        public P(int duration, int rocketDamage) {
            super("additionalrocket", new TargetData[]{
                    new TargetData(TargetType.SINGLE, FieldTarget.FRIENDLY, Rocket.class, DoubleRocket.class)
            });
            this.duration = duration;
            this.rocketDamage = rocketDamage;
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("additionalrocketTooltip1") + System.getProperty("line.separator") +
                    l.getWord("additionalrocketTooltip2") + " " +
                    FontLoader.colorString(String.valueOf(rocketDamage), 4) + " " +
                    l.getWord("additionalrocketTooltip3") + System.getProperty("line.separator") +
                    l.getWord("additionalrocketTooltip4") + " " +
                    FontLoader.colorString(String.valueOf(duration), 5) + " " +
                    l.getWord("additionalrocketTooltip5");
        }

        @Override
        public Spell createSpell(Player player) {
            return new AdditionalRocket(player, this);
        }
    }
    private static class AdditionalRocketEffect extends Effect<Tower> implements Effect.IAfterAttack {
        private int rocketDamage;
        private Player ownerPlayer;

        public AdditionalRocketEffect(int duration, int rocketDamage, Player ownerPlayer) {
            super("additionalrocket", true, duration, IAfterAttack.class);
            this.rocketDamage = rocketDamage;
            this.ownerPlayer = ownerPlayer;
        }

        @Override
        public void apply() {

        }

        @Override
        public void dispell() {

        }

        @Override
        public void afterAttack(GameObject target, int dmg) {
            FightScreen.Field f = ShardWar.fightScreen.getEnemyField(ownerPlayer);
            int num = AbilityHelper.rollNum(f.getNotEmptySlots().size());
            Slot t = f.getNotEmptySlots().get(num);
            Projectile pr = new Projectile(owner.getSlot(), owner.getSlot().getCenter(), t,
                    t.getColumn(), t.getRow(), rocketDamage);
            owner.getSlot().getOwner().projectiles.add(pr);
            owner.getSlot().getStage().addActor(pr);
        }
    }

    private int duration;
    private int rocketDamage;

    public AdditionalRocket(Player owner, P prototype) {
        super(owner, prototype);
        this.duration = prototype.duration;
        this.rocketDamage = prototype.rocketDamage;
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
        for (GameObject g:targets.get(0)){
            g.addEffect(new AdditionalRocketEffect(duration, rocketDamage, getOwnerPlayer()).setOwner((Tower) g));
        }
    }
}
