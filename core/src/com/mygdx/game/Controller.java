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
    OrthographicCamera cam;
    private Image Atk;
    public boolean AtkPressed, upPressed, leftPressed, rightPressed;




    public Controller() {
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, cam);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        Atk = new Image(new Texture("Attack_Btn.png"));
        Atk.setPosition(MyGdxGame.WIDTH, 0);

        Table table = new Table();      // Table for controller
        Table GameTable = new Table();  // Table for whole game
        Table AtkTable = new Table();   // Table for right buttons

        GameTable.setWidth(MyGdxGame.WIDTH);
        GameTable.setHeight(MyGdxGame.HEIGHT/2);

        float arrows = 60;

        Image atkImg = new Image(new Texture("Attack_Btn.png"));
        atkImg.setSize(64, 64);

        atkImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AtkPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                AtkPressed = false;
            }
        });

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

        float padding = 5;
        AtkTable.add(atkImg).size(atkImg.getWidth(), atkImg.getHeight()).pad(padding);
        stage.addActor(AtkTable);

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

        GameTable.row();
        GameTable.add(table);
        GameTable.add().expand(); // Make middle column take up as much space as possible
        GameTable.add(AtkTable);
        stage.addActor(GameTable);

        // Table debug lines
//        GameTable.setDebug(true);
//        table.setDebug(true);
//        AtkTable.setDebug(true);
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

    public boolean isAtkPressed() {
        return AtkPressed;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

}