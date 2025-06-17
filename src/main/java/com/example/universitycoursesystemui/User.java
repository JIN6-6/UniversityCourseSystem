package com.example.universitycoursesystemui;

/*
 * User 클래스는 시스템 내 사용자의 정보를 저장하는 기본 모델 클래스입니다.
 * 사용자 정보에는 사용자 식별자, 비밀번호, 이름, 사용자 역할, 그리고 전공 정보가 포함됩니다.
 */
public class User {

	// 사용자 고유 식별자 (예: 학번, 직원번호 등)
	private String userID;

	// 사용자 로그인에 사용되는 비밀번호
	private String password;

	// 사용자 이름
	private String name;

	// 사용자 역할 정보 (예: 관리자, 학생 등). role이라는 명칭으로도 사용됩니다.
	private String userType;

	// 사용자의 전공 정보를 저장하는 필드 (학생일 경우 전공 분야)
	private String major;

	/*
	 * 생성자(Constructor)
	 * 전달받은 매개변수들을 사용해 사용자 객체를 초기화합니다.
	 * userID, password, name, userType, major 값들을 각 필드에 설정합니다.
	 */
	public User(String userID, String password, String name, String userType, String major) {
		this.userID = userID;
		this.password = password;
		this.name = name;
		this.userType = userType;
		this.major = major; // major 필드 초기화
	}

	// Getter 메서드들: 각 필드의 값을 반환하여 외부에서 접근할 수 있도록 합니다.

	// 사용자 고유 식별자 반환
	public String getUserID() {
		return userID;
	}

	// 사용자 비밀번호 반환 (보안 이슈로 사용에 주의해야 함)
	public String getPassword() {
		return password;
	}

	// 사용자 이름 반환
	public String getName() {
		return name;
	}

	// 사용자 역할(userType)을 반환함. 시스템 내 권한 관리 등에 사용될 수 있음.
	public String getRole() {
		return userType;
	}

	// 사용자 전공 정보를 반환합니다.
	public String getMajor() {
		return major;
	}

	// Setter 메서드들: 필요에 따라 각 필드의 값을 수정할 수 있도록 합니다.

	// 비밀번호 변경을 위한 메서드
	public void setPassword(String password) {
		this.password = password;
	}

	// 사용자 이름을 수정할 때 사용
	public void setName(String name) {
		this.name = name;
	}

	// 사용자 역할(userType)을 수정할 때 사용 (예: 권한 변경 등)
	public void setRole(String userType) {
		this.userType = userType;
	}

	// 사용자 전공 정보를 수정할 때 사용합니다.
	public void setMajor(String major) {
		this.major = major;
	}
}