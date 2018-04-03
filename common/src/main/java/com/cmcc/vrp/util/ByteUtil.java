/**
 * @Title: ByteUtil.java
 * @Package com.cmcc.vrp.util
 * @author: qihang
 * @date: 2015年4月28日 下午4:03:13
 * @version V1.0
 */
package com.cmcc.vrp.util;

import java.util.List;

/**
 * @ClassName: ByteUtil
 * @Description: TODO
 * @author: qihang
 * @date: 2015年4月28日 下午4:03:13
 *
 */
public class ByteUtil {
    //将字符传左填充不足长度的空格，然后转化为存入bytes中
    public static byte[] convertStrToBytes(byte[] bytes, String convertStr) {
        int bytesLength = bytes.length;

        if (convertStr.length() < bytesLength) {
            convertStr = String.format("%-" + bytesLength + "s", convertStr);
        } else if (convertStr.length() > bytesLength) {
            convertStr = convertStr.substring(0, bytesLength);
        }

        return convertStr.getBytes();
    }


    public static byte[] concatByteArrays(byte[] first, byte[] second) {
        byte[] concatBytes = new byte[first.length + second.length];
        System.arraycopy(first, 0, concatBytes, 0, first.length);
        System.arraycopy(second, 0, concatBytes, first.length, second.length);
        return concatBytes;
    }


    public static byte[] concatByteArrays(List<byte[]> list) {
        int totalLength = 0;
        for (byte[] byteArray : list) {//遍历一遍，计算出合并后的总长度
            totalLength = totalLength + byteArray.length;
        }

        byte[] concatBytes = new byte[totalLength];

        int startIndex = 0;
        for (byte[] byteArray : list) {
            System.arraycopy(byteArray, 0, concatBytes, startIndex, byteArray.length);
            startIndex += byteArray.length;
        }

        return concatBytes;
    }

    //将给定的一个数字转化为4位长度的byte数组
    public static byte[] convertIntToBytes(int num) {
        byte[] resultByte = new byte[4];
        int count = 0;

        //计算需要256*n个字节可全部填充
        for (; count < 3; count++) {
            if (num < Math.pow(256, count + 1)) {
                break;
            }
        }

        Math.pow(256, count + 1);

        //
        while (count > 0) {
            resultByte[3 - count] = (byte) (num / Math.pow(256, count));
            num = (int) (num - Math.pow(256, count));
            count--;
        }
        resultByte[3] = (byte) (num);

        return resultByte;
    }

    //将给定的一个数字转化为4位长度的byte数组
    public static int convertBytesToInt(byte[] byteLength) {
        int totalLength = 0;
        if (byteLength == null || byteLength.length != 4) {
            return -1;
        }

        for (int i = 0; i < byteLength.length; i++) {
            int num = (int) byteLength[i];
            if (num < 0) {
                num += 256;
            }

            totalLength = (int) (totalLength + num * Math.pow(256, 3 - i));
        }
        return totalLength;

    }
}
