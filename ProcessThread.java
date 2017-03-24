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
    private static Medium medium = new Medium();;
    private ThreadLocalRandom rng = new ThreadLocalRandom();
    private int Ts = 100;
    private int Td = 240*Ts;
    public void run(){
        sleep();  
        checkMessage();
        boolean inUse = medium.getUse();
        if(inUse){
            while(inUse){
                this.sleep(Ts);
                inUse = medium.getUse();
            }
        }
        medium.setUse(true);
        this.sleep(30*Ts);
        long Tdifs = rng.nextLong(Ts, (2*8*Ts));
        this.sleep(Tdifs);
        this.sleep((long).45*Td);
        this.sleep(10*Ts);
        medium.setUse(false);
        this.sleep(Tdifs);





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