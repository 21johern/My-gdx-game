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

    public Player(int x, int y, PlayState playState){
        jumpCharacter = new Texture("Spritejump.png");
        walkCharacter = new Texture("walksprite.png");
        walk = new Animation(new TextureRegion(walkCharacter),4, 0.4f,2,2 );
        jump = new Animation(new TextureRegion(jumpCharacter),8, 1f,4,2 );  Box2D.init();
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        playerBody = playState.world.createBody(bodyDef);
        polygon = new PolygonShape();
        polygon.setAsBox(getTexture().getRegionWidth()/2f,getTexture().getRegionHeight()/2f);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;             
        fixtureDef.density = 0.5f;
        playerBody.createFixture(fixtureDef);
        position = new Vector3(playerBody.getPosition(), 0);
        fixtureDef.friction = 0.4f;
        playerBody.setFixedRotation(true);
        vel = this.playerBody.getLinearVelocity();

        Activity = "none";
        faceRight = true;
        height = 0;
        width = 0;
    }

    public void update(float dt){
        vel = this.playerBody.getLinearVelocity();
        System.out.println(vel.x);
        position.set(playerBody.getPosition(), 0);
        if (Activity == "Walking") {
            width = 32;
            height = 50;
            jump.pause();
            walk.resume(dt);
        }
        if (Activity == "Jumping") {
            width = 64;
            height = 50;
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
        playerBody.applyLinearImpulse(-500000f,0f,getPosition().x/2,getPosition().y/2,true);
    }
    public void walkRight() {
        Activity = "Walking";
        if (!faceRight) {
            walk.flipFrames();
            jump.flipFrames();
        }
        faceRight = true;
        playerBody.applyLinearImpulse(500000f,0f,getPosition().x/2,getPosition().y/2,true);
    }
    public void jump() {
        Activity = "Jumping";
        playerBody.applyLinearImpulse(0f,500000f,getPosition().x/2,getPosition().y/2,true);

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
