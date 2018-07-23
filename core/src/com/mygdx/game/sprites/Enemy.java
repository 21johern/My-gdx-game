package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.states.PlayState;


public class Enemy {

    protected Vector3 position;
    protected Texture EnemyWalk;
    protected Texture StabCharacter;
    protected Animations walk;
    protected Animations stab;
    protected String EnemyMActivity;
    protected boolean faceRight;
    public Vector2 vel;



    public Enemy(float x, float y, PlayState playState){

        EnemyMActivity = "none";
        faceRight = true;
    }

    public Vector3 getPosition() {
        return position;
    }
}