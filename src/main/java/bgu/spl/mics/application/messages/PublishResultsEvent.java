package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

import java.util.LinkedList;

public class PublishResultsEvent<T>  implements Event<T> {
    private Model model;
    private Future<T> future;

    public PublishResultsEvent(Model model){
        this.model=model;
        future = new Future<>();
    }

    @Override
    public Future<T> getFuture() {
        return future;
    }

    @Override
    public Model getModel() {
        return model;
    }
}
