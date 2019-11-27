
package podL4;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private static BigInteger[] pq;
    private static BigInteger n;
    private static BigInteger phi;
    private static BigInteger e;
    private static BigInteger d;
    
    private static BigInteger[] publicKey;
    private static BigInteger[] privateKey;
    
    public RSA(){
        generateKeys(2000);
        String message = "To jest przykladowa wiadomosc testowa zawierajaca okolo piecdziesiedziu znakow";
        String after = decrypt(encrypt(message));
        System.out.println("Przed: " + message);
        System.out.println("Po   : " + after);
    }
    
    public static void main(String[] args){
        new RSA();
    }
    
    private static void generateKeys(long minValue){
        generatePQ(minValue);
        generateN();
        generatePhi();
        generateE();
        generateD();
        
        publicKey = new BigInteger[]{e, n};
        privateKey = new BigInteger[]{d, n};
    }
    
    private static BigInteger[] encrypt(String message){
        BigInteger[] ret = new BigInteger[message.length()];
        int[] messageBytes = new int[message.length()];
        
        int index = 0;
        for(char c : message.toCharArray()){
            messageBytes[index++] = (int) c;
        }
        
        System.out.print("Zakodowano: ");
        for(int i=0; i<message.length(); i++){
            ret[i] = new BigInteger(String.valueOf(messageBytes[i])).pow(e.intValue()).mod(n);
            System.out.print(i+",");
        }
        System.out.println();
        return ret;
    }
    
    private static String decrypt(BigInteger[] message){
        StringBuilder ret = new StringBuilder();
        int i = 0;
        System.out.print("Odkodowano: ");
        for(BigInteger c : message){
            BigInteger m = c.pow(d.intValue()).mod(n);
            int b = m.intValue();
            ret.append(Character.toChars(b)[0]);
            System.out.print(i++ +",");
        }
        System.out.println();
        return ret.toString();
    }
    
    private static boolean isPrime(BigInteger val){
        if(val.mod(new BigInteger("2")).equals(BigInteger.ZERO)) return false;
        
        for(BigInteger r = new BigInteger("3"); r.compareTo(val.divide(new BigInteger("2"))) == -1; r=r.add(new BigInteger("2"))){
            if(val.mod(r).equals(BigInteger.ZERO)){
                return false;
            }
        }
        
        return true;
    }
    
    /*private static boolean isRelativelyFirst(BigInteger v1, BigInteger v2){
        // FOR (3; <v1; +=2)
        for(BigInteger r = new BigInteger("3"); r.compareTo(v1) == -1; r=r.add(new BigInteger("2"))){
            if(v1.mod(r).equals(BigInteger.ZERO) &&  v2.mod(r).equals(BigInteger.ZERO)) return false;
        }
        return true;
    }*/
    
    private static void generatePQ(long minValue){
        BigInteger[] ret = new BigInteger[2];
        int index = 0;
        BigInteger val = new BigInteger(String.valueOf(minValue));
        while(true){
            boolean isPrime = isPrime(val);
            if(isPrime/* && val%4==3*/){
                ret[index++]=val;
                System.out.println(val);
                if(index==2) break;
            }
            val = val.add(BigInteger.ONE);
        }
        pq = ret;
        System.out.println("Generated PQ");
    }
    
    private static void generateE(){
        Random generator = new Random();
        while(true){
            BigInteger ret = new BigInteger(String.valueOf(Math.abs(generator.nextLong()))).mod(phi);
            if(ret.compareTo(new BigInteger("10")) == -1) continue;
            if(isPrime(ret)) {
                e = new BigInteger(String.valueOf(ret));
                System.out.println("Generated E");
                return;
            }
        }
    }
    
    private static void generateD(){
        BigInteger ret = BigInteger.ONE;
        while(true){
            if(e.multiply(ret).subtract(BigInteger.ONE).mod(phi).equals(BigInteger.ZERO)) {
                d = ret;
                System.out.println("Generated D");
                return;
            }
            ret = ret.add(BigInteger.ONE);
        }
    }
    
    private static void generateN(){
        BigInteger ret = pq[0];
        n = ret.multiply(pq[1]);
        System.out.println("Generated N");
    }
    
    private static void generatePhi(){
        BigInteger ret = pq[0].subtract(BigInteger.ONE);
        phi = ret.multiply(pq[1].subtract(BigInteger.ONE));
        System.out.println("Generated PHI");
    }
}
