package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

public class PlayState extends State{
    private Texture bg;

    public PlayState(GameStateManager stateManager) {
        super(stateManager);

        // Change image to the correct background once we have it
        bg = new Texture("bg.png");
        cam.setToOrtho(false, MyGdxGame.WIDTH/2, MyGdxGame.HEIGHT/2);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        // Draw Things Here
        sb.end();

    }

    @Override
    public void dispose() {

    }
}
