package pl_coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl_coderslab.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users (email, username, password) VALUES (?,?,?)";


    public User create(User user) {
        try (Connection connection = DbUtil.connect();
             PreparedStatement preStmt = connection.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, user.getEmail());
            preStmt.setString(2, user.getUserName());
            preStmt.setString(3, hashPassword((user.getPassword())));
            preStmt.executeUpdate();
            ResultSet rs = preStmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
