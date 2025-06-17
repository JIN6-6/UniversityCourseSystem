package com.example.universitycoursesystemui.model; // 패키지 경로에 맞게 수정됨

import java.sql.Date;

/**
 * CompletedCourse 클래스
 * 이 클래스는 학생이 이수한 강의에 대한 정보를 저장하는 모델 클래스입니다.
 * 각 인스턴스는 학생의 ID, 강의의 ID, 강의 학점, 강의 유형, 성적, 그리고 수료 날짜 정보를 포함합니다.
 */
public class CompletedCourse {
    // 학생의 고유 ID
    private String studentId;
    // 강의의 고유 ID (예: MT01043-01)
    private String courseId;
    // 강의에 부여된 학점
    private int credit;
    // 강의 유형 (예: '전공필수', '전공선택', '교양')
    private String courseType;
    // 학생이 강의에서 받은 성적
    private String grade;
    // 강의를 이수한 날짜
    private Date completionDate;

    /**
     * CompletedCourse 생성자
     *
     * @param studentId      학생의 고유 ID
     * @param courseId       강의의 고유 ID
     * @param credit         강의의 학점
     * @param courseType     강의 유형 (예: '전공필수', '전공선택', '교양')
     * @param grade          학생이 받은 성적
     * @param completionDate 강의 수료 날짜
     */
    public CompletedCourse(String studentId, String courseId, int credit, String courseType, String grade, Date completionDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.credit = credit;
        this.courseType = courseType;
        this.grade = grade;
        this.completionDate = completionDate;
    }

    // Getters

    /**
     * 학생의 ID를 반환합니다.
     *
     * @return 학생의 고유 ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 강의의 ID를 반환합니다.
     *
     * @return 강의의 고유 ID
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * 강의의 학점을 반환합니다.
     *
     * @return 강의 학점
     */
    public int getCredit() {
        return credit;
    }

    /**
     * 강의의 유형을 반환합니다.
     *
     * @return 강의 유형 (예: '전공필수', '전공선택', '교양')
     */
    public String getCourseType() {
        return courseType;
    }

    /**
     * 학생이 받은 강의 성적을 반환합니다.
     *
     * @return 강의 성적
     */
    public String getGrade() {
        return grade;
    }

    /**
     * 강의 수료 날짜를 반환합니다.
     *
     * @return 강의 수료 날짜
     */
    public Date getCompletionDate() {
        return completionDate;
    }

    // Setters (필요에 따라 업데이트)

    /**
     * 강의 학점을 설정합니다.
     *
     * @param credit 새롭게 설정할 학점 값
     */
    public void setCredit(int credit) {
        this.credit = credit;
    }

    /**
     * 강의 유형을 설정합니다.
     *
     * @param courseType 새롭게 설정할 강의 유형
     */
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    /**
     * 강의 성적을 설정합니다.
     *
     * @param grade 새롭게 설정할 성적 값
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * 강의 수료 날짜를 설정합니다.
     *
     * @param completionDate 새롭게 설정할 수료 날짜
     */
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
}