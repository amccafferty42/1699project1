public class Medium{
    //variable to see if medium is in use
    private static boolean inUse;
    public Medium(){
        inUse = false;
    }
    //set true false
    public void setUse(boolean toBe){
        inUse = toBe;
    }
    //return if in use
    public boolean getUse(){
        return inUse;
    }
}