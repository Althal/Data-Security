
package secretsharing;

import java.util.Random;

public class SecretSharing {

    // k - przestrzeń liczbowa
    // s - sekret
    // n - liczba udziałów
    private static int[] simpleEnc(int k, int s, int n) throws Exception{
        if(s >= k) throw new Exception("Złe argumenty wejściowe");
        
        System.out.println("Randomy:");
        int[] random = new int[n];
        for(int i=0; i<n-1; i++){
            int val = getRandom(k-1);
            System.out.print(val + ", ");
            random[i] = val;
        }
        System.out.println();
        System.out.println("Wartość udziału:");        
        random[n-1] = getN(random, k, s);
        System.out.println(random[n-1]);
        
        return random;
    }
    
    private static int simpleDec(int[] enc, int k){
        int sum = 0;
        for(int i : enc) sum += i;
        int ret = sum % k;
        System.out.println("Sekret: " + ret);
        return ret;
    }
    
    private static int getRandom(int max){
        Random r = new Random();
        return Math.abs(r.nextInt()) % max;
    }
    
    private static int getN(int[] rands, int k, int s){
        int ret = s;
        for(int i : rands) ret -= i;
        return ret % k;
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
    
    private static int getPrime(int val){
        while(true){
            boolean isPrime = isPrime(val);
            if(isPrime){
                return val;
            }
            val++;
        }
    }
    
    private static int[] shamirEnc(int p, int s, int n, int t) throws Exception{
        if(s >= p) throw new Exception("Złe argumenty wejściowe");
        if(p <= s) throw new Exception("Złe argumenty wejściowe");
        if(p <= n) throw new Exception("Złe argumenty wejściowe");
        if(t > n) throw new Exception("Złe argumenty wejściowe");
        
        System.out.println("Randomy");
        int[] rand = new int[t-1];
        for(int i=0; i<t-1; i++){
            int val = getRandom(50);
            System.out.print(val + ", ");
            rand[i] = val;
        }
        //int[] rand = {352,62}; 
        System.out.println();
        return getShamirSecrets(rand, s, n, p);
    }
    
    private static int[] getShamirSecrets(int[] rands, int s, int n, int p){
        int[] ret = new int[n];
        System.out.println("S[]:");
        for(int i=0; i<n; i++){
            int argument = i+1;
            int sum = s;
            
            for(int j=0; j<rands.length; j++){
                sum += rands[j]*Math.pow(argument,j+1);             
            }
            sum = sum % p;
            System.out.print(sum + ", ");   
            ret[i] = sum;
        }
        
        return ret;
    }
    
    private static int shamirDec(int[] enc, int t ,int p){
        int[] val = new int[t];
        System.out.println();
        for(int i=1; i<=t; i++){
            int up = 1;
            int down = 1;
            
            for(int j=1; j<=t; j++){
                if(i==j)continue;
                
                up *= -j;
                down *= (i-j);
            }
            int freeValue = (enc[i-1] * up / down);
            //val[i-1] = freeValue > 0? freeValue % p : -((freeValue*-1) % p); 
            val[i-1] = freeValue % p;
            System.out.println(up + " " + down + " " + val[i-1]);
        }
        
        int ret = 0;
        for(int z : val) ret += z;
        while(ret < 0) ret += p;
        ret = ret % p;
        
        System.out.println();
        System.out.println("Secret: " + ret);
        return ret;
    }
    
    public static void main(String[] args) throws Exception {
        boolean simple = false;
        int s = 888;
        if(simple){
            int k = 550; 
            int[] a = simpleEnc(k,s,8);
            int res = simpleDec(a,k);
        } else {
            int prime = getPrime(2000);
            System.out.println("Prime: " + prime);
            int n = 6;
            int t = 4;
            int[] a = shamirEnc(prime,s,n,t);
            int res = shamirDec(a,t,prime);
        }

    }
    
}
