package labjava.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO {
    protected Connection con;

    protected DAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url  = "jdbc:mysql://localhost:3306/library_db"
                    + "?useSSL=false&allowPublicKeyRetrieval=true"
                    + "&useUnicode=true&characterEncoding=UTF-8"
                    + "&serverTimezone=Asia/Ho_Chi_Minh";
            String user = "root";
            String pass = "123456";

            con = DriverManager.getConnection(url, user, pass);
            System.out.println("[DAO] Connected OK -> " + url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot connect DB", e);
        }
    }
}

