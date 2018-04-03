package com.cmcc.vrp.util;

import com.cmcc.vrp.province.model.InterfaceRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by sunyiwei on 2016/7/15.
 */
public class GsonTest {
    @Test
    public void testGsonList() throws Exception {
        List<InterfaceRecord> interfaceRecordList = build();

        Gson gson = new Gson();
        String valueStr = gson.toJson(interfaceRecordList);
        System.out.println(valueStr);

        Type type = new TypeToken<LinkedList<InterfaceRecord>>() {
        }.getType();
        List<InterfaceRecord> parsedList = gson.fromJson(valueStr, type);
        for (InterfaceRecord interfaceRecord : parsedList) {
            System.out.println(gson.toJson(interfaceRecord));
        }
    }

    private List<InterfaceRecord> build() {
        List<InterfaceRecord> records = new LinkedList<InterfaceRecord>();
        for (int i = 0; i < 10; i++) {
            InterfaceRecord record = new InterfaceRecord();
            record.setEnterpriseCode(randomStr());
            record.setProductCode(randomStr());
            record.setPhoneNum(randomStr());
            record.setSerialNum(randomStr());
            record.setSysSerialNum(randomStr());
            record.setIpAddress(randomStr());
            record.setStatus(com.cmcc.vrp.enums.ChargeRecordStatus.PROCESSING.getCode());
            record.setCreateTime(new Date());
            record.setDeleteFlag(com.cmcc.vrp.util.Constants.DELETE_FLAG.UNDELETED.getValue());
            record.setFingerprint(randomStr());

            records.add(record);
        }

        return records;
    }

    private String randomStr() {
        StringBuilder stringBuilder = new StringBuilder();

        final int length = 5;
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) ('a' + r.nextInt(26)));
        }

        return stringBuilder.toString();
    }
}
