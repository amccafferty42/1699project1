private class Medium{
    //variable to see if medium is in use
    private static boolean inUse;
    private void init(){
        inUse = false;
    }
    //set true false
    private void setUse(boolean toBe){
        inUse = toBe;
    }
    //return if in use
    private boolean getUse(){
        return inUse;
    }
}