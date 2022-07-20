
package bgu.spl.mics.application.objects;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TrainModelEvent;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}

    private Type type;
    private Event event;
    private final Cluster cluster;
    private LinkedBlockingQueue<DataBatch> processedData;
    private int capacity;
    private LinkedBlockingQueue<DataBatch> unProcessedData;
    private int ticksNum; //number of ticks that the training takes for the specific type of gpu
    private int ticksCounter; //how many ticks have been so far
    private int gpuID;
    private int numUnprocessedBatches;
    private boolean complete;
    private Model result;



    public GPU(String type,int id)
    {

        numUnprocessedBatches=0;
        complete=false;
        if(type.equals("RTX3090"))
            this.type = Type.RTX3090;
        else if(type.equals("RTX2080"))
            this.type = Type.RTX2080;
        else
            this.type = Type.GTX1080;

        cluster = Cluster.getInstance();
        this.event = null;
        ticksCounter=0;
        if(this.type== Type.RTX3090) {
            ticksNum = 1;
            capacity=32;
            processedData=new LinkedBlockingQueue<>(32);
        }
        else if(this.type== Type.RTX2080) {
            ticksNum = 2;
            capacity=16;
            processedData=new LinkedBlockingQueue<>(16);
        }
        else {
            ticksNum = 4;
            capacity=8;
            processedData=new LinkedBlockingQueue<>(8);
        }

        gpuID=id;
        unProcessedData=new LinkedBlockingQueue<>();
    }

    /**
     * separate the data to dataBatches and send every dataBatch to the cluster
     */
    public void dataSeparator(){
        //System.out.println("Separator");
        int index=0;
        numUnprocessedBatches=0;
        while (index<event.getModel().getData().getSize()){
            unProcessedData.add(new DataBatch(event.getModel().getData(),index,gpuID));
            index+=1000;
            numUnprocessedBatches++;
        }
      //  System.out.println("#############data separator########### "+Thread.currentThread());
        System.out.println("size: "+ event.getModel().getData().getSize()+" current thread:  " +Thread.currentThread()+" num of numUnprocessedBatches: "+ numUnprocessedBatches);
    }

    /**
     * train the processed dataBatches
     */

    public void trainModel(){
        //System.out.println(numUnprocessedBatches);
        //System.out.println("training batch"+Thread.currentThread());

        if (ticksCounter == ticksNum && numUnprocessedBatches>0) {
            numUnprocessedBatches--;
            takeProcessedData();
           // System.out.println("current thread:  " +Thread.currentThread()+" num of numUnprocessedBatches after remove: "+ numUnprocessedBatches);
            ticksCounter=0;
            if(!processedData.isEmpty())
                processedData.remove();

        }
        //System.out.println(numUnprocessedBatches);
        if(numUnprocessedBatches==0)
        {
            /*System.out.println("name of student that finish train model: "+getEvent().getModel().getStudent().getName());
            System.out.println("finished train model: "+getEvent().getModel().getName());
            System.out.println("total num of data process"+cluster.getStatistics().getTotalDataBatchesProcessedByCPUs());
            System.out.println("cpu time used "+cluster.getStatistics().getCpuTime());*/
            event.getModel().setStatus("Trained");
            cluster.getStatistics().addTrainedModelNames(event.getModel().getName());
            result=event.getModel();
            System.out.println("result: "+result.toString());
            setComplete(true);
//            cluster.getStatistics().addGPUTime(totalCount);
           System.out.println("current thread:  " +Thread.currentThread()+" Trained ");

        }
    }
    /**
     * tests model
     */
    private String getRandomBoolean(float probability) { //in %
        double randomValue = Math.random()*100;  //0.0 to 99.9
        if(randomValue <= probability)
            return "Good";
        return "Bad";
    }
    public void testModel(){
      //  System.out.println("test batch"+Thread.currentThread());
        String stringResult;
        if (event.getModel().getStudent().getStatus().equals("MSc")){
            stringResult=getRandomBoolean(60);
        }
        else {
            stringResult=getRandomBoolean(80);
        }
        event.getModel().setResults(stringResult);
        result=event.getModel();
        System.out.println("result: "+result.toString());
        setComplete(true);
        System.out.println("finish test"+Thread.currentThread());

    }
    public void takeProcessedData(){
        if(processedData.size()<capacity )
        {
            try {
                processedData.add(cluster.getDoneProcess().get(gpuID).take());
                //System.out.println("take processed data"+Thread.currentThread());
            }
            catch (InterruptedException exception){
                System.out.println("Interrupted");
            }
        }
    }
    /**
     * updating the ticks on every tick the we get notify on from the broadcast
     */
    public void onTick(){
   //     System.out.println("-----tick--------");
        cluster.getStatistics().addGPUTime(1);
        if(event!=null && event.getClass()==TrainModelEvent.class) {
            ticksCounter++;
            trainModel();
        }
    }

    public Type getType(){return type;}
    public Event getModel(){return event;}
    public void setEvent(Event newEvent){
        System.out.println("set event "+Thread.currentThread());
            this.event = newEvent;
            if (newEvent!=null && event.getClass() == TrainModelEvent.class)
                dataSeparator();
    }
    public int getNumUnprocessedBatches(){return numUnprocessedBatches;}
    public int getTicksCounter(){return ticksCounter;}
    public int getId(){return gpuID;}
    public LinkedBlockingQueue<DataBatch> getProcessedData(){return processedData;}
    public LinkedBlockingQueue<DataBatch> getUnProcessedData(){return unProcessedData;}
    public int getCapacity(){
        return capacity;
    }
    public Event getEvent(){
        return event;
    }
    public boolean getComplete(){return complete;}
    public void setComplete(boolean isComplete){
        complete=isComplete;
    }
    public Model getResult() {
        return result;
    }
}
