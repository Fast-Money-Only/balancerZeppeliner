package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class Zeppeliner {

    private Texture texture;
    private float x, y;
    private float rotation;
    private Vector2 origin;
    private Array<Weight> weights;

    public Zeppeliner(Texture texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.origin = new Vector2(texture.getWidth() / 2f, texture.getHeight() / 2f);
        this.weights = new Array<>();
    }

    public Texture getTexture() {
        return texture;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void rotate(float angle) {
        this.rotation += angle;
    }

    public void addWeight(Weight weight) {
        Vector2 relativePosition = new Vector2((float) (weight.getX() - x), (float) (weight.getY() - y));
        weight.setOffset(relativePosition.x, relativePosition.y);
        weights.add(weight);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, origin.x, origin.y, texture.getWidth(), texture.getHeight(), 1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        for (Weight weight : weights) {
            batch.draw(weight.getTexture(), (float) (x + weight.getX()), (float) (y + weight.getY()));
        }
    }

    public void updateWeightsPosition() {
        for (Weight weight : weights) {
            // Calculate the distance between the weight and the center of the zeppeliner
            float distanceX = (float) (weight.getX() - origin.x);
            float distanceY = (float) (weight.getY() - origin.y);

            // Calculate the angle between the weight and the center of the zeppeliner
            float angle = (float) Math.atan2(distanceY, distanceX);

            // Calculate the distance between the weight and the center of the zeppeliner after rotation
            float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            // Calculate the new position of the weight based on the zeppeliner's rotation
            float newDistanceX = (float) (Math.cos(angle + Math.toRadians(rotation)) * distance);
            float newDistanceY = (float) (Math.sin(angle + Math.toRadians(rotation)) * distance);

            // Update the position of the weight
            weight.setX((float) (origin.x + newDistanceX - weight.getWidth() / 2));
            weight.setY((float) (origin.y + newDistanceY - weight.getHeight() / 2));
        }
    }

}
