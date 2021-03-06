package com.mygdx.game.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.Controller;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.EnemyMan;
import com.mygdx.game.sprites.EnemyMushroom;
import com.mygdx.game.sprites.EnemySkeleton;
import com.mygdx.game.sprites.Player;

public class PlayState extends State{
    public static final String TAG = PlayState.class.getName();
    public World world;
    public Box2DDebugRenderer debugRenderer;
    OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private Player player;

    private EnemyMan enemyMan;
    private EnemySkeleton enemySkeleton;
    private EnemyMushroom enemyMushroom;
    Music Swing;
    Music Jump;
    Music Background;


    private Controller controller;
    private ShapeRenderer shapeRenderer;
    public Array<Body> floors;
    public BodyDef floorDef;
    public PolygonShape floorShape;
    public MapObjects floorObjects;

    public MapObjects lavaObjects;
    public Array<Polygon> lava;

    public MapObjects exitObjects;
    public Array<Polygon> exit;



    public PlayState(GameStateManager stateManager) {
        super(stateManager);


        // Change image to the correct background once we have it
        cam.setToOrtho(false, MyGdxGame.WIDTH*State.PIXEL_TO_METER / 2, MyGdxGame.HEIGHT*State.PIXEL_TO_METER / 2) ;

        Box2D.init();
        world = new World(new Vector2(0, -9.8f), true);
        // the y value here is the gravity
        debugRenderer = new Box2DDebugRenderer();
        player = new Player(7, 10, world);
        enemyMan = new EnemyMan(22,7,world, player);
        enemySkeleton = new EnemySkeleton(8, 5, world, player);
        enemyMushroom = new EnemyMushroom(19,23/2,world, player);
        Swing = Gdx.audio.newMusic(Gdx.files.internal("Swing1.mp3"));
        Jump = Gdx.audio.newMusic(Gdx.files.internal("Jump.mp3"));
        Background = Gdx.audio.newMusic(Gdx.files.internal("Background music.mp3"));



        map = new TmxMapLoader().load("MenuMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, State.PIXEL_TO_METER);
        shapeRenderer = new ShapeRenderer();
        Gdx.app.log(TAG, "Application Listener Created");
        //mapObjects = .getLayers().get("Collision");
        controller = new Controller();
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            Gdx.input.setInputProcessor(controller);
        }

        floorObjects = map.getLayers().get("Ground").getObjects();

        lavaObjects = map.getLayers().get("Lava").getObjects();
        lava = new Array<Polygon>();

        exitObjects = map.getLayers().get("Exit").getObjects();
        exit = new Array<Polygon>();



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
        counter = 0;
        for (PolygonMapObject obj : lavaObjects.getByType(PolygonMapObject.class)) {
            float[] vertices = obj.getPolygon().getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] * State.PIXEL_TO_METER;
            }
            Polygon temp = new Polygon();
            temp.setVertices(vertices);
            temp.setPosition(obj.getPolygon().getX() * State.PIXEL_TO_METER, obj.getPolygon().getY() * State.PIXEL_TO_METER);
            lava.add(temp);
            counter++;
        }
        counter = 0;
        for (PolygonMapObject obj : exitObjects.getByType(PolygonMapObject.class)) {
            float[] vertices = obj.getPolygon().getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i] = vertices[i] * State.PIXEL_TO_METER;
            }
            Polygon temp = new Polygon();
            temp.setVertices(vertices);
            temp.setPosition(obj.getPolygon().getX() * State.PIXEL_TO_METER, obj.getPolygon().getY() * State.PIXEL_TO_METER);
            exit.add(temp);
            counter++;
        }

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        lavaCheck();
        exitCheck();

        if(player.health <= 0) {
            gsm.set(new DeathState(gsm));
            dispose();
        }

        if ((player.playerBody.getPosition().x <= (200 * State.PIXEL_TO_METER)) &&
                (player.playerBody.getPosition().y >= (360 * State.PIXEL_TO_METER))) {
            cam.position.set(200 * State.PIXEL_TO_METER,360 * State.PIXEL_TO_METER, 0);
        } else if ((player.playerBody.getPosition().x <= (200 * State.PIXEL_TO_METER)) &&
                (player.playerBody.getPosition().y <= (120 * State.PIXEL_TO_METER))) {
            cam.position.set(200 * State.PIXEL_TO_METER,120 * State.PIXEL_TO_METER, 0);
        } else if ((player.playerBody.getPosition().x >= (600 * State.PIXEL_TO_METER)) &&
                (player.playerBody.getPosition().y >= (360 * State.PIXEL_TO_METER))) {
            cam.position.set(600 * State.PIXEL_TO_METER,360 * State.PIXEL_TO_METER, 0);
        } else if ((player.playerBody.getPosition().x >= (600 * State.PIXEL_TO_METER)) &&
                (player.playerBody.getPosition().y <= (120 * State.PIXEL_TO_METER))) {
            cam.position.set(600 * State.PIXEL_TO_METER,120 * State.PIXEL_TO_METER, 0);
        } else if ((player.playerBody.getPosition().x <= (200 * State.PIXEL_TO_METER))) {
            cam.position.set(200 * State.PIXEL_TO_METER,player.playerBody.getPosition().y, 0);
        } else if ((player.playerBody.getPosition().x >= (600 * State.PIXEL_TO_METER))) {
            cam.position.set(600 * State.PIXEL_TO_METER,player.playerBody.getPosition().y, 0);
        }
        else if ((player.playerBody.getPosition().y >= (360 * State.PIXEL_TO_METER))) {
            cam.position.set(player.playerBody.getPosition().x,360 * State.PIXEL_TO_METER, 0);
        }
        else if ((player.playerBody.getPosition().y <= (120 * State.PIXEL_TO_METER))) {
            cam.position.set(player.playerBody.getPosition().x,120 * State.PIXEL_TO_METER, 0);
        }
        else{
            cam.position.set(player.playerBody.getPosition(), 0);
        }

        cam.update();

        Background.play();
        Background.setLooping(true);
        Background.setVolume(.2f);

        if (controller.isAtkPressed()) {
           player.attack();
           Swing.play();
        }else if(controller.isUpPressed()) {
            player.jump();
            Jump.play();
        } else if(controller.isLeftPressed()) {
            player.walkLeft();
        } else if(controller.isRightPressed()) {
            player.walkRight();
        }
        player.update(dt);
        enemyMan.update(dt);
        enemySkeleton.update(dt);
        enemyMushroom.update(dt);
    }

    public void lavaCheck(){
        for (Polygon deathLava : lava){
            if(Intersector.overlapConvexPolygons(player.bounds, deathLava)){
                player.health -= 1;
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {}
                }, 1);
            }
        }
    }

    public void exitCheck(){
        if((player.bounds.getX() <= 22) && (player.bounds.getX() >= 21) && (player.bounds.getY() <= 1) && (player.bounds.getX() >= 0)){
            gsm.set(new VictoryState(gsm));
            dispose();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        renderer.setView(cam);
        renderer.render();
        sb.begin();
        sb.draw(player.getTexture(Gdx.graphics.getDeltaTime()),player.getPosition().x,player.getPosition().y,1, 1);
        sb.draw(enemyMan.getTexture(),enemyMan.getPosition().x,enemyMan.getPosition().y,enemyMan.getWidth() * State.PIXEL_TO_METER, enemyMan.getHeight() * State.PIXEL_TO_METER);
        sb.draw(enemySkeleton.getTexture(),enemySkeleton.getPosition().x,enemySkeleton.getPosition().y,enemySkeleton.getWidth() * State.PIXEL_TO_METER, enemySkeleton.getHeight() * State.PIXEL_TO_METER);
        sb.draw(enemyMushroom.getTexture(),enemyMushroom.getPosition().x,enemyMushroom.getPosition().y,enemyMushroom.getWidth() * State.PIXEL_TO_METER, enemyMushroom.getHeight() * State.PIXEL_TO_METER);
        sb.end();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    //    shapeRenderer.rect(player.AtkHitbox.getX(),player.AtkHitbox.getY(),player.AtkHitbox.getWidth(),player.AtkHitbox.getHeight());
        shapeRenderer.end();
        controller.draw();
        cam.update();
//        debugRenderer.render(world, cam.combined);
        world.step(1/60f, 6, 2);
    }

    @Override
    public void dispose() {
        Swing.dispose();
        Background.dispose();
        Jump.dispose();
        world.dispose();
        map.dispose();
        shapeRenderer.dispose();
        renderer.dispose();
//        debugRenderer.dispose();
        floorShape.dispose();
        player.dispose();
        enemySkeleton.dispose();
        enemyMan.dispose();
        enemyMushroom.dispose();
    }
}