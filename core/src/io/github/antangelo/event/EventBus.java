package io.github.antangelo.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus
{
    private static Map<Class, List<IEventListener>> listeners = new HashMap<Class, List<IEventListener>>();

    public static void subscribe(Class eventType, IEventListener listener)
    {
        if (listeners.containsKey(eventType))
        {
            listeners.get(eventType).add(listener);
        }
        else
        {
            List<IEventListener> list = new ArrayList<IEventListener>();
            list.add(listener);
            listeners.put(eventType, list);
        }
    }

    public boolean post(Event event)
    {
        // We don't want to cancel the event if there are no handlers.
        if (!listeners.containsKey(event.getClass())) return true;
        boolean execute = true;

        for (IEventListener listener : listeners.get(event.getClass()))
        {
            // If the listener posts false, then execute will be false.
            // If execute is false and the listener posts true, execute will still be false.
            execute = listener.post(event) && execute;
        }

        return execute;
    }
}
