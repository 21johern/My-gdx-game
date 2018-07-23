package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.states.State;

public class EnemyMan extends Enemy {
    public BodyDef EnemybodyDef;
    public Body EnemyBody;
    public FixtureDef EnemyFixtureDef;
    public PolygonShape polygon;
    public int getWidth() {
        return width;
    }
    protected int width;
    public int getHeight() {
        return height;
    }
    protected int height;

    public EnemyMan (float x, float y, PlayState playState) {
        super(x, y, playState);

//Replace images once fixed.
        StabCharacter = new Texture("jumpSprite.png");
        EnemyWalk = new Texture("walkSprite.png");

//Correct once information known.
        walk = new Animations(new TextureRegion(EnemyWalk),4, 0.4f,2,2 );
        stab = new Animations(new TextureRegion(StabCharacter),8, 1f,4,2 );

        width = getTexture().getRegionWidth();
        height = getTexture().getRegionHeight();

        EnemybodyDef = new BodyDef();
        EnemybodyDef.type = BodyDef.BodyType.DynamicBody;
        EnemybodyDef.position.set(x, y);
        EnemyBody = playState.world.createBody(EnemybodyDef);
        polygon = new PolygonShape();
        polygon.set(new float[] {0, 0,(getWidth() * State.PIXEL_TO_METER), 0,
                (getWidth() * State.PIXEL_TO_METER), (getHeight() * State.PIXEL_TO_METER), 0, (getHeight() * State.PIXEL_TO_METER)});
        EnemyFixtureDef = new FixtureDef();
        EnemyFixtureDef.shape = polygon;
        EnemyFixtureDef.density = 0.5f;
        EnemyFixtureDef.friction = 0.4f;

        EnemyBody.createFixture(EnemyFixtureDef);
        position = new Vector3(EnemyBody.getPosition(), 0);
        EnemyBody.setFixedRotation(true);
        polygon.dispose();
        vel = this.EnemyBody.getLinearVelocity();





    }

    public void update(float dt){
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
        position.set(EnemyBody.getPosition(), 0);
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
        EnemyBody.applyLinearImpulse(-.05f,0f,getPosition().x/2,getPosition().y/2,true);
    }
    public void walkRight() {
        EnemyMActivity = "Walking";
        if (!faceRight) {
            walk.flipFrames();
            stab.flipFrames();
        }
        faceRight = true;
        EnemyBody.applyLinearImpulse(.05f,0f,getPosition().x/2,getPosition().y/2,true);
    }
    public void stab() {
        EnemyMActivity = "Jumping";
        EnemyBody.applyLinearImpulse(0f,.15f,getPosition().x/2,getPosition().y/2,true);

        float delay = 1;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                stab.pause();
                EnemyMActivity = "Walking";
            }
        }, delay);
    }
}