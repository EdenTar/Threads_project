package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private transient int processed;
    private int size;

    public Data(String type,int size)
    {
        if (type.equals("images") || type.equals("Images"))
            this.type=Type.Images;
        else if (type.equals("Text"))
            this.type=Type.Text;
        else
            this.type=Type.Tabular;
        this.size=size;
        this.processed=0;

    }


    public String getType(){
        return type.toString();
    }

   public int getSize(){
        return size;
   }

    public int setProcessed(){
        return  processed;
    }
}
