package bgu.spl.mics.application;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import java.io.*;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    //"C:/Users/MAY/Semester C/spl/SPLassignment2/example_input.json"
    //"C:/Users/ynal6/Desktop/School/Semester C/SPL/SPLassignment2/example_input.json"
    public static final String JSON_PATH = "example_input.json";

    public static Student[] students;
    public static ConfrenceInformation[] conferences;
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        File input = new File(JSON_PATH);
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        Gson gsonInput = new Gson();

        //students
        JsonArray StudentsArray = fileObject.getAsJsonArray("Students");
        students = DeserializeAllStudents(fileObject,gsonInput);
        //models
        List<Model[]> allModels = DeserializeAllModels(StudentsArray,students);
        //GPUS
        GPU[] gpus =DeserializeAllGPUS(fileObject, gsonInput);
        //CPUS
        CPU[] cpus = DeserializeAllCPUS(fileObject, gsonInput);
        //Conferences
        conferences = DeserializeAllConference(fileObject,gsonInput);
        //TickTime
        int tickTime = ((JsonElement) fileObject.get("TickTime")).getAsInt();
        //Duration
        int duration = ((JsonElement) fileObject.get("Duration")).getAsInt();


        createGpu(gpus);
        createCpu(cpus);
        createConferenceService(conferences);
        createStudents(students,allModels);
        createTimeService(tickTime,duration);
    }
    public static void printOutput() throws IOException {
        //output
        Gson gsonOutput = new Gson();
        outputClass output = new outputClass(students,conferences,Cluster.getInstance().getStatistics().getCpuTime(),Cluster.getInstance().getStatistics().getGpuTime(), Cluster.getInstance().getStatistics().getTotalDataBatchesProcessedByCPUs());
        String outputString = gsonOutput.toJson(output);
        Gson gsonOutputPrettyPrint = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(outputString);
        String prettyJsonString = gsonOutputPrettyPrint.toJson(je);

        //write into output.txt file
        FileWriter writer = new FileWriter("output.txt", true);
        writer.write(prettyJsonString);
        writer.close();

    }
    public static void createStudents(Student[] students, List<Model[]> allModels){
        for(int i=0;i<students.length;i++){
            StudentService studentService=new StudentService(students[i],allModels.get(i));
            Thread t=new Thread(studentService);
            t.start();
        }
    }
    public static void createGpu(GPU[] gpus){
        for(int i=0;i<gpus.length;i++){
           GPUService gpuService=new GPUService(gpus[i]);
            Thread t=new Thread(gpuService);
            t.start();
        }
    }
    public static void createCpu(CPU[] cpus){
        for(int i=0;i<cpus.length;i++){
            CPUService cpuService=new CPUService(cpus[i]);
            Thread t=new Thread(cpuService);
            t.start();

        }
    }
    public static void createConferenceService(ConfrenceInformation[] conferences){
        for(int i=0;i<conferences.length;i++){
            ConferenceService conferenceService=new ConferenceService(conferences[i]);
            Thread t=new Thread(conferenceService);
            t.start();
        }
    }

    public  static void createTimeService(int tickTime, int duration) throws InterruptedException {
        TimeService timeService=new TimeService(tickTime,duration);
        Thread t=new Thread(timeService);
        Thread.sleep(50);
        t.start();

    }
    public static Student[] DeserializeAllStudents(JsonObject fileObject, Gson gson){
        JsonArray StudentsArray = fileObject.getAsJsonArray("Students");
        Student[] students = new Student[StudentsArray.size()];
        for(int i=0 ; i<StudentsArray.size() ; i++)
        {
            JsonElement current = StudentsArray.get(i);
            String name = ((JsonObject) current).get("name").getAsString();
            String department = ((JsonObject) current).get("department").getAsString();
            String status = ((JsonObject) current).get("status").getAsString();
            students[i] = new Student(name,department,status);
        }
        return students;
    }

    public static ConfrenceInformation[] DeserializeAllConference(JsonObject fileObject, Gson gson){
        JsonArray ConferencesArray = fileObject.getAsJsonArray("Conferences");
        ConfrenceInformation[] conferenceInformation = new ConfrenceInformation[ConferencesArray.size()];
        for(int i=0 ; i<ConferencesArray.size() ; i++)
        {
            JsonElement current = ConferencesArray.get(i);
            String name = ((JsonObject) current).get("name").getAsString();
            int date = ((JsonObject) current).get("date").getAsInt();
            conferenceInformation[i] = new ConfrenceInformation(name,date);
        }
        return conferenceInformation;
    }
    public static CPU[] DeserializeAllCPUS(JsonObject fileObject, Gson gson){
        JsonArray CPUSArray = fileObject.getAsJsonArray("CPUS");
        int[] integerCPUS = gson.fromJson(CPUSArray.toString(), int[].class);
        CPU[] cpus = new CPU[integerCPUS.length];
        for(int i=0 ;i<integerCPUS.length ; i++) {
            cpus[i] = new CPU(integerCPUS[i]);
            Cluster.getInstance().AddCPU(cpus[i]);
        }
        return  cpus;
    }

    public static GPU[] DeserializeAllGPUS(JsonObject fileObject, Gson gson){
        JsonArray GPUSArray = fileObject.getAsJsonArray("GPUS");
        String[] stringGPUS = gson.fromJson(GPUSArray.toString(), String[].class);
        GPU[] gpus = new GPU[stringGPUS.length];
        for(int i=0 ;i<stringGPUS.length ; i++) {
            gpus[i] = new GPU(stringGPUS[i],i);
            Cluster.getInstance().AddGPU(gpus[i]);
        }
        return gpus;
    }

    public static List<Model[]> DeserializeAllModels(JsonArray StudentsArray, Student[] students){
        List<Model[]> allModels = new LinkedList<Model[]>();
        for(int i=0 ; i<students.length ; i++)
        {
            JsonObject object = (JsonObject)StudentsArray.get(i);
            JsonArray ModelsArray = object.getAsJsonArray("models");
            Model[] modelsOfStudent = new Model[ModelsArray.size()];
            int counter=0;
            for(JsonElement m : ModelsArray)
            {
                String name = ((JsonObject) m).get("name").getAsString();
                String type = ((JsonObject) m).get("type").getAsString();
                int size = ((JsonObject) m).get("size").getAsInt();
                modelsOfStudent[counter] = new Model(name,students[i],type,size);
                counter++;
            }
            allModels.add(modelsOfStudent);
        }
        return allModels;
    }


}
