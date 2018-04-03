package com.cmcc.vrp.boss.chongqing.pojo.model;

import com.cmcc.vrp.util.ByteUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * 包头
 * @author 潘鑫
 *
 */
public class PacketHead {

    public byte[] reqformnum = new byte[14];
    public byte[] opcode = new byte[6];
    public byte[] sessionid = new byte[12];
    public byte[] retcode = new byte[6];
    public byte[] curpkgno = new byte[4];
    public byte[] totalpkgno = new byte[4];

    int[] elementLength = {14, 6, 12, 6, 4, 4};//报头总计46个byte

    public PacketHead() {

    }

    public int getTotalLength() {
        int totalLength = 0;
        for (int i = 0; i < elementLength.length; i++) {
            totalLength += elementLength[i];
        }
        return totalLength;
    }

    //得到前置字段的长度
    public int getFormerLength(int index) {
        if (index < 1 || index > 6) {
            return -1;
        }

        int result = 0;
        for (int i = 0; i < 6 && i < index; i++) {
            result += elementLength[i];
        }

        return result;
    }


    /**
     * Buffer是接收到的数据,解析封装到各个字段中
     * @param recvBuffer   buff字符
     * @return   结果
     */
    public boolean readFromBuffer(byte[] recvBuffer) {
        if (recvBuffer.length < 46) {
            return false;
        }

        System.arraycopy(recvBuffer, getFormerLength(1), reqformnum, 0, reqformnum.length);
        System.arraycopy(recvBuffer, getFormerLength(2), opcode, 0, opcode.length);
        System.arraycopy(recvBuffer, getFormerLength(3), sessionid, 0, sessionid.length);
        System.arraycopy(recvBuffer, getFormerLength(4), retcode, 0, retcode.length);
        System.arraycopy(recvBuffer, getFormerLength(5), curpkgno, 0, curpkgno.length);
        System.arraycopy(recvBuffer, getFormerLength(6), totalpkgno, 0, totalpkgno.length);

        return true;
    }

    public byte[] getSendBytes() {
        List<byte[]> listBytes = new LinkedList<byte[]>();
        listBytes.add(reqformnum);
        listBytes.add(opcode);
        listBytes.add(sessionid);
        listBytes.add(retcode);
        listBytes.add(curpkgno);
        listBytes.add(totalpkgno);

        return ByteUtil.concatByteArrays(listBytes);

		/*byte[] sendBytes=new byte[getTotalLength()];
				
	    for(int i=0;i<reqformnum.length;i++){
	    	sendBytes[i+getFormerLength(1)]=reqformnum[i];
	    }
	   	   
	    for(int i=0;i<opcode.length;i++){
	    	sendBytes[i+getFormerLength(2)]=opcode[i];
	    }
	   	   
	    for(int i=0;i<sessionid.length;i++){
	    	sendBytes[i+getFormerLength(3)]=sessionid[i];
	    }
	   	   
	    for(int i=0;i<retcode.length;i++){
	    	sendBytes[i+getFormerLength(4)]=retcode[i];
	    }
	      
	    for(int i=0;i<curpkgno.length;i++){
	    	sendBytes[i+getFormerLength(5)]=curpkgno[i];
	    }
	      
	    for(int i=0;i<totalpkgno.length;i++){
	    	sendBytes[i+getFormerLength(6)]=totalpkgno[i];
	    }
	    
	    return sendBytes;*/

    }

    private char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }


    public void setReqformnum(String reqformnumStr) {
        reqformnum = ByteUtil.convertStrToBytes(reqformnum, reqformnumStr);
    }

    public void setOpcode(String opcodeStr) {
        opcode = ByteUtil.convertStrToBytes(opcode, opcodeStr);
    }

    public void setSessionid(String sessionidStr) {
        sessionid = ByteUtil.convertStrToBytes(sessionid, sessionidStr);
    }

    public void setRetcode(String retcodeStr) {
        retcode = ByteUtil.convertStrToBytes(retcode, retcodeStr);
    }


    public void setCurpkgno(String curpkgnoStr) {
        curpkgno = ByteUtil.convertStrToBytes(curpkgno, curpkgnoStr);
    }

    public void setTotalpkgno(String totalpkgnoStr) {
        totalpkgno = ByteUtil.convertStrToBytes(totalpkgno, totalpkgnoStr);
    }


}


