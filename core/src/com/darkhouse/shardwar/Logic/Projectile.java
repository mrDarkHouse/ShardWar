package com.darkhouse.shardwar.Logic;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.darkhouse.shardwar.Logic.GameEntity.Entity;
import com.darkhouse.shardwar.Logic.GameEntity.GameObject;
import com.darkhouse.shardwar.Logic.GameEntity.Tower.Tower;
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
    protected Entity target;
    protected float speed;
    private int line;

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

    public Projectile(Slot<Tower.TowerPrototype, Tower> slot, Vector2 startLocation, Entity target, int line) {//additional are in MultiShot, Glaive

//
//        super(tower.getTowerPrototype().getPrototype().getProjectileTexture());
//        this.tower = tower;
//        this.target = target;
//        this.speed = tower.getTowerPrototype().getPrototype().getProjectileSpeed();//custom
//        this.isMainProjectile = isMain;
//
//        afterHitAbilties = new Array<Ability.IAfterHit>();
////        texture = tower.getTowerPrototype().getPrototype().getProjectileTexture();//WHAT THE FUCK IT??!!
//        // (prototype.getPrototype().getPrototype().getAnotherPrototype.getTowerPrototype.getAnotherFuckingPrototype)
//
//        //position.set(tower.getX(), tower.getY());
////        setX(tower.getCenter().x);
////        setY(tower.getCenter().y);
//        setX(startLocation.x);
//        setY(startLocation.y);


        this(slot, startLocation, line);
        this.target = target;
    }

    public Projectile(Slot<Tower.TowerPrototype, Tower> slot, Vector2 startLocation, int line){//for non target projectiles
        super(ShardWar.main.getAssetLoader().get(
                slot.getObject().getObjectPrototype().getProjectileTexture(), Texture.class));
        this.tower = slot.getObject();
        this.slot = slot;
        this.speed = 200;
        this.line = line;

        setX(startLocation.x);
        setY(startLocation.y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        move(delta);
    }

    protected void move(float delta){
        position.set(getX(), getY());
        targetV.set(target.getXShoot(line),
                target.getYShoot(line));
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

            tower.attack(target);
//            System.out.println("attaked " + target);
            slot.getOwner().projectiles.removeValue(this, true);
            remove();
//            Map.projectiles.remove(this);
        }
        setRotation(dir.angle());

        //rotateBy(degree);

        setX(position.x);
        setY(position.y);
    }
//    private void hitTarget(){
//        target.hit(tower.getTowerPrototype().getDmg(), tower);
//    }

//    protected void afterHit(){
//        for (Ability.IAfterHit a:afterHitAbilties){
//            a.hit(target, ((int) (tower.getDmg(target, isMainProjectile) * dmgMultiplayer)), this);
//        }
//    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        setPosition(getX(), getY());
        setSize(12, 8);//default texture size sets without it
        setRotation(getRotation());
//        draw(batch, 1);
    }
}
