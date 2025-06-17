/**
 * UserDAO 클래스
 * 사용자 관련 데이터베이스 작업(역할 조회, 존재 확인, 등록 등)을 수행하는 클래스입니다.
 */
package com.example.universitycoursesystemui.dao;

import com.example.universitycoursesystemui.User; // User 모델 클래스 import 필요
import com.example.universitycoursesystemui.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager; // 필요 없으면 삭제해도 됨
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {

    /**
     * 사용자 ID와 비밀번호를 기반으로 사용자의 역할(userType)을 조회합니다.
     *
     * @param userID   사용자의 고유 ID
     * @param password 사용자의 비밀번호
     * @return Optional<String> - 조회된 사용자 역할 (예: student, admin 등).
     *         해당하는 사용자가 없으면 Optional.empty()를 반환합니다.
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생할 수 있는 예외
     */
    public Optional<String> getUserRole(String userID, String password) throws SQLException {
        // 사용자 ID와 password 조건에 따라 userType을 조회하기 위한 SQL 쿼리
        String sql = "SELECT userType FROM users WHERE userID = ? AND password = ?";
        // try-with-resources를 사용하여 Connection, PreparedStatement, ResultSet을 자동으로 닫아줍니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 첫번째 ?에 userID, 두번째 ?에 password 값을 설정합니다.
            pstmt.setString(1, userID);
            pstmt.setString(2, password);

            // 쿼리 실행 후 결과를 ResultSet으로 받아옵니다.
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // 결과가 있다면,
                    // userType 컬럼의 값을 Optional에 담아 반환합니다.
                    return Optional.of(rs.getString("userType"));
                }
            }
        }
        // 데이터가 조회되지 않으면 Optional.empty()를 반환합니다.
        return Optional.empty();
    }


    /**
     * 주어진 사용자 ID가 데이터베이스(users 테이블)에 존재하는지 확인합니다.
     *
     * @param userID 확인할 사용자의 고유 ID
     * @return true: 사용자가 존재함, false: 사용자가 존재하지 않음
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생할 수 있는 예외
     */
    public boolean isUserExists(String userID) throws SQLException {
        // 존재 여부를 확인하기 위해 상수 값 1을 선택하는 SQL 쿼리입니다.
        String sql = "SELECT 1 FROM users WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 사용자 ID를 쿼리 파라미터에 설정합니다.
            pstmt.setString(1, userID);
            // 쿼리 실행 후 결과가 존재하면 해당 사용자가 있다는 의미이므로 rs.next()가 true를 반환합니다.
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }


    /**
     * 새로운 사용자를 데이터베이스(users 테이블)에 추가합니다.
     *
     * @param user 추가할 사용자 정보를 담은 User 객체
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생할 수 있는 예외
     */
    public void addUser(User user) throws SQLException {
        // userID, password, name, userType, major 컬럼에 값을 삽입하는 SQL INSERT 쿼리입니다.
        String sql = "INSERT INTO users (userID, password, name, userType, major) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 각 파라미터에 User 객체의 값을 설정합니다.
            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getMajor()); // 사용자의 전공 정보를 추가합니다.
            // INSERT 쿼리를 실행하여 새로운 사용자를 DB에 저장합니다.
            pstmt.executeUpdate();
        }
    }

    /**
     * 특정 사용자 ID에 해당하는 전공 정보를 조회합니다.
     *
     * @param userId 조회할 사용자의 고유 ID
     * @return 사용자 전공 정보(String). 해당 정보가 없으면 null 반환.
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생할 수 있는 예외
     */
    public String getUserMajor(String userId) throws SQLException {
        // 조회 결과를 저장할 변수 (데이터가 없을 경우 null 반환)
        String major = null;
        // 사용자 ID에 해당하는 전공 정보를 조회하는 SQL 쿼리입니다.
        String sql = "SELECT major FROM users WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 쿼리의 첫번째 파라미터에 사용자 ID를 설정합니다.
            pstmt.setString(1, userId);
            // 쿼리를 실행하여 ResultSet을 받아옵니다.
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // 결과가 존재하면,
                    // major 컬럼의 값을 추출하여 변수에 저장합니다.
                    major = rs.getString("major");
                }
            }
        }
        return major;
    }


    /**
     * 특정 사용자 ID에 해당하는 이름 정보를 조회합니다.
     *
     * 주로 교수 등 이름 정보를 조회할 때 사용됩니다.
     *
     * @param userId 조회할 사용자의 고유 ID
     * @return 사용자 이름(String). 해당 정보가 없으면 null 반환.
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생할 수 있는 예외
     */
    public String getUserName(String userId) throws SQLException {
        // 조회된 이름을 저장할 변수 (정보가 없을 경우 null)
        String userName = null;
        // 사용자 ID에 해당하는 이름 정보를 조회하는 SQL 쿼리입니다.
        String sql = "SELECT name FROM users WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 사용자 ID를 쿼리 파라미터에 설정합니다.
            pstmt.setString(1, userId);
            // 쿼리 실행 후 결과를 ResultSet으로 받습니다.
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // 결과가 존재하면,
                    // name 컬럼의 값을 읽어와 변수에 저장합니다.
                    userName = rs.getString("name");
                }
            }
        }
        return userName;
    }
}