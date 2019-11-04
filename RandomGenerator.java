
package test;

import java.math.BigInteger;
import java.util.Random;

public class RandomGenerator {
    
    private static long[] pq;
    private static BigInteger x;
    private static BigInteger n;
    
    private static boolean[] row;
    
    public RandomGenerator(int minPrimeValue){
        pq = generatePQ(minPrimeValue);
        x = generateX();
        n = generateN();        
    }
    
    public void generate(){
        boolean[] ret = new boolean[20000];
        for(int i=0; i<20000; i++){
            x = getNextState();
            ret[i] = getByte(x);
        }
        row = ret;
    }
    
    public void show(){
        if(row == null) return;
        for(boolean b : row){
            if(b) System.out.print(1);
            else  System.out.print(0);
        }
        System.out.println("");
    }
  
    
    private static long[] generatePQ(long minValue){
        long[] ret = new long[2];
        int index = 0;
        long val = minValue;
        while(true){
            boolean isPrime = true;
            for(int i=3; i<val/2; i+=2){
                if(val%i == 0){
                    isPrime=false;
                    break;
                }
            }
            if(isPrime && val%4==3){
                ret[index++]=val;
                System.out.println(val);
                if(index==2) break;
            }
            val++;
        }
        return ret;
    }

    private static BigInteger generateX(){
          Random generator = new Random();
          while(true){
              long ret = Math.abs(generator.nextLong()) % pq[1];
              if(pq[1] % ret != 0) return new BigInteger(String.valueOf(ret));
          }
    }

    private static BigInteger generateN(){
        BigInteger ret = new BigInteger(String.valueOf(pq[0]));
        return ret.multiply(new BigInteger(String.valueOf(pq[1])));
    }

    private static boolean getByte(BigInteger x){
        return x.mod(new BigInteger("2")).intValue() == 1;
    }

    private static BigInteger getNextState(){
        BigInteger x2 = new BigInteger(String.valueOf(x));
        x2 = x2.multiply(new BigInteger(String.valueOf(x)));
        return x2.mod(n);
    }
    
    
    
    
    
    public boolean test(){
        if(!testSeries())       { System.out.println("Test serii negatywny"); return false; }  
        if(!testSequence())     { System.out.println("Test pokerowy negatywny"); return false; }  
        if(!testBytes())        { System.out.println("Test bitów negatywny"); return false; }  
        if(!testSmallSeries())  { System.out.println("Test małych serii negatywny"); return false; }  
        return true;
    }
    
    private boolean testSeries(){
        boolean state = row[0];
        int count = 0;
        
        for(boolean b : row){
            if(b == state){
                count++;
                if(count > 25) return false;
            }
            else {
                state = b;
                count = 1;
            }
        }
        return true;
    }
    
    private boolean testSmallSeries(){
        int[] seq = new int[6];
        
        boolean state = row[0];
        int count = 0;
        
        for(boolean b : row){
            if(b == state){
                count++;
            }
            else {
                if(count > 6) count = 6;
                seq[count-1]++;
                
                state = b;
                count = 1;
            }
        }
        
        //System.out.println("Test małych sekwencji:");
        //for(int i=0; i<6 ; i++) System.out.println(i+1 + " - " + seq[i]);
        
        if(seq[0] < 2315 || seq[0] > 2685) return false;
        if(seq[1] < 1114 || seq[1] > 1386) return false;
        if(seq[2] < 527  || seq[2] > 723) return false;
        if(seq[3] < 240  || seq[3] > 384) return false;
        if(seq[4] < 103  || seq[4] > 209) return false;
        if(seq[5] < 103  || seq[5] > 209) return false;
        
        return true;
    }
    
    private boolean testBytes(){
        int one = 0;
        for(boolean b : row){
            if(b) one++;
        }
        //System.out.println("Test bitów: " + one);
        return one > 9725 && one < 10275;
    }
    
    private boolean testSequence(){
        int[] seq = new int[16];
        for(int i=0; i<5000; i++){
            int value = 0;
            if(row[i*4]) value += 8;
            if(row[i*4+1]) value += 4;
            if(row[i*4+2]) value += 2;
            if(row[i*4+3]) value += 1;
            
            seq[value]++;
        }
        
        double x = 0;
        for(int s : seq) x += s *s; 
        x = x*16.0/5000.0 - 5000.0;
        
        //System.out.println("Test sekwencji: " + x);
        return x > 2.16 && x < 46.17;
    }
    
    
    
    
    public static void main(String[] args)
    {
        int minValue = 100000;
        
        RandomGenerator random = new RandomGenerator(minValue);
        random.generate(); 
        
        int tries = 0;
        
        while(!random.test()){
            tries++;
            minValue += 1000;
            random = new RandomGenerator(minValue);
            System.out.println("Try: " + tries);
        }
    }
}
