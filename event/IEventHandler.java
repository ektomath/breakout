
package breakout.event;

/*
    Contract for all classes acting as targets
    for events from model. BreakoutGUI implements this

    **** Nothing to do here ****
 */
public interface IEventHandler {
    void onModelEvent(ModelEvent evt);
}
