package com.darkhouse.shardwar.Logic.Slot;

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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darkhouse.shardwar.Logic.BuyWindow;
import com.darkhouse.shardwar.Logic.GameEntity.DamageSource;
import com.darkhouse.shardwar.Logic.GameEntity.Empty;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Spells.TowerSpells.Ability;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Player;
import com.darkhouse.shardwar.Screens.FightScreen;
import com.darkhouse.shardwar.ShardWar;

public abstract class Slot<T extends GameObject.ObjectPrototype, O extends GameObject> extends Entity {

    protected boolean player;
    private boolean flip;
    private int drawOffset;
    private Player user;
    protected O object;
    protected Empty emptyObject;
    protected BuyWindow tooltip;
    private FightScreen owner;
    private ProgressBar hpBar;
    private int column;
    private int row;
    public int selected[];// = -1;//TODO private
    public int selectedRow[];
    private int numberSelected;
    public int maxTargets;
    public Image targeter[];
    public boolean disable;

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
//        System.out.println(Arrays.toString(targeter));
        return targeter[numberSelected];
    }
    public Image[] getAllTargeters(){
        return targeter;
    }


    public boolean isDisable() {
        return disable;
    }
    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    protected TextureRegion choose;
    protected TextureRegion reserve;
    protected TextureRegion disableTexture;
    private boolean isChosen;
    private boolean reserved;//for multiply use

    public void reserve(){
        reserved = true;
    }
    public void unReserve(){
        reserved = false;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void choose(){
        isChosen = true;
//        setVisible(false);
//        a = new ColorAction();
//        a.setDuration(1f);
//        a.setEndColor(Color.ORANGE);
//        addAction(a);
//        setColor(Color.ORANGE);
    }
    public void unChoose(){
        isChosen = false;
//        setVisible(true);
//        setColor(Color.WHITE);
//        setColor(color);
//        removeAction(a);
//        getActions().clear();
//        fadeIn();

    }

    public Vector2 getCenter(){
        return new Vector2(getX() + getParent().getX() + getWidth() / 2,
                getY() + getParent().getY() + getHeight() / 2);
    }

    public FightScreen getOwner() {
        return owner;
    }

    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }

    public O getObject() {
        return object;
    }
    public Empty getEmptyObject() {
        return emptyObject;
    }
    public GameObject getSomeObject(){
        if(getObject() != null) return object;
        else return getEmptyObject();
    }

    //    public void setObject(T object) {
