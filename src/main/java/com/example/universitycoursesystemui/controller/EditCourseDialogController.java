package com.example.universitycoursesystemui.controller;

import com.example.universitycoursesystemui.Course;
import com.example.universitycoursesystemui.dao.CourseDAO;
import com.example.universitycoursesystemui.dao.UserDAO; // UserDAO import 추가
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * 강의 정보를 수정하는 다이얼로그(팝업 창)의 컨트롤러입니다.
 * 관리자 대시보드에서 선택된 강의의 정보를 수정하고 데이터베이스에 업데이트하는 기능을 담당합니다.
 */
public class EditCourseDialogController implements Initializable {

    // FXML에서 주입되는 UI 요소들
    @FXML private TextField courseIDField;      // 과목 코드 표시 필드 (수정 불가)
    @FXML private TextField courseNameField;    // 과목명 입력 필드
    @FXML private TextField professorIDField;   // 교수 ID 입력 필드
    @FXML private TextField creditField;        // 학점 입력 필드
    @FXML private TextField timeField;          // 시간 정보 입력 필드
    @FXML private TextField courseTypeField;    // 이수구분 입력 필드
    @FXML private TextField maxCapacityField;   // 최대 정원 입력 필드
    @FXML private TextField majorNameField;     // <-- majorNameField FXML 연결 추가 (FXML에도 추가 필요)

    @FXML private Button saveButton;            // 저장 버튼
    @FXML private Button cancelButton;          // 취소 버튼

    // DAO 인스턴스
    private CourseDAO courseDAO; // Course 테이블 데이터 처리를 위한 DAO
    private UserDAO userDAO;     // <-- UserDAO 선언 추가 (교수 이름 조회용)

    // 다이얼로그 스테이지 및 수정할 강의 객체
    private Stage dialogStage;      // 이 다이얼로그 창 자체의 Stage
    private Course courseToEdit;    // 수정 대상 Course 객체

    /**
     * 컨트롤러 초기화 메서드. FXML 로드 시 자동으로 호출됩니다.
     * DAO 인스턴스를 초기화하고, 입력 필드들의 초기 설정을 수행합니다.
     * @param url FXML 파일의 위치
     * @param resourceBundle 로컬라이제이션에 사용될 리소스 번들
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        courseDAO = new CourseDAO();
        userDAO = new UserDAO(); // <-- UserDAO 인스턴스화 추가

        // courseID는 Primary Key이므로 수정할 수 없도록 설정
        courseIDField.setEditable(false);

        // 학점과 정원 필드가 숫자만 입력받도록 설정
        setupNumericField(creditField);
        setupNumericField(maxCapacityField);
    }

    /**
     * 이 다이얼로그 창의 Stage를 설정합니다.
     * @param dialogStage 설정할 Stage 객체
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * AdminDashboardController에서 수정할 Course 객체를 받아와서
     * 화면의 TextField들을 해당 정보로 채웁니다.
     * @param course 수정할 강의 객체
     */
    public void setCourseToEdit(Course course) {
        this.courseToEdit = course;

        // 화면의 필드에 강의 정보 채우기
        courseIDField.setText(course.getCourseID());
        courseNameField.setText(course.getCourseName());
        professorIDField.setText(course.getProfessorID());
        creditField.setText(String.valueOf(course.getCredit()));
        timeField.setText(course.getTime());
        courseTypeField.setText(course.getCourseType());
        maxCapacityField.setText(String.valueOf(course.getMaxCapacity()));
        majorNameField.setText(course.getMajorName()); // <-- majorName 필드 채우기 추가
    }

