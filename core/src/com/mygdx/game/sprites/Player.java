package com.mygdx.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player {
    public Vector3 getPosition() {
        return position;
    }

    private Vector3 position;
    private Vector3 velocity;
    private Texture character;
    private Animation anim;
    private static final int GRAVITY = -15;
    private boolean faceRight;
    private int getX;
    private int getY;
    private Polygon polyBounds;
    private Vector2 botLeft;
    private Vector2 botRight;
    private Vector2 topLeft;
    private Vector2 topRight;



    public Player(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        faceRight = true;

        character = new Texture("walksprite.png");
        anim = new Animation(new TextureRegion(character),4, 1f,2,2 );
        //polyBounds = new Polygon();
        //polyBounds.setVertices(new float[]{0,0, anim.getFrame().getRegionWidth(), anim.getFrame().getRegionHeight(),
               // anim.getFrame().getRegionX(),anim.getFrame().getRegionY()});
        //botLeft= new Vector2();
       // botRight = new Vector2();
        //topLeft = new Vector2();
        //topRight = new Vector2();

    }

    public void update(float dt){
        anim.update(dt);
       // polyBounds.setPosition(getX)(), getY());
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

    public void walkLeft() {
        if (faceRight) {
            anim.flipFrames();
            faceRight = false;
        }

        velocity.set(-50, 0, 0);

    }
    public void walkRight() {
        if (faceRight) {

        } else {
            anim.flipFrames();
            faceRight = true;
        }

        velocity.set(+50, 0, 0);
    }

    public TextureRegion getTexture() {
        return anim.getFrame();
    }

}
