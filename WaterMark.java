
package watermark;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class WaterMark {
    
    private static String READ_PATH = "C:\\Test\\1.jpg";
    private static String SAVE_PATH = "C:\\Test\\2.jpg";
    
    private static final int MESSAGE_BITS = Integer.BYTES * 8;
    private static final int PIXELS_NEEDED = MESSAGE_BITS / 3 + 1;
    
    private static final int R_INDEX = 15;
    private static final int G_INDEX = 23;
    private static final int B_INDEX = 31;
    
    public static void main(String... args){
        try {
            encrypt(123456789, READ_PATH, SAVE_PATH);
            System.out.println(decrypt(SAVE_PATH));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void encrypt(int value, String inputPath, String outputPath) throws IOException, Exception{
        BufferedImage img = readImage(inputPath); 
        String[] pixels = convertIntTableToBinaryString(getPixels(img,PIXELS_NEEDED));
        
        printPixel(pixels);
        
        pixels = insertMessageToPixels(value, pixels);
        
        System.out.println();
        printPixel(pixels);
        BufferedImage encrypted = insertPixelsToImage(pixels,img);
        saveImage(encrypted,outputPath);
    }
    
    public static int decrypt(String inputPath) throws IOException, Exception{
        BufferedImage img = readImage(inputPath); 
        String[] pixels = convertIntTableToBinaryString(getPixels(img,PIXELS_NEEDED));
        
        StringBuilder message = new StringBuilder();
        for(String pixel : pixels) message.append(takeOutThreeBitsFromPixel(pixel));
        
        return binaryToInt(message.toString());
    }
    
    private static BufferedImage insertPixelsToImage(String[] pixels, BufferedImage img){
        BufferedImage ret = img;
        
        // Wymiary obrazka
        int width = ret.getWidth();
        
        for(int i=0; i<pixels.length; i++){
            int R = binaryToInt(pixels[i].substring(8,16));
            int G = binaryToInt(pixels[i].substring(17,24));
            int B = binaryToInt(pixels[i].substring(25,32));
            int val = new Color(R,G,B).getRGB();
            ret.setRGB(i%width, i/width, val);            
        }
        
        return ret;
    }
    
    private static String[] insertMessageToPixels(int insertValue, String[] pixels) throws IOException, Exception{
        if(insertValue > Integer.MAX_VALUE || insertValue < Integer.MIN_VALUE) throw new IOException("Nie można wstawić liczby");
        if(pixels.length != PIXELS_NEEDED) throw new IOException("Nie można wstawić liczby, zły rozmiar tablicy pixeli");
        
        String insertStringValue = convertIntToBinaryString(insertValue);
        while(insertStringValue.length() != PIXELS_NEEDED * 3) insertStringValue = "0" + insertStringValue;
        String[] ret = new String[PIXELS_NEEDED];
        
        for(int i=0; i<PIXELS_NEEDED; i++){
            String threeBits = insertStringValue.substring(i*3,(i+1)*3);
            ret[i] = insertThreeBitsToPixel(threeBits, pixels[i]);
        }
        
        return ret;
    }
    
    private static String insertThreeBitsToPixel(String threeBits, String pixel) throws Exception{
        if(threeBits.length() != 3) throw new Exception("Błąd wykonywania programu");
        
        char[] chars = pixel.toCharArray();
        chars[R_INDEX] = threeBits.charAt(0);
        chars[G_INDEX] = threeBits.charAt(1);
        chars[B_INDEX] = threeBits.charAt(2);
        
        return String.valueOf(chars);
    }
    
    private static String takeOutThreeBitsFromPixel(String pixel){
        return new StringBuilder().append(pixel.charAt(R_INDEX)).append(pixel.charAt(G_INDEX)).append(pixel.charAt(B_INDEX)).toString();
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
    
    private static void saveImage(BufferedImage image, String path) throws IOException{
        File output = new File(path); 
        ImageIO.write(image, "jpg", output); 
    }
    
    private static int binaryToInt(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i=numbers.length-1; i>=0; i--)
            if(numbers[i] == '1')
                result += Math.pow(2, (numbers.length-i-1));
        return result;
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
}
