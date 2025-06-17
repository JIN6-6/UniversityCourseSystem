package com.example.universitycoursesystemui;

// Enrollment 클래스는 학생과 강의 간의 수강 관계를 나타냅니다.
public class Enrollment {
    private String studentID;  // 수강 신청한 학생의 고유 학번
    private String courseID;   // 수강 신청된 강의의 고유 코드 (Course 객체의 ID와 동일)

    // 생성자(Constructor): 학번과 강의 코드를 매개변수로 받아 해당 필드를 초기화
    public Enrollment(String studentID, String courseID) {
        this.studentID = studentID;
        this.courseID = courseID;
    }

    // Getter 메서드들: 필드 값을 외부에서 읽을 수 있도록 제공
    public String getStudentID() {
        return studentID;
    }

    public String getCourseID() {
        return courseID;
    }

    // Setter 메서드들: 외부에서 해당 필드 값을 변경할 수 있도록 허용 (필요한 경우에만 사용)
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }
}