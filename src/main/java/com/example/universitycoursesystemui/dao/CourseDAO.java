package com.example.universitycoursesystemui.dao;

import com.example.universitycoursesystemui.Course;
import com.example.universitycoursesystemui.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * CourseDAO 클래스
 * 이 클래스는 강의(Course)와 관련된 데이터베이스 작업(강의 정보 조회, 추가, 수정, 수강 및 대기 인원 업데이트 등)을 수행합니다.
 */
public class CourseDAO {
    /**
     * 모든 강의 정보를 조회하여 Course 객체의 리스트로 반환합니다.
     *
     * @return 강의 정보 목록(List<Course>)
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     *
     * 강의 정보뿐만 아니라, 강의를 담당하는 교수의 이름(professorName) 및 전공 정보(major_name)도 함께 조회합니다.
     */
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        // courses 테이블과 users 테이블을 조인하여 강의 정보와 교수 이름, 전공 정보를 함께 조회하는 SQL 쿼리
        String sql = "SELECT c.*, u.name AS professorName, c.major_name FROM courses c JOIN users u ON c.professorID = u.userID";
        // try-with-resources를 사용하여 Connection, PreparedStatement, ResultSet를 자동으로 닫습니다.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            // 결과셋의 모든 레코드에 대해 Course 객체를 생성 후 리스트에 추가
            while (rs.next()) {
                courses.add(new Course(
                        rs.getString("courseID"),            // 강의 ID
                        rs.getString("courseName"),          // 강의명
                        rs.getString("professorID"),         // 담당 교수의 ID
                        rs.getString("professorName"),       // 담당 교수의 이름 (조인된 users 테이블에서 조회)
                        rs.getInt("credit"),                 // 학점
                        rs.getString("time"),                // 강의 시간 또는 요일 정보
                        rs.getString("courseType"),          // 강의 유형 (예: 전공, 교양)
                        rs.getInt("maxCapacity"),            // 최대 수강 가능 인원
                        rs.getInt("currentEnrollment"),      // 현재 등록된 학생 수
                        rs.getInt("waitlist_count"),         // 대기 목록에 있는 인원 수
                        rs.getString("major_name")           // 강의와 연관된 전공 정보
                ));
            }
        }
        return courses;
    }

    /**
     * 새로운 강의를 데이터베이스에 추가합니다.
     *
     * @param course 추가할 Course 객체
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     *
     * major_name 컬럼을 포함하여 강의의 모든 정보가 추가됩니다.
     */
    public void addCourse(Course course) throws SQLException {
        // 모든 강의 정보(major_name 포함)를 삽입하는 SQL INSERT 쿼리
        String sql = "INSERT INTO courses (courseID, courseName, professorID, credit, time, courseType, maxCapacity, currentEnrollment, waitlist_count, major_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Course 객체에서 필요한 각 필드를 쿼리의 파라미터에 설정
            pstmt.setString(1, course.getCourseID());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getProfessorID());
            pstmt.setInt(4, course.getCredit());
            pstmt.setString(5, course.getTime());
            pstmt.setString(6, course.getCourseType());
            pstmt.setInt(7, course.getMaxCapacity());
            pstmt.setInt(8, course.getCurrentEnrollment());
            pstmt.setInt(9, course.getWaitlistCount());
            pstmt.setString(10, course.getMajorName()); // 전공 정보 추가
            pstmt.executeUpdate(); // INSERT 쿼리 실행
        }
    }

    /**
     * 기존 강의 정보를 업데이트합니다.
     *
     * @param course 업데이트할 Course 객체
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     *
     * 강의명, 담당 교수, 학점, 시간, 강의 유형, 최대 수강 인원 및 전공 정보(major_name)을 업데이트합니다.
     */
    public void updateCourse(Course course) throws SQLException {
        // courseID를 기준으로 강의 정보를 업데이트하는 SQL 쿼리, major_name을 포함하여 업데이트합니다.
        String sql = "UPDATE courses SET courseName = ?, professorID = ?, credit = ?, time = ?, courseType = ?, maxCapacity = ?, major_name = ? WHERE courseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getProfessorID());
            pstmt.setInt(3, course.getCredit());
            pstmt.setString(4, course.getTime());
            pstmt.setString(5, course.getCourseType());
            pstmt.setInt(6, course.getMaxCapacity());
            pstmt.setString(7, course.getMajorName()); // 전공 정보 업데이트
            pstmt.setString(8, course.getCourseID());
            pstmt.executeUpdate(); // UPDATE 쿼리 실행
        }
    }

    /**
     * 특정 강의의 현재 등록 인원을 1 증가시킵니다.
     *
     * @param courseID 업데이트할 강의 ID
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     */
    public void incrementCurrentEnrollment(String courseID) throws SQLException {
        String sql = "UPDATE courses SET currentEnrollment = currentEnrollment + 1 WHERE courseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseID);
            pstmt.executeUpdate(); // 현재 등록 인원 증가 실행
        }
    }

    /**
     * 특정 강의의 현재 등록 인원을 1 감소시킵니다.
     *
     * @param courseID 업데이트할 강의 ID
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     */
    public void decrementCurrentEnrollment(String courseID) throws SQLException {
        String sql = "UPDATE courses SET currentEnrollment = currentEnrollment - 1 WHERE courseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseID);
            pstmt.executeUpdate(); // 현재 등록 인원 감소 실행
        }
    }

    /**
     * 특정 강의의 대기 인원 수를 1 증가시킵니다.
     *
     * @param courseID 업데이트할 강의 ID
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     */
    public void incrementWaitlistCount(String courseID) throws SQLException {
        String sql = "UPDATE courses SET waitlist_count = waitlist_count + 1 WHERE courseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseID);
            pstmt.executeUpdate(); // 대기 인원 수 증가 실행
        }
    }

    /**
     * 특정 강의의 대기 인원 수를 1 감소시킵니다.
     *
     * @param courseID 업데이트할 강의 ID
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     */
    public void decrementWaitlistCount(String courseID) throws SQLException {
        String sql = "UPDATE courses SET waitlist_count = waitlist_count - 1 WHERE courseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseID);
            pstmt.executeUpdate(); // 대기 인원 수 감소 실행
        }
    }

    /**
     * 주어진 강의 ID에 해당하는 강의 정보를 조회하여 Course 객체로 반환합니다.
     *
     * @param courseId 조회할 강의 ID
     * @return 조회된 강의 정보(Course 객체) 또는 강의를 찾을 수 없을 경우 null
     * @throws SQLException 데이터베이스 접근 또는 쿼리 실행 중 발생하는 예외
     *
     * 강의 정보를 조회할 때 교수의 이름(professorName)과 전공 정보(major_name)도 함께 반환합니다.
     */
    public Course getCourseById(String courseId) throws SQLException {
        // courses 테이블과 users 테이블을 조인하여 특정 강의에 대한 세부 정보를 조회하는 SQL 쿼리
        String sql = "SELECT c.*, u.name AS professorName, c.major_name FROM courses c JOIN users u ON c.professorID = u.userID WHERE c.courseID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 조회된 결과를 기반으로 새로운 Course 객체를 생성 및 반환
                    return new Course(
                            rs.getString("courseID"),
                            rs.getString("courseName"),
                            rs.getString("professorID"),
                            rs.getString("professorName"),
                            rs.getInt("credit"),
                            rs.getString("time"),
                            rs.getString("courseType"),
                            rs.getInt("maxCapacity"),
                            rs.getInt("currentEnrollment"),
                            rs.getInt("waitlist_count"),
                            rs.getString("major_name")
                    );
                }
            }
        }
        // 강의 정보를 찾지 못한 경우 null 반환
        return null;
    }
}
