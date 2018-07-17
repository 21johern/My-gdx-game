package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Controller {
    private SpriteBatch batch;
    Viewport viewport;
    Stage stage;
    boolean upPressed, leftPressed, rightPressed;
    OrthographicCamera cam;
    private Image Atk;
    private Circle AtkBtnHitbox;
    private boolean AtkPressed;

    public boolean isAtkPressed() {
        return AtkPressed;
    }

    class TouchInfo {
        float touchX = 0;
        float touchY = 0;
    }


    public Controller() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, cam);
        stage = new Stage(viewport, MyGdxGame.batch);
        Gdx.input.setInputProcessor(stage);

        Atk = new Image(new Texture("Attack_Btn.png"));
        Atk.setPosition(MyGdxGame.WIDTH, 0);
        AtkBtnHitbox = new Circle(Atk.getX(), Atk.getY(), (Atk.getWidth()/2));


        Table table = new Table();
        Table GameTable = new Table();
        Table AtkTable = new Table();
        table.left().bottom();
        AtkTable.right().bottom();
        GameTable.setWidth(MyGdxGame.WIDTH);
        GameTable.setHeight(MyGdxGame.HEIGHT/2);




        float arrows = 60;

        Image atkImg = new Image(new Texture("Attack_Btn.png"));
        atkImg.setSize(64, 64);


        Image upImg = new Image(new Texture("upArrow.png"));
        upImg.setSize(arrows, arrows);

        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });
        Image leftImg = new Image(new Texture("leftArrow.png"));
        leftImg.setSize(arrows, arrows);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
        Image rightImg = new Image(new Texture("rightArrow.png"));
        rightImg.setSize(arrows, arrows);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });

        GameTable.row();
        GameTable.add(table);
        GameTable.add();
        GameTable.add(AtkTable);
        stage.addActor(GameTable);

        float padding = 5;
        table.add().pad(padding);
        table.add(upImg).size(upImg.getWidth(), upImg.getHeight()).pad(padding);
        table.add().pad(padding);
        table.row();
        table.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight()).pad(padding);
        table.add().pad(padding);
        table.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight()).pad(padding);
        table.row();
        table.add().pad(padding);
        table.add().pad(padding);
        stage.addActor(table);


        AtkTable.add(atkImg).size(atkImg.getWidth(), atkImg.getHeight()).pad(padding);
        stage.addActor(AtkTable);

    }

    public void draw() {
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void drawDebug(ShapeRenderer sr) {
        sr.circle(AtkBtnHitbox.x, AtkBtnHitbox.y, AtkBtnHitbox.radius);
    }

    private void checkCollisions(TouchInfo t) {
        if (AtkBtnHitbox.contains(t.touchX, t.touchY)) {
            AtkPressed = true;
        }
    }


    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        AtkPressed = false;
        return true;
    }


    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        cam.unproject(touchPos);

        if(AtkBtnHitbox.contains(touchPos.x,touchPos.y)){
            AtkPressed = true;
        }
        return true;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        cam.unproject(touchPos);

        if(AtkBtnHitbox.contains(touchPos.x,touchPos.y)){
            AtkPressed = true;
        }
        return true;
    }

    public boolean isAttackPressed() { return AtkPressed; }

}