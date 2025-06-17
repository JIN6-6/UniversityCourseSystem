package com.example.universitycoursesystemui;

import javafx.beans.property.SimpleStringProperty;

// 수강 신청 UI 화면에서, 사용자가 신청한 강의 목록의 각 "행(row)"에 해당하는 정보를 저장하는 클래스입니다.
public class EnrolledCourseInfo {

    private final Course course; // 수강 신청한 강의 정보를 담고 있는 Course 객체
    private final SimpleStringProperty status; // 수강 상태 정보: "수강 중" 또는 "대기 중" 등의 문자열 상태

    // 생성자(Constructor): 강의 객체와 상태 문자열을 받아와 초기화합니다.
    public EnrolledCourseInfo(Course course, String status) {
        this.course = course;
        // JavaFX의 Property 시스템을 활용하여 문자열 상태를 UI와 연동 가능하게 포장
        this.status = new SimpleStringProperty(status);
    }

    // 수강 상태(status)를 문자열로 반환 (예: TableView에서 셀에 표시될 값)
    public String getStatus() {
        return status.get();
    }

    // JavaFX의 바인딩 기능을 위해 사용되는 속성 반환 (Observable 프로퍼티)
    public SimpleStringProperty statusProperty() {
        return status;
    }

    // 아래 메서드들은 Course 객체의 정보를 꺼내기 위한 위임 메서드들입니다.
    //     - TableView에서 각 열에 표시할 값을 반환하기 위해 사용됩니다.

    public String getCourseID() {
        return course.getCourseID(); // 강의 코드
    }

    public String getCourseName() {
        return course.getCourseName(); // 강의명
    }

    public String getProfessorName() {
        return course.getProfessorName(); // 담당 교수명
    }

    public int getCredit() {
        return course.getCredit(); // 학점
    }

    public String getTime() {
        return course.getTime(); // 강의 시간표 정보
    }

    public String getCourseType() {
        return course.getCourseType(); // 강의 유형 (전공, 교양 등)
    }
}