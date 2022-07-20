package bgu.spl.mics.application.objects;

import java.util.LinkedList;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private LinkedList<Model> publishedModels;

    public ConfrenceInformation(String name,int date){
        this.name=name;
        this.date=date;
        this.publishedModels=new LinkedList<>();
    }

    public int getDate(){return date;}

    public  LinkedList<Model> getPublishedModels(){
        return publishedModels;
    }
}
