package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {
//     create table account (
//     account_id int primary key auto_increment,
//     username varchar(255) unique,
//     password varchar(255)
// );
    // create table message (
    //     message_id int primary key auto_increment,
    //     posted_by int,
    //     message_text varchar(255),
    //     time_posted_epoch bigint,
    //     foreign key (posted_by) references  account(account_id)
    // );
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "select * from message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return messages;
    }
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
    try {
        
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

        String checkSql = "select count(*) from account where account_id = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setInt(1, message.getPosted_by());
            try (ResultSet rs = checkStatement.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    return null;
                }
            }
        }

        String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            try (ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys()) {
                if (pkeyResultSet.next()) {
                    int generated_message_id = (int) pkeyResultSet.getLong(1);
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
}

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "select * from message where message_id=?;";
            
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void deleteMessage(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "delete from message where message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean updateMessage(int id, Message message) { 
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            String checkSql = "select count(*) from message where message_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setInt(1, id);
                try (ResultSet rs = checkStatement.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        return false;
                    }
                }
            }
    
            if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
                return false;
            }
    
            String sql = "update message set message_text = ? where message_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, message.getMessage_text());
                preparedStatement.setInt(2, id);
    
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    return true;
                } else {
                    return false;
                }
            }
    
        } catch (SQLException e) {
            return false;
        }
    }



    public List<Message> getAllMessagesFromAccount(int id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try {
            String sql = "select * from message join account on message.posted_by = account.account_id where account.account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            ResultSet rs = preparedStatement.executeQuery();
          
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return messages;
    }
}