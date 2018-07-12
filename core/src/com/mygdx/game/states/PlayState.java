package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private Controller controller;
    private ShapeRenderer shapeRenderer;
    Texture bg;
    public static final String TAG = PlayState.class.getName();

    // Creates lava objects from map then fetches the ones specifically in the lava layer.
    public TiledMapTileLayer lavaCollider;
    public MapObjects lavaObjects;

    public TiledMapTileLayer waterCollider;
    public MapObjects waterObjects;

    public TiledMapTileLayer chestCollider;
    public MapObjects chestObjects;

    public TiledMapTileLayer doorCollider;
    public MapObjects doorObjects;

    public TiledMapTileLayer leftWallCollider;
    public MapObjects leftWallObjects;

    public TiledMapTileLayer rightWallCollider;
    public MapObjects rightWallObjects;

    public TiledMapTileLayer ceilingCollider;
    public MapObjects ceilingObjects;

    public TiledMapTileLayer floorCollider;
    public MapObjects floorObjects;


    public PlayState(GameStateManager stateManager) {
        super(stateManager);
        map = new TmxMapLoader().load("MenuMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        shapeRenderer = new ShapeRenderer();
        bg = new Texture("MenuMap.png");
        Gdx.app.log(TAG, "Application Listener Created");
        //mapObjects = .getLayers().get("Collision");
        controller = new Controller();

        // Gets Lava objects and draws polygons.
        lavaCollider = (TiledMapTileLayer)map.getLayers().get("Lava");
        lavaObjects = lavaCollider.getObjects();

        waterCollider = (TiledMapTileLayer)map.getLayers().get("Water");
        waterObjects = waterCollider.getObjects();

        chestCollider = (TiledMapTileLayer)map.getLayers().get("Chest");
        chestObjects = chestCollider.getObjects();

        doorCollider = (TiledMapTileLayer)map.getLayers().get("Exit");
        doorObjects = doorCollider.getObjects();

        leftWallCollider = (TiledMapTileLayer)map.getLayers().get("leftWall");
        leftWallObjects = leftWallCollider.getObjects();

        rightWallCollider = (TiledMapTileLayer)map.getLayers().get("rightWall");
        rightWallObjects = rightWallCollider.getObjects();

        ceilingCollider = (TiledMapTileLayer)map.getLayers().get("Ceiling");
        ceilingObjects = rightWallCollider.getObjects();

        floorCollider = (TiledMapTileLayer)map.getLayers().get("Ground");
        floorObjects = floorCollider.getObjects();


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
        renderer.setView(cam);
        renderer.render();
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg,0,0);
        sb.draw(player.getTexture(),player.getPosition().x,player.getPosition().y,player.getWidth(), player.getHeight());
        sb.end();


        controller.draw();
    }

    @Override
    public void dispose() {

    }
}