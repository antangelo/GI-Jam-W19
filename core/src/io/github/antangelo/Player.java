package io.github.antangelo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import io.github.antangelo.event.*;

public class Player implements IEventListener
{
    private SpriteBody spriteBody;
    private int jumpingFrames = -1, jumpCounter = 0;
    private boolean standing = true;

    private static final int JUMP_ACTION = 1;

    public Player(World world, Texture texture)
    {
        spriteBody = new SpriteBody(texture, world,
                new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f));
        EventBus.subscribe(CollisionEvent.class, this);
        EventBus.subscribe(ControllerEvent.class, this);
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

    public void axisChanged(int axis, float value)
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
        else if (event instanceof ControllerEvent)
        {
            ControllerEvent controllerEvent = (ControllerEvent) event;

            switch (controllerEvent.type)
            {
                case BUTTON_DOWN:
                    this.actionDown(controllerEvent.identifier);
                    break;
                case BUTTON_UP:
                    this.actionUp(controllerEvent.identifier);
                    break;
                case AXIS:
                    this.axisChanged(controllerEvent.identifier, controllerEvent.value);
                    break;
            }
        }

        return true;
    }
}
