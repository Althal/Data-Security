
package podL4;

import java.math.BigInteger;
import java.util.ArrayList;

public class PrimatyElement {
    
    private static int[] getNG(int minValue){
        int n = getPrime(minValue);
        int g = getPrimaryElement(n);
        
        while(g == -1){
            n = getPrime(n++);
            g = getPrimaryElement(n);
        }
    
        return new int[]{n,g};
    }
    
    private static int getPrime(int val){
        while(true){
            boolean isPrime = isPrime(val);
            if(isPrime){
                return val;
            }
            val++;
        }
    }
    
    private static boolean isPrime(int val){
        if(val%2 == 0) return false;
        
        for(int r=3; r<val/2; r+=2){
            if(val%r == 0){
                return false;
            }
        }
        
        return true;
    }
    
    private static int getPrimaryElement(int n){        
        ArrayList<Integer> rests;
        for(int i = 2; i<n; i++){
            rests = new ArrayList<>();
            int power = 1;
            while(true){
                int rest = new BigInteger(String.valueOf(i)).pow(power++).mod(new BigInteger(String.valueOf(n))).intValue();
                if(!rests.contains(rest)){
                    rests.add(rest);
                }
                else{
                    if(n == rests.size()+1) return i;
                    else break;
                }
            }
        }
        return -1;
    }
    
    public static void main(String... args){
        int[] a = getNG(10050);
        System.out.println(a[0] + " " + a[1]);
        System.out.println(getPrimaryElement(151));
    }
}
