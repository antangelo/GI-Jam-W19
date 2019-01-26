package io.github.antangelo.event.listener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import io.github.antangelo.event.CollisionEvent;
import io.github.antangelo.event.CollisionEvent.CollisionEventType;
import io.github.antangelo.event.EventBus;

public class CollisionListener implements ContactListener
{
    private EventBus eventBus;

    public CollisionListener(EventBus bus)
    {
        this.eventBus = bus;
    }

    @Override
    public void beginContact(Contact contact)
    {
        eventBus.post(new CollisionEvent(CollisionEventType.BEGIN_CONTACT, contact));
    }

    @Override
    public void endContact(Contact contact)
    {
        eventBus.post(new CollisionEvent(CollisionEventType.END_CONTACT, contact));
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold)
    {
        // Only in pre-solve does setEnabled cancel the collision, so no sense in having it anywhere else.
        contact.setEnabled(eventBus.post(new CollisionEvent(CollisionEventType.PRE_SOLVE, contact).setManifold(manifold)));
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse)
    {
        eventBus.post(new CollisionEvent(CollisionEventType.POST_SOLVE, contact).setContactImpulse(contactImpulse));
    }
}
