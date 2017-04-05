import java.lang.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*Ts =100ms
Td = 240Ts
Tdifs = 30Ts
Tcw = ܹ݇kW where ݇k=1 and ܹWis a
random number, where Ts<W<2NTs
 
*/



public class ProcessThread extends Thread{
    //initialize variables
    private static Medium medium = new Medium();
    private ThreadLocalRandom rng;
    private int Ts = 100;
    private int Td = 240*Ts;
    private int Tdifs = 30*Ts;
    private int k;
    private int W;
    private int Tcw;
    private int Ttot = 0;
    private int Tifs = 10*Ts;
    private boolean firstRun = true;

    private int M;
    private int P;
    private int Tp;
    
    

    //this method will run on ___.start();

    public ProcessThread(int m, int p, int tp){
        M = m;
        P = p;
        Tp = tp;
        rng = ThreadLocalRandom.current();
        W  = rng.nextInt(Ts, (2*8*Ts));
    }
    public void run(){
        //sleep for Td then check if there is a message until there is one
        if(firstRun){
            sleep();  
            checkMessage();
        }
        //check if medium is in use. If it is sleep then check again.
        boolean inUse = medium.getUse();
        System.out.println("Busy: " + inUse);
        if(inUse){
            while(inUse){
                Ttot += Ts;
                try{
                    this.sleep(Ts);
                }
                catch(InterruptedException i){
                    System.err.println(i);
                }
                inUse = medium.getUse();
            }
        }
        System.out.println("Running thread: " + this.getId());
        System.out.println("permission to use");
        //sleep for Tdifs and increment total time
        try{
            this.sleep(Tdifs);
        }
        catch(InterruptedException i){
            System.err.println(i);
        }
        Ttot +=Tdifs;

        //calculate Tcw and sleep for Ts. increment Ttot
        k =1;
        Tcw = k * W;
        try{
            this.sleep(Ts);
        }
        catch(InterruptedException i){
            System.err.println(i);
        }
        Tcw -= Ts;
        Ttot += Ts;

        //check if medium is busy. If not checks to make sure Tcw < 0, if true sets medium to busy and begins
        //transmission. If false sleeps and decrements Tcw while incrementing Ttot.
        //if medium is busy runs through checkAfterFail.
        System.out.println("weird part of the thing where checkuse can loop");
        while(!checkUse()){
            try{
                this.sleep(Ts);
            }
            catch(InterruptedException i){
                System.err.println(i);
            }
            Tcw -= Ts;
            Ttot += Ts;
        }
        // NEED A SEMAPHORE/MUTEX HERE
        medium.setUse(true);
       
    
        System.out.println("Sending packets");

        //*********************
        //Wait for message transmission. Not sure if sleep is allowed here or he wants a diff method
        //this.sleep((long).45*Td); Also have to ask if ACK time is multiplied by M.
        try{
            this.sleep(Tp*M);
        }
        catch(InterruptedException i){
            System.err.println(i);
        }
        Ttot += (Tp*M);
        //*********************
        //Wait for ACK. Again not sure if sleep is allowed.
        try{
            this.sleep(Tifs);
        }
        catch(InterruptedException i){
            System.err.println(i);
        }
        Ttot += Tifs;
        //setuse to false
        medium.setUse(false);

        //wait for another Tdifs.
        try{
            this.sleep(Tdifs);
        }
        catch(InterruptedException i){
            System.err.println(i);
        }
        Ttot += Tdifs;


        checkMoreData();

    }

    //check if medium is busy. If not checks to make sure Tcw < 0, if true returns false to move run() forward.
    //If false returns true so proper increment/decrement happen.
    private boolean checkUse(){
        boolean inUse = medium.getUse();
        if(!inUse){
            if(Tcw > 0){
                return true;
            }
            else return false;
        } 
        else{
            try{
                this.sleep(Ts);
            }
            catch(InterruptedException i){
                System.err.println(i);
            }
            Ttot += Ts;
             inUse = checkAfterFail();
            return inUse;
        }

        
    }

    //after checkUse() fails the path changes, this method allows for the path change.
    private boolean checkAfterFail(){
        boolean inUse = medium.getUse();
        if(inUse){
            try{
                this.sleep(Ts);
            }
            catch(InterruptedException i){
                System.err.println(i);
            }
            Ttot += Ts;
            inUse = checkAfterFail();
            return inUse;
        }
        else{
            try{
                this.sleep(Tdifs);
            }
            catch(InterruptedException i){
                System.err.println(i);
            }
            Ttot += Tdifs;
            if(Tcw < 0){
                k = 2*k;
                if(k > 16) k = 16;
                Tcw = k*W;
            }
            return inUse;
        }
    }
    //sleep for Td
    private void sleep(){
        try{
            this.sleep(Td);
        }
        catch(InterruptedException i){
            System.err.println(i);
        }

    }

    //if there is a message return to program. If no message sleep then check again.
    private void checkMessage(){
        int hasMessage = rng.current().nextInt(100);
        System.out.println(this.getId() + " hasMessage: " + hasMessage);
        System.out.println(P);
        if(hasMessage >= P){
            sleep();
            checkMessage();
        }
        else{
            return;
        }
            
    }


    //checks for more data to send. If none print Ttot then exit. If data restart run();
    private void checkMoreData(){
        int hasMessage = rng.current().nextInt(100);
        System.out.println(hasMessage);
        if(hasMessage >= P){
            System.out.println("Total Time: " + Ttot);
            System.out.println("Ending thread" + this.getId());
        }
        else{
            System.out.println("Going again in Thread " + this.getId());
            firstRun = false;
            this.run();
        }
    }
}