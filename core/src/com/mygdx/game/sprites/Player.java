package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Player {private Vector3 position;
    private Vector3 velocity;
    private Texture character;
    private Animation anim;
    private static final int GRAVITY = -15;

    public Player(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        character = new Texture("walksprite.png");
        anim = new Animation(new TextureRegion(character),4, 0.4f,2,2 );
    }

    public void update(float dt){
        velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(0, velocity.y, 0);
        velocity.scl(1 / dt);
        anim.update(dt);
    }
    public TextureRegion getTexture() {
        return anim.getFrame();
    }

    public Vector3 getPosition() {
        return position;
    }
}
