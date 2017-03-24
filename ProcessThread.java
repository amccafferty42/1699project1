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
    private ThreadLocalRandom rng = new ThreadLocalRandom();
    private int Ts = 100;
    private int Td = 240*Ts;
    private int Tdifs = 30*Ts;
    private int k;
    private int W = rng.nextInt(Ts, (2*8*Ts));
    private int Tcw;
    private int Tp;
    private int Ttot = 0;
    private int Tifs = 10*Ts;
    private boolean firstRun = true;

    private int M;
    
    

    //this method will run on ___.start();
    public void run(){
        //sleep for Td then check if there is a message until there is one
        if(firstRun){
            sleep();  
            checkMessage();
        }
        //check if medium is in use. If it is sleep then check again.
        boolean inUse = medium.getUse();
        if(inUse){
            while(inUse){
                Ttot += Ts;
                this.sleep(Ts);
                inUse = medium.getUse();
            }
        }
        

        //sleep for Tdifs and increment total time
        this.sleep(Tdifs);
        Ttot +=Tdifs;

        //calculate Tcw and sleep for Ts. increment Ttot
        k =1;
        Tcw = k * W;
        this.sleep(Ts);
        Tcw -= Ts;
        Ttot += Ts;

        //check if medium is busy. If not checks to make sure Tcw < 0, if true sets medium to busy and begins
        //transmission. If false sleeps and decrements Tcw while incrementing Ttot.
        //if medium is busy runs through checkAfterFail.
        while(!checkUse()){
            this.sleep(Ts);
            Tcw -= Ts;
            Ttot += Ts;
        }
        medium.setUse(true);
       
        

        //*********************
        //Wait for message transmission. Not sure if sleep is allowed here or he wants a diff method
        //this.sleep((long).45*Td); Also have to ask if ACK time is multiplied by M.
        this.sleep(Tp*M);
        Ttot += (Tp*M);
        //*********************
        //Wait for ACK. Again not sure if sleep is allowed.
        this.sleep(Tifs);
        Ttot += Tifs;
        //setuse to false
        medium.setUse(false);

        //wait for another Tdifs.
        this.sleep(Tdifs);
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
            this.sleep(Ts);
            Ttot += Ts;
             inUse = checkAfterFail();
            return inUse;
        }

        
    }

    //after checkUse() fails the path changes, this method allows for the path change.
    private boolean checkAfterFail(){
        boolean inUse = medium.getUse();
        if(inUse){
            this.sleep(Ts);
            Ttot += Ts;
            inUse = checkAfterFail();
            return inUse;
        }
        else{
            this.sleep(Tdifs);
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
        this.sleep(Td);

    }

    //if there is a message return to program. If no message sleep then check again.
    private void checkMessage(){
        int hasMessage = rng.nextInt(100);
        if(hasMessage >= 40){
            sleep();
            checkMessage();
        }
        else{
            return;
        }
            
    }


    //checks for more data to send. If none print Ttot then exit. If data restart run();
    private void checkMoreData(){
        int hasMessage = rng.nextInt(100);
        if(hasMessage >= 40){
            System.out.println("Total Time: " + Ttot);
        }
        else{
            firstRun = false;
            this.run();
        }
    }
}