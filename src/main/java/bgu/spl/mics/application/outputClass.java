package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Student;

import java.util.LinkedList;

public class outputClass {
    private Student[] students;
    private ConfrenceInformation[] conferences;
    private int cpuTimeUsed;
    private int gpuTimeUsed;
    private int batchesProcessed;

    public outputClass(Student[] students, ConfrenceInformation[] conferences, int cpuTimeUsed,int gpuTimeUsed,  int batchesProcessed){
        this.students=students;
        this.conferences=conferences;
        this.cpuTimeUsed=cpuTimeUsed;
        this.gpuTimeUsed=gpuTimeUsed;
        this.batchesProcessed=batchesProcessed;
    }
}
