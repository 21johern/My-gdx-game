package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Controller;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.Player;

public class PlayState extends State{
    private Player player;
    private Controller controller;
    Texture bg;

    public PlayState(GameStateManager stateManager) {
        super(stateManager);
        bg = new Texture("MenuMap.png");
        controller = new Controller();
        // Change image to the correct background once we have it
        player = new Player(50, 100);
        cam.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);



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
        sb.draw(bg,0,0);
        sb.draw(player.getTexture(),224,320,32,50);
        sb.end();

        controller.draw();
    }

    @Override
    public void dispose() {

    }
}
