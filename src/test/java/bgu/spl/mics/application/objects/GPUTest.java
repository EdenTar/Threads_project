package bgu.spl.mics.application.objects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;
import static bgu.spl.mics.application.objects.GPU.Type.RTX3090;

public class GPUTest {
    private GPU gpu;
    @Before
    public void setUp() throws Exception {
        Model model= new Model("YOLO10",new Student("eden","dep","status"),"images",2000);
        gpu=new GPU("RTX3090",1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void dataSeparator() {
        gpu.dataSeparator();
        assertFalse(gpu.getUnProcessedData().isEmpty());
    }

    public void testTrainData(DataBatch dataBatch) {
        gpu.getProcessedData().add(dataBatch);
        int unprocessedData=gpu.getNumUnprocessedBatches();
        gpu.trainModel();
        assertTrue(unprocessedData>gpu.getNumUnprocessedBatches());
        /*for (int i=0 ; i<gpu.getTicksNum()-1 ; i++)
        {
            gpu.onTick();
            assertTrue(gpu.getProcessedData().contains(dataBatch));
        }
        gpu.onTick();
        assertFalse(gpu.getProcessedData().contains(dataBatch));*/
    }

    @Test
    public void onTick() {
        int ticksNum=Cluster.getInstance().getStatistics().getGpuTime();
        gpu.onTick();
        assertTrue(Cluster.getInstance().getStatistics().getGpuTime()>ticksNum);
    }
}