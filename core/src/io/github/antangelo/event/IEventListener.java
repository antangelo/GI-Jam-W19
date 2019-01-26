package io.github.antangelo.event;

public interface IEventListener
{
    /**
     * Called when an event is posted
     *
     * @param event The event that is being posted
     * @return true if the event is cancelled, false if the event should pass.
     */
    boolean post(Event event);
}
