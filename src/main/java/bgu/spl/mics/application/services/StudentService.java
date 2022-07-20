package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.LinkedList;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
    private Student student;
    private Model[] models;
    private int counterOfModels;
    private boolean modelInProcess;
    public StudentService( Student _student, Model[] _models) {
        super(_student.getName()+" StudentService");
        student=_student;
        models=_models;
        counterOfModels=0;
        modelInProcess=false;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(PublishConferenceBroadcast.class, (m) ->student.setPapersRead(student.getPapersRead() + 1));
        subscribeBroadcast(TerminateBroadcast.class, (m) -> this.terminate());
        subscribeBroadcast(TickBroadcast.class, (m)->onTick());
        subscribeBroadcast(FinishTrainModelBroadcast.class, (m) -> finishTrainModelBroadcast(m));
        subscribeBroadcast(FinishTestBroadcast.class, (m)->finishTestBroadcast(m));
        subscribeBroadcast(FinishPublishConferenceBroadcast.class, (m)->finishPublishConferenceBroadcast(m));
    }

    public void onTick(){
        if (counterOfModels>=models.length)
            return;;
        if (!modelInProcess) {
            modelInProcess=true;
            TrainModelEvent<Model> trainModelEvent = new TrainModelEvent<Model>(models[counterOfModels]);
            System.out.println("start train name:" + models[counterOfModels].getName());
            sendEvent(trainModelEvent);
        }
    }
    public void finishTrainModelBroadcast(FinishTrainModelBroadcast modelBroadcast){
        if(counterOfModels<models.length&&models[counterOfModels]!=null && models[counterOfModels].getName()!=null) {
            if (modelBroadcast.getModel().getName().equals(models[counterOfModels].getName())) {
                System.out.println("finish train, start test");
                TestModelEvent<Model> testModelEvent = new TestModelEvent<Model>(models[counterOfModels]);
                sendEvent(testModelEvent);
            }
        }
    }

    public void finishTestBroadcast(FinishTestBroadcast modelBroadcast){
        if(models[counterOfModels]!=null && models[counterOfModels].getName()!=null) {
            if (modelBroadcast.getModel().getName().equals(models[counterOfModels].getName())) {
                System.out.println(modelBroadcast.getModel().getResults());
                if (modelBroadcast.getModel().getResults().equals("Good")) {
                    PublishResultsEvent<Model> publishResultsEvent = new PublishResultsEvent<Model>(models[counterOfModels]);
                    System.out.println("student start publish broadcast");
                    sendEvent(publishResultsEvent);
                } else {
                    student.AddTrainedModel(models[counterOfModels]);
                    System.out.println("student finished model- " + models[counterOfModels].getName());
                    counterOfModels++;
                    modelInProcess = false;
                }
            }}
    }

    public void finishPublishConferenceBroadcast(FinishPublishConferenceBroadcast modelBroadcast){
        Boolean needPublish=false;
        if (modelBroadcast==null || modelBroadcast.getModels()==null )
            return;
        for (String str : modelBroadcast.getModels())
        {
            if (counterOfModels<models.length && str.equals(models[counterOfModels].getName()))
                needPublish=true;
        }
        if (needPublish) {
            System.out.println("arrive to publish");
            student.setPublications(student.getPublications() + 1);
            student.AddTrainedModel(models[counterOfModels]);
            System.out.println("-------------------student finished model- " + models[counterOfModels].getName());
            counterOfModels++;
            modelInProcess = false;
        }
    }

    public void sendModel(){

//        for (Model m : models) {
//            TrainModelEvent<Model> trainModelEvent = new TrainModelEvent<Model>(models[counterOfModels]);
//            System.out.println("start train name:"+models[counterOfModels].getName());
//            sendEvent(trainModelEvent);
//            trainModelEvent.getFuture().get();
//            System.out.println("finish train, start test");
//            TestModelEvent<Model> testModelEvent = new TestModelEvent<Model>(models[counterOfModels]);
//            sendEvent(testModelEvent);
//            System.out.println("wait for resolve");
//            String result = testModelEvent.getFuture().get().getResults();
//            System.out.println(result);
//            System.out.println("finish test, start publish");
//            if (trainModelEvent.getModel().getResults().equals("Good"))
//            {
////                PublishResultsEvent<Model> publishResultsEvent = new PublishResultsEvent<Model>(models[counterOfModels]);
////                sendEvent(publishResultsEvent);
////                publishResultsEvent.getFuture().get();
////                student.setPublications(student.getPublications()+1);
//            }
//            student.AddTrainedModel(models[counterOfModels]);
//            System.out.println("student finished model- "+models[counterOfModels].getName());
//            counterOfModels++;
//        }
    }
}