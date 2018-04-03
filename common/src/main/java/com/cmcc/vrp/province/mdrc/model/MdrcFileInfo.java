package com.cmcc.vrp.province.mdrc.model;

import org.apache.commons.lang.math.NumberUtils;


/**
 * Mdrc 储存的File在S3信息的解析，
 * 从S3的名称解析得到file_name
 */
public class MdrcFileInfo {
    String name;  //文件名称

    String wholeName;  //在S3储存的Key的名称

    Long length;  //文件大小

    String fileType;  //文件类型

    public MdrcFileInfo(String s3KeyName) {
        //mdrc_3_10246_account.png  S3的key值
        String[] split = s3KeyName.split("_");

        if (split.length < 4) {
            return;
        } else {
            wholeName = s3KeyName;
            length = NumberUtils.toLong(split[2]);
            name = "";
            for (int i = 3; i < split.length; i++) {
                name += split[i];
            }

            fileType = getFileTypeFromName(name);
        }
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getWholeName() {
        return wholeName;
    }


    public void setWholeName(String wholeName) {
        this.wholeName = wholeName;
    }


    public Long getLength() {
        return length;
    }


    public void setLength(Long length) {
        this.length = length;
    }


    public String getFileType() {
        return fileType;
    }


    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    private String getFileTypeFromName(String fileName) {
        String[] split = fileName.split("\\.");
        return split[split.length - 1];
    }

}
