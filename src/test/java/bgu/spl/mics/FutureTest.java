package bgu.spl.mics;

import bgu.spl.mics.application.objects.Model;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static java.util.concurrent.TimeUnit.MICROSECONDS;

public class FutureTest {
    private Future future;

    @Before
    public void setUp() throws Exception {
        future=new Future();
    }

    @Test
    public void get() {
        Thread t = new Thread(()->{
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve("updateField");
        });
        t.start();
        try{
            future.get();
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
        assertEquals("updatedField", future.get());
    }

    @Test
    public void resolve() {
        //Model model=new Model();
        //future.resolve(model);
        //assertNotNull(future.get());
    }

    @Test
    public void isDone() {
        assertFalse(future.isDone());
        future.resolve("Done");
        assertTrue(future.isDone());
    }

    @Test
    public void testGet() {
        Thread t = new Thread(()->{
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve("result");
        });
        assertNull(future.get());
        t.start();
        try{
            future.get(200,MICROSECONDS );
            TimeUnit.MICROSECONDS.sleep(200);
            assertNotNull(future.get());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}