package io.edurt.datacap;

import io.edurt.datacap.core.Logger;
import io.edurt.datacap.core.RedisStatement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.*;

public class RedisTest {
    private final static Logger LOGGER = new Logger(RedisStatement.class);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.itmuch.redis.jdbc.redis.RedisDriver");

        Connection connection = DriverManager.getConnection("jdbc:redis://localhost:6379/0");
        Statement statement = connection.createStatement();

        connection.setSchema("11");
        ResultSet rs = statement.executeQuery("get a");
        while (rs.next()) {
            String string = rs.getString(0);
            System.out.println(string);
        }

//        statement.execute("set a b");
//        ResultSet rs = statement.executeQuery("get a");
//        while (rs.next()) {
//            LOGGER.log("rs1:" + rs.getString(0));
//        }
//
        ResultSet resultSet = statement.executeQuery("keys *");
        while (resultSet.next()) {
            LOGGER.log(resultSet.getString(0));
        }

        connection.setSchema("11");
        ResultSet resultSet2 = statement.executeQuery("set ab99 ab88");
        while (resultSet2.next()) {
            LOGGER.log(resultSet.getString(0));
        }

        resultSet.close();
        statement.close();
        connection.close();

//        statement.execute("ZADD runoobkey 2 mongodb");
//        statement.execute("ZADD runoobkey 3 elasticsearch");
//        statement.execute("ZADD runoobkey 4 mysql");
//
//        ResultSet rs2 = statement.executeQuery("ZRANGE runoobkey 0 10 WITHSCORES");
//        while (rs2.next()) {
//            LOGGER.log("rs2:" + rs2.getString(0));
//        }
//
//        statement.execute("HMSET myhash field1 field2");
//        ResultSet rs3 = statement.executeQuery("HGETALL myhash");
//        while (rs3.next()) {
//            LOGGER.log("rs3:" + rs3.getString(0));
//        }

//        ResultSet rs4 = statement.executeQuery("get user");
//        while (rs4.next()) {
//            LOGGER.log("rs4:" + rs4.getString(0));
//        }
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class User {
    private String name;
    private Short age;
    private String email;
    private BigDecimal money;

}
