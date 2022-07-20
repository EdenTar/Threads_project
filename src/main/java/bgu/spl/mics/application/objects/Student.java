package bgu.spl.mics.application.objects;

import java.util.LinkedList;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private LinkedList<Model> trainedModels;

    public Student(String _name, String _department, String _status)
    {
        name = _name;
        department = _department;
        if(_status.equals("MSc"))
            status = Degree.MSc;
        else
            status = Degree.PhD;
        publications=0;
        papersRead=0;
        trainedModels = new LinkedList<Model>();
    }

    public String getName(){return name;}
    public String getDepartment(){return department;}
    public String getStatus(){
        if(status== Degree.MSc)
            return "MSc";
        return "PhD";
    }
    public void AddTrainedModel(Model trainedModel){
        if (trainedModels==null)
            trainedModels = new LinkedList<Model>();
        trainedModels.add(trainedModel);
    }
    public int getPublications(){return publications;}
    public int getPapersRead(){return papersRead;}
    public synchronized void setPapersRead(int _papersRead){papersRead=_papersRead;}
    public synchronized void setPublications(int _publications){publications=_publications;}

}
