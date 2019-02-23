package com.darkhouse.shardwar.Logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.AssaultTower;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

public class Slot<T extends GameObject.ObjectPrototype, O extends GameObject> extends Entity {

    private boolean player;
    private Player user;
    protected O object;
    protected BuyWindow tooltip;
    private FightScreen owner;
    private ProgressBar hpBar;
    private int line;
    private int row;
    private int selected[];// = -1;
    private int numberSelected;
    private int maxTargets;
    private Image targeter[];

    public void setTargeter(Image i){
//        System.out.println(Arrays.toString(targeter));
        targeter[numberSelected] = i;
    }
    public void flushTargeter(){
        for (int i = 0; i < targeter.length; i++) {
            targeter[i] = null;
        }
    }
    public Image getTargeter(){
        return targeter[numberSelected];
    }

    public FightScreen getOwner() {
        return owner;
    }

    public int getLine() {
        return line;
    }
    public int getRow() {
        return row;
    }

    public O getObject() {
        return object;
    }

//    public void setObject(T object) {
//        this.object = object;
//    }
    public void clearObject(){
        this.object = null;
    }

    public void select(int selected) {
        this.selected[numberSelected] = selected;
    }
    public void flushSelect(){
        for (int i = 0; i < selected.length; i++) {
            selected[i] = -1;
        }
    }
    public void endSelect(){
        numberSelected++;
//        System.out.println("selected " + Arrays.toString(this.selected));
        if(numberSelected >= maxTargets){
            numberSelected = 0;
        }
    }


    public boolean isNoSelected(){
        boolean b = false;
        for (int i = 0; i < selected.length; i++) {
            if(selected[i] == -1) b = true;
        }
        return b;
    }

    public int[] getSelected() {
        return selected;
    }

    @Override
    public boolean isExist() {
        return object != null;
    }

    @Override
    public Vector2 getShootPosition(int line) {
        return new Vector2(getX() + getParent().getX() + getWidth()/2, getY() + getParent().getY() + getHeight()/2);
    }

    public Slot(Texture texture, boolean player, FightScreen owner, Player user, int line, int row) {
        super(getTexture(texture, player));
        this.player = player;
        this.owner = owner;
        this.user = user;
        this.line = line;
        this.row = row;
        init();
        selected = new int[]{-1};
    }

    private static TextureRegion getTexture(Texture base, boolean player){
        if(player){
            return new TextureRegion(base);
        }else {
            TextureRegion r = new TextureRegion(base);
            r.flip(false, true);
            return r;
        }
    }

    public boolean empty(){
        return object == null;
    }

    private void fadeIn(){
        AlphaAction a = new AlphaAction();
        a.setAlpha(0.4f);
        addAction(a);
    }
    private void fadeOut(){
        AlphaAction a = new AlphaAction();
        a.setAlpha(1f);
        addAction(a);
    }

    protected void init(){
        fadeIn();
        if(true) {//player
            addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if(user == owner.getCurrentPlayerObject()) {
                        if (empty() && getColor().a != 1f) {
                            fadeOut();
                        }
                    }
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                    if(user == owner.getCurrentPlayer()) {
                        if (empty()) {
                            fadeIn();
                        }
//                    }
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(user == owner.getCurrentPlayerObject()) {
                        owner.hideTooltips();
                        if (object == null) {
                            tooltip.show();
                        }else {
                            if(owner.getRound() != 1) {
                                selectTarget();
                            }
                        }
                    }else owner.hideTooltips();
                    return true;
                }
            });
        }
    }

    private void selectTarget(){
        if(object instanceof Tower){
            owner.setTargetSlot(this);
        }
    }

    public void initTooltip(){
        tooltip.setPosition(getX() + getParent().getX() + 35, getY() + getParent().getY() + 35);
//        AlphaAction a = new AlphaAction();
//        a.setAlpha(0.0f);
//        tooltip.addAction(a);
        tooltip.setVisible(false);
        tooltip.init(getStage());
        //getStage().addActor(tooltip);
    }
    public void hideTooltip(){
        tooltip.hide();
    }


    public void build(T prototype){
        if(this.object == null && user.deleteShards(prototype.getCost())){
            this.object = ((O) prototype.getObject());//TODO this works but need remake
//            this.object = new Tower(object);
            object.setSlot(this);
            object.setOwner(owner.getCurrentPlayerObject());
            fadeOut();
            tooltip.hide();

            initHpBar();

            if(object instanceof AssaultTower){
                selected = new int[]{-1, -1};
                targeter = new Image[2];
                maxTargets = 2;
            }else {
                selected = new int[]{-1};
                maxTargets = 1;
                targeter = new Image[1];
            }

        }
    }

    private void initHpBar(){

        int height = 8;
        hpBar = new ProgressBar(0, object.getMaxHealth(), 0.2f, false,
                ShardWar.main.getAssetLoader().getSkin(), "health-bar");
        hpBar.setValue(object.getHealth());
        float yCoord;
        if(player) yCoord = getY() + getParent().getY() - 10;
        else yCoord = getY() + getParent().getY() + getHeight() + 10 - height;
        hpBar.setPosition(getX() + getParent().getX(), yCoord);
        hpBar.setSize(getWidth(), height);
        hpBar.getStyle().background.setMinHeight(height);
        hpBar.getStyle().knobBefore.setMinHeight(height - 2);
//            hpBar.debug();
        getStage().addActor(hpBar);
    }

    public void hasChanged(){
        updateHpBar();
        updateListeners();
    }
    private void updateListeners(){
        if(object == null) fadeIn();
    }
    private void updateHpBar(){
        if(object != null) hpBar.setValue(object.getHealth());
        else if(hpBar != null) {
            hpBar.remove();
            hpBar = null;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        if(player){
//            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
//        }else {
////            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
//            batch.draw(texture, getX(), getY(), getWidth(), getHeight(),
//                    0, 0, texture.getWidth(), texture.getHeight(), false, true);
//        }

        if(object != null) {
            if(player) batch.draw(object.getTexture(), getX(), getY(), getWidth(), getHeight());
            else batch.draw(object.getTexture(), getX(), getY(), getWidth(), getHeight(),
                    0, 0, ((int) getWidth()), ((int)getHeight()), false, true);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(object != null) {
            object.physic(delta);
        }
    }

    @Override
    public String toString() {
        return "[Object: " + object + " row:" + row + " line:" + line + "]";
    }

    @Override
    public void dmg(int dmg, GameObject source) {
        object.dmg(dmg, source);
    }
}
