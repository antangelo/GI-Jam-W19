package io.github.antangelo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.antangelo.event.CollisionEvent;
import io.github.antangelo.event.Event;
import io.github.antangelo.event.EventBus;
import io.github.antangelo.event.IEventListener;

public class EvilObstacle implements IEventListener
{
    private GIPlatformer game;
    public SpriteBody body;

    private Texture texture;

    public EvilObstacle(String sprite, World world, GIPlatformer game, Vector2 position)
    {
        this.game = game;
        this.texture = new Texture(sprite);
        this.body = new SpriteBody(BodyDef.BodyType.StaticBody, texture, world, position);
        EventBus.subscribe(CollisionEvent.class, this);
    }

    @Override
    public boolean post(Event event)
    {
        if (event instanceof CollisionEvent)
        {
            CollisionEvent collisionEvent = (CollisionEvent) event;
            if (!collisionEvent.involves(body.getBody()) || !collisionEvent.involves(game.player.getSprite().getBody()))
            {
                return true;
            }
            if (collisionEvent.collisionEventType != CollisionEvent.CollisionEventType.BEGIN_CONTACT) return true;

            game.shouldKillPlayer = true;
        }

        return true;
    }

    public void dispose()
    {
        this.texture.dispose();
    }
}
