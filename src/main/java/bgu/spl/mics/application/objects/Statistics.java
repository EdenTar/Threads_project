package bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private LinkedBlockingQueue<String> trainedModelNames;
    private AtomicInteger totalDataBatchesProcessedByCPUs;
    private AtomicInteger cpuTime;
    private AtomicInteger gpuTime;

    public Statistics(){
        trainedModelNames = new LinkedBlockingQueue<>();
        totalDataBatchesProcessedByCPUs=new AtomicInteger();
        int val1;
        do{
            val1=totalDataBatchesProcessedByCPUs.get();
        }while (!totalDataBatchesProcessedByCPUs.compareAndSet(val1,0));
        cpuTime=new AtomicInteger();
        int val2;
        do{
            val2=cpuTime.get();
        }while (!cpuTime.compareAndSet(val2,0));
        gpuTime=new AtomicInteger();
        int val3;
        do{
            val3=gpuTime.get();
        }while (!gpuTime.compareAndSet(val3,0));
    }
    public  void addTrainedModelNames(String trainedModelName) {
        try {
            trainedModelNames.put(trainedModelName);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

    public  void addTotalDataBatchesProcessedByCPUs(int add){
        int val;
        do{
            val=totalDataBatchesProcessedByCPUs.get();
        }while (!totalDataBatchesProcessedByCPUs.compareAndSet(val,val+add));
    }

    public  void addCPUTime(int add){
        int val;
        do{
            val=cpuTime.get();
        }while (!cpuTime.compareAndSet(val,val+add));
      }

    public  void addGPUTime(int add){
        int val;
        do{
            val=gpuTime.get();
        }while (!gpuTime.compareAndSet(val,val+add));
      }

    public LinkedBlockingQueue<String> getTrainedModelNames() {
        return trainedModelNames;
    }
    public int getTotalDataBatchesProcessedByCPUs() {
        return totalDataBatchesProcessedByCPUs.get();
    }
    public int getGpuTime()
    {
        return gpuTime.get();
    }
    public int getCpuTime() {
        return cpuTime.get();
    }

}


