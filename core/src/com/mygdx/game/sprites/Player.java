package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Controller;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.states.State;

public class Player {

    private Vector3 position;
    private Texture walkCharacter, jumpCharacter, attackCharacter;

    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walk, jump, attack;
    float stateTime;
    private Controller controller;


    private String Activity, LastActivity;
    private boolean faceRight;
    public BodyDef bodyDef;
    public Body playerBody;
    public FixtureDef fixtureDef;
    public Vector2 vel;
    public TextureRegion walkRegion;
    public TextureRegion jumpRegion;
    public TextureRegion attackRegion;
    public TextureRegion region;
    private boolean isAttacking, isJumping;

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
        controller = new Controller();

        jumpCharacter = new Texture("jumpSprite.png");
        walkCharacter = new Texture("walkSprite.png");
        attackCharacter = new Texture("attackSprite.png");

        TextureRegion[][] tmp1 = TextureRegion.split(walkCharacter, walkCharacter.getWidth() / 2, walkCharacter.getHeight() / 2);
        TextureRegion[][] tmp2 = TextureRegion.split(jumpCharacter, jumpCharacter.getWidth() / 2, jumpCharacter.getHeight() / 4);
        TextureRegion[][] tmp3 = TextureRegion.split(attackCharacter, attackCharacter.getWidth() / 2, attackCharacter.getHeight() / 4);

        Array<TextureRegion> walkFrames = new Array<TextureRegion>();
        Array<TextureRegion> jumpFrames = new Array<TextureRegion>();
        Array<TextureRegion> attackFrames = new Array<TextureRegion>();

        int count = 0;

        // For loop to add frames from the split TextureRegion into the Array
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (count < 4) {
                    walkFrames.add(tmp1[j][i]); // Fill in Array with frames
                    count++;
                }
            }
        }

        int count2 = 0;

        // For loop to add frames from the split TextureRegion into the Array
        for (int k = 0; k < 2; k++) {
            for (int l = 0; l < 4; l++) {
                if (count2 < 8) {
                    jumpFrames.add(tmp2[l][k]); // Fill in Array with frames
                    count2++;
                }
            }
        }

        int count3 = 0;

        // For loop to add frames from the split TextureRegion into the Array
        for (int m = 0; m < 2; m++) {
            for (int n = 0; n < 4; n++) {
                if (count3 < 7) {
                    attackFrames.add(tmp3[n][m]); // Fill in Array with frames
                    count3++;
                }
            }
        }

        walk = new Animation<TextureRegion>(0.125f, walkFrames); // Set Animations to use Array of frames
        jump = new Animation<TextureRegion>(0.125f, jumpFrames); // Set Animations to use Array of frames
        attack = new Animation<TextureRegion>(0.125f, attackFrames); // Set Animations to use Array of frames

        region = new TextureRegion();

        stateTime = 0;
        Activity = "none";
        LastActivity = "none";
        isAttacking = false;
        isJumping = false;
        width = getTexture(Gdx.graphics.getDeltaTime()).getRegionWidth();
        height = getTexture(Gdx.graphics.getDeltaTime()).getRegionHeight();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        playerBody = playState.world.createBody(bodyDef);
        polygon = new PolygonShape();
        polygon.set(new float[] {0, 0,(getWidth() * State.PIXEL_TO_METER), 0,
                (getWidth() * State.PIXEL_TO_METER), (getHeight() * State.PIXEL_TO_METER), 0, (getHeight() * State.PIXEL_TO_METER)});
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;             
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

        playerBody.createFixture(fixtureDef);
        position = new Vector3(playerBody.getPosition(), 0);
        playerBody.setFixedRotation(true);
        polygon.dispose();
        vel = this.playerBody.getLinearVelocity();


        faceRight = true;

    }

    public void update(float dt){
        flipFrames();
//        vel = this.playerBody.getLinearVelocity();
//        System.out.println(vel.x);
        position.set(playerBody.getPosition(), 0);
//         if(Activity.equals("Jumping")){
//            Activity = "Jumping";
//            if (vel.y == 0) {
//                controller.upPressed = false;
//            }
//        } else{
//            Activity = "Walking";
//        }
        System.out.println(playerBody.getLinearVelocity().y);
        if (isAttacking) {
            Activity = "Attacking";
            if (attack.isAnimationFinished(stateTime)) {
                isAttacking = false;
            }
        } else if (isJumping) {
            Activity = "Jumping";

            if (playerBody.getLinearVelocity().y == 0) {
                isJumping = false;
            }
        } else {
            Activity = "Walking";
        }
    }

    public void walkLeft() {
        playerBody.applyLinearImpulse(-.05f,0f,getPosition().x/2,getPosition().y/2,true);
        isAttacking = false;
        isJumping = false;
    }
    public void walkRight() {
        playerBody.applyLinearImpulse(.05f,0f,getPosition().x/2,getPosition().y/2,true);
        isAttacking = false;
        isJumping = false;
    }
    public void jump() {
        playerBody.applyLinearImpulse(0f,.15f,getPosition().x/2,getPosition().y/2,true);
        isAttacking = false;
        isJumping = true;
    }
    public void attack() {
        isAttacking = true;
        isJumping = false;

    }


    public TextureRegion getTexture(float dt) {
        walkRegion = walk.getKeyFrame(stateTime, true);
        jumpRegion = jump.getKeyFrame(stateTime,false);
        attackRegion = attack.getKeyFrame(stateTime,false);
        region = walkRegion;
        if (Activity.equals("Attacking")){
            region = attackRegion;
        } else if (Activity.equals("Jumping")) {
            region = jumpRegion;
        } else if (Activity.equals("Walking")) {
            region = walkRegion;
        }

        stateTime = LastActivity.equals(Activity) ? stateTime + dt : 0;
        LastActivity = Activity;
        return region;
    }

    public void flipFrames(){
    if (Activity == "Walking")
        if ((playerBody.getLinearVelocity().x < 0 && faceRight)) {
            flipAll(walk);
            flipAll(jump);
            flipAll(attack);
            faceRight = false;
        } else if ((playerBody.getLinearVelocity().x > 0 && !faceRight)) {
            flipAll(walk);
            flipAll(jump);
            flipAll(attack);
            faceRight = true;
        }
    }

    private void flipAll (Animation anim) {
        for (Object obj : anim.getKeyFrames()) {
            TextureRegion reg = (TextureRegion) obj;
            reg.flip(true, false);
        }
    }


    public Vector3 getPosition() {
        return position;
    }
}
