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

    public void post(Event event)
    {
        if (!listeners.containsKey(event.getClass())) return;

        for (IEventListener listener : listeners.get(event.getClass()))
        {
            // TODO: Implement event cancelling
            listener.post(event);
        }
    }
}
