package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.LinkedList;

public class FinishPublishConferenceBroadcast  implements Broadcast {

    private LinkedList<String> models;
    public FinishPublishConferenceBroadcast(LinkedList<String> models){
        this.models=models;
    }
    public LinkedList<String> getModels(){return models;}

}


