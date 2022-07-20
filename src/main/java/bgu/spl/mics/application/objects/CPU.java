package bgu.spl.mics.application.objects;


/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    private int cores;
    private DataBatch currentData;
    private Cluster cluster;
    private int ticksCounter;

    public CPU(int coresUpdate)
    {
        cores = coresUpdate;
        cluster = Cluster.getInstance();
        currentData = null;
        ticksCounter=0;
    }

    /**
     * process the event by 1 tick- if the current databash finish the process- return it to cluster
     * @pre onTick() was called before
     */
    public void processing (){
        assert currentData != null;
        if ((currentData.GetTypeString().equals("Images") && ticksCounter==((32/cores)*4)) ||
                (currentData.GetTypeString().equals("Text") && ticksCounter==((32/cores)*2)) ||
                (currentData.GetTypeString().equals("Tabular") && ticksCounter==(32/cores)))
        {
            cluster.addProcessedData(currentData);
            removeDataBatch(currentData);
            cluster.getStatistics().addCPUTime(ticksCounter);
            cluster.getStatistics().addTotalDataBatchesProcessedByCPUs(1);
            ticksCounter=0;
        }
    }
    /**
     * called by the timeService for each tick
     * @pre tick recently happened
     * @post @pre_ticksCounter= ticksCounter+1
     */
    public void onTick()
    {
        if (currentData==null) {
            currentData = cluster.getDatabatch();
        }
        else {
            ticksCounter++;
            processing();
        }
    }

    /**
     * add the received databatch to the collection
     * @param dataBatch
     * @pre dataBatch!=null
     * @post data.pop()=dataBatch and GetIsProcessOpen()==true
     */
    public void addDataBatch(DataBatch dataBatch){
        currentData = dataBatch;
    }

    /**
     * remove the received databatch from the collection
     * @param dataBatch
     * @pre dataBatch!=null and IsDataBatchInData(dataBatch)==true
     * @post IsDataBatchInData(dataBatch)==false
     */
    public void removeDataBatch(DataBatch dataBatch){
        currentData = null;
    }

    public int GetCores(){return cores;}
    public Cluster GetCluster(){return cluster;}
    public boolean IsDataBatchInData(DataBatch dataBatch) {
        if (dataBatch==currentData)
            return true;
    return false;}
    public DataBatch getCurrentData(){return currentData;}
    public int getTicksCounter(){return ticksCounter;}

}
