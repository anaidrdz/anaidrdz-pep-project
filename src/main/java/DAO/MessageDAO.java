package DAO;

import Model.Message;
import Util.ConnectionUtil;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message addNewMessage(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());

        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        
        if(rs.next()) {
            int messageId = rs.getInt(1);
            return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }
        return null;
    }
    
    public List<Message> getAllMessages() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
    
        String sql = "SELECT * FROM message";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()) {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
        }
        return messages;
    }
    
    public Message getMessageById(int messageId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, messageId);
        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()) {
            return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
        }
        return null;
    }

    public Message deleteMessageById(int messageId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String selectMessage = "SELECT * FROM message WHERE message_id=?";

        PreparedStatement selectStatement = connection.prepareStatement(selectMessage);
        selectStatement.setInt(1, messageId);

        ResultSet rs = selectStatement.executeQuery();
        Message deletedMsg = null;
        if(rs.next()){
            deletedMsg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
        } 
        else {
            return null;
        }

        String sql = "DELETE FROM message WHERE message_id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, messageId);

        preparedStatement.executeUpdate();

        return deletedMsg;
    }

    public Message updateMessage(String newMessage, int messageId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, newMessage);
        preparedStatement.setInt(2, messageId);
        
        preparedStatement.executeUpdate();

        String selectStmt = "SELECT * FROM message WHERE message_id =?";
        PreparedStatement selectStatement = connection.prepareStatement(selectStmt);
        selectStatement.setInt(1, messageId);

        ResultSet rs = selectStatement.executeQuery();

        if(rs.next()) {
            return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
        }

        return null;
    }

    public List<Message> getAllMessagesByAccount(int accountId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, accountId);

        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()) {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
        }

        return messages;
    }
    
}
