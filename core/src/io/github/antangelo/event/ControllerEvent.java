package io.github.antangelo.event;

import com.badlogic.gdx.controllers.Controller;

public class ControllerEvent extends Event
{
    public enum ControllerEventType
    {
        BUTTON_UP,
        BUTTON_DOWN,
        AXIS,
    }

    public ControllerEventType type;
    public Controller controller;
    public int identifier;
    public float value;

    public ControllerEvent(ControllerEventType eventType, Controller controller, int id)
    {
        this.type = eventType;
        this.controller = controller;
        this.identifier = id;
    }

    public ControllerEvent(ControllerEventType type, Controller controller, int id, float value)
    {
        this(type, controller, id);
        this.value = value;
    }
}
