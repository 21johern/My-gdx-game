package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class EnemyM {
    public Vector3 getPosition() {
        return position;
    }

    private Vector3 position;
    private Vector3 velocity;
    public Texture enemyM;
    private Animation anim;
    private static final int GRAVITY = -15;
    private boolean faceRight;

    public int getWidth() {
        return width;
    }

    private int width;

    public int getHeight() {
        return height;
    }

    private int height;


    public EnemyM(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        faceRight = true;
        enemyM = new Texture("EnemyM.png");
        anim = new Animation(new TextureRegion(enemyM),6, 1f,3,3 );
    }

    public void update(float dt){
        anim.update(dt);
//        velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        velocity.scl(1 / dt);
        anim.update(dt);
        if (velocity.x < 0) {
            velocity.x += 1;
        }
        if (velocity.x > 0) {
            velocity.x -= 1;
        }

    }
    public TextureRegion getTexture() {
        return anim.getFrame();
    }

}
