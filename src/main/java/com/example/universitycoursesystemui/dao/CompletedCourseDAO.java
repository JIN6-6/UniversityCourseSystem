package com.example.universitycoursesystemui.dao;

import com.example.universitycoursesystemui.model.CompletedCourse; // CompletedCourse 모델 클래스 import
import com.example.universitycoursesystemui.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CompletedCourseDAO 클래스
 * 이 클래스는 학생의 수강 완료 강의(CompletedCourse)와 관련된 데이터베이스 작업(조회, 추가 등)을 수행합니다.
 */
public class CompletedCourseDAO {

    /**
     * 주어진 학생 ID에 해당하는 수강 완료 강의 정보를 조회합니다.
     *
     * @param studentId 조회할 학생의 고유 ID
     * @return CompletedCourse 객체들의 리스트. 해당 학생의 수강 완료 강의 정보가 담깁니다.
     * @throws SQLException 데이터베이스 작업 중 발생하는 예외
     */
    public List<CompletedCourse> getCompletedCoursesByStudentId(String studentId) throws SQLException {
        // 조회 결과를 저장할 리스트 생성
        List<CompletedCourse> completedCourses = new ArrayList<>();
        // student_id가 일치하는 레코드를 조회하는 SQL 쿼리
        String sql = "SELECT student_id, course_id, credit, course_type, grade, completion_date " +
                "FROM completed_courses WHERE student_id = ?";

        // try-with-resources 구문을 사용하여 Connection, PreparedStatement, ResultSet을 자동으로 닫습니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 쿼리의 첫 번째 파라미터에 studentId 값을 설정합니다.
            pstmt.setString(1, studentId);

            // SQL 쿼리를 실행하고 결과 셋(ResultSet)을 받아옵니다.
            try (ResultSet rs = pstmt.executeQuery()) {
                // 결과 셋의 각 행에 대해 CompletedCourse 객체를 생성하여 리스트에 추가합니다.
                while (rs.next()) {
                    completedCourses.add(new CompletedCourse(
                            rs.getString("student_id"),  // 학생 ID
                            rs.getString("course_id"),     // 강의 ID
                            rs.getInt("credit"),           // 학점
                            rs.getString("course_type"),   // 강의 유형
                            rs.getString("grade"),         // 성적
                            rs.getDate("completion_date")  // 수료 날짜
                    ));
                }
            }
        }
        // 조회된 CompletedCourse 객체 리스트 반환
        return completedCourses;
    }

    /**
     * 새로운 수강 완료 강의 정보를 데이터베이스에 추가합니다.
     *
     * @param completedCourse 추가할 CompletedCourse 객체
     * @throws SQLException 데이터베이스 작업 중 발생하는 예외
     */
    public void addCompletedCourse(CompletedCourse completedCourse) throws SQLException {
        // completed_courses 테이블에 수강 완료 강의 정보를 삽입하는 SQL INSERT 쿼리
        String sql = "INSERT INTO completed_courses (student_id, course_id, credit, course_type, grade, completion_date) VALUES (?, ?, ?, ?, ?, ?)";

        // try-with-resources 구문으로 Connection과 PreparedStatement를 자동으로 닫습니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 각 파라미터에 CompletedCourse 객체의 속성 값을 설정합니다.
            pstmt.setString(1, completedCourse.getStudentId());
            pstmt.setString(2, completedCourse.getCourseId());
            pstmt.setInt(3, completedCourse.getCredit());
            pstmt.setString(4, completedCourse.getCourseType());
            pstmt.setString(5, completedCourse.getGrade());
            pstmt.setDate(6, completedCourse.getCompletionDate());

            // SQL INSERT 쿼리를 실행하여 데이터를 DB에 추가합니다.
            pstmt.executeUpdate();
        }
    }
}