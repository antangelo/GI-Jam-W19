package io.github.antangelo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import io.github.antangelo.event.CollisionEvent;
import io.github.antangelo.event.Event;
import io.github.antangelo.event.EventBus;
import io.github.antangelo.event.IEventListener;

public class Player implements IEventListener
{
    private SpriteBody spriteBody;
    private int jumpingFrames = -1, jumpCounter = 0;
    private boolean standing = true;

    public static final int JUMP_ACTION = 0;

    public Player(World world, Texture texture)
    {
        spriteBody = new SpriteBody(texture, world,
                new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f));
        EventBus.subscribe(CollisionEvent.class, this);
    }

    public SpriteBody getSprite()
    {
        return spriteBody;
    }

    public void update()
    {
        if (jumpingFrames != -1 && jumpingFrames < 40)
        {
            jumpingFrames++;
            spriteBody.getBody().applyForceToCenter(0, 600f / Math.min(jumpingFrames, 20f), true);
        }
        else if (jumpingFrames >= 40)
        {
            jumpingFrames = -1;
            jumpCounter = 2;
        }

        spriteBody.update();
    }

    public void axisDown(int axis, float value)
    {

    }

    public void actionDown(int actionType)
    {
        switch (actionType)
        {
            case JUMP_ACTION:
                if (!canJump()) break;
                spriteBody.getBody().setLinearVelocity(spriteBody.getBody().getLinearVelocity().x, 0f);
                jumpCounter++;
                jumpingFrames = 0;
                break;
        }
    }

    public void actionUp(int actionType)
    {
        switch (actionType)
        {
            case JUMP_ACTION:
                if (!canJump()) break;
                jumpingFrames = -1;
                break;
        }
    }

    boolean canJump()
    {
        //System.out.println(jumpingFrames);
        return jumpCounter < 2 && standing;
    }

    @Override
    public boolean post(Event event)
    {
        if (event instanceof CollisionEvent)
        {
            CollisionEvent collisionEvent = (CollisionEvent) event;
            if (collisionEvent.collisionEventType == CollisionEvent.CollisionEventType.BEGIN_CONTACT
                    && collisionEvent.involves(spriteBody.getBody()))
            {
                jumpCounter = 0;
            }
        }

        return true;
    }
}
