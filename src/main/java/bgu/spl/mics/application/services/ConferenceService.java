package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.ConfrenceInformation;

import java.util.LinkedList;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConferenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {

    private ConfrenceInformation confrenceInformation;
    private LinkedList<String> goodModels;
    private int tickCount;

    public ConferenceService(ConfrenceInformation confrenceInformation) {
        super("ConferenceService");
        goodModels=new LinkedList<>();
        this.confrenceInformation=confrenceInformation;
        tickCount=0;
    }

    public void publish(){
            //System.out.println("tick count on conference: " + tickCount + "  conference date " + confrenceInformation.getDate());

        if (tickCount==confrenceInformation.getDate()) {
           // System.out.println("---------------------tick count on conference: "+ tickCount+"  conference date "+confrenceInformation.getDate());
            sendBroadcast(new PublishConferenceBroadcast(goodModels));
            sendBroadcast(new FinishPublishConferenceBroadcast(goodModels));
            MessageBusImpl.getInstance().unregister(this);
            terminate();
        }
    }

    public void onTick(){
        tickCount++;
        publish();
    }

    @Override
    protected void initialize() {
        subscribeEvent(PublishResultsEvent.class,(m)->{goodModels.add(m.getModel().getName());
        confrenceInformation.getPublishedModels().add(m.getModel());});
        subscribeBroadcast(TickBroadcast.class,(m)->onTick());
        subscribeBroadcast(TerminateBroadcast.class,(m)->this.terminate());
    }
}

/*ConferenceService: Each conference will have this service, responsible for
aggregating good results and publishing them via the PublishConferenceBroadcast,
after publishing results the conference will unregister from the system.

PublishResultsEvent: Sent by the student, handled by the Conference micro
service, the conference simply aggregates the model names of successful models
from the students (until it publishes everything).

PublishConferenceBroadcast: Sent by the conference at a set time (according to
time ticks, see below), will broadcast all the aggregated results to all the students.
After this event is sent, the conference unregisters from the system.

 */