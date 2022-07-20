package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;

import java.util.Collection;
import java.util.LinkedList;

public class PublishConferenceBroadcast implements Broadcast {

    private LinkedList<String> models;

    public PublishConferenceBroadcast(LinkedList<String> models){
        this.models=models;
    }

}
