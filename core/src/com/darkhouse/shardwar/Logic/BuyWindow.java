package com.darkhouse.shardwar.Logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.*;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.BrickWall;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.EnergyWall;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.SpikeWall;
import com.darkhouse.shardwar.Logic.GameEntity.Wall.Wall;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.ShardWar;

public abstract class BuyWindow<T extends GameObject.ObjectPrototype, O extends GameObject> extends Window {

    private static class BuyItem<T extends GameObject.ObjectPrototype, O extends GameObject> extends Table {
        private Slot<T, O> owner;
        private T object;

        public BuyItem(final T object, Slot<T, O> owner) {
            this.owner = owner;
            this.object = object;
            add(new Image(object.getTexture())).spaceRight(5f);
            add(new Label(object.getName(), ShardWar.main.getAssetLoader().getSkin())).fill().expand();
            add(new Label(" " + object.getCost(), ShardWar.main.getAssetLoader().getSkin())).fill();
            add(new Image(ShardWar.main.getAssetLoader().get("shard.png", Texture.class)));
//            debug();

            init();
        }

        private void init(){
            addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    owner.build(object);
                    return true;
                }
            });
        }

    }

//    private Slot owner;

    public static class TowerWindow extends BuyWindow{

        public TowerWindow(Slot<Tower.TowerPrototype, Tower> owner) {
            super(owner, "Tower");
        }

        @Override
        protected Tower.TowerPrototype[] getItems() {
            return new Tower.TowerPrototype[]{
                    new Shotgun.P(),
                    new Assault.P(),
                    new Rocket.P()};
        }
    }
    public static class WallWindow extends BuyWindow{

        public WallWindow(Slot<Wall.WallPrototype, Wall> owner) {
            super(owner, "Wall");
        }

        @Override
        protected Wall.WallPrototype[] getItems() {
            return new Wall.WallPrototype[]
                    {new BrickWall.P(),
                     new EnergyWall.P(1),
                     new SpikeWall.P(2)};
        }
    }

    public BuyWindow(Slot<T, O> owner, String title) {
        super(title, ShardWar.main.getAssetLoader().getSkin(), "description");
        getTitleLabel().setAlignment(Align.center);
        setMovable(false);
//        if(type == Tower.class) {
////            getTitleLabel().setText("Tower");
////            BuyItem<Tower> item1 = new BuyItem<T>(new SniperTower(), owner);
////            BuyItem<Tower> item2 = new BuyItem<T>(new AssaultTower(), owner);
////
////            add(item1).row();
////            add(item2);
//
//        }
//        if(type == Wall.class) {
//            getTitleLabel().setText("Wall");
//        }

        defaults().align(Align.left);
        for(T t:getItems()){
            add(new BuyItem<T, O>(t, owner)).row();
        }


//        this.owner = owner;

        pack();
    }

    abstract protected T[] getItems();

    public void show(){
        setVisible(true);
        toFront();
    }

    public void hide(){
        setVisible(false);
    }


    public void init(Stage stage) {
        stage.addActor(this);
    }




}
