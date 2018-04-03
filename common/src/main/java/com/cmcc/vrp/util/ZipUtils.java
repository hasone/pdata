package com.cmcc.vrp.util;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * zip压缩工具类
 */
public class ZipUtils {

    private static Logger LOGGER = Logger.getLogger(ZipUtils.class);

    /**
     * 压缩文件
     * <p>
     *
     * @param input
     * @param output
     */
    public static void compress(File input, File output) {
        if (input == null || !input.exists() || output == null) {
            return;
        }

    }

    /**
     * 压缩文件
     * <p>
     *
     * @param input
     * @param output
     */
    public static void compress(String input, String output) {
        File inputFile = new File(input);
        File outputFile = new File(output);

        if (inputFile.exists()) {

            compress(inputFile, outputFile);
        }
    }

//	public static void main(String[] args) {
//		String input = "C:\\temp\\mdrc\\template\\41";
//		String output = "C:\\Users\\cmcc\\Desktop\\test.zip";
//		compress(new File(input), new File(output));
//	}

}
