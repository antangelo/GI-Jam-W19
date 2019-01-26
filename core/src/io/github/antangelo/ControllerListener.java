package io.github.antangelo;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import io.github.antangelo.event.ControllerEvent;
import io.github.antangelo.event.ControllerEvent.ControllerEventType;
import io.github.antangelo.event.EventBus;

public class ControllerListener extends ControllerAdapter
{
    private EventBus eventBus;

    public ControllerListener(EventBus eventBus)
    {
        this.eventBus = eventBus;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonId)
    {
        return eventBus.post(new ControllerEvent(ControllerEventType.BUTTON_DOWN, controller, buttonId));
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonId)
    {
        return eventBus.post(new ControllerEvent(ControllerEventType.BUTTON_UP, controller, buttonId));
    }

    @Override
    public boolean axisMoved(Controller controller, int axisId, float value)
    {
        return eventBus.post(new ControllerEvent(ControllerEventType.AXIS, controller, axisId, value));
    }
}
