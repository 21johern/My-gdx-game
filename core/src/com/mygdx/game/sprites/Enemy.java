package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.game.states.PlayState;


public class Enemy {
    protected BodyDef EnemybodyDef;
    public Body EnemyBody;
    protected Texture EnemyWalk;
    protected Texture StabCharacter;
    protected Animations walk;
    protected Animations stab;
    protected String EnemyMActivity;
    protected boolean faceRight;
    public Vector2 vel;
    public PlayState game;

    public Circle detection;
    public Player player;

    public Enemy(float x, float y, PlayState playState, Player player){
        game = playState;
        this.player = player;
        EnemyMActivity = "none";
        faceRight = true;

        EnemybodyDef = new BodyDef();
        EnemybodyDef.type = BodyDef.BodyType.DynamicBody;
        EnemybodyDef.position.set(x, y);
        EnemyBody = playState.world.createBody(EnemybodyDef);

        EnemyBody.setFixedRotation(true);
        vel = EnemyBody.getLinearVelocity();

        detection = new Circle();
        detection.set(EnemyBody.getPosition().x, EnemyBody.getPosition().y, 4);
    }

    public void update(float dt) {
        if (isDetected()) {
            followPlayer();
        }
    }

    private boolean isDetected() {
        // if circle of detection contains player's center, return true
        if (detection.contains(player.playerBody.getWorldCenter())) {
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

    public Body getBody() {
        return EnemyBody;
    }
}
