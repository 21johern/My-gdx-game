package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Controller;
import com.mygdx.game.states.BossLevel;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.states.State;

public class LevelTwoPlayer {

    private Vector3 position;
    private Texture walkCharacter, jumpCharacter, attackCharacter;

    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walk, jump, attack;
    float stateTime;
    private Controller controller;


    private String Activity, LastActivity;
    private boolean faceRight;
    public BodyDef bodyDef;
    public Body player2Body;
    public FixtureDef fixtureDef;
    public Vector2 vel;
    public int health;
    public TextureRegion walkRegion;
    public TextureRegion jumpRegion;
    public TextureRegion attackRegion;
    public TextureRegion region;
    private boolean isAttacking, isJumping;
    private final float MAX_VELOCITY = .01f;
    public PolygonShape polygon;
    public Polygon bounds;
    public Rectangle AtkHitbox;
    public int getWidth() {
        return width;
    }

    private int width;

    public int getHeight() {
        return height;
    }

    private int height;

    public LevelTwoPlayer(float x, float y, PlayState LevelTwo){
        controller = new Controller();

        jumpCharacter = new Texture("jumpSprite.png");
        walkCharacter = new Texture("walksprite.png");
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
        health = 2;
        Activity = "none";
        LastActivity = "none";
        isAttacking = false;
        isJumping = false;
        width = getTexture(Gdx.graphics.getDeltaTime()).getRegionWidth();
        height = getTexture(Gdx.graphics.getDeltaTime()).getRegionHeight();

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        player2Body = LevelTwo.world.createBody(bodyDef);

        polygon = new PolygonShape();
        bounds = new Polygon();
        bounds.setVertices(new float[] {((getWidth()/5) * State.PIXEL_TO_METER), 0,((getWidth()-2) * State.PIXEL_TO_METER), 0,
                ((getWidth()-2) * State.PIXEL_TO_METER), ((getHeight()-4) * State.PIXEL_TO_METER), ((getWidth()/5) * State.PIXEL_TO_METER), ((getHeight()-4) * State.PIXEL_TO_METER)});
        polygon.set(bounds.getVertices());

        AtkHitbox = new Rectangle();
        AtkHitbox.set(((getWidth()/5) * State.PIXEL_TO_METER), ((getHeight()) * State.PIXEL_TO_METER),
                ((getWidth()+4) * State.PIXEL_TO_METER), ((getHeight()-4) * State.PIXEL_TO_METER));

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;

        player2Body.createFixture(fixtureDef);
        position = new Vector3(player2Body.getPosition(), 0);
        player2Body.setFixedRotation(true);
        vel = this.player2Body.getLinearVelocity();
        bounds.setPosition(player2Body.getPosition().x, player2Body.getPosition().y);

        faceRight = true;

    }

    public void update(float dt){

        if(player2Body.getPosition().y <= 0){
            health = 0;
        }

        flipFrames();

        position.set(player2Body.getPosition(), 0);
        AtkHitbox.setPosition(player2Body.getPosition().x, player2Body.getPosition().y);
        bounds.setPosition(player2Body.getPosition().x, player2Body.getPosition().y);

        if (isAttacking) {
            Activity = "Attacking";
            if (attack.isAnimationFinished(stateTime)) {
                isAttacking = false;
            }
        } else if (isJumping) {
            Activity = "Jumping";

            if (player2Body.getLinearVelocity().y == 0) {
                isJumping = false;
            }
        } else {
            Activity = "Walking";
        }
    }

    public void walkLeft() {
        if(vel.x < MAX_VELOCITY) {
            player2Body.applyLinearImpulse(-.015f, 0f, getPosition().x / 2, getPosition().y / 2, true);
        }
        isAttacking = false;
        isJumping = false;
    }
    public void walkRight() {
        if(vel.x > -MAX_VELOCITY){
            player2Body.applyLinearImpulse(.015f,0f,getPosition().x/2,getPosition().y/2,true);
        }
        isAttacking = false;
        isJumping = false;
    }
    public void jump() {
        if(vel.y == 0) {
            player2Body.applyLinearImpulse(0f, .55f, getPosition().x / 2, getPosition().y / 2, true);
        }
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
        if (Activity.equals("Walking")) {
            if ((player2Body.getLinearVelocity().x < 0 && faceRight)) {
                flipAll(walk);
                flipAll(jump);
                flipAll(attack);
                faceRight = false;
            } else if ((player2Body.getLinearVelocity().x > 0 && !faceRight)) {
                flipAll(walk);
                flipAll(jump);
                flipAll(attack);
                faceRight = true;
            }
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

    public void dispose() {
        walkCharacter.dispose();
        jumpCharacter.dispose();
        attackCharacter.dispose();
    }
}
