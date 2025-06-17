package com.example.universitycoursesystemui.dao;

import com.example.universitycoursesystemui.model.Enrollment;
import com.example.universitycoursesystemui.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * EnrollmentDAO 클래스
 * 학생의 수강 신청(enrollment) 관련 데이터베이스 작업(추가, 삭제, 조회 등)을 수행하는 DAO 클래스입니다.
 */
public class EnrollmentDAO {

    /**
     * 주어진 학생과 과목 정보를 enrollments 테이블에 추가합니다.
     *
     * @param studentID 학생의 고유 ID
     * @param courseID  과목의 고유 ID
     * @throws SQLException 데이터베이스 작업 도중 발생할 수 있는 예외
     */
    public void addEnrollment(String studentID, String courseID) throws SQLException {
        // enrollments 테이블에 studentID와 courseID를 삽입하는 SQL 쿼리
        String sql = "INSERT INTO enrollments (studentID, courseID) VALUES (?, ?)";

        // try-with-resources 구문으로 Connection과 PreparedStatement를 자동으로 닫습니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 첫 번째 placeholder에 studentID를 설정합니다.
            pstmt.setString(1, studentID);
            // 두 번째 placeholder에 courseID를 설정합니다.
            pstmt.setString(2, courseID);
            // INSERT 쿼리를 실행하여 수강 등록 정보를 추가합니다.
            pstmt.executeUpdate();
        }
    }


    /**
     * 주어진 학생과 과목 정보를 enrollments 테이블에서 삭제합니다.
     *
     * @param studentID 학생의 고유 ID
     * @param courseID  과목의 고유 ID
     * @throws SQLException 데이터베이스 작업 도중 발생할 수 있는 예외
     */
    public void deleteEnrollment(String studentID, String courseID) throws SQLException {
        // enrollments 테이블에서 학생 ID와 과목 ID가 일치하는 레코드를 삭제하는 SQL 쿼리
        String sql = "DELETE FROM enrollments WHERE studentID = ? AND courseID = ?";

        // try-with-resources 구문으로 Connection과 PreparedStatement를 자동으로 닫습니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 첫 번째 placeholder에 studentID를 설정합니다.
            pstmt.setString(1, studentID);
            // 두 번째 placeholder에 courseID를 설정합니다.
            pstmt.setString(2, courseID);
            // DELETE 쿼리를 실행하여 수강 신청 정보를 삭제합니다.
            pstmt.executeUpdate();
        }
    }

    /**
     * 특정 학생의 모든 수강 신청 정보를 조회하여 Enrollment 객체의 리스트로 반환합니다.
     *
     * @param studentID 조회할 학생의 고유 ID
     * @return 학생의 수강 신청 정보를 담은 Enrollment 객체 리스트
     * @throws SQLException 데이터베이스 작업 도중 발생할 수 있는 예외
     */
    public List<Enrollment> getEnrollmentsByStudentID(String studentID) throws SQLException {
        // 조회 결과를 저장할 리스트 생성
        List<Enrollment> enrollments = new ArrayList<>();
        // studentID를 조건으로 enrollments 테이블에서 수강 신청 정보를 조회하는 SQL 쿼리
        String sql = "SELECT studentID, courseID FROM enrollments WHERE studentID = ?";

        // try-with-resources 구문으로 Connection, PreparedStatement, ResultSet를 자동으로 닫습니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // placeholder에 studentID 값을 설정합니다.
            pstmt.setString(1, studentID);

            // 쿼리를 실행하여 결과 셋(ResultSet)을 가져옵니다.
            try (ResultSet rs = pstmt.executeQuery()) {
                // 결과 셋에서 각 레코드를 순회하며 Enrollment 객체를 생성합니다.
                while (rs.next()) {
                    Enrollment enrollment = new Enrollment(
                            rs.getString("studentID"), // 학생 ID 조회
                            rs.getString("courseID")   // 과목 ID 조회
                    );
                    // 생성된 Enrollment 객체를 리스트에 추가합니다.
                    enrollments.add(enrollment);
                }
            }
        }
        // 조회된 수강 신청 정보 리스트를 반환합니다.
        return enrollments;
    }
}
