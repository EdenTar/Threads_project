package bgu.spl.mics;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.example.messages.ExampleEvent;
import junit.framework.TestCase;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class MessageBusTest {

    private MessageBusImpl messageBus;
    private GPU gpu;
    private GPUService gpuService;
    private Event event;
    private Broadcast broadcast1;
    private Broadcast broadcast2;
    private Broadcast broadcast3;
    private Broadcast broadcast4;
    private StudentService studentService1;
    private StudentService studentService2;
    private StudentService studentService3;
    private StudentService studentService4;
    private Model model1;
    private Model model2;
    private Model model3;
    private Student student1;
    private Student student2;
    private Student student3;
    @Before
    public void setUp() throws Exception {

        gpu = new GPU("RTX3090",1 );
        gpuService= new GPUService(gpu);
       gpuService= new GPUService(new GPU("GTX1080",1));
     //   broadcast1=new PublishConferenceBroadcast();
      //  broadcast2=new PublishConferenceBroadcast();
     //  broadcast3=new PublishConferenceBroadcast();
        //broadcast4=null;

        //studentService2=null;
        student1=new Student("n1","d1","s");
        student2=new Student("n2","d2","s");
        student3=new Student("n3","d3","s");
        model1=new Model("m1",student1,"images",2000);
        model2=new Model("m2",student2,"images",2000);
        model3=new Model("m3",student3,"images",2000);
        Model[] models=new Model[3];
        models[0]=model1;
        models[1]=model2;
        models[2]=model3;
        LinkedList<Model> list=new LinkedList();
        list.add(model1);
        list.add(model2);

        studentService1=new StudentService(student1,models);
       //studentService3=new StudentService(student1);
      // studentService4=new StudentService(student2);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void subscribeEvent() {

//        //microservice==null
//        assertThrows((MessageBusImpl.getInstance().isSubscribeEvent(event.getClass())),NullPointerException.class);
//
//        //m.isMicroServiceRegistred(m)==false
//        assertFalse(messageBus.isSubscribeEvent(TrainModelEvent.class,gpuService));
//        messageBus.register(gpuService);
//        //before subscribeEvent
//        assertFalse(messageBus.isSubscribeEvent(trainModelEvent.getClass(),gpuService));
//        messageBus.subscribeEvent(TrainModelEvent.class, gpuService);
//        //after subscribeEvent
//        assertTrue(messageBus.isSubscribeEvent(.getClass(),gpuService));
    }

    @Test
    public void subscribeBroadcast() {
        messageBus.register(studentService1);
        messageBus.subscribeBroadcast(TickBroadcast.class,gpuService);
    }

    @Test
    public void complete() {
        ExampleEvent exampleEvent = new ExampleEvent("CompleteTest");
        Future<String> future = exampleEvent.getFuture();
        assertFalse(future.isDone());
        gpuService.complete(exampleEvent,"CompleteTest");
        assertTrue(future.isDone());
    }

    @Test
    public void sendBroadcast() {
        // messageBus.sendBroadcast(broadcast4);
   //     assertFalse(messageBus.IsBroadcastSent(broadcast4));

        messageBus.sendBroadcast(broadcast1);
        assertFalse(messageBus.IsBroadcastSent(broadcast1));

//        StudentService studentService=new StudentService("name");
//        messageBus.subscribeBroadcast(broadcast2.getClass(),studentService);
//        assertTrue(messageBus.IsBroadcastSent(broadcast1));
    }

    @Test
    public void sendEvent() {
        //e = null
        assertFalse(messageBus.getInstance().isSendEvent(null));
        ExampleEvent exampleEvent = new ExampleEvent("sentEventTest");
        //before register
        assertFalse(messageBus.getInstance().isSendEvent(exampleEvent));
        messageBus.getInstance().register(gpuService);
        //before subscribe
        assertFalse(messageBus.getInstance().isSendEvent(exampleEvent));
        messageBus.getInstance().subscribeEvent(exampleEvent.getClass(),gpuService);
        //before send
        assertFalse(messageBus.getInstance().isSendEvent(exampleEvent));
        messageBus.getInstance().sendEvent(exampleEvent);
        //after send
        assertTrue(messageBus.getInstance().isSendEvent(exampleEvent));
    }

    @Test
    public void register() {
           messageBus.register(gpuService);
       // assertFalse(messageBus.isMicroServiceRegistred(studentService2));

        messageBus.register(studentService1);
        assertTrue(messageBus.isMicroServiceRegistered(studentService1));
    }

    @Test
    public void unregister() {
        messageBus.getInstance().register(gpuService);
        assertTrue(messageBus.getInstance().isMicroServiceRegistered(gpuService));
        messageBus.getInstance().unregister(gpuService);
        assertFalse(messageBus.getInstance().isMicroServiceRegistered(gpuService));
    }

    @Test
    public void awaitMessage() {
        Thread t = new Thread(()->{
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageBus.subscribeBroadcast(broadcast1.getClass(),studentService1);
            messageBus.sendBroadcast(broadcast1);

        });
        t.start();
        assertFalse(messageBus.isServiceQueueEmpty(studentService1));
        try {
            messageBus.awaitMessage(studentService1);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        try{
            Thread.sleep(100);
            assertEquals(Thread.State.WAITING, t.getState());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            Thread.sleep(5000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(messageBus.isServiceQueueEmpty(studentService1));
    }
}