package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Controller;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.EnemyM;
import com.mygdx.game.sprites.EnemyS;
import com.mygdx.game.sprites.Player;

public class PlayState extends State{
    private Player player;
    private EnemyS enemyS;
    private Controller controller;
    private ShapeRenderer shapeRenderer;
    private Rectangle rectangle;
    private EnemyM enemyM;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    Texture bg;
    private MapObjects mapObjects;
    public static final String TAG = PlayState.class.getName();

    public PlayState(GameStateManager stateManager) {
        super(stateManager);
        shapeRenderer = new ShapeRenderer();
        rectangle = new Rectangle(224, 320, 32, 50);
        bg = new Texture("MenuMap.png");
        Gdx.app.log(TAG, "Application Listener Created");
        //mapObjects = .getLayers().get("Collision");
        controller = new Controller();
        // Change image to the correct background once we have it
        player = new Player(224, 320);
        enemyS = new EnemyS(250, 300);
        enemyM = new EnemyM(240, 350);
        cam.setToOrtho(false, MyGdxGame.WIDTH/2 , MyGdxGame.HEIGHT/2);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if (!(cam.position.x - MyGdxGame.WIDTH / 4 < 0)){
                cam.position.set(player.getPosition());
            if ((cam.position.x + MyGdxGame.WIDTH / 4 > 757))
                cam.position.set(player.getPosition());

        } else {

        }
        cam.update();
        System.out.println(player.getPosition().x);
        if(player.getPosition().x < 0){
            player.getPosition().x = 0;
        }
        if(player.getPosition().x > 757){
            player.getPosition().x = 757;
        }
        if(controller.isUpPressed()) {

        } else if(controller.isDownPressed()) {

        } else if(controller.isLeftPressed()) {
            player.walkLeft();
        } else if(controller.isRightPressed()) {
            player.walkRight();
        }
        player.update(dt);
        rectangle.setPosition(player.getPosition().x,player.getPosition().y);
        enemyS.update(dt);
        enemyM.update(dt);


    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg,0,0);
        sb.draw(enemyS.getTexture(),enemyS.getPosition().x,enemyS.getPosition().y,50, 50);
        sb.draw(enemyM.getTexture(),enemyM.getPosition().x, enemyM.getPosition().y, 80, 80);
        sb.draw(player.getTexture(),player.getPosition().x,player.getPosition().y,32, 50);
        sb.end();


        controller.draw();
    }

    @Override
    public void dispose() {

    }
}