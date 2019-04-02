package com.darkhouse.shardwar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.darkhouse.shardwar.Logic.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.Spell;
import com.darkhouse.shardwar.Logic.SpellPanel;

public class Player extends Entity {
    private int shards;
    private float[] lineX;
    private ProgressBar healthBar;
    private final int maxShards = 40;

    private SpellPanel spellPanel;

    public void setSpellPanel(SpellPanel spellPanel) {
        this.spellPanel = spellPanel;
    }

    public boolean addSpell(Spell.SpellPrototype spellPrototype){
        return spellPanel.addSpell(spellPrototype);
    }


    public int getShards() {
        return shards;
    }
    public boolean deleteShards(int cost){
        if(shards >= cost){
            shards -= cost;
            return true;
        }else return false;
    }
    public void addShards(int value){
        if(shards + value <= maxShards) shards += value;
        else shards = maxShards;
        updateBar();
    }

    @Override
    public Vector2 getShootPosition(int line) {
        return new Vector2(lineX[line], getY());
    }

    @Override
    public boolean isExist() {
        return true;
    }

    public Player() {
//        super(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));


    }

    public void init(){
//        healthBar = new ProgressBar(0, maxShards, 0.1f, true, ShardWar.main.getAssetLoader().getSkin(), "health-bar");
//        healthBar.setSize(Gdx.graphics.getWidth(), 40);
//        healthBar.getStyle().background.setMinWidth(Gdx.graphics.getWidth());
//        healthBar.getStyle().knobBefore.setMinWidth(Gdx.graphics.getWidth() - 2);
//        healthBar.setPosition(0, 0);
//        System.out.println(healthBar.getX() + " " + getX() + " "  +
//                healthBar.getY() + " " + getY());
//        updateBar();
//        healthBar.pack();

        addShards(10);
    }

    public void setShootCoord(float[] lineX){
        this.lineX = lineX;
    }

    @Override
    public void dmg(int dmg, DamageSource source) {
        if(!deleteShards(dmg)){
            shards = 0;
            updateBar();
            loose();
        }else updateBar();

    }

    private void updateBar(){
//        healthBar.setValue(shards);
    }


    private void loose(){
        System.out.println("Loose");
        System.exit(0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//        healthBar.draw(batch, parentAlpha);
    }
}
