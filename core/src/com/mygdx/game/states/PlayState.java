package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Controller;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.Player;

import javax.swing.JOptionPane;

public class PlayState extends State{
    private Player player;
    private Controller controller;
    private ShapeRenderer shapeRenderer;
    Texture bg;
    public static final String TAG = PlayState.class.getName();

    public PlayState(GameStateManager stateManager) {
        super(stateManager);
        shapeRenderer = new ShapeRenderer();
        bg = new Texture("MenuMap.png");
        Gdx.app.log(TAG, "Application Listener Created");
        controller = new Controller();
        // Change image to the correct background once we have it
        player = new Player(224, 320);
        cam.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if(controller.isUpPressed()) {
            player.jump();
        } else if(controller.isDownPressed()) {

        } else if(controller.isLeftPressed()) {
            player.walkLeft();
        } else if(controller.isRightPressed()) {
            player.walkRight();
        }
        player.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg,0,0);
        sb.draw(player.getTexture(),player.getPosition().x,player.getPosition().y,player.getWidth(), player.getHeight());
        sb.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(153/255f,95/255f,45/255f,1f );
        // Hitbox needs to be fixed.
//        shapeRenderer.rect(224,230,220,200);
        shapeRenderer.end();
        controller.draw();
    }

    @Override
    public void dispose() {

    }
}