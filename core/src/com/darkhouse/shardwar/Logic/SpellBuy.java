package com.darkhouse.shardwar.Logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Spell;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

public class SpellBuy extends Dialog {

    private Container[] spells;

    private class Container extends Actor {
        private Texture slotTexture;
        private Spell.SpellPrototype spell;
        private Texture spellTexture;

        public Container(Texture slotTexture) {
            this.slotTexture = slotTexture;
            setSize(70, 70);
//            this.spellTexture = spellTexture;
        }

        public void setSpell(final Spell.SpellPrototype spell) {
            this.spell = spell;
            spellTexture = ShardWar.main.getAssetLoader().getSpell(spell.getName());
            addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(Container.this.spell != null) {
                        if(ShardWar.fightScreen.getCurrentPlayerObject().addSpell(Container.this.spell)) {
//                        SpellBuy.this.setTouchable(Touchable.disabled);
                            SpellBuy.this.hide();
                            Container.this.spell = null;
                        }
                    }
                    return true;
                }
            });
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(slotTexture, getX(), getY(), getWidth(), getHeight());
            if(spell != null) {
                batch.draw(spellTexture, getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    public SpellBuy() {
        super("Spells", ShardWar.main.getAssetLoader().getSkin(), "description");
        getTitleLabel().setAlignment(Align.center);
        TextButton closeButton = new TextButton("X", ShardWar.main.getAssetLoader().getSkin(), "description");
//        closeButton.setSize(10, 10);
        closeButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SpellBuy.this.hide();
                return true;
            }
        });
        getTitleTable().add(closeButton).height(getPadTop());

//        getTitleTable().padTop(20);
//        getTitleTable().padBottom(50);
//        getTitleLabel().setHeight(40);
        setMovable(false);
        defaults().align(Align.center).pad(20);
        padRight(8);
        spells = new Container[3];
//        debug();
        for (int i = 0; i < 3; i++) {
            spells[i] = new Container(ShardWar.main.getAssetLoader().get("towerSlot.png", Texture.class));
            add(spells[i]).expand();
        }
        pack();
    }
    public void addSpell(Spell.SpellPrototype prototype){
        for (int i = 0; i < 3; i++) {
//            System.out.println(i);
            if(spells[i].spell == null){
                spells[i].setSpell(prototype);
                break;
            }
        }
    }
    public void clearSpells(){
        for (int i = 0; i < 3; i++) {
            if(spells[i].spell != null){
                spells[i].spell = null;
            }
        }
    }


}
