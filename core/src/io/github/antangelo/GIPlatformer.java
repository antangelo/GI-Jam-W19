package io.github.antangelo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import io.github.antangelo.event.EventBus;

public class GIPlatformer extends ApplicationAdapter
{
    private SpriteBatch batch;
    private Texture img;
    private Player player;
    private Sprite groundSprite;
    private Body groundBody;
    private World world;

    private EventBus eventBus;

    private static final Vector2 GRAVITY = new Vector2(0, -9.8F);
    public static final float PIXEL_TO_METER = 100.0f;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        world = new World(GRAVITY, true);
        eventBus = new EventBus();
        player = new Player(world, new Texture("char1.png"));

        Controllers.addListener(new ControllerListener(eventBus));
        world.setContactListener(new CollisionListener(eventBus));

        this.groundSprite = new Sprite(img);
        this.groundSprite.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 10f - 250);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((groundSprite.getX() + groundSprite.getWidth() / 2) / PIXEL_TO_METER,
                (groundSprite.getY() + groundSprite.getHeight() / 2) / PIXEL_TO_METER);

        this.groundBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(groundSprite.getWidth() / 2f / GIPlatformer.PIXEL_TO_METER,
                groundSprite.getHeight() / 2f / GIPlatformer.PIXEL_TO_METER);

        groundBody.createFixture(polygonShape, 1);
    }

    @Override
    public void render()
    {
        world.step(1 / 60f, 6, 2);

        player.update();
        groundSprite.setPosition(groundBody.getPosition().x * PIXEL_TO_METER - groundSprite.getWidth() / 2,
                groundBody.getPosition().y * PIXEL_TO_METER - groundSprite.getHeight() / 2);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY());
        batch.draw(groundSprite, groundSprite.getX(), groundSprite.getY());
        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        img.dispose();
        world.dispose();
    }
}
