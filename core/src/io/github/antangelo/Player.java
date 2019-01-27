package io.github.antangelo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import io.github.antangelo.event.*;

public class Player implements IEventListener
{
    private SpriteBody spriteBody;
    private Texture idleTexture;
    private Texture[] slamTextures, uppercutTextures;
    private int jumpingFrames = -1, jumpCounter = 0, slammingFrames = -1, bashingFrames = -1;
    private boolean standing = true, canMove = true;

    private static final int JUMP_ACTION = 1, SLAM_ACTION = 2, UPPERCUT_ACTION = 3;

    public Player(World world, Texture texture)
    {
        idleTexture = texture;
        spriteBody = new SpriteBody(texture, world,
                new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 230));
        EventBus.subscribe(CollisionEvent.class, this);
        EventBus.subscribe(ControllerEvent.class, this);
    }

    public boolean isDamaging()
    {
        return slammingFrames != -1 || bashingFrames != -1;
    }

    public void loadPlayerTextures()
    {
        slamTextures = new Texture[6];
        final String slamTexturePrefix = "char1_move_slam/charslam64_";

        for (int i = 0; i < slamTextures.length; i++)
        {
            slamTextures[i] = new Texture(slamTexturePrefix + (i + 1) + ".png");
        }

        uppercutTextures = new Texture[7];
        final String uppercutTexturePrefix = "char1_move_uppercut/charbash64_";

        for (int i = 0; i < uppercutTextures.length; i++)
        {
            uppercutTextures[i] = new Texture(uppercutTexturePrefix + (i + 1) + ".png");
        }
    }

    private void disposeTextures()
    {
        for (Texture t : slamTextures)
        {
            t.dispose();
        }

        for (Texture t : uppercutTextures)
        {
            t.dispose();
        }
    }

    private void setSlamTexture(int frame)
    {
        if (frame < 3)
        {
            spriteBody.setTexture(slamTextures[0]);
        }
        else if (frame < 5)
        {
            spriteBody.setTexture(slamTextures[1]);
        }
        else if (frame < 8)
        {
            spriteBody.setTexture(slamTextures[2]);
        }
        else if (frame < 10)
        {
            spriteBody.setTexture(slamTextures[3]);
        }
        else if (frame < 12)
        {
            spriteBody.setTexture(slamTextures[4]);
        }
        else
        {
            spriteBody.setTexture(slamTextures[5]);
        }
    }

    private void setUppercutTexture(int frame)
    {
        if (frame < 7)
        {
            spriteBody.setTexture(uppercutTextures[0]);
        }
        else if (frame < 10)
        {
            spriteBody.setTexture(uppercutTextures[1]);
        }
        else if (frame < 15)
        {
            spriteBody.setTexture(uppercutTextures[2]);
        }
        else if (frame < 20)
        {
            spriteBody.setTexture(uppercutTextures[3]);
        }
        else if (frame < 28)
        {
            spriteBody.setTexture(uppercutTextures[4]);
        }
        else if (frame < 30)
        {
            spriteBody.setTexture(uppercutTextures[5]);
        }
        else if (frame < 32)
        {
            spriteBody.setTexture(uppercutTextures[6]);
        }
        else
        {
            spriteBody.setTexture(idleTexture);
        }
    }

    public SpriteBody getSprite()
    {
        return spriteBody;
    }

    public void update()
    {
        if (bashingFrames >= 0)
        {
            // Looking for height around 1.7 - this gives just under that value
            spriteBody.getBody().applyForceToCenter(0, -2.5f, true);
            bashingFrames++;

            this.setUppercutTexture(bashingFrames);

            if (Math.abs(spriteBody.getBody().getLinearVelocity().y) < 0.1)
            {
                bashingFrames = -1;
                canMove = true;
            }
        }

        /* When jumping, before the max jump height frame count apply a force */
        if (jumpingFrames != -1 && jumpingFrames < 40)
        {
            jumpingFrames++;
            spriteBody.getBody().applyForceToCenter(0, 20f / Math.min(jumpingFrames, 20f), true);
        }
        else if (jumpingFrames >= 40) // Above the max height, no force
        {
            jumpingFrames = -1;
        }

        /* Hold still in the air for 20 frames, then slam (this is done in reverse order in these if blocks) */
        if (slammingFrames >= 0 && slammingFrames >= 20)
        {
            spriteBody.getBody().applyLinearImpulse(new Vector2(0, -5f),
                    spriteBody.getBody().getWorldCenter(), true);
            slammingFrames = -2; // Prime for hitting ground
        }
        else if (slammingFrames >= 0)
        {
            this.setSlamTexture(slammingFrames);

            spriteBody.getBody().setLinearVelocity(spriteBody.getBody().getLinearVelocity().x, 0f);
            slammingFrames++;
        }

        spriteBody.update();
    }

    public void dispose(World world)
    {
        this.getSprite().getBody().setActive(false);
        this.idleTexture.dispose();
        this.disposeTextures();
    }

    private void axisChanged(int axis, float value)
    {
        if (!canMove || GIPlatformer.getInstance().victory || Math.abs(value) < 0.2) return;

        switch (axis)
        {
            case 0: // Left and right on the left stick
                // Since J = ∆p = ∆v * m
                spriteBody.getBody().applyLinearImpulse(new Vector2(0.05f * value * spriteBody.getBody().getMass(), 0),
                        spriteBody.getBody().getWorldCenter(), true);
                break;
            case 1: // Up and down on the left stick
                break;
            case 2: // Left and right on the right stick
                break;
            case 3: // Up and down on the right stick
                break;
            /*case 4:
                if (value < 0.2 || dashingFrames != -1) break;
                spriteBody.getBody().applyLinearImpulse(new Vector2(-0.5f * value * spriteBody.getBody().getMass(), 0),
                        spriteBody.getBody().getWorldCenter(), true);
                //dashingFrames = 0;
                break;
            case 5:
                if(value < 0.2 || dashingFrames != -1) break;
                spriteBody.getBody().applyLinearImpulse(new Vector2(0.5f * value * spriteBody.getBody().getMass(), 0),
                        spriteBody.getBody().getWorldCenter(), true);
                //dashingFrames = 0;
                break;*/
        }
    }

    private void actionDown(int actionType)
    {
        switch (actionType)
        {
            case JUMP_ACTION:
                if (!canJump()) break;
                spriteBody.getBody().setLinearVelocity(spriteBody.getBody().getLinearVelocity().x, 0f);
                jumpCounter++;
                jumpingFrames = 0;
                break;
            case SLAM_ACTION:
                if (standing || bashingFrames != -1 || slammingFrames != -1 || GIPlatformer.getInstance().victory)
                {
                    break;
                }
                slammingFrames = 0;
                break;
            case UPPERCUT_ACTION:
                if (!standing || GIPlatformer.getInstance().victory) break;
                bashingFrames = 0;
                canMove = false;
                jumpCounter = 1; // Leave one jump available when bashing
                spriteBody.getBody().applyLinearImpulse(new Vector2(0, 2.5f),
                        spriteBody.getBody().getWorldCenter(), true);
                break;
            case 0:
                if (GIPlatformer.getInstance().victory)
                {
                    GIPlatformer.getInstance().victory = false;
                    GIPlatformer.getInstance().shouldKillPlayer = true;
                }
                break;
        }
    }

    private void actionUp(int actionType)
    {
        if (actionType == JUMP_ACTION)
        {
            jumpingFrames = -1;
        }
    }

    private boolean canJump()
    {
        return jumpCounter <= 1 && slammingFrames == -1 && bashingFrames == -1 && !GIPlatformer.getInstance().victory;
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
                if (collisionEvent.involves(GIPlatformer.getInstance().house.getBody()))
                {
                    GIPlatformer.getInstance().victory = true;
                }

                jumpCounter = 0;
                standing = true;

                if (slammingFrames != -1)
                {
                    spriteBody.getBody().setLinearVelocity(spriteBody.getBody().getLinearVelocity().x, 0);
                    spriteBody.getBody().applyLinearImpulse(new Vector2(0, 0.5f),
                            spriteBody.getBody().getWorldCenter(), true);
                    slammingFrames = -1;
                    spriteBody.setTexture(this.idleTexture);
                }
            }
            else if (collisionEvent.collisionEventType == CollisionEvent.CollisionEventType.END_CONTACT
                    && collisionEvent.involves(spriteBody.getBody()))
            {
                standing = false;
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
                case AXIS_POLLING:
                    this.axisChanged(controllerEvent.identifier, controllerEvent.value);
                    break;
            }
        }

        return true;
    }
}
