package com.darkhouse.shardwar.Logic.GameEntity.Spells;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Model.AnimationActor;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;
import com.darkhouse.shardwar.Tools.AssetLoader;
import com.darkhouse.shardwar.Tools.FontLoader;

import java.util.ArrayList;
import java.util.TimerTask;

public class ClawSmash extends Spell{

    public static class P extends SpellPrototype{
        private int[] dmg;

        public P(int dmg1, int dmg2, int dmg3) {
            super("clawsmash", new TargetData[]{
                    new TargetData(TargetType.HLINE, FieldTarget.ENEMY, Wall.class, Tower.class),
                    new TargetData(TargetType.VLINE, FieldTarget.ENEMY, Wall.class, Tower.class),
                    new TargetData(NonTargetType.X, FieldTarget.ENEMY, Wall.class, Tower.class)
            });
            this.dmg = new int[]{dmg1, dmg2, dmg3};
            setSameTargets();
        }

        @Override
        public String getTooltip() {
            AssetLoader l = ShardWar.main.getAssetLoader();
            return l.getWord("clawsmashTooltip1") + " " +
                    FontLoader.colorString(String.valueOf(dmg[0]), 4) + "/" +
                    FontLoader.colorString(String.valueOf(dmg[1]), 4) + "/" +
                    FontLoader.colorString(String.valueOf(dmg[2]), 4) +
                    l.getWord("clawsmashTooltip2");
        }

        @Override
        public Spell createSpell(Player player) {
            return new ClawSmash(player, this);
        }
    }

    private int[] dmg;

    public ClawSmash(Player owner, P prototype) {
        super(owner, prototype);
        this.dmg = prototype.dmg;

        TextureAtlas a = ShardWar.main.getAssetLoader().getAnimation("clawsmash");
        Animation<TextureAtlas.AtlasRegion> anim = new Animation<>(0.1f, a.findRegions("horiz"));
        Animation<TextureAtlas.AtlasRegion> anim2 = new Animation<>(0.1f, a.findRegions("vert"));
        Animation<TextureAtlas.AtlasRegion> anim3 = new Animation<>(0.1f, a.findRegions("x"));
        anim.setPlayMode(Animation.PlayMode.NORMAL);
        anim2.setPlayMode(Animation.PlayMode.NORMAL);
        anim3.setPlayMode(Animation.PlayMode.NORMAL);
        animationActor1 = new AnimationActor(anim);
        animationActor2 = new AnimationActor(anim2);
        animationActor3 = new AnimationActor(anim3);

    }
    private AnimationActor animationActor1;
    private AnimationActor animationActor2;
    private AnimationActor animationActor3;

    private int i = 0;

    private void useWave(ArrayList<GameObject> targets){
        FightScreen.Field f = ShardWar.fightScreen.getEnemyField(getOwnerPlayer());
        if (i == 0) {
            int r = targets.get(0).getSlot().getRow();
//            Slot s = targets.get(0).getSlot();
            Slot s = f.getOnRow(r).get(0);
            animationActor1.setPosition(s.getX() + s.getParent().getX(),
                    s.getY() + s.getParent().getY() + s.getHeight()/4);
            animationActor1.setSize(s.getWidth()*5, s.getHeight()/2);
            ShardWar.fightScreen.getStage().addActor(animationActor1);
        }else if(i == 1){
            int c = targets.get(0).getSlot().getColumn();
            int r;
            if(ShardWar.fightScreen.getPlayerIndex(getOwnerPlayer()) == 1) r = 2;
            else                                                           r = 0;
            Slot s = f.getOnColumn(c).get(r);
            animationActor2.setPosition(s.getX() + s.getParent().getX() + s.getWidth()/4,
                    s.getY() + s.getParent().getY());
            animationActor2.setSize(s.getWidth()/2, s.getHeight()*4);
            ShardWar.fightScreen.getStage().addActor(animationActor2);
        }else if(i == 2){
//            Slot s = targets.get(0).getSlot();
            int corner;
            if(ShardWar.fightScreen.getPlayerIndex(getOwnerPlayer()) == 1) corner = 0;
            else                                                           corner = 2;
            Slot s = f.get(corner, 0);
            animationActor3.setPosition(s.getX() + s.getParent().getX(),
                    s.getY() + s.getParent().getY());
            animationActor3.setSize(s.getWidth()*5, s.getHeight()*4);
            ShardWar.fightScreen.getStage().addActor(animationActor3);
        }
        for (GameObject g:targets){
            g.dmg(dmg[i], this);
        }
    }

    @Override
    public void use(ArrayList<ArrayList<GameObject>> targets) {
//        useWave(targets.get(i));
//        i++;
        ShardWar.fightScreen.t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(i >= targets.size()) {
                    cancel();
                    return;
                }
                useWave(targets.get(i));
                i++;
            }
        }, 0, 400);
//        for (int i = 0; i < targets.size(); i++) {
//            useWave(targets.get(i), i);
//
//        }
    }
}
