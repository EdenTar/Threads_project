package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    enum Status {PreTrained, Training, Trained, Tested}
    enum Results {None, Good, Bad}

private String name;
private Data data;
private transient Student student;
private Status status;
private Results results;

public Model(String _name, Student _student, String _type, int _size)
{
    name = _name;
    data = new Data(_type,_size);
    student = _student;
    status = Status.PreTrained;
    results = Results.None;
}
public void setStatus(String status){
        if(status.equals("Training"))
        this.status=Status.Training;
        else if(status.equals("Trained"))
        this.status=Status.Trained;
        else if(status.equals("Tested"))
        this.status=Status.Tested;
        else
        this.status=Status.PreTrained;}
    public String getName(){ return name;}
    public Data getData(){ return data;}
    public Student getStudent() { return student;}
    public Status getStatus(){return status;}
    public void setResults(String _results){
    if (_results.equals("Good"))
        this.results = Results.Good;
    else if (_results.equals("Bad"))
        this.results = Results.Good;
    else
        this.results = Results.None;
}
    public String getResults(){
        if (results==Results.Good)
            return "Good";
        else if (results==Results.Bad)
            return "Bad";
        return "None";
    }


}
