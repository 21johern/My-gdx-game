package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.states.State;

public class Player {

    private Vector3 position;
    private Texture walkCharacter;
    private Texture jumpCharacter;
    private Animation walk;
    private Animation jump;
    private String Activity;
    private boolean faceRight;
    public BodyDef bodyDef;
    public Body playerBody;
    public FixtureDef fixtureDef;
    public Vector2 vel;

    public PolygonShape polygon;
    public int getWidth() {
        return width;
    }

    private int width;

    public int getHeight() {
        return height;
    }

    private int height;

    public Player(float x, float y, PlayState playState){

        jumpCharacter = new Texture("Spritejump.png");
        walkCharacter = new Texture("walksprite.png");
        walk = new Animation(new TextureRegion(walkCharacter),4, 0.4f,2,2 );
        jump = new Animation(new TextureRegion(jumpCharacter),8, 1f,4,2 );
        width = getTexture().getRegionWidth();
        height = getTexture().getRegionHeight();
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        playerBody = playState.world.createBody(bodyDef);
        polygon = new PolygonShape();
        polygon.set(new float[] {0, 0,(getWidth() * State.PIXEL_TO_METER), 0,
                (getWidth() * State.PIXEL_TO_METER), (getHeight() * State.PIXEL_TO_METER), 0, (getHeight() * State.PIXEL_TO_METER)});
//        polygon.setAsBox(getTexture().getRegionWidth()* State.PIXEL_TO_METER,
//                getTexture().getRegionHeight()*State.PIXEL_TO_METER);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;             
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

        playerBody.createFixture(fixtureDef);
        position = new Vector3(playerBody.getPosition(), 0);
        playerBody.setFixedRotation(true);
        polygon.dispose();
        vel = this.playerBody.getLinearVelocity();

        Activity = "none";
        faceRight = true;
    }

    public void update(float dt){
        vel = this.playerBody.getLinearVelocity();
        System.out.println(vel.x);
        if (Activity == "Walking") {
            jump.pause();
            walk.resume(dt);
        }
        if (Activity == "Jumping") {
            walk.pause();
            jump.resume(dt);
        }
        position.set(playerBody.getPosition(), 0);
    }

    public void walkLeft() {
        Activity = "Walking";
        if (faceRight) {
            walk.flipFrames();
            jump.flipFrames();
        }
        faceRight = false;
        playerBody.applyLinearImpulse(-.05f,0f,getPosition().x/2,getPosition().y/2,true);
    }
    public void walkRight() {
        Activity = "Walking";
        if (!faceRight) {
            walk.flipFrames();
            jump.flipFrames();
        }
        faceRight = true;
        playerBody.applyLinearImpulse(.05f,0f,getPosition().x/2,getPosition().y/2,true);
    }
    public void jump() {
        Activity = "Jumping";
        playerBody.applyLinearImpulse(0f,.15f,getPosition().x/2,getPosition().y/2,true);

        float delay = 1;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                jump.pause();
                Activity = "Walking";
            }
        }, delay);
    }


    public TextureRegion getTexture() {
        if (Activity == "Jumping"){
            return jump.getFrame();
        }
        return walk.getFrame();
    }

    public Vector3 getPosition() {
        return position;
    }
}
