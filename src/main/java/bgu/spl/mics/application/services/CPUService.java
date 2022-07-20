package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;

/**
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    private CPU cpu;
    public CPUService(CPU _cpu) {
        super("CPU_Service");
        cpu=_cpu;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,(m)-> cpu.onTick());
        subscribeBroadcast(TerminateBroadcast.class,(m)->this.terminate());
    }
}
