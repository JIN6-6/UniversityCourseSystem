package com.example.universitycoursesystemui.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection 클래스
 * 이 클래스는 MySQL 데이터베이스 연결을 생성하고 종료하는 유틸리티 클래스로서,
 * 데이터베이스 연결 URL, 사용자 이름 및 비밀번호를 상수로 정의하고 있습니다.
 *
 * 클래스가 로드될 때 JDBC 드라이버를 명시적으로 로드하며, 안정적인 연결을 위해
 * getConnection() 및 closeConnection() 메서드를 제공합니다.
 */
public class DatabaseConnection {

    // MySQL 데이터베이스 연결 URL (JDBC 형식)
    private static final String URL = "jdbc:mysql://localhost:3306/university_course_db?serverTimezone=UTC";
    // 데이터베이스 사용자 이름 (필요에 따라 본인의 MySQL 사용자 이름으로 수정)
    private static final String USER = "root"; // 본인의 MySQL 사용자 이름으로 변경
    // 데이터베이스 비밀번호 (필요에 따라 본인의 비밀번호로 수정)
    private static final String PASSWORD = "password"; // 본인의 MySQL 비밀번호로 변경

    // Static 초기화 블록:
    // 클래스가 처음 로드될 때 JDBC 드라이버를 명시적으로 로드합니다.
    // MySQL 8.0.x 버전에서는 자동 로딩이 가능하지만, 명시적 로드는 안정성을 높입니다.
    static {
        try {
            // MySQL JDBC 드라이버를 로드합니다.
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            // 드라이버 클래스를 찾지 못한 경우 에러 메시지를 출력하고 스택 트레이스를 표시합니다.
            System.err.println("MySQL JDBC Driver not found. Make sure the JAR is in the classpath.");
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 연결을 생성하여 반환하는 메서드입니다.
     *
     * @return Connection 객체 - MySQL 데이터베이스와의 연결 객체
     * @throws SQLException 연결 실패 시 발생하는 예외
     */
    public static Connection getConnection() throws SQLException {
        // 연결 시도 정보를 출력 (디버깅 목적)
        System.out.println("Attempting to connect to database at: " + URL);
        // DriverManager를 통해 데이터베이스 연결을 생성하고 반환합니다.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 주어진 Connection 객체를 안전하게 닫는 메서드입니다.
     * 연결 객체가 null이 아닌지 확인한 후 닫으며, 예외 발생 시 에러 메시지를 출력합니다.
     *
     * @param connection 닫을 데이터베이스 연결 객체
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) { // 연결 객체가 null이 아니라면 종료 시도
            try {
                connection.close(); // 연결 종료
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                // 연결 종료 중 예외가 발생하면 에러 메시지와 스택 트레이스를 출력합니다.
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}