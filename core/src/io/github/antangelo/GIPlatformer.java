package io.github.antangelo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.antangelo.event.ControllerEvent;
import io.github.antangelo.event.EventBus;
import io.github.antangelo.event.listener.CollisionListener;
import io.github.antangelo.event.listener.ControllerListener;

public class GIPlatformer extends ApplicationAdapter
{
    private SpriteBatch batch;
    private String ice256, ice128, ice96, houseTex, evil96, evil128, evil224;
    public Player player;
    private World world;

    public boolean shouldKillPlayer = false, victory = false;

    private SpriteBody groundPlat1, groundPlat2, groundPlat3, groundPlat4, groundPlat5,
            groundPlat6, groundPlat7;
    public SpriteBody house;
    private EvilObstacle evilObstacle1, evilObstacle2;
    private EvilObstacle[] wall, path;

    private Sprite victorySprite;
    private Texture victoryTexture;

    private OrthographicCamera camera;

    private EventBus eventBus;
    private static GIPlatformer instance;

    private static final Vector2 GRAVITY = new Vector2(0, -5F);
    public static final float PIXEL_TO_METER = 100.0f;

    @Override
    public void create()
    {
        instance = this;
        batch = new SpriteBatch();

        ice256 = "ice256.png";
        ice128 = "ice128.png";
        ice96 = "ice96.png";
        houseTex = "house224.png";
        evil128 = "evil128.png";
        evil224 = "evil224.png";
        evil96 = "evil96.png";

        victoryTexture = new Texture("victory.png");
        victorySprite = new Sprite(victoryTexture);

        world = new World(GRAVITY, true);
        eventBus = new EventBus();
        player = new Player(world, new Texture("char1-64x.png"));
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        victorySprite.setPosition(camera.viewportWidth / 2, camera.viewportHeight / 2);

        player.loadPlayerTextures();

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        Controllers.addListener(new ControllerListener(eventBus));
        world.setContactListener(new CollisionListener(eventBus));

        groundPlat1 = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(ice256), world,
                new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 10f - 250f));
        groundPlat2 = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(ice128), world,
                new Vector2(Gdx.graphics.getWidth() / 2f + 400f, Gdx.graphics.getHeight() / 10f));
        groundPlat3 = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(ice96), world,
                new Vector2(Gdx.graphics.getWidth() / 2f + 980f, Gdx.graphics.getHeight() / 10f + 50f));
        groundPlat4 = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(ice256), world,
                new Vector2(Gdx.graphics.getWidth() / 2f + 1400f, Gdx.graphics.getHeight() / 10f));
        groundPlat5 = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(ice256), world,
                new Vector2(Gdx.graphics.getWidth() / 2f + 1900f, Gdx.graphics.getHeight() / 10f - 250f));
        groundPlat6 = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(ice256), world,
                new Vector2(Gdx.graphics.getWidth() / 2f + 2156f, Gdx.graphics.getHeight() / 10f - 250f));
        groundPlat7 = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(ice256), world,
                new Vector2(Gdx.graphics.getWidth() / 2f + 2412f, Gdx.graphics.getHeight() / 10f - 250f));

        house = new SpriteBody(BodyDef.BodyType.StaticBody, new Texture(houseTex), world,
                new Vector2(Gdx.graphics.getWidth() / 2f + 2412f, Gdx.graphics.getHeight() / 10f - 26f));

        evilObstacle1 = new EvilObstacle(evil128, world, this,
                new Vector2(Gdx.graphics.getWidth() / 2f + 600f, Gdx.graphics.getHeight() / 10f + 50f));
        evilObstacle2 = new EvilObstacle(evil224, world, this,
                new Vector2(Gdx.graphics.getWidth() / 2f - 400f, Gdx.graphics.getHeight() / 10f + 150f));

        wall = new EvilObstacle[4];
        for (int i = 0; i < 4; i++)
        {
            wall[i] = new EvilObstacle(evil128, world, this,
                    new Vector2(Gdx.graphics.getWidth() / 2f + 1200f,
                            Gdx.graphics.getHeight() / 10f + 150f + 50f * i));
        }

        path = new EvilObstacle[5];
        for (int i = 0; i < 5; i++)
        {
            float xOffset = i % 2 == 0 ? -100f : 100f;
            path[i] = new EvilObstacle(evil96, world, this,
                    new Vector2(Gdx.graphics.getWidth() / 2f + 1900f + xOffset,
                            Gdx.graphics.getHeight() / 10f + 100f * i));
        }
    }

    public static GIPlatformer getInstance()
    {
        return instance;
    }

    private void killPlayer()
    {
        player.dispose(world);
        player = new Player(world, new Texture("char1-64x.png"));
        player.loadPlayerTextures();
    }

    @Override
    public void render()
    {
        world.step(1 / 60f, 6, 2);

        // Controller polling for axes on the right and left sticks.
        for (Controller controller : Controllers.getControllers())
        {
            for (int i = 0; i <= 6; i++)
            {
                eventBus.post(new ControllerEvent(ControllerEvent.ControllerEventType.AXIS_POLLING, controller, i,
                        controller.getAxis(i)));
            }
        }

        player.update();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (player.getSprite().getX() > Gdx.graphics.getWidth() / 2)
        {
            camera.position.set(player.getSprite().getX(), camera.viewportHeight / 2f, 0);
        }

        if (player.getSprite().getY() < 0 || shouldKillPlayer) // Resets the player.
        {
            this.killPlayer();
            shouldKillPlayer = false;
            return;
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(135f / 255f, 206f / 255f, 235f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY());

        groundPlat1.draw(batch);
        groundPlat2.draw(batch);
        groundPlat3.draw(batch);
        groundPlat4.draw(batch);
        groundPlat5.draw(batch);
        groundPlat6.draw(batch);
        groundPlat7.draw(batch);
        house.draw(batch);

        evilObstacle1.body.draw(batch);
        evilObstacle2.body.draw(batch);

        for (EvilObstacle evilObstacle : wall)
        {
            evilObstacle.body.draw(batch);
        }

        for (EvilObstacle evilObstacle : path)
        {
            evilObstacle.body.draw(batch);
        }

        if (victory)
        {
            victorySprite.setPosition(camera.position.x - victorySprite.getWidth() / 2,
                    camera.position.y - victorySprite.getHeight() / 2);
            batch.draw(victorySprite, victorySprite.getX(), victorySprite.getY());
        }

        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();

        evilObstacle1.dispose();
        evilObstacle2.dispose();

        groundPlat1.dispose();
        groundPlat2.dispose();
        groundPlat3.dispose();
        groundPlat4.dispose();
        groundPlat5.dispose();
        groundPlat6.dispose();
        groundPlat7.dispose();

        house.dispose();

        for (EvilObstacle e : wall)
        {
            if (e != null)
            {
                e.dispose();
            }
        }

        for (EvilObstacle e : path)
        {
            if (e != null)
            {
                e.dispose();
            }
        }

        player.dispose(world);
        world.dispose();
    }
}
