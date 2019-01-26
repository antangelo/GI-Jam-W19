package io.github.antangelo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GIPlatformer extends ApplicationAdapter
{
    private SpriteBatch batch;
    private Texture img;
    private SpriteBody sprite;
    private World world;

    private static final Vector2 GRAVITY = new Vector2(0, -98F);

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        world = new World(GRAVITY, true);
        sprite = new SpriteBody(img, world);

        sprite.setRealPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
    }

    @Override
    public void render()
    {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        sprite.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        img.dispose();
    }
}
