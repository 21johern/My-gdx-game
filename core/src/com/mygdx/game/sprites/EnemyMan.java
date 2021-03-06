package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
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

public class EnemyMan extends Enemy {
    public FixtureDef EnemyFixtureDef;
    public int getWidth() {
        return width;
    }
    protected int width;
    public int getHeight() {
        return height;
    }
    protected int height;

    public EnemyMan (float x, float y, World world, Player player) {
        super(x, y, world, player);

//Replace images once fixed.
        StabCharacter = new Texture("jumpSprite.png");
        EnemyWalk = new Texture("walksprite.png");

//Correct once information known.
        walk = new Animations(new TextureRegion(EnemyWalk),4, 0.4f,2,2 );
        stab = new Animations(new TextureRegion(StabCharacter),8, 1f,4,2 );

        width = getTexture().getRegionWidth();
        height = getTexture().getRegionHeight();
        bounds.setVertices(new float[] {-1, -1,(getWidth() * State.PIXEL_TO_METER) + 1, -1,
                (getWidth() * State.PIXEL_TO_METER) + 1, (getHeight() * State.PIXEL_TO_METER) + 1, -1, (getHeight() * State.PIXEL_TO_METER) + 1});
        polygon.set(new float[] {0, 0,(getWidth() * State.PIXEL_TO_METER), 0,
                (getWidth() * State.PIXEL_TO_METER), (getHeight() * State.PIXEL_TO_METER), 0, (getHeight() * State.PIXEL_TO_METER)});
        EnemyFixtureDef = new FixtureDef();
        EnemyFixtureDef.shape = polygon;
        EnemyFixtureDef.density = 0.5f;
        EnemyFixtureDef.friction = 0.4f;

        EnemyBody.createFixture(EnemyFixtureDef);
    }

    public void update(float dt){

        detection.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 4);


        position.set(EnemyBody.getPosition(), 0);
        bounds.setPosition(EnemyBody.getPosition().x, EnemyBody.getPosition().y);
        attackRange.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 1);
        detection.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 4);
        atkCount -= 1;
        if (isDetected()) {
            followPlayer();
        }
        if (isInAttackRange()) {
            attackPlayer();
        }
        vel = this.EnemyBody.getLinearVelocity();
//        System.out.println(vel.x);
        if (EnemyMActivity == "Walking") {
            stab.pause();
            walk.resume(dt);
        }
        if (EnemyMActivity == "Jumping") {
            walk.pause();
            stab.resume(dt);
        }
        //position.set(EnemyBody.getPosition(), 0);
    }

    public TextureRegion getTexture() {
        if (EnemyMActivity == "Jumping"){
            return stab.getFrame();
        }
        return walk.getFrame();
    }

    public void walkLeft() {
        EnemyMActivity = "Walking";
        if (faceRight) {
            walk.flipFrames();
            stab.flipFrames();
        }
        faceRight = false;
        EnemyBody.applyLinearImpulse(-.05f,0f,EnemyBody.getPosition().x/2,EnemyBody.getPosition().y/2,true);
    }
    public void walkRight() {
        EnemyMActivity = "Walking";
        if (!faceRight) {
            walk.flipFrames();
            stab.flipFrames();
        }
        faceRight = true;
        EnemyBody.applyLinearImpulse(.05f,0f,EnemyBody.getPosition().x/2,EnemyBody.getPosition().y/2,true);
    }
    public void stab() {
        EnemyMActivity = "Jumping";
        EnemyBody.applyLinearImpulse(0f,.15f,EnemyBody.getPosition().x/2,EnemyBody.getPosition().y/2,true);

        float delay = 1;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                stab.pause();
                EnemyMActivity = "Walking";
            }
        }, delay);
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

    public void dispose(){
        EnemyWalk.dispose();
        StabCharacter.dispose();
    }
}