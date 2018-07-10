package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;

import javax.swing.JOptionPane;

public class Player {
    public Vector3 getPosition() {
        return position;
    }

    private Vector3 position;
    private Vector3 velocity;
    private Texture walkCharacter;
    private Texture jumpCharacter;
    private Animation walk;
    private Animation jump;
    private String Activity;
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

    public Player(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        Activity = "none";
        faceRight = true;
        height = 0;
        width = 0;
        jumpCharacter = new Texture("Spritejump.png");
        walkCharacter = new Texture("walksprite.png");
        jump = new Animation(new TextureRegion(jumpCharacter),9, 1f,5,2 );
        walk = new Animation(new TextureRegion(walkCharacter),4, 0.4f,2,2 );
    }

    public void update(float dt){
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
//        velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        velocity.scl(1 / dt);
        walk.update(dt);
        if (velocity.x < 0) {
            velocity.x += 1;
        } else if (velocity.x > 0) {
            velocity.x -= 1;
        }
        if (velocity.y > 0) {
            velocity.y -= 2;
        }

    }

    public void walkLeft() {
        Activity = "Walking";
        if (faceRight) {
            walk.flipFrames();
            jump.flipFrames();
        }
        faceRight = false;
        velocity.set(-50, 0, 0);
    }
    public void walkRight() {
        Activity = "Walking";
        if (!faceRight) {
            walk.flipFrames();
            jump.flipFrames();
        }
        faceRight = true;
        velocity.set(50, 0, 0);
    }
    public void jump() {
        Activity = "Jumping";
        velocity.set(velocity.x, 50, 0);

        float delay = 1;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
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
