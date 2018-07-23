package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class EnemyS {
    public Vector3 getPosition() {
        return position;
    }

    private Vector3 position;
    private Vector3 velocity;
    private Texture enemyS;
    private Animation anim;
    //private static final int GRAVITY = -15;

    public int getWidth() {
        return width;
    }

    private int width;

    public int getHeight() {
        return height;
    }

    private int height;


    public EnemyS(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        enemyS = new Texture("EnemyS.png");
        anim = new Animation(new TextureRegion(enemyS),12, 0.4f,4,3 );
    }

    public void update(float dt){
        //velocity.add(0, GRAVITY, 0);

//        if (velocity.x < 0) {
//            velocity.x += 1;
//        }
//        if (velocity.x > 0) {
//            velocity.x -= 1;
//        }
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        velocity.scl(1 / dt);
        anim.update(dt);
    }

    public TextureRegion getTexture() {
        return anim.getFrame();
    }

}
