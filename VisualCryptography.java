
package visualcryptography;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class VisualCryptography {

    private static String READ_PATH = "C:\\Test\\5.png";
    private static String SAVE_PATH_FIRST  = "C:\\Test\\6.png";
    private static String SAVE_PATH_SECOND = "C:\\Test\\7.png";
    private static String OUTPUT_PATH = "C:\\Test\\8.png";
    
    private static int PIXELS_NEEDED = 100*100;
    
    private static String WHITE = "11111111111111111111111111111111";
    private static String BLACK = "11111111000000000000000000000000";
    
    private static int BLACK_PIXEL = BLACK_PIXEL1();
    private static int WHITE_PIXEL = WHITE_PIXEL1();
    
    
    public static void main(String[] args) throws Exception {
        encrypt();
        decrypt();
    }
    
    
    
    private static void decrypt() throws Exception{
        BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);        
        BufferedImage img1 = readImage(SAVE_PATH_FIRST);
        BufferedImage img2 = readImage(SAVE_PATH_SECOND);
        
        String[] pix1 = convertIntTableToBinaryString(getPixels(img1,200*200));
        String[] pix2 = convertIntTableToBinaryString(getPixels(img2,200*200));
        
        for(int i=0; i<100; i++){
            for(int j=0; j<100; j++){
                if(getFourPixels(pix1,i,j) == getFourPixels(pix2,i,j)) img.setRGB(i,j,WHITE_PIXEL);
                else img.setRGB(i,j,BLACK_PIXEL);
            }
        }
        
        saveImage(img,OUTPUT_PATH);
    }
    
    
    private static void encrypt() throws IOException{
        BufferedImage img = readImage(READ_PATH);
        String[] pixels = convertIntTableToBinaryString(getPixels(img,PIXELS_NEEDED));
        
        BufferedImage img1 = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
        BufferedImage img2 = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
        
        for(int i=0; i<100; i++){
            for(int j=0; j<100; j++){
                
                int random = getRandom(6);                
                String[] outputPixels = new String[4];
                switch (random) {
                    case 0:
                        outputPixels = new String[]{BLACK, WHITE, BLACK, WHITE};
                        break;
                    case 1:
                        outputPixels = new String[]{BLACK, BLACK, WHITE, WHITE};
                        break;
                    case 2:
                        outputPixels = new String[]{WHITE, BLACK, BLACK, WHITE};
                        break;
                    case 3:
                        outputPixels = new String[]{WHITE, WHITE, BLACK, BLACK};
                        break;
                    case 4:
                        outputPixels = new String[]{BLACK, WHITE, WHITE, BLACK};
                        break;
                    case 5:
                        outputPixels = new String[]{WHITE, BLACK, WHITE, BLACK};
                        break;
                    default:
                        break;
                }
                
                int index = 0;
                
                if(pixels[i*100+j].equals(WHITE)){
                    for(int k=0; k<4; k++){
                        if(outputPixels[k].equals(WHITE)){
                            img1.setRGB(i*2+index/2, j*2+index%2, WHITE_PIXEL);
                            img2.setRGB(i*2+index/2, j*2+index%2, WHITE_PIXEL);
                        }
                        else{
                            img1.setRGB(i*2+index/2, j*2+index%2, BLACK_PIXEL);
                            img2.setRGB(i*2+index/2, j*2+index%2, BLACK_PIXEL);
                        }
                        
                        index++;
                    }
                } 
                else {
                    for(int k=0; k<4; k++){
                        if(outputPixels[k].equals(WHITE)){
                            img1.setRGB(i*2+index/2, j*2+index%2, WHITE_PIXEL);
                            img2.setRGB(i*2+index/2, j*2+index%2, BLACK_PIXEL);
                        }
                        else{
                            img1.setRGB(i*2+index/2, j*2+index%2, BLACK_PIXEL);
                            img2.setRGB(i*2+index/2, j*2+index%2, WHITE_PIXEL);
                        }
                        
                        index++;
                    }
                }
            }
        }
        
        saveImage(img1, SAVE_PATH_FIRST);
        saveImage(img2, SAVE_PATH_SECOND);
    }
    
    private static int WHITE_PIXEL1(){
        int R = 255;
        int G = 255;
        int B = 255;
        return new Color(R,G,B).getRGB();
    }
    
    private static int BLACK_PIXEL1(){
        int R = 0;
        int G = 0;
        int B = 0;
        return new Color(R,G,B).getRGB();
    }
    
    private static int getFourPixels(String[] img, int i, int j){
        int ret = 0;
        if(img[i*400 + j*2].equals(BLACK)) ret +=1;
        if(img[i*400 + j*2+1].equals(BLACK)) ret +=2;
        if(img[i*400+200 + j*2].equals(BLACK)) ret +=4;
        if(img[i*400+200 + j*2+1].equals(BLACK)) ret +=8;
        return ret;
    }
    
    private static int getRandom(int max){
        Random r = new Random();
        return Math.abs(r.nextInt()) % max;
    }
    
    private static void saveImage(BufferedImage image, String path) throws IOException{
        File output = new File(path); 
        ImageIO.write(image, "png", output); 
    }
    
    private static String[] convertIntTableToBinaryString(int[] i){
        String[] ret = new String[i.length];
        int index = 0;
        for(int j : i) ret[index++] = convertIntToBinaryString(j);
        return ret;
    }
    
    private static String convertIntToBinaryString(int i){
        return Integer.toBinaryString(i);
    }
    
    private static int[] getPixels(BufferedImage img, int count) throws IOException{
        int[] ret = new int[count];
        
        // Wymiary obrazka
        int width = img.getWidth(); 
        int height = img.getHeight();
        
        // Sprawdzenie możliwości osadzenia wiadomości
        if(count > width * height){
            throw new IOException("Obrazek nie posiada tylu pikselów");
        }
        
        for(int i=0; i<count; i++){
            ret[i] = img.getRGB(i%width, i/width);
        }
        
        return ret;
    }
    
    private static BufferedImage readImage(String path) throws IOException{
        File file = new File(path); 
        return ImageIO.read(file); 
    }

    
    public static void printPixel(String... pix) {
        for(String p : pix){
            System.out.println(p);
            int pixel = binaryToInt(p);
            int R = binaryToInt(p.substring(8,16));
            int G = binaryToInt(p.substring(17,24));
            int B = binaryToInt(p.substring(25,32));
            System.out.println("ARGB: " + R + ", " + G + ", " + B);
        }
    }
    
    private static int binaryToInt(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i=numbers.length-1; i>=0; i--)
            if(numbers[i] == '1')
                result += Math.pow(2, (numbers.length-i-1));
        return result;
    }
}
