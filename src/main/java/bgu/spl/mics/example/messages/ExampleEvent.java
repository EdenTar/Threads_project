package bgu.spl.mics.example.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class ExampleEvent implements Event<String>{

    private String senderName;
    private Future<String> future;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
        future = new Future<String>();
    }

    public String getSenderName() {
        return senderName;
    }

    @Override
    public Future<String> getFuture() {
        return future;
    }

    @Override
    public Model getModel() {
        return null;
    }
}