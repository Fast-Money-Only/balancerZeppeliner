package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.List;

public class GameScreen implements Screen {

    final BalancerZeppeliner game;
    OrthographicCamera camera;

    Viewport viewport;

    Zeppeliner zeppeliner;

    Weight person, bomb, water, fuel, food;

    private Weight selectedWeight, newWeight;

    Background background;

    Texture zeppelinImg, personImg, bombImg, waterImg, fuelImg, foodImg, BGimg;

    int antalMænd, antalVand, antalFuel, antalBomber, antalMad;

    Array<Weight> vægtObjekter;

    public GameScreen(BalancerZeppeliner game) {
        this.game = game;

        BGimg = new Texture(Gdx.files.internal("skyBG.jpg"));
        zeppelinImg = new Texture(Gdx.files.internal("zeppelin.png"));
        personImg = new Texture(Gdx.files.internal("person.png"));
        bombImg = new Texture(Gdx.files.internal("bombe.png"));
        waterImg = new Texture(Gdx.files.internal("water.png"));
        fuelImg = new Texture(Gdx.files.internal("fuel.png"));
        foodImg = new Texture(Gdx.files.internal("food.png"));


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1792, 1344);

        background = new Background(BGimg,0,camera.viewportHeight);


        zeppeliner = new Zeppeliner(zeppelinImg, 450, 500 );
        person = new Weight(40, personImg, 100, 10);
        bomb = new Weight(10, bombImg, 300, 10);
        water = new Weight(10, waterImg, 650, 10);
        fuel = new Weight(20, fuelImg, 900, 10);
        food = new Weight(2, foodImg, 1300, 10);

        vægtObjekter = new Array<>();
        vægtObjekter.add(person);
        vægtObjekter.add(bomb);
        vægtObjekter.add(water);
        vægtObjekter.add(fuel);
        vægtObjekter.add(food);

        selectedWeight = null;
        newWeight = null;
    }

    @Override
    public void show() {

    }


    public void grabWeight(){
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            double posY = touchPos.y;
            double posX = touchPos.x;

            selectedWeight = null;

            for (Weight weight : vægtObjekter) {
                if (posY > weight.getY() && posY < (weight.getY() + weight.getSize().height)
                        && posX > weight.getX() && posX < (weight.getX() + weight.getSize().width)) {
                    selectedWeight = weight;
                    newWeight = new Weight(weight.getVægt(), weight.getTexture(), (float) weight.getX(), (float) weight.getY());
                    vægtObjekter.add(newWeight);
                    break;
                }
            }
        }
    }


    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(BGimg, 0, 0);
        game.font.getData().setScale(2, 2);
        game.font.draw(game.batch, "Vi skal have læsset Zeppelineren med ", 30, BGimg.getHeight() - 30);
        game.font.draw(game.batch, antalMænd + "/5 mænd", 30, BGimg.getHeight() - 60);
        game.font.draw(game.batch, antalBomber + "/10 bomber", 30, BGimg.getHeight() - 90);
        game.font.draw(game.batch, antalVand + "/10 glas vand", 30, BGimg.getHeight() - 120);
        game.font.draw(game.batch, antalFuel + "/4 sæt brændstof", 30, BGimg.getHeight() - 150);
        game.font.draw(game.batch, antalMad + "/10 tallerkener mad", 30, BGimg.getHeight() - 180);
        zeppeliner.draw(game.batch);
        for (Weight w : vægtObjekter){
            game.batch.draw(w.getTexture(), (float) w.getX(), (float) w.getY());
        }

        game.batch.end();

        grabWeight();
        if (newWeight != null) {
            handleWeightMovementAndRotation();
        }

    }

    private void handleWeightMovementAndRotation() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            newWeight.setX(touchPos.x - newWeight.getSize().width / 2);
            newWeight.setY(touchPos.y - newWeight.getSize().height / 2);
        } else {
            if (newWeight.getX() > zeppeliner.getX() && newWeight.getX() < (zeppeliner.getX() + zeppeliner.getTexture().getWidth())
                    && newWeight.getY() > zeppeliner.getY() && newWeight.getY() < (zeppeliner.getY() + zeppeliner.getTexture().getHeight())) {

                float zeppelinerCenterX = zeppeliner.getX() + zeppeliner.getTexture().getWidth() / 2;
                float rotationFactor = newWeight.getVægt();

                if (newWeight.getX() < zeppelinerCenterX) {
                    zeppeliner.rotate(rotationFactor);
                } else {
                    zeppeliner.rotate(-rotationFactor);
                }

                if (newWeight.getTexture().equals(personImg)){
                    antalMænd++;
                    System.out.println("antal mænd: " + antalMænd);
                } else if (newWeight.getTexture().equals(waterImg)) {
                    antalVand++;
                    System.out.println("antal vand: " + antalVand);
                } else if (newWeight.getTexture().equals(fuelImg)) {
                    antalFuel++;
                    System.out.println("antal fuel: " + antalFuel);
                } else if (newWeight.getTexture().equals(foodImg)) {
                    antalMad++;
                    System.out.println("antal mad: " + antalMad);
                } else if (newWeight.getTexture().equals(bombImg)) {
                    antalBomber++;
                    System.out.println("antal bomber: " + antalBomber);
                }
            }
            newWeight = null;
        }
    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
