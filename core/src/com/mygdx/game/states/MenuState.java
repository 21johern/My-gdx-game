package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

public class MenuState extends State {
    Texture background;
    private Texture playBtn;

    public MenuState(GameStateManager stateManager) {
        super(stateManager);
        background = new Texture("MenuMap.png");
        playBtn = new Texture("playBtn.png");
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
        sb.begin();
        sb.draw(background,0,0);
        sb.draw(playBtn, (MyGdxGame.WIDTH / 2) - (playBtn.getWidth() / 2), MyGdxGame.HEIGHT / 2);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
