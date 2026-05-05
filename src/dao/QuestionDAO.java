package dao;

import db.DBConnection;
import java.sql.*;

public class QuestionDAO {

    public static void addQuestion(int customerId, String questionText) {
        String query = "INSERT INTO Question (customer_id, question_text) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ps.setString(2, questionText);
            ps.executeUpdate();

            System.out.println("Question submitted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void replyToQuestion(int questionId, int employeeId, String replyText) {
        String query = "UPDATE Question SET employee_id = ?, reply_text = ?, reply_time = NOW() WHERE question_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ps.setString(2, replyText);
            ps.setInt(3, questionId);
            ps.executeUpdate();

            System.out.println("Reply added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}