package com.example.universitycoursesystemui.controller;

import com.example.universitycoursesystemui.MainApplication;
import com.example.universitycoursesystemui.model.User; // User 모델 클래스 import 필요
import com.example.universitycoursesystemui.dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * 회원가입 화면의 컨트롤러입니다.
 * 새로운 사용자 계정을 생성하고 데이터베이스에 저장하는 기능을 담당합니다.
 * 사용자 유형(역할)과 학과/전공 정보도 함께 입력받습니다.
 */
public class SignUpController implements Initializable {

    // FXML에서 주입되는 UI 요소들
    @FXML private TextField userIDField;        // 사용자 ID 입력 필드
    @FXML private PasswordField passwordField;    // 비밀번호 입력 필드
    @FXML private PasswordField confirmPasswordField; // 비밀번호 확인 입력 필드
    @FXML private TextField nameField;          // 사용자 이름 입력 필드
    @FXML private ComboBox<String> roleComboBox; // 사용자 역할(유형) 선택 콤보박스
    @FXML private TextField majorField;         // 학과/전공 입력 필드 (users 테이블의 major 컬럼과 연결)

    // DAO(Data Access Object) 인스턴스
    private UserDAO userDAO; // 사용자 데이터 처리를 위한 DAO

    // 회원가입 다이얼로그 창의 Stage (LoginController에서 이 Stage를 설정해줌)
    private Stage dialogStage;

    /**
     * 컨트롤러 초기화 메서드. FXML 로드 시 자동으로 호출됩니다.
     * UserDAO 인스턴스를 초기화하고, 역할 콤보박스에 선택 항목들을 추가합니다.
     * @param url FXML 파일의 위치
     * @param resourceBundle 로컬라이제이션에 사용될 리소스 번들
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDAO = new UserDAO();
        // 콤보박스에 'student', 'professor', 'admin' 선택 항목 추가
        roleComboBox.setItems(FXCollections.observableArrayList("student", "professor", "admin"));
    }

    /**
     * 회원가입 다이얼로그 창의 Stage를 설정합니다.
     * 이 메서드는 LoginController에서 회원가입 다이얼로그를 띄울 때 호출됩니다.
     * @param dialogStage 회원가입 창의 Stage 객체
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * "가입하기" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 입력된 회원 정보를 검증하고 데이터베이스에 새로운 사용자를 추가합니다.
     * @param event ActionEvent (버튼 클릭 이벤트)
     */
    @FXML
    private void handleSignUp(ActionEvent event) {
        // 입력 필드 유효성 검사
        if (!isInputValid()) {
            return; // 유효하지 않으면 회원가입 처리 중단
        }

        // 입력 필드에서 값 가져오기
        String userID = userIDField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String userType = roleComboBox.getValue();
        String major = majorField.getText(); // 학과/전공 필드에서 값 가져오기

        try {
            // 사용자 ID 중복 확인
            if (userDAO.isUserExists(userID)) {
                showAlert(Alert.AlertType.ERROR, "가입 실패", "이미 사용 중인 아이디입니다.", "다른 아이디를 입력해주세요.");
                return;
            }

            // User 객체 생성 시 학과/전공(major) 값 포함
            User newUser = new User(userID, password, name, userType, major);
            userDAO.addUser(newUser); // 데이터베이스에 사용자 추가

            showAlert(Alert.AlertType.INFORMATION, "회원가입 성공", "회원가입이 완료되었습니다.", "이제 해당 아이디로 로그인할 수 있습니다.");

            // 회원가입 성공 후 창 닫기 또는 로그인 화면으로 이동
            if (dialogStage != null) {
                dialogStage.close(); // 다이얼로그 창인 경우 닫기
            } else {
                // MainApplication을 통해 로그인 화면으로 전환 (팝업이 아닌 메인 윈도우 전환 방식일 때)
                MainApplication.showLoginScreen();
            }

        } catch (SQLException e) {
            // 데이터베이스 관련 예외 처리
            showAlert(Alert.AlertType.ERROR, "데이터베이스 오류", "회원가입 중 오류가 발생했습니다.", e.getMessage());
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
        } catch (IOException e) {
            // 화면 전환 중 예외 처리 (MainApplication.showLoginScreen 호출 시 발생 가능)
            showAlert(Alert.AlertType.ERROR, "화면 로드 오류", "로그인 화면을 로드할 수 없습니다.", e.getMessage());
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
        }
    }

    /**
     * "취소" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 회원가입 창을 닫거나 로그인 화면으로 돌아갑니다.
     * @param event ActionEvent (버튼 클릭 이벤트)
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        // 취소 버튼 클릭 시 창 닫기 또는 로그인 화면으로 돌아가기
        if (dialogStage != null) {
            dialogStage.close(); // 다이얼로그 창인 경우 닫기
        } else {
            // MainApplication을 통해 로그인 화면으로 전환 (팝업이 아닌 메인 윈도우 전환 방식일 때)
            try {
                MainApplication.showLoginScreen();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "화면 로드 오류", "로그인 화면으로 돌아갈 수 없습니다.", e.getMessage());
                e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
            }
        }
    }

    /**
     * 입력 필드들의 유효성을 검사합니다.
     * 모든 필수 필드가 채워져 있고, 비밀번호 일치 여부 등을 확인합니다.
     * @return 모든 입력이 유효하면 true, 그렇지 않으면 false를 반환합니다.
     */
    private boolean isInputValid() {
        String errorMessage = ""; // 오류 메시지를 누적할 변수

        // 각 필드의 유효성 검사 및 오류 메시지 추가
        if (userIDField.getText() == null || userIDField.getText().trim().isEmpty()) {
            errorMessage += "아이디를 입력해주세요.\n";
        }
        if (passwordField.getText() == null || passwordField.getText().isEmpty()) {
            errorMessage += "비밀번호를 입력해주세요.\n";
        } else if (passwordField.getText().length() < 4) {
            errorMessage += "비밀번호는 4자 이상이어야 합니다.\n";
        }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errorMessage += "비밀번호가 일치하지 않습니다.\n";
        }
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "이름을 입력해주세요.\n";
        }
        if (roleComboBox.getValue() == null) {
            errorMessage += "역할을 선택해주세요.\n";
        }
        if (majorField.getText() == null || majorField.getText().trim().isEmpty()) { // 학과/전공 필드 유효성 검사
            errorMessage += "학과/전공을 입력해주세요.\n";
        }

        // 오류 메시지가 비어있으면 모든 입력이 유효함
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // 오류 메시지가 있으면 경고창 표시 (항상 4개 인자를 받는 showAlert 호출)
            showAlert(Alert.AlertType.ERROR, "입력 오류", "아래 항목을 확인해주세요.", errorMessage);
            return false;
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