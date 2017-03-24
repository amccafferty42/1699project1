import java.util.*;
import java.lang.*;
public class Main{
    public static void main (String [] args) {
        ProcessThread [] threads = new ProcessThread[8];
        for(int i=0; i<8;i++){
            threads[i] = new ProcessThread();
            threads[i].start();
        }
    }
}