package com.cmcc.vrp.util;

import java.util.zip.GZIPInputStream; 
import java.io.FileOutputStream; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.IOException; 

import org.apache.log4j.Logger;

/**
 *  gz文件解压类
 */
public class UncompressFileGZIP { 
    private static Logger logger = Logger.getLogger(UncompressFileGZIP.class);
    
    /** 
     * Uncompress the incoming file. 
     * @param inFileName Name of the file to be uncompressed 
     */ 
    public static boolean doUncompressFile(String inFileName) { 
        FileOutputStream out = null; 
        try { 

            if (!getExtension(inFileName).equalsIgnoreCase("gz")) { 
                logger.error("File name must have extension of \".gz\""); 
                return false;
            } 

            logger.info("Opening the compressed file."); 
            GZIPInputStream in = null; 
            try { 
                in = new GZIPInputStream(new FileInputStream(inFileName)); 
            } catch(FileNotFoundException e) { 
                logger.error("File not found. " + inFileName); 
                return false;
            } 

            logger.info("Open the output file."); 
            String outFileName = getFileName(inFileName); 
            
            try { 
                out = new FileOutputStream(outFileName); 
            } catch (FileNotFoundException e) { 
                logger.error("Could not write to file. " + outFileName); 
                return false;
            } 

            logger.info("Transfering bytes from compressed file to the output file."); 
            byte[] buf = new byte[1024]; 
            int len; 
            while((len = in.read(buf)) > 0) { 
                out.write(buf, 0, len); 
            } 

            logger.info("Closing the file and stream"); 
            in.close(); 
            out.close(); 
            return true;
        
        } catch (IOException e) { 
            logger.error(e.getMessage());
            return false;
        } catch (Exception e) { 
            logger.error(e.getMessage());
            return false;
        } finally{
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    } 

    /** 
     * Used to extract and return the extension of a given file. 
     * @param f Incoming file to get the extension of 
     * @return <code>String</code> representing the extension of the incoming 
     *         file. 
     */ 
    public static String getExtension(String f) { 
        String ext = ""; 
        int i = f.lastIndexOf('.'); 

        if (i > 0 &&  i < f.length() - 1) { 
            ext = f.substring(i+1); 
        }      
        return ext; 
    } 

    /** 
     * Used to extract the filename without its extension. 
     * @param f Incoming file to get the filename 
     * @return <code>String</code> representing the filename without its 
     *         extension. 
     */ 
    public static String getFileName(String f) { 
        String fname = ""; 
        int i = f.lastIndexOf('.'); 

        if (i > 0 &&  i < f.length() - 1) { 
            fname = f.substring(0,i); 
        }      
        return fname; 
    } 
} 