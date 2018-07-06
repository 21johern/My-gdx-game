package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

public class MenuState extends State {



    public MenuState(GameStateManager stateManager) {
        super(stateManager);
    }

    @Override
    protected void handleInput() {
        gsm.set(new PlayState(gsm));
        dispose();
    }

    @Override
    public void update(float dt) {
        if(Gdx.input.justTouched()) {
            handleInput();
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
