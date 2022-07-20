package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent<T> implements Event<T> {
    private Model model;
    private Future<T> future;

    public TrainModelEvent (Model _model){
        model = _model;
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
