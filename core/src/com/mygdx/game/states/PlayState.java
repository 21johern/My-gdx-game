package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.Player;

public class PlayState extends State{
    private Player player;
    private Texture bg;
    public PlayState(GameStateManager stateManager) {
        super(stateManager);

        bg = new Texture("map1.tmx");
        // Change image to the correct background once we have it
        player = new Player(50, 100);
        cam.setToOrtho(false, MyGdxGame.WIDTH/1, MyGdxGame.HEIGHT/1);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        player.update(dt);

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(player.getTexture(),50,50);
        sb.end();

    }

    @Override
    public void dispose() {

    }
}
