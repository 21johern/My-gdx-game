package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.states.State;


public class Enemy {
    protected Vector3 position;
    protected BodyDef EnemybodyDef;
    protected Body EnemyBody;
    protected Texture EnemyWalk;
    protected Texture StabCharacter;
    protected Animations walk;
    protected Animations stab;
    protected String EnemyMActivity;
    protected boolean faceRight;
    public Vector2 vel;
    public PlayState game;
    public Circle attackRange;
    public Circle detection;
    public Player player;
    protected Polygon bounds;
    protected PolygonShape polygon;
    public int atkCount;

    public Enemy(float x, float y, World world, Player player){
        this.player = player;
        EnemyMActivity = "none";
        faceRight = true;
        position = new Vector3(x, y, 0);
        bounds = new Polygon();
        polygon = new PolygonShape();
        EnemybodyDef = new BodyDef();
        EnemybodyDef.type = BodyDef.BodyType.DynamicBody;
        EnemybodyDef.position.set(x, y);
        EnemyBody = world.createBody(EnemybodyDef);

        EnemyBody.setFixedRotation(true);
        vel = EnemyBody.getLinearVelocity();
        attackRange = new Circle();
        attackRange.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 2);
        detection = new Circle();
        detection.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 4);

        atkCount = 0;
    }

    public void update(float dt) {
        bounds.setPosition(EnemyBody.getPosition().x, EnemyBody.getPosition().y);
        attackRange.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 2);
        detection.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 4);
        atkCount -= 1;
        if (isDetected()) {
            followPlayer();
        }
        if (isInAttackRange()) {
            attackPlayer();
        }
    }

    private boolean isDetected() {
        // if circle of detection contains player's center, return true
        if (detection.contains(player.playerBody.getWorldCenter())) {
            return true;
        }
        return false;
    }

    private boolean isInAttackRange() {
        if(attackRange.contains(player.playerBody.getWorldCenter())){
            return true;
        }
        return false;
    }

    private void followPlayer() {
        // if enemy is left of player, move right
        if (EnemyBody.getPosition().x < player.getPosition().x) {
            EnemyBody.applyLinearImpulse(new Vector2(.02f, 0), new Vector2(EnemyBody.getPosition().x, EnemyBody.getPosition().y), true);
        } else if (EnemyBody.getPosition().x > player.getPosition().x){
            // else, if enemy is right of player, move left
            EnemyBody.applyLinearImpulse(new Vector2(-.02f, 0), new Vector2(EnemyBody.getPosition().x, EnemyBody.getPosition().y), true);
        }
    }
    private boolean attackPlayer(){
        // run anim
        if (Intersector.overlapConvexPolygons(bounds, player.bounds) && atkCount <= 0) {
            player.health -= 1;
            atkCount = 60;
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    stab.pause();
                    EnemyMActivity = "Walking";
                }
            }, 1);
            return true;
        }

        return false;
    }
    public Vector3 getPosition() {
        return position;
    }

    public Body getBody() {
        return EnemyBody;
    }
}
