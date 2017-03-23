private class Medium{
    //variable to see if medium is in use
    private static boolean inUse;
    private void init(){
        inUse = false;
    }
    private void setUse(boolean toBe){
        inUse = toBe;
    }
}