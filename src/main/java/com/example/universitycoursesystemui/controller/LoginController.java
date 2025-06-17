package com.example.universitycoursesystemui.controller;

import com.example.universitycoursesystemui.MainApplication;
import com.example.universitycoursesystemui.dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 로그인 화면의 컨트롤러입니다.
 * 사용자 인증을 처리하고, 로그인 성공 시 해당 역할에 맞는 대시보드 화면으로 전환합니다.
 * 회원가입 화면으로 이동하는 기능도 제공합니다.
 */
public class LoginController implements Initializable {

    // FXML에서 주입되는 UI 요소들
    @FXML private TextField userIDField;     // 사용자 ID 입력 필드
    @FXML private PasswordField passwordField; // 비밀번호 입력 필드

    // DAO(Data Access Object) 인스턴스
    private UserDAO userDAO; // 사용자 데이터 처리를 위한 DAO

    /**
     * 컨트롤러 초기화 메서드. FXML 로드 시 자동으로 호출됩니다.
     * DAO 인스턴스를 초기화합니다.
     * @param url FXML 파일의 위치
     * @param resourceBundle 로컬라이제이션에 사용될 리소스 번들
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = new UserDAO();
        // System.out.println("LoginController initialize() 호출됨."); // 디버그용 출력 제거
    }

    /**
     * "로그인" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 입력된 ID와 비밀번호를 검증하고, 유효한 사용자일 경우 역할에 따라 대시보드 화면으로 전환합니다.
     * @param event ActionEvent (버튼 클릭 이벤트)
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        // System.out.println("로그인 버튼 클릭됨."); // 디버그용 출력 제거
        String userID = userIDField.getText();
        String password = passwordField.getText();

        // 입력 필드 유효성 검사
        if (userID.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "로그인 실패", "아이디와 비밀번호를 모두 입력해주세요.");
            return;
        }

        try {
            // UserDAO를 통해 사용자 역할 정보를 조회합니다.
            Optional<String> userRole = userDAO.getUserRole(userID, password);

            if (userRole.isPresent()) {
                // DB에서 가져온 사용자 역할을 소문자로 변환하고 앞뒤 공백을 제거하여 비교합니다.
                String role = userRole.get().trim().toLowerCase();

                // 역할에 따라 적절한 대시보드 화면으로 전환합니다.
                if ("student".equals(role)) {
                    MainApplication.showStudentDashboard(userID);
                    // System.out.println("학생 로그인 성공: " + userID); // 디버그용 출력 제거
                } else if ("professor".equals(role)) {
                    showAlert(Alert.AlertType.INFORMATION, "로그인 성공", "교수 로그인", "환영합니다, 교수님!");
                    // System.out.println("교수 로그인 성공: " + userID); // 디버그용 출력 제거

                } else {
                    // 알 수 없는 사용자 유형인 경우
                    showAlert(Alert.AlertType.ERROR, "로그인 실패", "알 수 없는 사용자 유형입니다.");
                }
            } else {
                // 아이디 또는 비밀번호가 잘못된 경우
                showAlert(Alert.AlertType.ERROR, "로그인 실패", "잘못된 아이디 또는 비밀번호입니다.");
            }
        } catch (SQLException | IOException e) {
            // 데이터베이스 오류 또는 화면 로드 실패 시
            showAlert(Alert.AlertType.ERROR, "로그인 오류", "데이터베이스 오류 또는 화면 로드 실패", e.getMessage());
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
        }
    }

    /**
     * "회원가입" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 회원가입 화면으로 전환합니다.
     * @param event ActionEvent (버튼 클릭 이벤트)
     */
    @FXML
    private void handleSignUp(ActionEvent event) {
        // System.out.println("회원가입 버튼 클릭됨."); // 디버그용 출력 제거
        try {
            // MainApplication을 통해 회원가입 화면으로 전환합니다.
            MainApplication.showSignUpScreen();
        } catch (IOException e) {
            // 화면 로드 중 오류 발생 시 경고창 표시
            showAlert(Alert.AlertType.ERROR, "화면 로드 오류", "회원가입 화면을 로드할 수 없습니다.", e.getMessage());
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
        }
    }

    /**
     * 사용자에게 알림 메시지를 표시하는 헬퍼 메서드입니다.
     * contentText가 없는 오버로드된 버전입니다.
     * @param alertType 알림 타입 (INFORMATION, ERROR, WARNING 등)
     * @param title 알림 창의 제목
     * @param headerText 알림 창의 헤더 텍스트
     */
    private void showAlert(Alert.AlertType alertType, String title, String headerText) {
        showAlert(alertType, title, headerText, ""); // contentText를 빈 문자열로 설정하여 4개 인자 버전 호출
    }

    /**
     * 사용자에게 알림 메시지를 표시하는 헬퍼 메서드입니다.
     * @param alertType 알림 타입 (INFORMATION, ERROR, WARNING 등)
     * @param title 알림 창의 제목
     * @param headerText 알림 창의 헤더 텍스트
     * @param contentText 알림 창의 내용 텍스트
     */
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait(); // 사용자가 확인 버튼을 누를 때까지 대기
    }
}