//        this.object = object;
//    }
    public void clearObject(){
        this.object = null;
//        this.object = new Empty();
    }

    public void select(int selected) {
        this.selected[numberSelected] = selected;
    }
    public void selectRow(int selected){
        this.selectedRow[numberSelected] = selected;
    }

    public void flushSelect(){
        for (int i = 0; i < selected.length; i++) {
            selected[i] = -1;
        }
        if(selectedRow != null){
            for (int i = 0; i < selectedRow.length; i++) {
                selectedRow[i] = -1;
            }
        }
        numberSelected = 0;
    }
    public void endSelect(){
        numberSelected++;
//        System.out.println("selected " + Arrays.toString(this.selected));
        if(numberSelected >= maxTargets){
            numberSelected = 0;
        }
    }


    public boolean isNoSelected(){
//        boolean b = false;
//        for (int i = 0; i < selected.length; i++) {
//            if(selected[i] == -1) b = true;
//        }
//        return b;
//        if(empty()) return false;
        boolean b = false;
        for (int aSelected : selected) {
            if (aSelected != -1) b = true;
        }
        return !b;
    }
    public int getNumberSelected(){
        int a = 0;
        for (int aSelected : selected) {
            if (aSelected != -1) a++;
        }
        return a;
    }

    public int[] getSelected() {
        return selected;
    }

    public int[] getSelectedRow() {
        return selectedRow;
    }

    @Override
    public boolean isExist() {
        return !empty();
    }


    public Slot(Texture texture, boolean player, boolean flip, int drawOffset, FightScreen owner, Player user, int column, int row) {
//        super(getTexture(texture, player));
        this.player = player;
        this.flip = flip;
        this.drawOffset = drawOffset;
        this.owner = owner;
        this.user = user;
        this.column = column;
        this.row = row;
        setDrawable(new TextureRegionDrawable(getTexture(texture)));
        init();
        selected = new int[]{-1};
    }

    protected TextureRegion getTexture(Texture base){
        if(!flip || player){
            return new TextureRegion(base);
        }else {
            TextureRegion r = new TextureRegion(base);
            r.flip(false, true);
            return r;
        }
    }

    public boolean empty(){
        return object == null;
//        return object.getClass() == Empty.class;
    }

    protected void fadeIn(){
        AlphaAction a = new AlphaAction();
        a.setAlpha(0.4f);
        addAction(a);
    }
    protected void fadeOut(){
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
                        if (empty() && !disable && getColor().a != 1f) {
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
                        if(disable) return true;
                        if (empty()) {
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
            if(!((Tower) object).isDisarm()) {
                owner.setTargetSlot((Slot<Tower.TowerPrototype, Tower>) this);
            }
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

    public void setObject(O object) {
        this.object = object;
    }

    public void setEmptyObject(Empty emptyObject) {
        this.emptyObject = emptyObject;
    }

    public void build(T prototype){
        if(empty() && user.deleteShards(prototype.getCost())){
            this.object = ((O) prototype.getObject());//TODO this works but need remake
//            this.object = new Tower(object);
            object.setSlot(this);
            object.setOwner(owner.getCurrentPlayerObject());
            object.init();
            fadeOut();
            tooltip.hide();

            initHpBar();

            selected = new int[]{-1};
            targeter = new Image[1];
            maxTargets = 1;
            for (Ability a:prototype.getAbilities()){
                a.setOwner(object);
                a.build();
            }

//            if(prototype.haveAbility(MultiShot.class)){
//                MultiShot a = prototype.getAbility(MultiShot.class);
//                int n = a.getShoots();
//                selected = new int[n];
//                for (int i = 0; i < n; i++) {
//                    selected[i] = -1;
//                }
//                targeter = new Image[n];
//                maxTargets = n;
//                ((Tower) object).setShootDelay(a.getShotDelay());
//            }else {
//                selected = new int[]{-1};
//                targeter = new Image[1];
//                maxTargets = 1;
//            }

//            if(object instanceof AssaultTower){//rework with ability
//                selected = new int[]{-1, -1};
//                targeter = new Image[2];
//                maxTargets = 2;
//            }else {
//                selected = new int[]{-1};
//                maxTargets = 1;
//                targeter = new Image[1];
//            }

        }
    }

    private void initHpBar(){
        int height = 8;
        hpBar = new ProgressBar(0, object.getMaxHealth(), 0.2f, false,
                ShardWar.main.getAssetLoader().getHpBarStyle(height));
        hpBar.setValue(object.getHealth());
        float yCoord;
        if(player) yCoord = getY() + getParent().getY() - 10;
        else yCoord = getY() + getParent().getY() + getHeight() + 10 - height;
        hpBar.setPosition(getX() + getParent().getX(), yCoord);
        hpBar.setSize(getWidth(), height);
//        hpBar.getStyle().background.setMinHeight(height);
//        hpBar.getStyle().knobBefore.setMinHeight(height - 2);
//            hpBar.debug();
//        hpBar.pack();
        getStage().addActor(hpBar);
    }

    public void hasChanged(){
        updateHpBar();
        updateListeners();
    }
    private void updateListeners(){
        if(empty()) fadeIn();
    }
    public void updateHpBar(){
        if(!empty()) hpBar.setValue(object.getHealth());
        else if(hpBar != null) {
            hpBar.remove();
            hpBar = null;
        }
    }
    public void clearSelecting(){
//        for (int i = 0; i < selected.length; i++) {
//            selected[i] = -1;
//        }
        selected = null;
        selectedRow = null;
//        if(selectedRow != null){
//            for (int i = 0; i < selectedRow.length; i++) {
//                selectedRow[i] = -1;
//            }
//        }
        targeter = null;
        numberSelected = 0;

//        for (int i = 0; i < targeter.length; i++) {
//            targeter[i] = null;
//        }
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

        if(/*object != null*/ !empty()) {
            if(!flip || player) batch.draw(object.getTexture(), getX() + drawOffset, getY() + drawOffset,
                    getWidth() - drawOffset*2, getHeight() - drawOffset*2);
            else batch.draw(object.getTexture(), getX() + drawOffset, getY() + drawOffset,
                    getWidth() - drawOffset*2, getHeight() - drawOffset*2,
                    0, 0, ((int) getWidth()), ((int)getHeight()), false, true);
        }
        if(isChosen){
            batch.draw(choose, getX(), getY(), getWidth(), getHeight());
        }
        if(reserved){
            batch.draw(reserve, getX(), getY(), getWidth(), getHeight());
        }
        if(disable){
            batch.draw(disableTexture, getX(), getY(), getWidth(), getHeight());
        }
        drawEffects(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(object != null) {
            object.physic(delta);
        }
    }

    private void drawEffects(Batch batch){
        if(getSomeObject() != null) {
            getSomeObject().effectBar.draw(batch, 1);
        }
    }

    @Override
    public String toString() {
        return "[Object: " + object + " row:" + row + " column:" + column + "]";
    }

    @Override
    public int dmg(int dmg, DamageSource source) {
        return object.dmg(dmg, source);
    }
}
