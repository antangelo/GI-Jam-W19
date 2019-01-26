package io.github.antangelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class SpriteBody extends Sprite
{
    private Body body;

    //TODO: Implement SpriteProps for more general sprites
    public static class SpriteProps
    {

    }

    public SpriteBody(Texture texture, World world, Vector2 position)
    {
        this(BodyDef.BodyType.DynamicBody, texture, world, position);
    }

    public SpriteBody(BodyDef.BodyType bodyType, Texture texture, World world, Vector2 position)
    {
        super(texture);

        this.setPosition(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set((this.getX() + this.getWidth() / 2) / GIPlatformer.PIXEL_TO_METER,
                (this.getY() + this.getHeight() / 2) / GIPlatformer.PIXEL_TO_METER);

        body = world.createBody(bodyDef);

        // Assuming the sprite is a box, will be solved in the future with SpriteProps ideally
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(this.getWidth() / 2f / GIPlatformer.PIXEL_TO_METER,
                this.getHeight() / 2f / GIPlatformer.PIXEL_TO_METER);

        Fixture fixture = body.createFixture(polygonShape, 1);
        polygonShape.dispose();
    }

    public void update()
    {
        this.setPosition(this.body.getPosition().x * GIPlatformer.PIXEL_TO_METER - this.getWidth() / 2,
                this.body.getPosition().y * GIPlatformer.PIXEL_TO_METER - this.getHeight() / 2);

        this.setRotation((float) Math.toDegrees(body.getAngle()));
    }

    public Body getBody()
    {
        return body;
    }

    public void setPosition(Vector2 position)
    {
        this.setPosition(position.x, position.y);
    }

    public void setRealPosition(Vector2 position)
    {
        this.setPosition(position.x, position.y);
    }

    public void setRealPosition(float x, float y)
    {
        this.body.setTransform(x, y, body.getAngle());
        this.setPosition(x, y);
    }
}
