
package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;



public class FinishTestBroadcast implements Broadcast {
    private Model model;
    public FinishTestBroadcast(Model model){
        this.model=model;
    }
    public Model getModel() {
        return model;
    }
}


