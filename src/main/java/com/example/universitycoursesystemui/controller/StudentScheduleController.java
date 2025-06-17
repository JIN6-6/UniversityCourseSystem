package com.example.universitycoursesystemui.controller;

import com.example.universitycoursesystemui.Course;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn; // TableColumn 임포트
import javafx.scene.control.TableView;    // TableView 임포트
import javafx.scene.control.cell.PropertyValueFactory; // PropertyValueFactory 임포트
import javafx.stage.Stage; // Stage 임포트

import java.net.URL;
import java.util.ResourceBundle;

public class StudentScheduleController implements Initializable {

    @FXML private Label scheduleStudentIDLabel;
    @FXML private Label scheduleInfoLabel; // FXML에 있음
    @FXML private TableView<Course> scheduleTable; // StudentSchedule.fxml에 추가된 TableView fx:id
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> professorIDColumn; // DB 스키마에 맞춰 professorID
    @FXML private TableColumn<Course, Integer> creditColumn;     // DB 스키마에 맞춰 credit
    @FXML private TableColumn<Course, String> timeColumn;


    private String currentStudentID;
    // private ObservableList<Course> enrolledCourses; // 이 필드는 필요 없을 수 있음

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TableColumn과 Course 모델의 필드 매핑 (DB 컬럼명에 맞춰)
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        professorIDColumn.setCellValueFactory(new PropertyValueFactory<>("professorID"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        System.out.println("StudentScheduleController 초기화됨.");
    }

    public void setStudentAndCourses(String studentID, ObservableList<Course> courses) {
        this.currentStudentID = studentID;
        // this.enrolledCourses = courses; // 필드에 저장하지 않고 바로 사용
        scheduleStudentIDLabel.setText("학생 ID: " + studentID);
        scheduleInfoLabel.setText("총 " + courses.size() + "개 강의 수강 중.");
        scheduleTable.setItems(courses); // 전달받은 강의 목록을 테이블뷰에 설정
    }

    @FXML
    private void handleCloseSchedule() { // FXML의 onAction="#handleCloseSchedule"에 매핑
        Stage stage = (Stage) scheduleStudentIDLabel.getScene().getWindow(); // 어떤 FXML 요소든 가능
        stage.close();
    }
}