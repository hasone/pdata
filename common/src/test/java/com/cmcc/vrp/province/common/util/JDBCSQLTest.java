package com.cmcc.vrp.province.common.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCSQLTest {

    final static String URL = "jdbc:mysql://192.168.32.45:3306/chongqing?useUnicode=true&characterEncoding=UTF-8";
    final static String USERNAME = "chongqing";
    final static String PASSWORD = "Q7KVmqKqwboHUUazt3x2vZYonYyHeRZ2cM";

    static {
        try {
            // 加载MySql的驱动类
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace();
        }
    }

    public static List<String> createPhones() {
        List<String> list = new ArrayList<String>();
        String phone = "1862013";
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    for (int x = 0; x < 10; x++) {
                        list.add(phone + i + j + k + x);

                    }
                }
            }
        }
        return list;
    }

    public static Connection getConnection() {
        Connection con = null;
        try {

            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
        }
        return con;
    }

    public static void main(String[] args) throws SQLException {
        // System.out.println(JDBCSQLTest.getConnection());
        System.out.println(JDBCSQLTest.createPhones());
//		new JDBCSQLTest().batchInsertTest();
//		new JDBCSQLTest().insertTest();

    }

    public void insertTest() throws SQLException {
        List<String> phones = createPhones();
        Connection conn = getConnection();
        System.out.println("start  -----> " + System.currentTimeMillis());
        for (int i = 0; i < phones.size(); i++) {
            insert(phones.get(i), conn);
        }
        System.out.println("end    -----> " + System.currentTimeMillis());

    }

    public void insert(String phone, Connection conn) throws SQLException {
        String sql = "insert into chongqing_present_record (rule_id,mobile,status,create_time) values(?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, 1);
        stmt.setString(2, phone);
        stmt.setInt(3, 1);
        stmt.setDate(4, new Date(0l));
        stmt.execute();
        stmt.close();
    }

    public void batchInsertTest() throws SQLException {
        String sql = "insert into chongqing_present_record (rule_id,mobile,status,create_time) values(?,?,?,?)";
        Connection conn = getConnection();

        PreparedStatement stmt = conn.prepareStatement(sql);
        int batchNum = 500;// 每500条为一批
        List<String> list = createPhones();
        System.out.println("start  -----> " + System.currentTimeMillis());
        for (int i = 0; i < list.size(); i++) {
            stmt.setInt(1, 1);
            stmt.setString(2, list.get(i));
            stmt.setInt(3, 1);
            stmt.setDate(4, new Date(0l));
            if (i != 0 && i % batchNum == 0) {
                System.out.println("-----");

                stmt.executeBatch();

                stmt.clearBatch();
            }
            stmt.addBatch();
        }
        System.out.println("end    -----> " + System.currentTimeMillis());

        stmt.close();

        conn.close();
    }
}
