package io.github.antangelo.event;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionEvent extends Event
{
    public enum CollisionEventType
    {
        BEGIN_CONTACT,
        END_CONTACT,
        PRE_SOLVE,
        POST_SOLVE
    }

    public CollisionEventType collisionEventType;
    public Contact contact;
    public Manifold manifold;
    public ContactImpulse contactImpulse;

    public CollisionEvent(CollisionEventType type, Contact contact)
    {
        this.collisionEventType = type;
        this.contact = contact;
    }

    public CollisionEvent setManifold(Manifold manifold)
    {
        this.manifold = manifold;
        return this;
    }

    public CollisionEvent setContactImpulse(ContactImpulse contactImpulse)
    {
        this.contactImpulse = contactImpulse;
        return this;
    }

    public boolean involves(Body body)
    {
        return this.contact.getFixtureA().getBody().equals(body) || this.contact.getFixtureB().getBody().equals(body);
    }
}
