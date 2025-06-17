package com.example.universitycoursesystemui.controller;

import com.example.universitycoursesystemui.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 관리자 대시보드 화면의 컨트롤러입니다.
 * 관리자 로그인 후 표시되는 메인 화면으로,
 * 관리자 ID 표시, 로그아웃 등의 기본 기능을 제공합니다.
 * 향후 강의 추가/수정/삭제 등의 관리자 기능이 추가될 수 있습니다.
 */
public class AdminDashboardController implements Initializable {

    // FXML에서 주입되는 UI 요소: 관리자 ID를 표시할 Label
    @FXML
    private Label adminIdLabel;

    // 현재 로그인한 관리자의 ID를 저장하는 필드
    private String loggedInAdminID;

    /**
     * 컨트롤러 초기화 메서드. FXML 로드 시 자동으로 호출됩니다.
     * @param url FXML 파일의 위치
     * @param resourceBundle 로컬라이제이션에 사용될 리소스 번들
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 관리자 대시보드 초기화 시 필요한 로직을 여기에 추가합니다.
        // 현재는 디버그 메시지 외에 특별한 초기화 작업이 없습니다.
        // System.out.println("AdminDashboardController 초기화됨."); // 디버그용 출력 제거
    }

    /**
     * 로그인한 관리자의 ID를 설정하고, 해당 ID를 UI에 표시합니다.
     * LoginController에서 관리자 로그인 성공 시 호출됩니다.
     * @param adminID 로그인한 관리자의 ID
     */
    public void setLoggedInAdminID(String adminID) {
        this.loggedInAdminID = adminID;
        adminIdLabel.setText(loggedInAdminID); // UI Label에 관리자 ID 표시
    }

    /**
     * "로그아웃" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 애플리케이션을 로그인 화면으로 되돌립니다.
     * @param event ActionEvent (버튼 클릭 이벤트)
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // MainApplication을 통해 로그인 화면으로 전환합니다.
            MainApplication.showLoginScreen();
        } catch (IOException e) {
            // 화면 로딩 중 오류 발생 시 경고창 표시
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
            showAlert(Alert.AlertType.ERROR, "로그아웃 오류", "로그인 화면으로 돌아가는 중 오류가 발생했습니다.", e.getMessage());
        }
    }

    // --- 관리자 기능 추가를 위한 주석 (향후 개발 시 활용) ---
    // 여기에 강의 추가/수정/삭제 등의 관리자 기능을 위한 핸들러 메소드들을 추가할 수 있습니다.
    // 예를 들어, '강의 추가' 버튼 클릭 시 새 다이얼로그를 띄우는 메서드:
    // @FXML
    // private void handleShowAddCourseDialog() {
    //     try {
    //         // AddCourseDialog.fxml을 로드하여 새 Stage에 띄웁니다.
    //         FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/com/example/universitycoursesystemui/AddCourseDialog.fxml"));
    //         Parent root = loader.load();
    //
    //         // AddCourseDialogController에 Stage 정보 전달 (필요 시)
    //         AddCourseDialogController controller = loader.getController();
    //         Stage dialogStage = new Stage();
    //         dialogStage.setTitle("새 강의 추가");
    //         dialogStage.initModality(Modality.WINDOW_MODAL); // 부모 창 위에 모달로 띄움
    //         dialogStage.initOwner(adminIdLabel.getScene().getWindow()); // 부모 창 설정
    //         Scene scene = new Scene(root);
    //         controller.setDialogStage(dialogStage);
    //         dialogStage.setScene(scene);
    //         dialogStage.showAndWait(); // 다이얼로그가 닫힐 때까지 대기
    //
    //         // 다이얼로그가 닫힌 후 처리할 로직 (예: 추가된 강의로 테이블 새로고침)
    //         // if (controller.getNewCourse() != null) {
    //         //    showAlert(Alert.AlertType.INFORMATION, "알림", "새 강의가 추가되었습니다.", controller.getNewCourse().getCourseName());
    //         //    handleRefreshCourseList(); // 강의 목록 새로고침 메서드 호출
    //         // }
    //
    //     } catch (IOException e) {
    //         showAlert(Alert.AlertType.ERROR, "오류", "강의 추가 다이얼로그 로딩 실패", e.getMessage());
    //         e.printStackTrace();
    //     }
    // }
    // --- 관리자 기능 추가 주석 끝 ---

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