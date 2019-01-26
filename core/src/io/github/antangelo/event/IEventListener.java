package io.github.antangelo.event;

public interface IEventListener
{
    /**
     * Called when an event is posted
     *
     * @param event The event that is being posted
     * @return false if the event is cancelled, true if the event should execute.
     */
    boolean post(Event event);
}
