package com.example.universitycoursesystemui.logic;

import com.example.universitycoursesystemui.model.Course;
import com.example.universitycoursesystemui.dao.CourseDAO;
import com.example.universitycoursesystemui.dao.CompletedCourseDAO;
import com.example.universitycoursesystemui.model.CompletedCourse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Map; // Map 클래스 import
import java.util.LinkedHashMap; // 순서를 유지하는 LinkedHashMap import


/**
 * CourseRecommender 클래스
 * 학생에게 추천 강의를 선정하는 로직을 구현합니다.
 * 이 클래스는 학생의 이수 내역과 전공 정보를 기반으로 아직 이수 또는 수강 중이지 않은 강의를 추천합니다.
 *
 * 추천 알고리즘은 간단한 KNN 아이디어를 적용하여,
 *  - 학생이 이미 이수한 과목(분반 포함)과 중복되지 않는 강의
 *  - 학생의 전공과 일치하는 강의
 *  - 학점이 0이 아닌, 실질적인 수강 강의만 추천 목록에 포함시키는 조건으로 동작합니다.
 *
 * 또한, 추천 목록에서 과목명이 중복되는 경우 하나의 분반만 추천하여 팝업 등의 UI를 깔끔하게 유지합니다.
 */
public class CourseRecommender {

    private CourseDAO courseDAO;
    private CompletedCourseDAO completedCourseDAO;

    /**
     * 기본 생성자에서 CourseDAO와 CompletedCourseDAO 인스턴스들을 초기화합니다.
     */
    public CourseRecommender() {
        this.courseDAO = new CourseDAO();
        this.completedCourseDAO = new CompletedCourseDAO();
    }

    /**
     * 특정 학생에게 과목을 추천하는 메서드 (간단한 KNN 아이디어 적용)
     * - 학생의 이수 과목과 동일한 전공, 유사한 이수 구분을 가진 과목을 우선 추천
     * - 아직 이수하지 않은 과목만 추천하며, 이미 이수한 과목의 다른 분반도 제외
     * - 추천 목록에서 '과목명'이 중복되는 경우 하나의 분반만 추천 (팝업을 깔끔하게)
     *
     * @param studentId    추천을 받을 학생 ID
     * @param studentMajor 학생의 전공 (UserDAO에서 가져온 값)
     * @return 추천 강의의 리스트
     * @throws SQLException 데이터베이스 작업 중 발생할 수 있는 예외
     */
    public List<Course> getRecommendedCourses(String studentId, String studentMajor) throws SQLException {
        // 결과로 반환될 추천 강의 리스트
        List<Course> recommendedCourses = new ArrayList<>();
        // 과목명 중복을 방지하기 위해 사용될 맵 (key: 과목명, value: 해당 Course 객체)
        Map<String, Course> uniqueCourseNames = new LinkedHashMap<>(); // 순서 유지를 위함

        // 1. 학생이 이미 이수한 강의의 전체 CourseID와 기본 과목 코드(분반 제외) 목록 가져오기
        Set<String> completedCourseIds = new HashSet<>();    // 예: MT01043-01과 같은 전체 강의 ID
        Set<String> completedBaseCourseIds = new HashSet<>();  // 예: MT01043 (분반 번호 제외)

        // CompletedCourseDAO를 통해 해당 학생이 이수한 강의 목록을 가져옵니다.
        List<CompletedCourse> studentCompletedCourses = completedCourseDAO.getCompletedCoursesByStudentId(studentId);
        for (CompletedCourse cc : studentCompletedCourses) {
            completedCourseIds.add(cc.getCourseId());
            // 강의 ID 길이가 7 이상이면 앞 7글자만 추출하여 기본 강의 코드를 생성
            if (cc.getCourseId().length() >= 7) {
                completedBaseCourseIds.add(cc.getCourseId().substring(0, 7));
            }
        }

        // 2. 현재 수강 중인 강의 ID 목록 가져오기 (EnrollmentDAO 연동 필요)
        // EnrollmentDAO를 사용하여 현재 수강 중인 강의도 제외 대상에 포함해야 합니다.
        // 이 예제에서는 EnrollmentDAO를 직접 호출하지 않고, 주석으로 처리해두었습니다.
        Set<String> enrolledCourseIds = new HashSet<>();
        /*
         * 예시 구현:
         * for (Enrollment enrollment : enrollmentDAO.getEnrollmentsByStudentID(studentId)) {
         *     enrolledCourseIds.add(enrollment.getCourseID());
         * }
         */

        // 3. 데이터베이스에서 모든 강의 목록을 가져옵니다.
        List<Course> allCourses = courseDAO.getAllCourses();

        // 4. 추천 로직: 학생의 전공과 일치하며 아직 이수/수강하지 않은 강의를 필터링
        for (Course course : allCourses) {
            // 현재 강의의 전체 CourseID (예: MT01043-01)
            String currentCourseId = course.getCourseID();
            // 강의의 기본 코드를 추출 (예: MT01043), courseID 길이가 7이상인 경우
            String currentBaseCourseId = (currentCourseId.length() >= 7) ? currentCourseId.substring(0, 7) : currentCourseId;

            // 조건:
            // (A) 이미 이수한 강의 (전체 강의ID 또는 기본 강의코드가 포함되어 있음)
            // (B) 현재 수강 중인 강의 (EnrollmentDAO 연동 시 조건 활성화)
            // (C) 추천 목록에 이미 해당 과목명이 포함되어 있다면 중복 추가하지 않음
            if (completedCourseIds.contains(currentCourseId) ||
                    completedBaseCourseIds.contains(currentBaseCourseId) ||
                    // enrolledCourseIds.contains(currentCourseId) || // 수강 중인 강의가 있다면 제외 (EnrollmentDAO 연동 시 활성화)
                    uniqueCourseNames.containsKey(course.getCourseName())) {
                continue;
            }

            // 과목의 전공 정보(majorName)가 없거나, 학생의 전공과 일치하지 않으면 추천 대상에서 제외
            if (course.getMajorName() == null || !studentMajor.equals(course.getMajorName())) {
                continue;
            }

            // 학점이 0인 강의는 추천에서 제외 (졸업인증 과목 등)
            if (course.getCredit() == 0) {
                continue;
            }

            // 모든 조건을 통과한 강의는 추천 후보에 추가
            // 과목명 중복을 방지하기 위해 LinkedHashMap을 사용하여 키로 과목명을 관리
            uniqueCourseNames.put(course.getCourseName(), course);
        }

        // 중복 제거된 추천 후보들을 리스트로 변환
        recommendedCourses.addAll(uniqueCourseNames.values());

        // 추천 강의를 정렬합니다.
        // 먼저 학점이 높은 순으로 정렬하고, 학점이 동일하면 과목ID 순으로 정렬합니다.
        recommendedCourses.sort(Comparator.comparing(Course::getCredit).reversed()
                .thenComparing(Course::getCourseID));

        // 상위 N개(여기서는 최대 5개) 강의만 최종 추천 목록으로 반환합니다.
        int numRecommendations = Math.min(recommendedCourses.size(), 5);
        return recommendedCourses.subList(0, numRecommendations);
    }
}