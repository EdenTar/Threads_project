package bgu.spl.mics.application.services;
import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.GPU;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;
    private LinkedBlockingQueue<Event> arrivedTrainEvents;


    public GPUService(GPU gpu) {
        super("GPUService");
        this.gpu=gpu;
        arrivedTrainEvents=new LinkedBlockingQueue<>();
    }

    private void startNewEvent(){
        if(gpu.getComplete()) {
            System.out.println("complete enter");
            callComplete();
        }

        if(gpu.getEvent()==null) {
            Event e = null;
            try {
                if (!arrivedTrainEvents.isEmpty()) {
                    e = arrivedTrainEvents.take();
                    gpu.setEvent(e);
                    e.getModel().setStatus("Training");
                }
            } catch (InterruptedException exception) {
                System.out.println("interrupted thread");
            }

        }
    }
    private void callComplete(){

        complete(gpu.getEvent(),gpu.getResult());
        if(gpu.getResult()!=null && gpu.getEvent().getClass()==TrainModelEvent.class)
            sendBroadcast(new FinishTrainModelBroadcast(gpu.getResult()));
        else if (gpu.getResult()!=null && gpu.getEvent().getClass()==TestModelEvent.class)
            sendBroadcast(new FinishTestBroadcast(gpu.getResult()));
        System.out.println("&&&complete&&&" + gpu.getEvent().getClass());
        gpu.setComplete(false);
        gpu.setEvent(null);
    }
    @Override
    protected void initialize() {
        subscribeEvent(TrainModelEvent.class,(m)->arrivedTrainEvents.add(m));
        subscribeEvent(TestModelEvent.class,  (m)->{gpu.setEvent(m); gpu.testModel(); callComplete();});
        subscribeBroadcast(TerminateBroadcast.class,(m)->this.terminate());
        subscribeBroadcast(TickBroadcast.class,(m)->{ startNewEvent();gpu.onTick();});
    }

}