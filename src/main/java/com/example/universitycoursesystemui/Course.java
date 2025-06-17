package com.example.universitycoursesystemui;

/**
 * Course 클래스
 * 이 클래스는 강의에 관련된 정보를 저장하는 모델 클래스입니다.
 * 강의 ID, 강의 이름, 담당 교수의 정보, 학점, 강의 시간, 수강 인원 등의 정보를 관리합니다.
 */
public class Course {
	// 강의의 고유 ID (예: 과목 코드 + 분반번호, ex. MT01043-01)
	private String courseID;
	// 강의 이름
	private String courseName;
	// 강의를 담당하는 교수의 고유 ID
	private String professorID;
	// 강의를 담당하는 교수의 이름
	private String professorName;
	// 강의 학점
	private int credit;
	// 강의 시간 또는 요일 정보
	private String time;
	// 강의 유형 (예: 전공, 교양 등)
	private String courseType;
	// 강의의 최대 수강 인원
	private int maxCapacity;
	// 현재 등록된 학생 수
	private int currentEnrollment;
	// 대기 목록에 등록된 학생 수
	private int waitlistCount;
	// 강의와 연관된 전공 정보
	private String majorName; // <-- 이 줄을 추가합니다.

	/**
	 * 생성자: 모든 필드를 초기화하여 Course 객체를 생성합니다.
	 *
	 * @param courseID           강의의 고유 ID
	 * @param courseName         강의의 이름
	 * @param professorID        담당 교수의 고유 ID
	 * @param professorName      담당 교수의 이름
	 * @param credit             강의 학점
	 * @param time               강의 시간 또는 요일 정보
	 * @param courseType         강의 유형 (예: 전공, 교양 등)
	 * @param maxCapacity        강의의 최대 수강 인원
	 * @param currentEnrollment  현재 수강 중인 학생 수
	 * @param waitlistCount      대기 목록에 등록된 학생 수
	 * @param majorName          강의와 연관된 전공 정보
	 */
	public Course(String courseID, String courseName, String professorID, String professorName, int credit, String time, String courseType, int maxCapacity, int currentEnrollment, int waitlistCount, String majorName) {
		this.courseID = courseID;
		this.courseName = courseName;
		this.professorID = professorID;
		this.professorName = professorName;
		this.credit = credit;
		this.time = time;
		this.courseType = courseType;
		this.maxCapacity = maxCapacity;
		this.currentEnrollment = currentEnrollment;
		this.waitlistCount = waitlistCount;
		this.majorName = majorName; // <-- 이 줄을 추가합니다.
	}

	// Getters: 각 필드의 값을 반환하는 메서드들

	/**
	 * 강의의 고유 ID를 반환합니다.
	 * @return courseID 강의 ID
	 */
	public String getCourseID() {
		return courseID;
	}

	/**
	 * 강의 이름을 반환합니다.
	 * @return courseName 강의 이름
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * 담당 교수의 고유 ID를 반환합니다.
	 * @return professorID 교수 ID
	 */
	public String getProfessorID() {
		return professorID;
	}

	/**
	 * 담당 교수의 이름을 반환합니다.
	 * @return professorName 교수 이름
	 */
	public String getProfessorName() {
		return professorName;
	}

	/**
	 * 강의의 학점을 반환합니다.
	 * @return credit 강의 학점
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * 강의 시간을 반환합니다.
	 * @return time 강의 시간
	 */
	public String getTime() {
		return time;
	}

	/**
	 * 강의 유형을 반환합니다.
	 * @return courseType 강의 유형
	 */
	public String getCourseType() {
		return courseType;
	}

	/**
	 * 강의의 최대 수강 인원을 반환합니다.
	 * @return maxCapacity 최대 수강 인원
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}

	/**
	 * 현재 등록된 학생 수를 반환합니다.
	 * @return currentEnrollment 현재 학생 수
	 */
	public int getCurrentEnrollment() {
		return currentEnrollment;
	}

	/**
	 * 대기 목록에 등록된 학생 수를 반환합니다.
	 * @return waitlistCount 대기 인원 수
	 */
	public int getWaitlistCount() {
		return waitlistCount;
	}

	/**
	 * 강의와 연관된 전공 정보를 반환합니다.
	 * @return majorName 전공 정보
	 */
	public String getMajorName() {
		return majorName; // <-- 이 Getter를 추가합니다.
	}

	// Setters: 각 필드의 값을 수정하는 메서드들

	/**
	 * 강의 이름을 설정합니다.
	 * @param courseName 새로운 강의 이름
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * 담당 교수의 ID를 설정합니다.
	 * @param professorID 새로운 교수 ID
	 */
	public void setProfessorID(String professorID) {
		this.professorID = professorID;
	}

	/**
	 * 강의의 학점을 설정합니다.
	 * @param credit 새로운 학점 값
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}

	/**
	 * 강의 시간을 설정합니다.
	 * @param time 새로운 강의 시간 정보
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * 강의 유형을 설정합니다.
	 * @param courseType 새로운 강의 유형
	 */
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	/**
	 * 강의의 최대 수강 인원을 설정합니다.
	 * @param maxCapacity 새로운 최대 수강 인원 값
	 */
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	/**
	 * 강의와 연관된 전공 정보를 설정합니다.
	 * @param majorName 새로운 전공 정보
	 */
	public void setMajorName(String majorName) {
		this.majorName = majorName; // <-- 이 Setter를 추가합니다.
	}
}