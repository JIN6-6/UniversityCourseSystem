package com.example.universitycoursesystemui.controller;

import com.example.universitycoursesystemui.Course;
import com.example.universitycoursesystemui.dao.CourseDAO;
import com.example.universitycoursesystemui.dao.UserDAO; // UserDAO import 추가
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * 새 강의를 추가하는 다이얼로그(팝업 창)의 컨트롤러입니다.
 * 관리자 대시보드에서 새로운 강의 정보를 입력하고 데이터베이스에 저장하는 기능을 담당합니다.
 */

public class AddCourseDialogController implements Initializable {

    // FXML에서 주입되는 UI 요소들
    @FXML private TextField courseIDField;      // 과목 코드 입력 필드
    @FXML private TextField courseNameField;    // 과목명 입력 필드
    @FXML private TextField professorIDField;   // 교수 ID 입력 필드
    @FXML private TextField creditField;        // 학점 입력 필드
    @FXML private TextField timeField;          // 시간 정보 입력 필드 (예: 월10-12, 수13-15)
    @FXML private TextField courseTypeField;    // 이수구분 입력 필드 (예: 전공필수, 전공선택, 교양)
    @FXML private TextField maxCapacityField;   // 최대 정원 입력 필드
    @FXML private TextField majorNameField;     // 과목 전공명 입력 필드 (예: 소프트웨어융합학과)

    // DAO(Data Access Object) 인스턴스
    private CourseDAO courseDAO; // Course 테이블 데이터 처리를 위한 DAO
    private UserDAO userDAO;     // User 테이블 데이터 처리를 위한 DAO (교수 이름 조회용)

    // 다이얼로그 스테이지 및 새로 추가될 강의 객체
    private Stage dialogStage; // 이 다이얼로그 창 자체의 Stage
    private Course newCourse;  // 새로 추가될 Course 객체

    /**
     * 컨트롤러 초기화 메서드. FXML 로드 시 자동으로 호출됩니다.
     * DAO 인스턴스를 초기화합니다.
     * @param url FXML 파일의 위치
     * @param resourceBundle 로컬라이제이션에 사용될 리소스 번들
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        courseDAO = new CourseDAO();
        userDAO = new UserDAO();
    }

    /**
     * 이 다이얼로그 창의 Stage를 설정합니다.
     * @param dialogStage 설정할 Stage 객체
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * 다이얼로그에서 새로 생성된 Course 객체를 반환합니다.
     * @return 새로 생성된 Course 객체 (취소 시 null)
     */
    public Course getNewCourse() {
        return newCourse;
    }

    /**
     * "저장" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 입력된 강의 정보를 검증하고 데이터베이스에 저장합니다.
     */
    @FXML
    private void handleSave() {
        // 입력 필드 유효성 검사
        if (!isInputValid()) {
            return; // 유효하지 않으면 저장하지 않고 종료
        }

        String professorName = null;
        try {
            // 입력된 교수 ID로 교수 이름을 데이터베이스에서 조회합니다.
            professorName = userDAO.getUserName(professorIDField.getText());
            if (professorName == null) {
                // 교수 ID가 존재하지 않으면 오류 메시지를 표시하고 종료합니다.
                showAlert(AlertType.ERROR, "입력 오류", "존재하지 않는 교수 ID입니다.", "유효한 교수 ID를 입력해주세요.");
                return;
            }
        } catch (SQLException e) {
            // 데이터베이스 조회 중 오류 발생 시
            showAlert(AlertType.ERROR, "DB 오류", "교수 정보 조회 중 오류 발생", e.getMessage());
            e.printStackTrace(); // 콘솔에 스택 트레이스 출력
            return;
        }

        try {
            // 모든 입력 필드로부터 값을 가져와 새로운 Course 객체를 생성합니다.
            // Course 생성자는 총 10개의 인자를 받습니다.
            newCourse = new Course(
                    courseIDField.getText(),        // 과목 코드
                    courseNameField.getText(),      // 과목명
                    professorIDField.getText(),     // 교수 ID
                    professorName,                  // 교수 이름 (조회된 값)
                    Integer.parseInt(creditField.getText()), // 학점 (문자열 -> 정수 변환)
                    timeField.getText(),            // 시간 정보
                    courseTypeField.getText(),      // 이수구분
                    Integer.parseInt(maxCapacityField.getText()), // 최대 정원 (문자열 -> 정수 변환)
                    0,                              // 현재 수강인원 초기값 (0)
                    0,                              // 대기인원 초기값 (0)
                    majorNameField.getText()        // 과목 전공명
            );

            // 생성된 Course 객체를 데이터베이스에 추가합니다.
            courseDAO.addCourse(newCourse);

            // 성공 메시지를 표시하고 다이얼로그 창을 닫습니다.
            showAlert(AlertType.INFORMATION, "성공", "강의가 성공적으로 추가되었습니다.", "강의 ID: " + newCourse.getCourseID());
            dialogStage.close();

        } catch (SQLException e) {
            // 데이터베이스 관련 예외 처리
            String errorMessage = e.getMessage().toLowerCase();
            if (errorMessage.contains("duplicate entry")) {
                // 이미 존재하는 강의 ID인 경우
                showAlert(AlertType.ERROR, "DB 오류", "이미 존재하는 강의 ID입니다.", "다른 강의 ID를 사용해주세요.");
            } else if (errorMessage.contains("foreign key constraint") || errorMessage.contains("cannot add or update a child row")) {
                // 외래 키 제약 조건 위반 (예: 존재하지 않는 교수 ID, 잘못된 과목 ID 등)
                showAlert(AlertType.ERROR, "DB 오류", "교수 ID 또는 과목 ID가 유효하지 않습니다.", "유효한 교수 ID와 과목 ID를 입력해주세요.");
            } else {
                // 그 외의 SQL 오류
                showAlert(AlertType.ERROR, "DB 오류", "강의 추가 중 오류가 발생했습니다.", e.getMessage());
            }
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
        } catch (NumberFormatException e) {
            // 숫자 변환 오류 (학점이나 정원에 숫자가 아닌 문자가 입력된 경우)
            showAlert(AlertType.ERROR, "입력 오류", "학점, 정원은 유효한 숫자여야 합니다.", e.getMessage());
        }
    }

