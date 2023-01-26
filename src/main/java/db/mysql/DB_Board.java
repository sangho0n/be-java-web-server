package db.mysql;

import db.UserIdSession;
import model.Article;
import model.User;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class DB_Board {
    private static final String JDBC_URI = "jdbc:mysql://localhost/Java_was";
    private static final String ADMIN_ID = "admin";
    private static final String ADMIN_PASSWORD = "admin1234";

    private static Connection conn = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;

    public static void addArticle(String article, String sid_uid) throws SQLException {
        String userId = UserIdSession.getUserId(sid_uid);
        Calendar currCal = Calendar.getInstance();
        currCal.getWeekYear();
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URI + "?" +
                    "user=" + ADMIN_ID + "&" +
                    "password=" + ADMIN_PASSWORD);

            String sql = "INSERT INTO board(writerId, content, date) VALUES (?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, article);
            pstmt.setString(3,
                         currCal.get(Calendar.YEAR) + "-" +
                            new Integer(currCal.get(Calendar.MONTH)+1).toString() + "-"+
                            currCal.get(Calendar.DAY_OF_MONTH)
            );

            pstmt.executeUpdate();
        }catch (SQLException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            pstmt.close();
            conn.close();
        }
    }
    public static Collection<Article> findAll() throws SQLException {
        Collection<Article> allArticles = Collections.EMPTY_LIST;

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URI + "?" +
                    "user=" + ADMIN_ID + "&" +
                    "password=" + ADMIN_PASSWORD);

            String sql = "SELECT * FROM board";
            // uid, password, name, email
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while(rs.next())
            {
                allArticles.add(
                        new Article(
                                rs.getString("writerId"),
                                rs.getString("content"),
                                rs.getString("date")
                        )
                );
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            pstmt.close();
            conn.close();
        }

        return allArticles;
    }
}