package com.cmcc.vrp.ec.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * @author lgk8023
 *
 */
@XStreamAlias("FileInfo")
public class UploadFileRespData {

    @XStreamAlias("FileType")
	String fileType;
	
    @XStreamAlias("FileName")
	String fileName;
	
    @XStreamAlias("Key")
	String key;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
