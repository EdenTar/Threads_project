package bgu.spl.mics.application.objects;



import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {


	public static class ClusterSingletonHolder{
		private static Cluster instance=new Cluster();
	}
	private HashMap<Integer, LinkedBlockingQueue<DataBatch>> doneProcess;
	private LinkedList<CPU> CPUS;
	private LinkedList<GPU> GPUS;
	private Statistics statistics;

	public Cluster(){
		doneProcess = new HashMap<Integer,LinkedBlockingQueue<DataBatch>>();
		CPUS = new LinkedList<CPU>();
		GPUS = new LinkedList<GPU>();
		statistics = new Statistics();
	}
	public void AddGPU(GPU gpu){
		doneProcess.put(gpu.getId(), new LinkedBlockingQueue<DataBatch>());
		GPUS.add(gpu);
	}
	public void AddCPU(CPU cpu) {
		CPUS.add(cpu);
	}
	public static Cluster getInstance() {
		return ClusterSingletonHolder.instance;
	}

	public synchronized DataBatch getDatabatch(){
		while(GPUS.getFirst().getUnProcessedData().isEmpty())
		{
			GPUS.addLast(GPUS.removeFirst());
		}
		if (GPUS.getFirst().getUnProcessedData().isEmpty())
			return null;
		DataBatch dataBatch = GPUS.getFirst().getUnProcessedData().remove();
		GPUS.addLast(GPUS.removeFirst());
		return dataBatch;

	}
	public  void addProcessedData(DataBatch dataBatch){
			doneProcess.get(dataBatch.getGPUid()).add(dataBatch);
	}
	public HashMap<Integer,LinkedBlockingQueue<DataBatch> > getDoneProcess(){
		return doneProcess;
	}
	public  Statistics getStatistics(){return statistics;}
}
