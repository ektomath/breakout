package breakout.event;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
     Service to send events **from model** to GUI
     GUI must know if there has been a collision in the model etc.

     NOTE: Events **from GUI** to model handled by JavaFX events and keyboard listeners etc.
     see BreakoutGUI

     To use this:

     // In class interested to get events
     {
        EventBus.INSTANCE.register( this )    // Register instance to get events
     }

     @Override
     void onModelEvent(ModelEvent evt){
        // This method called by EventBus for all registered instances
        if( evt.type == ... )  // Interested in this event?
              .. = evt.getData;   // Possibly use, data sent from model
     }


     // In class emitting events (model).
     {
        EventBus.INSTANCE.publish(new ModelEvent('type', 'data'));  // Send to all registered
     }


     *** Nothing to do here ****

 */
public enum EventBus {

    INSTANCE;

    private final List<IEventHandler> handlers
            = Collections.synchronizedList(new ArrayList<>());
    private boolean trace = false;

    public void register(IEventHandler handler) {
        handlers.add(handler);
    }

    public void unRegister(IEventHandler handler) {
        handlers.remove(handler);
    }

    public void publish(ModelEvent evt) {
        // Tracking all events
        if (trace) {
            System.out.println(evt);
        }
        // Possible threads
        synchronized (handlers) {
            handlers.stream().forEach((evh) -> {
                evh.onModelEvent(evt);
            });
        }
    }

    public void publish(ModelEvent.Type tag) {
        publish(new ModelEvent(tag, null));
    }
}
