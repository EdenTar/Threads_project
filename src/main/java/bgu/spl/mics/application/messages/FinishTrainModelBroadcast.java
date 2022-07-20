package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class FinishTrainModelBroadcast implements Broadcast {

    private Model model;
    public FinishTrainModelBroadcast(Model model){
        this.model = model;}
    public Model getModel() {
        return model;
    }
}
