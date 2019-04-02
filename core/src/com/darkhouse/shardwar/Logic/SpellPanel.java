package com.darkhouse.shardwar.Logic;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Spell;
import com.darkhouse.shardwar.Model.ShapeTarget;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

import java.util.ArrayList;

public class SpellPanel extends Table {

    private class SpellSlot extends Actor {

        public class SoloTargeter extends InputListener {
//            private Array<Class<? extends GameObject>> targetTypes;
            private ShapeTarget circle;

            public SoloTargeter(/*Array<Class<? extends GameObject>> targetTypes, */Vector2 startCoord) {
//            this.targetTypes = targetTypes;
                circle = new ShapeTarget(new ShapeRenderer(), 20);
                circle.setPosition(startCoord.x, startCoord.y);
                SpellPanel.this.getStage().addActor(circle);
//            SpellThrower.this.stage.addActor(circle);
                isTargeting = true;
            }

            public void cancelTargeting(){
                clearSelect();
                SpellPanel.this.getStage().removeListener(this);
                isTargeting = false;
                circle.remove();
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                circle.setPosition(event.getStageX() - 10, event.getStageY() - 10);
                Slot target = getTarget(new Vector2(x, y));
                if(target != null) {
                    ArrayList<Slot> all = ShardWar.fightScreen.getEnemyField(player).getObjects();
                    ArrayList<Slot> targets = spell.getSpellType().getTargets(ShardWar.fightScreen.getEnemyField(player), target);
                    for (Slot s:all){
                        if(targets.contains(s)){
                            s.choose();
                        }else s.unChoose();
                    }
//                    for (Slot s:targets) {
//                        s.choose();
//                    }
                }else clearSelect();
                return true;
            }
            private void clearSelect(){
                FightScreen.Field f = ShardWar.fightScreen.getEnemyField(player);
                for (Slot s:f.getObjects()){
                    s.unChoose();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == 0) use(new Vector2(x, y));
                else cancelTargeting();
                return true;
            }
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                if(button == 0) use(new Vector2(x, y));
//                else cancelTargeting();
//            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ESCAPE){
                    cancelTargeting();
                    return true;
                }else return false;
            }

            private Slot getTarget(Vector2 point){
//            Effectable target = Level.getMap().getTargetUnit(point, targetTypes);
                Slot target = ShardWar.fightScreen.getEnemyField(player).getByCoords(point.x, point.y);
                if(target != null) return target;
                else return null;
            }

            private void use(Vector2 point){
                Slot target = getTarget(point);
                if(target != null) {
                    ArrayList<Slot> targets = spell.getSpellType().getTargets(ShardWar.fightScreen.getEnemyField(player), target);
                    ArrayList<GameObject> trueTargets = new ArrayList<GameObject>();
//                    System.out.println(spell.getAffectedTypes());
                    for (Slot g:targets){
                        if(g.object != null){
                            if(spell.getAffectedTypes().contains(g.getObject().getClass().getSuperclass())){//TODO rework
                                trueTargets.add(g.getObject());
                            }
                        }
                    }
                    if(!trueTargets.isEmpty()) {//optional
                        spell.use(trueTargets);
                        removeSpell();
                        cancelTargeting();
                    }
                }
            }
        }

        private Spell spell;
        private Texture bgTexture;
        private Texture texture;
        private int index;

        public void setSpell(Spell.SpellPrototype prototype) {
            this.spell = prototype.createSpell(player);
            texture = ShardWar.main.getAssetLoader().getSpell(prototype.getName());
        }

        public void removeSpell(){
            this.spell = null;
        }

        public SpellSlot(int index) {
            this.index = index;
            bgTexture = ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class);
        }
        public void init(){
            addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(ShardWar.fightScreen.getCurrentPlayerObject() == player && spell != null) {
                        useSpell(spell, new Vector2(x, y));
//                        removeSpell();
                    }
                    return true;
                }

//                @Override
//                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                    super.touchUp(event, x, y, pointer, button);
//                    if(ShardWar.fightScreen.getCurrentPlayerObject() == player && spell != null) {
//                        useSpell(spell);
////                        removeSpell();
//                    }
//                }
            });
        }


        private void useSpell(Spell spell, Vector2 useCoord){
            if(spell.getSpellType() instanceof Spell.NonTargetType) {
                FightScreen f = ShardWar.fightScreen;
                ArrayList<Slot> targetSlots = ((Spell.NonTargetType) spell.getSpellType()).getTargets(f.getEnemyField(player));
                ArrayList<GameObject> targets = new ArrayList<GameObject>();
                for (Slot s:targetSlots){
                    if(s.getObject() != null) targets.add(s.getObject());
                }
                spell.use(targets);
                removeSpell();
            }
            if(spell.getSpellType() instanceof Spell.TargetType) {
                getStage().addListener(new SoloTargeter(new Vector2(
                        useCoord.x + getParent().getX() + index*getWidth() - 5, useCoord.y + getParent().getY() - 5)
                        //5 - size between top of table and actor (getHeight() - 10, getHeight() - 10)


                        /*new Vector2(
                        getX() + getParent().getX() + getWidth()/2, getY() + getParent().getY() + getHeight()/2))*/));
            }

        }


        public boolean isEmpty(){
            return spell == null;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(bgTexture, getX(), getY(), getWidth(), getHeight());
            if(spell != null) {
                batch.draw(texture, getX(), getY(), getWidth(), getHeight());
            }
        }
    }



    private boolean isTargeting;

    private Player player;
    private SpellSlot[] slots;
//    private ArrayList<SpellSlot> spells;

    public SpellPanel(Player player) {
        super();
        this.player = player;
    }
    public void init(){
        setBackground(ShardWar.main.getAssetLoader().getSkin().getDrawable("info-panel"));
        slots = new SpellSlot[4];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new SpellSlot(i);
            slots[i].setSize(getHeight() - 10, getHeight() - 10);
            add(slots[i]);
            slots[i].init();
        }


        player.setSpellPanel(this);
//        pack();
    }



    public boolean addSpell(Spell.SpellPrototype spellPrototype){
//        SpellSlot s = new SpellSlot();
//        add(s);
        for (int i = 0; i < slots.length; i++) {
            if(slots[i].isEmpty()) {
                slots[i].setSpell(spellPrototype);
                return true;
            }
        }
        return false;
//        spells.add(s);
    }

//    public void deleteSpell(SpellSlot spell){
//        spell.remove();
////        spells.remove(spell);
//    }
}