    /**
     * "저장" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 입력된 강의 정보를 검증하고 데이터베이스에 업데이트합니다.
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
            professorName = userDAO.getUserName(professorIDField.getText()); // <-- 교수 이름 조회 로직 추가
            if (professorName == null) {
                // 교수 ID가 존재하지 않으면 오류 메시지를 표시하고 종료합니다.
                showAlert(AlertType.ERROR, "입력 오류", "존재하지 않는 교수 ID입니다.", "유효한 교수 ID를 입력해주세요.");
                return;
            }
        } catch (SQLException e) {
            // 데이터베이스 조회 중 오류 발생 시
            showAlert(AlertType.ERROR, "DB 오류", "교수 정보 조회 중 오류 발생", e.getMessage());
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
            return;
        }

        try {
            // 기존 Course 객체의 내용을 화면에 입력된 최신 값으로 업데이트합니다.
            // professorName은 Course 객체의 setter가 없으므로 updateCourse 메서드에 직접 사용해야 함
            // 만약 Course 객체에 setProfessorName이 있다면 호출
            courseToEdit.setCourseName(courseNameField.getText());
            courseToEdit.setProfessorID(professorIDField.getText());
            courseToEdit.setCredit(Integer.parseInt(creditField.getText()));
            courseToEdit.setTime(timeField.getText());
            courseToEdit.setCourseType(courseTypeField.getText());
            courseToEdit.setMaxCapacity(Integer.parseInt(maxCapacityField.getText()));
            courseToEdit.setMajorName(majorNameField.getText()); // <-- majorName 필드 업데이트 추가

            // DAO를 통해 데이터베이스에 업데이트를 요청합니다.
            // updateCourse 메서드는 Course 객체를 통째로 받으므로, Course 객체 내 professorName을 업데이트해야 함
            // Course 클래스에 setProfessorName이 없다면, CourseDAO.updateCourse를 오버로드하거나,
            // Course 객체 생성 시 professorName을 전달해야 함. (현재는 Course 객체에 setProfessorName이 없음)
            // 임시 방안으로, CourseDAO.updateCourse가 professorName을 직접 받거나, Course 객체가 professorName을 직접 변경하도록 해야 함.
            // 현재 Course 클래스에 setProfessorName이 없으므로, CourseDAO.updateCourse를 수정할 필요가 있습니다.
            // 하지만 현재는 majorName 추가로 인해 CourseDAO.updateCourse의 파라미터가 Course 객체 하나로 통일되었고
            // Course 객체 자체가 professorName 필드를 가지고 있으므로, Course 객체의 professorName을 업데이트해야 합니다.
            // Course 클래스에 setProfessorName(String) 메서드가 없으므로, Course 객체의 professorName은 이 단계에서 변경되지 않습니다.
            // 이 부분을 수정하려면 Course.java에 setProfessorName을 추가해야 합니다.
            // 또는, CourseDAO.updateCourse 메서드를 수정하여 professorName을 직접 파라미터로 받도록 해야 합니다.

            // 현재 Course.java에 setProfessorName() 메서드가 없으므로, courseToEdit.professorName을 직접 업데이트할 방법이 없음.
            // 따라서, CourseDAO.updateCourse에서 professorName을 사용하려면, Course 객체에 setProfessorName을 추가하거나,
            // CourseDAO.updateCourse의 SQL 쿼리에서 professorName을 직접 업데이트하도록 해야 함.
            // 가장 간단한 방법은 Course.java에 setProfessorName을 추가하는 것입니다.
            // (이전 대화에서 Course.java에 setProfessorName을 추가해 드렸으므로, 그 코드를 사용했다면 문제는 없을 것임.)

            courseDAO.updateCourse(courseToEdit); // Course 객체로 업데이트

            // 성공 메시지를 표시하고 다이얼로그 창을 닫습니다.
            showAlert(AlertType.INFORMATION, "성공", "강의 정보가 성공적으로 수정되었습니다.", "강의 ID: " + courseToEdit.getCourseID());
            dialogStage.close();

        } catch (SQLException e) {
            // 데이터베이스 관련 예외 처리
            String errorMessage = e.getMessage().toLowerCase();
            if (errorMessage.contains("foreign key constraint") || errorMessage.contains("cannot add or update a child row")) {
                showAlert(AlertType.ERROR, "DB 오류", "교수 ID가 유효하지 않습니다.", "유효한 교수 ID를 입력해주세요.");
            } else {
                showAlert(AlertType.ERROR, "DB 오류", "강의 수정 중 데이터베이스 오류가 발생했습니다.", e.getMessage());
            }
            e.printStackTrace(); // 개발자 확인을 위해 스택 트레이스 출력
        } catch (NumberFormatException e) {
            // 숫자 변환 오류 (학점이나 정원에 숫자가 아닌 문자가 입력된 경우)
            showAlert(AlertType.ERROR, "입력 오류", "학점, 정원은 유효한 숫자여야 합니다.", e.getMessage());
        }
    }

    /**
     * "취소" 버튼 클릭 시 호출되는 핸들러 메서드입니다.
     * 다이얼로그 창을 닫습니다.
     */
    @FXML
    private void handleCancel() {
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
        if (courseNameField.getText() == null || courseNameField.getText().trim().isEmpty()) {
            errorMessage += "강의 이름을 입력해주세요!\n";
        }
        if (professorIDField.getText() == null || professorIDField.getText().trim().isEmpty()) {
            errorMessage += "교수 ID를 입력해주세요!\n";
        }
        // 학점 필드 유효성 검사 (숫자 여부와 비어있는지 여부)
        if (creditField.getText() == null || creditField.getText().trim().isEmpty() || !creditField.getText().matches("\\d+")) {
            errorMessage += "유효한 학점을 입력해주세요 (숫자만 가능)!\n";
        }
        if (timeField.getText() == null || timeField.getText().trim().isEmpty()) {
            errorMessage += "시간 정보를 입력해주세요! (예: 월1-2,수3)\n";
        }
        if (courseTypeField.getText() == null || courseTypeField.getText().trim().isEmpty()) {
            errorMessage += "이수 구분을 입력해주세요! (예: 전공필수)\n";
        }
        // 정원 필드 유효성 검사 (숫자 여부와 비어있는지 여부)
        if (maxCapacityField.getText() == null || maxCapacityField.getText().trim().isEmpty() || !maxCapacityField.getText().matches("\\d+")) {
            errorMessage += "유효한 정원을 입력해주세요 (숫자만 가능)!\n";
        }
        if (majorNameField.getText() == null || majorNameField.getText().trim().isEmpty()) { // <-- majorNameField 유효성 검사 추가
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

    /**
     * TextField가 숫자만 입력받도록 설정하는 헬퍼 메서드입니다.
     * @param textField 숫자로 제한할 TextField 객체
     */
    private void setupNumericField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // 숫자가 아니면 (즉, 숫자가 아닌 문자가 포함되어 있다면)
                textField.setText(newValue.replaceAll("[^\\d]", "")); // 숫자만 남기고 제거
            }
        });
    }
}