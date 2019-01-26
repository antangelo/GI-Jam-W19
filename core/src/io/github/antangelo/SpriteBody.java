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

    public SpriteBody(Texture texture, World world)
    {
        super(texture);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(this.getX(), this.getY());

        body = world.createBody(bodyDef);

        // Assuming the sprite is a box, will be solved in the future with SpriteProps ideally
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(this.getWidth() / 2f, this.getHeight() / 2f);

        Fixture fixture = body.createFixture(polygonShape, 1);
        polygonShape.dispose();
    }

    public void update()
    {
        this.setPosition(this.body.getPosition().x, this.body.getPosition().y);
    }

    public Body getBody()
    {
        return body;
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