    /**
     * "취소" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 새로운 강의 객체를 null로 설정하고 다이얼로그 창을 닫습니다.
     */
    @FXML
    private void handleCancel() {
        newCourse = null; // 새로운 강의 객체를 null로 설정하여 저장되지 않았음을 알립니다.
        dialogStage.close(); // 다이얼로그 창 닫기
    }

    /**
     * 입력 필드들의 유효성을 검사합니다.
     * 모든 필수 필드가 채워져 있고, 숫자 필드는 유효한 숫자인지 확인합니다.
     * @return 모든 입력이 유효하면 true, 그렇지 않으면 false를 반환합니다.
     */
    private boolean isInputValid() {
        String errorMessage = ""; // 오류 메시지를 누적할 변수

        // 각 필드의 유효성 검사 및 오류 메시지 추가
        if (courseIDField.getText() == null || courseIDField.getText().trim().isEmpty()) {
            errorMessage += "과목 코드를 입력해주세요!\n";
        }
        if (courseNameField.getText() == null || courseNameField.getText().trim().isEmpty()) {
            errorMessage += "과목명을 입력해주세요!\n";
        }
        if (professorIDField.getText() == null || professorIDField.getText().trim().isEmpty()) {
            errorMessage += "교수 ID를 입력해주세요!\n";
        }
        if (creditField.getText() == null || creditField.getText().trim().isEmpty()) {
            errorMessage += "학점을 입력해주세요!\n";
        } else if (!creditField.getText().trim().matches("\\d+")) { // 숫자인지 정규식으로 검사
            errorMessage += "학점은 숫자만 입력할 수 있습니다.\n";
        }
        if (timeField.getText() == null || timeField.getText().trim().isEmpty()) {
            errorMessage += "시간을 입력해주세요! (예: 월10-12, 수13-15)\n";
        }
        if (courseTypeField.getText() == null || courseTypeField.getText().trim().isEmpty()) {
            errorMessage += "이수구분을 입력해주세요! (예: 전공필수)\n";
        }
        if (maxCapacityField.getText() == null || maxCapacityField.getText().trim().isEmpty()) {
            errorMessage += "정원을 입력해주세요!\n";
        } else if (!maxCapacityField.getText().trim().matches("\\d+")) { // 숫자인지 정규식으로 검사
            errorMessage += "정원은 숫자만 입력할 수 있습니다.\n";
        }
        if (majorNameField.getText() == null || majorNameField.getText().trim().isEmpty()) {
            errorMessage += "과목 전공을 입력해주세요!\n";
        }

        // 오류 메시지가 비어있으면 모든 입력이 유효함
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // 오류 메시지가 있으면 경고창 표시
            showAlert(AlertType.ERROR, "입력 오류", "아래 항목들을 확인해주세요.", errorMessage);
            return false;
        }
    }

    /**
     * 사용자에게 알림 메시지를 표시하는 헬퍼 메서드입니다.
     * @param alertType 알림 타입 (INFORMATION, ERROR, WARNING 등)
     * @param title 알림 창의 제목
     * @param headerText 알림 창의 헤더 텍스트
     * @param contentText 알림 창의 내용 텍스트
     */
    private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait(); // 사용자가 확인 버튼을 누를 때까지 대기
    }
}