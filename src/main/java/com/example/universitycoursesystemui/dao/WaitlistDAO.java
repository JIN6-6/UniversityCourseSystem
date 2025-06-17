package com.example.universitycoursesystemui.dao;

import com.example.universitycoursesystemui.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * WaitlistDAO 클래스
 * 대기목록(waitlist) 관련 데이터베이스 작업(삽입, 삭제, 조회 등)을 수행하는 DAO 클래스입니다.
 */

public class WaitlistDAO {
    /**
     * 대기목록에 학생을 추가하는 메서드
     *
     * @param studentId 학생의 고유 ID
     * @param courseId  과목의 고유 ID
     * @throws SQLException 데이터베이스 작업 중 예외 발생 시 처리
     */

    // 대기열 추가
    public void add(String studentId, String courseId) throws SQLException {
        // 대기목록 테이블에 학생과 과목 정보를 삽입하는 SQL 쿼리
        String sql = "INSERT INTO waitlist (student_id, course_id) VALUES (?, ?)";
        // try-with-resources 구문을 통해 Connection과 PreparedStatement를 자동으로 close합니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId); // 첫 번째 placeholder에 studentId 값을 설정
            pstmt.setString(2, courseId);  // 두 번째 placeholder에 courseId 값을 설정
            pstmt.executeUpdate();         // SQL INSERT 쿼리를 실행하여 데이터를 추가
        }
    }


    /**
     * 대기목록에서 특정 학생의 레코드를 제거하는 메서드
     *
     * @param studentId 삭제할 학생의 고유 ID
     * @param courseId  삭제할 과목의 고유 ID
     * @throws SQLException 데이터베이스 작업 중 예외 발생 시 처리
     */
    public void remove(String studentId, String courseId) throws SQLException {
        // 학생과 과목 조건에 맞는 레코드를 삭제하는 SQL 쿼리
        String sql = "DELETE FROM waitlist WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId); // 조건에 맞는 학생 ID 설정
            pstmt.setString(2, courseId);  // 조건에 맞는 과목 ID 설정
            pstmt.executeUpdate();         // SQL DELETE 쿼리 실행하여 대상 레코드 삭제
        }
    }


    /**
     * 특정 과목의 대기목록에서 가장 먼저 등록한 학생의 ID를 조회하는 메서드
     *
     * @param courseId 조회할 과목의 고유 ID
     * @return Optional<String> - 대기목록 상에서 우선 순위 1인 학생의 ID. 결과가 없으면 Optional.empty() 반환.
     * @throws SQLException 데이터베이스 작업 중 예외 발생 시 처리
     */
    public Optional<String> findNextStudent(String courseId) throws SQLException {
        // 해당 과목의 등록 시각(registered_at)이 가장 빠른 학생의 ID를 가져오기 위한 SQL 쿼리
        String sql = "SELECT student_id FROM waitlist WHERE course_id = ? ORDER BY registered_at ASC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId); // 과목 ID를 쿼리의 파라미터로 설정
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // 결과셋에 데이터가 존재하면
                    return Optional.of(rs.getString("student_id")); // 첫 번째 레코드의 student_id를 반환
                }
            }
        }
        // 데이터가 존재하지 않을 경우 빈 Optional 반환
        return Optional.empty();
    }


    /**
     * 특정 학생이 대기 중인 모든 과목의 ID 목록을 조회하는 메서드
     *
     * @param studentId 조회할 학생의 고유 ID
     * @return List<String> - 해당 학생이 대기 중인 과목들의 ID 목록
     * @throws SQLException 데이터베이스 작업 중 예외 발생 시 처리
     */
    public List<String> getWaitlistedCourseIdsByStudent(String studentId) throws SQLException {
        List<String> courseIds = new ArrayList<>();
        // 학생의 대기목록에서 과목 ID만을 조회하는 SQL 쿼리
        String sql = "SELECT course_id FROM waitlist WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId); // 대상 학생 ID를 쿼리 파라미터로 설정
            try (ResultSet rs = pstmt.executeQuery()) {
                // 결과셋에 포함된 모든 course_id를 리스트에 추가
                while (rs.next()) {
                    courseIds.add(rs.getString("course_id"));
                }
            }
        }
        // 조회된 과목 ID 리스트 반환
        return courseIds;
    }

}