package DAO;
import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;


public class AccountDAO {
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
    public Account getAccountByUsername(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where username=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account acc = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return acc;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean verifyLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select count(*) from account where username = ? and password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Account getAccountByID(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "select * from account where account_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        if (account.getUsername()!=null && account.getUsername().isEmpty()==false && account.getPassword().length()>=4) {
        
        String sql = "insert into account (username, password) values (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            try (ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys()) {
                if (pkeyResultSet.next()) {
                    int generated_account_id = (int) pkeyResultSet.getLong(1);
                    return new Account(generated_account_id, account.getUsername(), account.getPassword());
                }
            }
        }
     catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
    return null;
}
}
