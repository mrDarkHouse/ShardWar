package com.darkhouse.shardwar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.darkhouse.shardwar.Logic.DamageReceiver;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;

public class Player extends Entity {
    private int shards;
    private float[] lineX;

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
        shards += value;
    }

    @Override
    public Vector2 getShootPosition(int line) {
        return new Vector2(lineX[line], getY());
    }

    public Player() {
        super(ShardWar.main.getAssetLoader().get("shard.png", Texture.class));


        shards = 10;
    }
    public void setShootCoord(float[] lineX){
        this.lineX = lineX;
    }

    @Override
    public void dmg(int dmg, GameObject source) {
        if(!deleteShards(dmg)){
            loose();
        }
    }

    private void loose(){

    }


}
