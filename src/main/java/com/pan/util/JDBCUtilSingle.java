package com.pan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class JDBCUtilSingle {
    // 该url为缺省方式（省略主机跟端口）
    // 完整为：jdbc:mysql//localhost:3306/test

    static Connection conn = null;

    // 获得连接
    public Connection getConnection(String drive,String url,String name,String password) {
        try {
            Class.forName(drive);// 推荐使用方式
            conn = DriverManager.getConnection(url,name,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }

    // 关闭连接
    public void closeConnection(ResultSet rs, Statement statement, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}