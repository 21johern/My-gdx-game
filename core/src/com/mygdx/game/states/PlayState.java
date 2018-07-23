package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Controller;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.EnemyM;
import com.mygdx.game.sprites.EnemyS;
import com.mygdx.game.sprites.Player;



public class PlayState extends State{
    OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private Player player;
    private EnemyS enemyS;
    private EnemyM enemyM;
    private Controller controller;
    private ShapeRenderer shapeRenderer;
    private Rectangle rectangle;
    public float y;
    public float x;
    public float viewportWidth;
    public float viewportHeight;
    private float zoom;
    Texture bg;
    public int camViewportHalfX;
    public int camViewportHalfY;
    public int mapWidth;
    public int mapHeight;
    public static final String TAG = PlayState.class.getName();



    public PlayState(GameStateManager stateManager) {
        super(stateManager);
        map = new TmxMapLoader().load("MenuMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        shapeRenderer = new ShapeRenderer();
        bg = new Texture("MenuMap.png");
        Gdx.app.log(TAG, "Application Listener Created");
        rectangle = new Rectangle(224, 320, 32, 50);


        controller = new Controller();


        // Change image to the correct background once we have it
        cam.setToOrtho(false, MyGdxGame.WIDTH/2 , MyGdxGame.HEIGHT/2);
        player = new Player(224, 320);
        enemyS = new EnemyS(250, 300);
        enemyM = new EnemyM(240, 350);

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
           // cam.position.x = MathUtils.clamp(cam.position.x, camViewportHalfX, mapWidth - camViewportHalfX);
           // cam.position.y = MathUtils.clamp(cam.position.y, camViewportHalfY, mapHeight -camViewportHalfY);

        } else {

        }

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
        cam.update();
        float scaledViewportWidthHalfExtent = viewportWidth * zoom * 0.5f;
        float scaledViewportHeightHalfExtent = viewportHeight * zoom * 0.5f;

        // Horizontal
        if (player.x < scaledViewportWidthHalfExtent)
            player.x = scaledViewportWidthHalfExtent;
        else if (player.x > 0 - scaledViewportWidthHalfExtent)
            player.x = 0 - scaledViewportWidthHalfExtent;

        // Vertical
        if (player.y < scaledViewportHeightHalfExtent)
            player.y = scaledViewportHeightHalfExtent;
        else if (player.y > 0 - scaledViewportHeightHalfExtent)
            player.y = 0 - scaledViewportHeightHalfExtent;

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        //renderer.setView(cam);
        //renderer.render();



        sb.begin();
        sb.draw(bg,0,0);
        sb.draw(player.getTexture(),player.getPosition().x,player.getPosition().y,player.getWidth(), player.getHeight());
        sb.draw(enemyM.getTexture(),enemyM.getPosition().x,enemyM.getPosition().y,enemyM.getWidth(), enemyM.getHeight());
        sb.draw(enemyS.getTexture(),enemyS.getPosition().x,enemyS.getPosition().y,enemyS.getWidth(), enemyS.getHeight());
        sb.end();
        controller.draw();
    }

    @Override
    public void dispose() {

    }
}