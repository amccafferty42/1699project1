import java.util.*;
import java.lang.*;
public class Main{
    public static void main (String [] args) {
        Scanner in = new Scanner(System.in);
        int probability = -1;
        //get probability, success = random number under what they enter
        while(probability < 0 || probability > 100){
            System.out.println("Enter the probability as a number from 0 to 100: " );
            probability = in.nextInt();
        }

        //get transtime
        double transTime = -1;
        while(transTime < (.3 * 240 *100) || transTime > (.6 * 240 * 100)){
            System.out.println("Enter the transmission time of a single packet(Number from " + (.3 * 240 *100) + " to " + (.6 * 240 *100));
            transTime = in.nextDouble();
        }
        //get numPackets
        int numPackets = -1;
        while(numPackets < 1 || numPackets > 6){
            System.out.println("Enter the number of packets(Number from 1 to 6): " );
            numPackets = in.nextInt();
        }

        //get numthreads
        int numThreads = -1;
        while(numThreads < 1 || numPackets > 8){
            System.out.println("Enter the number of threads(Number from 1 to 8): " );
            numThreads = in.nextInt();
        }

        //initialize proper number of threads.
        ProcessThread [] threads = new ProcessThread[8];
        for(int i=0; i<numThreads;i++){
            System.out.println("Thread " + i + " starting");
            threads[i] = new ProcessThread(numPackets, probability, (int)transTime);
            threads[i].start();
        }
        /*for(int i=numThreads; i<8; i++){
            threads[i] = new ProcessThread(numPackets, 0, (int)transTime);
            threads[i].start();
        }*/
    }
}