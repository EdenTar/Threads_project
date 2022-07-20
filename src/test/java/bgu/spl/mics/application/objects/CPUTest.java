package bgu.spl.mics.application.objects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CPUTest {
    private CPU cpu;
    private Data data;
    private DataBatch dataBatch;
    @Before
    public void setUp() throws Exception {

        cpu=new CPU(32);
        data = new Data("Images",100000);
        dataBatch = new DataBatch(data,0,0);
    }

    @Test
    public void onTick() {

        //Images
        Data dataImages = new Data("Images",0);
        DataBatch dataBatchImages = new DataBatch(dataImages,0,0);
        testProcessing(dataBatchImages, (32/cpu.GetCores()*4));
        cpu=new CPU(32);
        data = new Data("Images",100000);
        dataBatch = new DataBatch(data,0,0);
        //Text
        Data dataText = new Data("Text",0);
        DataBatch dataBatchText = new DataBatch(dataText,0,0);
        testProcessing(dataBatchText, (32/cpu.GetCores()*2));
        cpu=new CPU(32);
        data = new Data("Images",100000);
        dataBatch = new DataBatch(data,0,0);
        //Tabular
        Data dataTabular = new Data("Tabular",0);
        DataBatch dataBatchTabular = new DataBatch(dataTabular,0,0);
        testProcessing(dataBatchTabular, (32/cpu.GetCores()));
    }


    private void testProcessing(DataBatch dataBatch, int endProcess)
    {
        cpu.addDataBatch(dataBatch);
        for (int i=0 ; i<endProcess-1 ; i++)
        {
            cpu.onTick();
            assertTrue(cpu.IsDataBatchInData(dataBatch));
        }
        assertEquals(endProcess,cpu.getTicksCounter()+1);
    }

    @Test
    public void addDataBatch() {
        assertFalse(cpu.IsDataBatchInData(dataBatch));
        cpu.addDataBatch(dataBatch);
        assertTrue(cpu.IsDataBatchInData(dataBatch));
    }

    @Test
    public void removeDataBatch() {
        cpu.addDataBatch(dataBatch);
        cpu.removeDataBatch(dataBatch);
        assertFalse(cpu.IsDataBatchInData(dataBatch));
    }


}