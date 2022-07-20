package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    /**
     * Enum representing the Data type.
     */
    enum Type {
        Images, Text, Tabular
    }
    private Data data;
    private int start_Index;
    private int gpuID;
    public DataBatch(Data data, int start_Index,int gpuID)
    {
        this.data=data;
        this.start_Index=start_Index;
        this.gpuID=gpuID;
    }
    public int getGPUid(){return gpuID;}
    public String GetTypeString(){return data.getType();}
    public int getStart_Index(){return start_Index;}
    
}
