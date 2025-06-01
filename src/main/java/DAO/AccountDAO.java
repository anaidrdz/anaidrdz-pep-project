package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {

    public Account getAccountWithUserName(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?";
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        }
        return null;
    }

    public Account getAccountWithId(int accountId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, accountId);

        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()) {
            return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        }
        return null;
    }

    public Account addNewAccount(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        preparedStatement.executeUpdate();

        ResultSet returnedSet = preparedStatement.getGeneratedKeys();
        if(returnedSet.next()){
            int generated_account_id = (int) returnedSet.getLong(1);
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
        }
        return null;
    }

    public Account userLogin(String username, String password) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username =? AND password=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            int accountId = rs.getInt("account_id");
            String userName = rs.getString("username");
            String passWord = rs.getString("password");
            return new Account(accountId, userName, passWord);
        }
        return null;
    }
}