package com.darkhouse.shardwar.Logic;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
import com.darkhouse.shardwar.Logic.Slot.Slot;
import com.darkhouse.shardwar.ShardWar;

public class Projectile extends Image {

//    private Texture texture;
//    protected boolean isMainProjectile;
//    protected Array<Ability.IAfterHit> afterHitAbilties;
//    public void addAbilities(Ability.IAfterHit a){
//        afterHitAbilties.add(a);
//    }

    protected Slot<Tower.TowerPrototype, Tower> slot;
    protected Tower tower;
    protected Slot target;
    protected float speed;
    private int line;
    private int row = -1;
    private int dmg = -1;

//    protected float dmgMultiplayer = 1.0f;
//
//    public void setDmgMultiplayer(float dmgMultiplayer) {
//        this.dmgMultiplayer = dmgMultiplayer;
//    }

    protected Vector2 position = new Vector2();
    protected Vector2 velocity = new Vector2();
    protected Vector2 movement = new Vector2();
    protected Vector2 targetV = new Vector2();
    protected Vector2 dir = new Vector2();


    //TODO remove this shit in constructors

    public Projectile(Slot<Tower.TowerPrototype, Tower> slot, Vector2 startLocation, Slot target, int line,
                      int row, int dmg){//separate from tower
        this(slot, startLocation, target, line, row);
        this.dmg = dmg;
    }


    public Projectile(Slot<Tower.TowerPrototype, Tower> slot, Vector2 startLocation, Slot target, int line) {
        this(slot, startLocation, line);
        this.target = target;
        move(0);//rotate to target
    }
    public Projectile(Slot<Tower.TowerPrototype, Tower> slot, Vector2 startLocation, Slot target, int line, int row) {
        this(slot, startLocation, target, line);
        this.row = row;
    }


    public Projectile(Slot<Tower.TowerPrototype, Tower> slot, Vector2 startLocation, int line){//for non target projectiles
        super(ShardWar.main.getAssetLoader().get(
                slot.getObject().getObjectPrototype().getProjectileTexture(), Texture.class));
        this.tower = slot.getObject();
        this.slot = slot;
        this.speed = 400;
        this.line = line;

        setX(startLocation.x);
        setY(startLocation.y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        move(delta);
    }

    private boolean checkIfTargetDie(){
        if(!target.isExist()){
            if(row == -1) {//global
                target = ShardWar.fightScreen.searchTarget(slot, line, row);
                return false;
            }else {
                removeProjectile();
                return true;
            }
        }else return false;
    }

    protected void move(float delta){
        if (delta > 0.05) System.out.println(delta);
        if(checkIfTargetDie()) return;
        position.set(getX(), getY());
        targetV.set(target.getObject().getXShoot(line),
                target.getObject().getYShoot(line));
        dir.set(targetV).sub(position).nor();
        velocity.set(dir).scl(speed);
        movement.set(velocity).scl(delta);
        if(position.dst2(targetV) > movement.len2()){
            position.add(movement);
        }else {
            position.set(targetV);
//            tower.hitTarget(target, isMainProjectile);
//            if(tower.hitTarget(target, /*((int) (*/tower.getDmg(target, isMainProjectile) * dmgMultiplayer)/*))*/){
//                afterHit();
//            }

            if(dmg == -1) tower.attack(target.getObject());
            else target.getObject().dmg(dmg, tower, tower.isImmune());
//            System.out.println("attaked " + target);
            removeProjectile();
//            Map.projectiles.remove(this);
        }
        setRotation(dir.angle());

        //rotateBy(degree);

        setX(position.x);
        setY(position.y);
    }

    private void removeProjectile(){
        slot.getOwner().projectiles.removeValue(this, true);
        remove();
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        setPosition(getX(), getY());
//        setSize(12, 8);//default texture size sets without it
        setRotation(getRotation());
//        draw(batch, 1);
    }
}
