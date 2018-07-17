package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Controller;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.EnemyM;
import com.mygdx.game.sprites.EnemyS;
import com.mygdx.game.sprites.Player;

public class PlayState extends State{
    public static final String TAG = PlayState.class.getName();
    public World world;
    public Box2DDebugRenderer debugRenderer;
    OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private Player player;

    private EnemyM enemyM;

    private Controller controller;
    private ShapeRenderer shapeRenderer;
    public Array<Body> floors;
    public BodyDef floorDef;
    public PolygonShape floorShape;

    // Creates lava objects from map then fetches the ones specifically in the lava layer.
//    public TiledMapTileLayer lavaCollider;
//    public MapObjects lavaObjects;
//
//    public TiledMapTileLayer waterCollider;
//    public MapObjects waterObjects;
//
//    public TiledMapTileLayer chestCollider;
//    public MapObjects chestObjects;
//
//    public TiledMapTileLayer doorCollider;
//    public MapObjects doorObjects;
//
//    public TiledMapTileLayer leftWallCollider;
//    public MapObjects leftWallObjects;
//
//    public TiledMapTileLayer rightWallCollider;
//    public MapObjects rightWallObjects;
//
//    public TiledMapTileLayer ceilingCollider;
//    public MapObjects ceilingObjects;
//
    public MapObjects floorObjects;


    public PlayState(GameStateManager stateManager) {
        super(stateManager);

        // Change image to the correct background once we have it
        cam.setToOrtho(false, MyGdxGame.WIDTH*State.PIXEL_TO_METER, MyGdxGame.HEIGHT*State.PIXEL_TO_METER);

        Box2D.init();
        // the y value here is the gravity
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        player = new Player(7, 10, this);
        enemyM = new EnemyM(22,7,this);

        map = new TmxMapLoader().load("MenuMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, State.PIXEL_TO_METER);
        shapeRenderer = new ShapeRenderer();
        Gdx.app.log(TAG, "Application Listener Created");
        //mapObjects = .getLayers().get("Collision");
        controller = new Controller();

        floorObjects = map.getLayers().get("Ground").getObjects();

        floors = new Array<Body>();
        floorDef = new BodyDef();
        floorShape = new PolygonShape();

        int counter = 0;
        for (PolygonMapObject obj : floorObjects.getByType(PolygonMapObject.class)) {
            floorDef.position.set(obj.getPolygon().getX() * State.PIXEL_TO_METER, obj.getPolygon().getY() * State.PIXEL_TO_METER);
            floors.add(world.createBody(floorDef));
            float[] vertices = obj.getPolygon().getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] * State.PIXEL_TO_METER;
            }
            floorShape.set(vertices);
            floors.get(counter).createFixture(floorShape, 0.0f);
            counter++;
        }

        // Gets Lava objects and draws polygons.
//        lavaCollider = (TiledMapTileLayer)map.getLayers().get("Lava");
//        lavaObjects = lavaCollider.getObjects();
//
//        waterCollider = (TiledMapTileLayer)map.getLayers().get("Water");
//        waterObjects = waterCollider.getObjects();
//
//        chestCollider = (TiledMapTileLayer)map.getLayers().get("Chest");
//        chestObjects = chestCollider.getObjects();
//
//        doorCollider = (TiledMapTileLayer)map.getLayers().get("Exit");
//        doorObjects = doorCollider.getObjects();


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
            player.jump();
        } else if (controller.isAtkPressed()) {

        } else if(controller.isLeftPressed()) {
            player.walkLeft();
        } else if(controller.isRightPressed()) {
            player.walkRight();
        }
        player.update(dt);
        enemyM.update(dt);


    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        renderer.setView(cam);
        renderer.render();
        sb.begin();
        sb.draw(player.getTexture(),player.getPosition().x,player.getPosition().y,player.getWidth() * State.PIXEL_TO_METER, player.getHeight() * State.PIXEL_TO_METER);
        sb.draw(enemyM.getTexture(),enemyM.getPosition().x,enemyM.getPosition().y,enemyM.getWidth() * State.PIXEL_TO_METER, enemyM.getHeight() * State.PIXEL_TO_METER);
        sb.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(153/255f,95/255f,45/255f,1f );
        // Hitbox needs to be fixed.
//        shapeRenderer.rect(224,230,220,200);
        shapeRenderer.end();
        controller.draw();
        cam.update();
        debugRenderer.render(world, cam.combined);
        world.step(1/60f, 6, 2);
    }

    @Override
    public void dispose() {

    }
}