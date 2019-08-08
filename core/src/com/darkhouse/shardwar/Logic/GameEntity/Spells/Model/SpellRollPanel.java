package com.darkhouse.shardwar.Logic.GameEntity.Spells.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Spell;
import com.darkhouse.shardwar.Model.Tooltip.TooltipListener;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

import java.util.ArrayList;

public class SpellRollPanel extends Table {
    private final static int drawOffset = 2;


    private ArrayList<Spell.SpellPrototype> spellPool;

    public SpellRollPanel(ArrayList<Spell.SpellPrototype> spellPool) {
        setBackground(ShardWar.main.getAssetLoader().getSkin().getDrawable("info-panel"));
        this.spellPool = spellPool;
    }

    private class Container extends Actor {
        private Texture slotTexture;
        private Spell.SpellPrototype spell;
        private Texture spellTexture;

        public Container(Spell.SpellPrototype spell) {
            this.spell = spell;
            this.spellTexture = ShardWar.main.getAssetLoader().getSpell(spell.getName());
            this.slotTexture = ShardWar.main.getAssetLoader().getSpellSlotBgTexture(spell.getTier());
            int size = Gdx.graphics.getWidth()/30;
            setSize(size, size);//35
            defaults().space(3f);
            defaults().spaceBottom(10f);
            defaults().pad(3f);
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(slotTexture, getX(), getY(), getWidth(), getHeight());
            if(spell != null) {
                batch.draw(spellTexture, getX() + drawOffset, getY() + drawOffset,
                        getWidth() - drawOffset*2, getHeight() - drawOffset*2);
            }
        }
    }

    public void init(){
        createTable();
        pack();
    }

    private void createTable(){
        int t = 0;
//        int m = (spellPool.size()/3 - FightScreen.PREROLL_ROUNDS)*3;
        for (int i = 0; i < FightScreen.PREROLL_ROUNDS; i++) {
            int r = FightScreen.ROLL_ROUND + (ShardWar.fightScreen.getRound() -
                    ShardWar.fightScreen.getRound()%FightScreen.ROLL_ROUND) + t;
            add(new Label(String.valueOf(r), ShardWar.main.getAssetLoader().getSkin()));
            for (int j = 0; j < 3; j++) {
                add(new Container(spellPool.get(t)));
                t++;
            }
            row();
        }
    }

    public void nextRoll(){
        clear();
        createTable();
    }



}
