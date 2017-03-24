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
    private static Medium medium = new Medium();;
    private ThreadLocalRandom rng = new ThreadLocalRandom();
    private int Ts = 100;
    private int Td = 240*Ts;

    //this method will run on ___.start();
    public void run(){
        //sleep for Td then check if there is a message until there is one
        sleep();  
        checkMessage();

        //check if medium is in use. If it is sleep then check again. After it is clear set it to busy.
        boolean inUse = medium.getUse();
        if(inUse){
            while(inUse){
                this.sleep(Ts);
                inUse = medium.getUse();
            }
        }
        medium.setUse(true);

        //sleep for Tdifs
        this.sleep(30*Ts);

        //sleep for Tcw
        long Tcw = rng.nextLong(Ts, (2*8*Ts));
        this.sleep(Tcw);

        //*********************
        //Wait for message transmission. Not sure if sleep is allowed here or he wants a diff method
        this.sleep((long).45*Td);
        //*********************
        //Wait for ACK. Again not sure if sleep is allowed.
        this.sleep(10*Ts);
        //setuse to false
        medium.setUse(false);

        //wait for another Tdifs.
        this.sleep(Ts*30);





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
}