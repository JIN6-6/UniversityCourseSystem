package com.example.universitycoursesystemui.controller;

import com.example.universitycoursesystemui.model.Course;
import com.example.universitycoursesystemui.model.EnrolledCourseInfo;
import com.example.universitycoursesystemui.model.Enrollment;
import com.example.universitycoursesystemui.MainApplication;
import com.example.universitycoursesystemui.dao.CourseDAO;
import com.example.universitycoursesystemui.dao.EnrollmentDAO;
import com.example.universitycoursesystemui.dao.WaitlistDAO;
import com.example.universitycoursesystemui.dao.CompletedCourseDAO; // CompletedCourseDAO import
import com.example.universitycoursesystemui.model.CompletedCourse; // CompletedCourse model import
import com.example.universitycoursesystemui.dao.UserDAO; // UserDAO import
import com.example.universitycoursesystemui.logic.CourseRecommender; // CourseRecommender import

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class StudentDashboardController implements Initializable {

    // FXML 변수들
    @FXML private Label studentIdLabel;
    @FXML private Label currentCreditsLabel;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> courseIDColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> professorIDColumn;
    @FXML private TableColumn<Course, Integer> creditColumn;
    @FXML private TableColumn<Course, String> timeColumn;
    @FXML private TableColumn<Course, String> courseTypeColumn; // FXML에 String으로 연결됨
    @FXML private TableColumn<Course, Integer> maxCapacityColumn;
    @FXML private TableColumn<Course, Integer> currentEnrollmentColumn;
    @FXML private TableColumn<Course, Integer> waitlistCountColumn;
    @FXML private TableView<EnrolledCourseInfo> myEnrollmentTable;
    @FXML private TableColumn<EnrolledCourseInfo, String> myCourseIDColumn;
    @FXML private TableColumn<EnrolledCourseInfo, String> myCourseNameColumn;
    @FXML private TableColumn<EnrolledCourseInfo, String> myProfessorIDColumn;
    @FXML private TableColumn<EnrolledCourseInfo, Integer> myCreditColumn;
    @FXML private TableColumn<EnrolledCourseInfo, String> myTimeColumn;
    @FXML private TableColumn<EnrolledCourseInfo, String> myCourseTypeColumn;
    @FXML private TableColumn<EnrolledCourseInfo, String> myStatusColumn;
    @FXML private GridPane scheduleGrid;

    // 졸업 요건 표시를 위한 FXML Label
    @FXML private Label majorMandatoryCreditsLabel;
    @FXML private Label majorTotalCreditsLabel;
    @FXML private Label totalCreditsLabel;


    // 멤버 변수들
    private Pane[][] schedulePanes = new Pane[7][6]; // [행][열] -> (시간 헤더 포함 총 7행) x (요일 헤더 포함 총 6열)
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    private WaitlistDAO waitlistDAO;
    private CompletedCourseDAO completedCourseDAO; // CompletedCourseDAO 추가
    private UserDAO userDAO; // UserDAO 추가
    private CourseRecommender courseRecommender; // CourseRecommender 추가
    private String loggedInStudentID;
    private ObservableList<Course> allCourses = FXCollections.observableArrayList();
    private ObservableList<EnrolledCourseInfo> myEnrolledCourses = FXCollections.observableArrayList();
    private Map<String, Color> courseColorMap = new HashMap<>();
    private List<Color> colorPalette = new ArrayList<>(List.of(
            Color.web("#FFAB91"), Color.web("#A5D6A7"), Color.web("#81D4FA"), Color.web("#CE93D8"),
            Color.web("#FFCC80"), Color.web("#80CBC4"), Color.web("#F48FB1"), Color.web("#B0BEC5")));
    private int currentColorIndex = 0;

    private final Map<String, Integer> dayMap = Map.of("월", 0, "화", 1, "수", 2, "목", 3, "금", 4);
    private final String[] times = {"1", "2", "3", "4", "5", "6"}; // 교시별 시간
    private final String[] days = {"월", "화", "수", "목", "금"}; // 요일

    private static final int MAX_CREDITS = 21; // 최대 수강 학점

    // 졸업 요건 기준 (기획안에서 제시된 값: 전공 필수 14학점, 전공 전체 42학점)
    private static final int REQUIRED_MAJOR_MANDATORY_CREDITS = 14; // 전공 필수
    private static final int REQUIRED_MAJOR_TOTAL_CREDITS = 42; // 전공 전체 (필수 + 선택)
    private static final int REQUIRED_TOTAL_CREDITS = 130; // 총 졸업 학점 (기획안에 없으므로 임시 값)
    private String studentMajor; // 학생의 전공을 저장할 필드 추가

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("컨트롤러 initialize() 호출됨."); // 디버그용 출력
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
        waitlistDAO = new WaitlistDAO();
        completedCourseDAO = new CompletedCourseDAO(); // CompletedCourseDAO 인스턴스화
        userDAO = new UserDAO(); // UserDAO 인스턴스화
        courseRecommender = new CourseRecommender(); // CourseRecommender 인스턴스화
        initializeCourseTable();
        initializeMyEnrollmentTable();
        initializeScheduleGrid(); // 시간표 그리드 초기화
    }

    private void initializeCourseTable() {
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        professorIDColumn.setCellValueFactory(new PropertyValueFactory<>("professorName"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        courseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("courseType")); // Changed to String
        maxCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        currentEnrollmentColumn.setCellValueFactory(new PropertyValueFactory<>("currentEnrollment"));
        waitlistCountColumn.setCellValueFactory(new PropertyValueFactory<>("waitlistCount"));

        courseTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                    return;
                }
                if (item.getCurrentEnrollment() >= item.getMaxCapacity()) {
                    setStyle("-fx-background-color: #FFF9C4;"); // 노란색 배경 (정원 초과)
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void initializeMyEnrollmentTable() {
        myCourseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        myCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        myProfessorIDColumn.setCellValueFactory(new PropertyValueFactory<>("professorName"));
        myCreditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        myTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        myCourseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("courseType"));
        myStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        myEnrollmentTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(EnrolledCourseInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                    return;
                }
                if ("대기 중".equals(item.getStatus())) {
                    setStyle("-fx-background-color: #ECEFF1;"); // 회색 배경 (대기 중)
                } else {
                    setStyle("");
                }
            }
        });
    }

    /**
     * 시간표 GridPane을 초기화하고 요일/시간 헤더 및 빈 셀들을 추가합니다.
     */
    private void initializeScheduleGrid() {
        scheduleGrid.getChildren().clear(); // 기존 자식들 모두 제거

        // 1. 요일 헤더 추가 (첫 번째 행, col 1부터 시작)
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: lighter; -fx-alignment: center; -fx-background-color: #f0f0f0; -fx-border-color: #e0e0e0;");
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            scheduleGrid.add(dayLabel, i + 1, 0); // i+1은 GridPane 열 인덱스 (0열은 시간 헤더)
        }

        // 2. 시간(교시) 헤더 추가 (첫 번째 열, row 1부터 시작)
        for (int i = 0; i < times.length; i++) {
            Label timeLabel = new Label(times[i]);
            timeLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: lighter; -fx-alignment: center; -fx-background-color: #f0f0f0; -fx-border-color: #e0e0e0;");
            timeLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            scheduleGrid.add(timeLabel, 0, i + 1); // i+1은 GridPane 행 인덱스 (0행은 요일 헤더)
        }

        // 3. 시간표의 빈 칸(셀) 채우기 및 schedulePanes 초기화
        for (int row = 1; row <= times.length; row++) {
            for (int col = 1; col <= days.length; col++) {
                Pane cell = new Pane();
                cell.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0.5;");
                cell.setPrefSize(15, 15); // FXML의 prefWidth/prefHeight와 일치하게 15로 설정
                scheduleGrid.add(cell, col, row);
                schedulePanes[row][col] = cell;
            }
        }
    }

    // 학생 ID 설정 및 초기 데이터 로딩
    public void setLoggedInStudentID(String studentID) {
        this.loggedInStudentID = studentID;
        studentIdLabel.setText(loggedInStudentID);
        // 학생 ID 설정 시 학생의 전공 정보도 로드
        try {
            this.studentMajor = userDAO.getUserMajor(loggedInStudentID);
            if (studentMajor == null) {
                showAlert(Alert.AlertType.WARNING, "경고", "전공 정보 없음", "해당 학생의 전공 정보가 없습니다. 졸업 요건 계산에 문제가 있을 수 있습니다.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "전공 정보 로딩 실패", e.getMessage());
            System.err.println("Failed to load student major: " + e.getMessage());
            e.printStackTrace();
        }
        handleRefresh(); // 로그인 후 데이터 로드 및 UI 업데이트
    }

    @FXML
    private void handleRefresh() {
        System.out.println("새로고침 버튼 클릭됨."); // 디버그용 출력
        loadCourses();
        loadMyEnrolledCourses();
        updateCurrentCredits();
        updateGraduationRequirements(); // 졸업 요건 업데이트 추가
        // AI 추천 로직 호출 및 콘솔 출력 (버튼 클릭 시로 이동)
    }

    // AI 추천 과목 버튼 클릭 핸들러
    @FXML
    private void handleShowRecommendations(ActionEvent event) { // <-- 이 메서드 추가 (onAction="#handleShowRecommendations"와 연결)
        System.out.println("AI 추천 과목 버튼 클릭됨.");
        if (loggedInStudentID == null || studentMajor == null) {
            showAlert(Alert.AlertType.WARNING, "추천 불가", "로그인 후 학생 정보를 로드해주세요.", null);
            return;
        }

        try {
            List<Course> recommendations = courseRecommender.getRecommendedCourses(loggedInStudentID, studentMajor);
            StringBuilder sb = new StringBuilder("추천 과목:\n\n");
            if (recommendations.isEmpty()) {
                sb.append("현재 추천할 과목이 없습니다.");
            } else {
                for (Course course : recommendations) {
                    sb.append("ID: ").append(course.getCourseID())
                            .append("\n이름: ").append(course.getCourseName())
                            .append("\n학점: ").append(course.getCredit())
                            .append("\n유형: ").append(course.getCourseType())
                            .append("\n전공: ").append(course.getMajorName())
                            .append("\n------------------\n");
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "AI 추천 과목", "나에게 맞는 추천 과목", sb.toString());

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "과목 추천 중 오류 발생", e.getMessage());
            System.err.println("과목 추천 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadCourses() {
        try {
            allCourses.setAll(courseDAO.getAllCourses());
            courseTable.setItems(allCourses);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "전체 강의 목록 로딩 실패", e.getMessage());
            System.err.println("loadCourses() SQLException: " + e.getMessage()); // 디버그 출력
            e.printStackTrace();
        }
    }

    private void loadMyEnrolledCourses() {
        if (loggedInStudentID == null || allCourses == null) return;
        List<EnrolledCourseInfo> myCombinedList = new ArrayList<>();
        try {
            List<String> enrolledIDs = enrollmentDAO.getEnrollmentsByStudentID(loggedInStudentID)
                    .stream().map(Enrollment::getCourseID).collect(Collectors.toList());
            List<String> waitlistedIDs = waitlistDAO.getWaitlistedCourseIdsByStudent(loggedInStudentID);
            for (Course course : allCourses) {
                if (enrolledIDs.contains(course.getCourseID())) {
                    myCombinedList.add(new EnrolledCourseInfo(course, "수강 중"));
                } else if (waitlistedIDs.contains(course.getCourseID())) {
                    myCombinedList.add(new EnrolledCourseInfo(course, "대기 중"));
                }
            }
            myEnrolledCourses.setAll(myCombinedList);
            myEnrollmentTable.setItems(myEnrolledCourses);
            updateScheduleGridUI(); // 시간표 UI 업데이트
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "내 강의 목록 로딩 실패", e.getMessage());
            System.err.println("loadMyEnrolledCourses() SQLException: " + e.getMessage()); // 디버그 출력
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEnrollCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "선택 오류", "수강할 강의를 선택해주세요.", null);
            return;
        }

        // 강의 정보를 DB에서 최신으로 다시 가져와서 사용
        Course latestCourseInfo;
        try {
            latestCourseInfo = courseDAO.getCourseById(selectedCourse.getCourseID());
            if (latestCourseInfo == null) {
                showAlert(Alert.AlertType.ERROR, "오류", "강의 정보를 찾을 수 없습니다.", null);
                return;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "강의 정보 로딩 중 오류 발생", e.getMessage());
            System.err.println("handleEnrollCourse() SQLException: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // --- 모든 조건 판단은 latestCourseInfo 기반으로 단일화 ---

        // 1. 이미 신청/대기 중인지 확인
        boolean isAlreadyRegistered = myEnrolledCourses.stream()
                .anyMatch(info -> info.getCourseID().equals(latestCourseInfo.getCourseID()));
        if (isAlreadyRegistered) {
            showAlert(Alert.AlertType.ERROR, "신청 불가", "이미 수강신청 또는 대기 중인 강의입니다.", null);
            return;
        }

        // 2. 학점 초과 확인
        int currentCredits = myEnrolledCourses.stream()
                .filter(info -> "수강 중".equals(info.getStatus()))
                .mapToInt(EnrolledCourseInfo::getCredit).sum();
        if (currentCredits + latestCourseInfo.getCredit() > MAX_CREDITS) {
            showAlert(Alert.AlertType.ERROR, "학점 초과", "최대 수강 가능 학점(" + MAX_CREDITS + "학점)을 초과합니다.", null);
            return;
        }

        // 3. 시간표 충돌 확인
        Optional<EnrolledCourseInfo> conflictingCourseOpt = findConflictingCourse(latestCourseInfo);
        if (conflictingCourseOpt.isPresent()) {
            handleConflict(latestCourseInfo, conflictingCourseOpt.get()); // 충돌 해결은 latestCourseInfo로
            return;
        }

        // 4. 정원 초과 판단 및 대기열/수강 신청 로직
        if (latestCourseInfo.getCurrentEnrollment() >= latestCourseInfo.getMaxCapacity()) {
            Optional<ButtonType> result = showConfirmation("정원 초과", "'" + latestCourseInfo.getCourseName() + "' 강의는 정원이 가득 찼습니다.", "대기열에 등록하시겠습니까?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                enrollToWaitlist(latestCourseInfo);
            } else {
                // 사용자가 대기열 등록을 취소한 경우
                showAlert(Alert.AlertType.INFORMATION, "취소", "수강 신청이 취소되었습니다.", null);
            }
        } else {
            enrollToCourse(latestCourseInfo);
        }
    }

    private void enrollToCourse(Course course) {
        try {
            enrollmentDAO.addEnrollment(loggedInStudentID, course.getCourseID());
            courseDAO.incrementCurrentEnrollment(course.getCourseID());
            showAlert(Alert.AlertType.INFORMATION, "성공", "수강신청이 완료되었습니다.", null);
            handleRefresh(); // 성공 시 UI 새로고침
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "수강신청 중 오류 발생", e.getMessage());
            System.err.println("enrollToCourse() SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enrollToWaitlist(Course course) {
        try {
            waitlistDAO.add(loggedInStudentID, course.getCourseID());
            courseDAO.incrementWaitlistCount(course.getCourseID());
            showAlert(Alert.AlertType.INFORMATION, "성공", "대기열에 성공적으로 등록되었습니다.", null);
            handleRefresh(); // 성공 시 UI 새로고침
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "대기열 등록 중 오류 발생", e.getMessage());
            System.err.println("enrollToWaitlist() SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleConflict(Course selectedCourse, EnrolledCourseInfo conflictingCourse) {
        String conflictReason = conflictingCourse.getCourseID().split("-")[0]
                .equals(selectedCourse.getCourseID().split("-")[0]) ? "동일 과목의 다른 분반" : "시간표 중복";

        Optional<ButtonType> result = showConfirmation("수강신청 불가",
                "'" + selectedCourse.getCourseName() + "' 강의는 " + conflictReason + "으로 인해 '" +
                        conflictingCourse.getCourseName() + "' 강의와 충돌합니다.",
                "기존 강의를 취소하고 이 강의로 교체하시겠습니까?");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            handleSwitchCourse(conflictingCourse, selectedCourse);
        }
    }

    private Optional<EnrolledCourseInfo> findConflictingCourse(Course newCourse) {
        List<Pair<Integer, Integer>> newCourseSlots = parseTimeString(newCourse.getTime());
        String newCourseBaseID = newCourse.getCourseID().split("-")[0];

        for (EnrolledCourseInfo enrolledInfo : myEnrolledCourses) {
            if ("수강 중".equals(enrolledInfo.getStatus())) {
                String enrolledCourseBaseID = enrolledInfo.getCourseID().split("-")[0];
                if (enrolledCourseBaseID.equals(newCourseBaseID)) {
                    return Optional.of(enrolledInfo);
                }

                List<Pair<Integer, Integer>> enrolledSlots = parseTimeString(enrolledInfo.getTime());
                for (Pair<Integer, Integer> newSlot : newCourseSlots) {
                    if (enrolledSlots.contains(newSlot)) {
                        return Optional.of(enrolledInfo);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private void handleSwitchCourse(EnrolledCourseInfo oldCourseInfo, Course newCourse) {
        try {
            enrollmentDAO.deleteEnrollment(loggedInStudentID, oldCourseInfo.getCourseID());
            courseDAO.decrementCurrentEnrollment(oldCourseInfo.getCourseID());

            processWaitlist(oldCourseInfo.getCourseID()); // 현원 감소 후 대기열 처리

            enrollmentDAO.addEnrollment(loggedInStudentID, newCourse.getCourseID());
            courseDAO.incrementCurrentEnrollment(newCourse.getCourseID());

            showAlert(Alert.AlertType.INFORMATION, "교체 완료", "성공적으로 과목이 교체되었습니다.", null);
            handleRefresh();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "교체 실패", "과목 교체 중 오류가 발생했습니다.", e.getMessage());
            System.err.println("handleSwitchCourse() SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleWithdrawCourse() {
        EnrolledCourseInfo selected = myEnrollmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "선택 오류", "취소할 강의를 선택해주세요.", null);
            return;
        }
        String courseID = selected.getCourseID();
        if ("대기 중".equals(selected.getStatus())) {
            try {
                waitlistDAO.remove(loggedInStudentID, courseID);
                courseDAO.decrementWaitlistCount(courseID);
                showAlert(Alert.AlertType.INFORMATION, "성공", "대기열에서 정상적으로 삭제되었습니다.", null);
                handleRefresh();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "오류", "대기열 취소 중 오류 발생", e.getMessage());
                System.err.println("handleWithdrawCourse() Waitlist SQLException: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        // 수강 중인 강의 취소 로직
        try {
            enrollmentDAO.deleteEnrollment(loggedInStudentID, courseID);
            courseDAO.decrementCurrentEnrollment(courseID);
            showAlert(Alert.AlertType.INFORMATION, "성공", "수강이 정상적으로 취소되었습니다.", null);

            processWaitlist(courseID); // 현원 감소 후 대기열 처리

            handleRefresh();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "수강 취소 중 오류 발생", e.getMessage());
            System.err.println("handleWithdrawCourse() Enrollment SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processWaitlist(String courseId) throws SQLException {
        Optional<String> nextStudentIdOpt = waitlistDAO.findNextStudent(courseId);
        if (nextStudentIdOpt.isPresent()) {
            String nextStudentId = nextStudentIdOpt.get();
            enrollmentDAO.addEnrollment(nextStudentId, courseId);
            courseDAO.incrementCurrentEnrollment(courseId);
            waitlistDAO.remove(nextStudentId, courseId);
            courseDAO.decrementWaitlistCount(courseId);
            System.out.println("대기자 " + nextStudentId + "가 " + courseId + " 과목에 자동 등록되었습니다.");
        }
    }

    private void updateCurrentCredits() {
        if (myEnrolledCourses == null) {
            currentCreditsLabel.setText("0 / " + MAX_CREDITS);
            return;
        }
        int totalCredits = myEnrolledCourses.stream()
                .filter(info -> "수강 중".equals(info.getStatus()))
                .mapToInt(EnrolledCourseInfo::getCredit)
                .sum();
        currentCreditsLabel.setText(totalCredits + " / " + MAX_CREDITS);
    }

    /**
     * 졸업 요건 현황을 계산하고 UI를 업데이트합니다.
     */
    private void updateGraduationRequirements() {
        if (loggedInStudentID == null) { // 학생 ID가 없으면 기본값으로 초기화
            majorMandatoryCreditsLabel.setText("0/" + REQUIRED_MAJOR_MANDATORY_CREDITS);
            majorTotalCreditsLabel.setText("0/" + REQUIRED_MAJOR_TOTAL_CREDITS);
            totalCreditsLabel.setText("0/" + REQUIRED_TOTAL_CREDITS);
            return;
        }
        // studentMajor가 null이면 N/A로 표시 (전공 정보가 없으므로 정확한 계산 불가)
        if (studentMajor == null) {
            majorMandatoryCreditsLabel.setText("N/A/" + REQUIRED_MAJOR_MANDATORY_CREDITS);
            majorTotalCreditsLabel.setText("N/A/" + REQUIRED_MAJOR_TOTAL_CREDITS);
            totalCreditsLabel.setText("N/A/" + REQUIRED_TOTAL_CREDITS);
            return;
        }

        int currentMajorMandatoryCredits = 0;
        int currentMajorTotalCredits = 0;
        int currentTotalCredits = 0;

        try {
            // 1. 이미 이수한 과목 학점 가져오기 (CompletedCourseDAO 사용)
            List<CompletedCourse> completedCourses = completedCourseDAO.getCompletedCoursesByStudentId(loggedInStudentID);
            for (CompletedCourse course : completedCourses) {
                // CourseDAO에서 과목의 상세 정보(특히 majorName)를 다시 가져옴
                Course fullCourseInfo = courseDAO.getCourseById(course.getCourseId());
                // 내 전공 과목인 경우에만 집계
                if (fullCourseInfo != null && studentMajor.equals(fullCourseInfo.getMajorName())) {
                    currentTotalCredits += course.getCredit();
                    if ("전공필수".equals(course.getCourseType())) {
                        currentMajorMandatoryCredits += course.getCredit();
                        currentMajorTotalCredits += course.getCredit(); // 전공 전체에도 포함
                    } else if ("전공선택".equals(course.getCourseType())) {
                        currentMajorTotalCredits += course.getCredit();
                    }
                }
                // TODO: '교양' 학점도 있다면 여기에 추가 로직 (CourseType에 따라 분기)
                // else if ("교양".equals(course.getCourseType())) {
                //     currentTotalCredits += course.getCredit();
                // }
            }

            // 2. 현재 수강 중인 과목 학점 합산 (myEnrolledCourses에서 '수강 중'인 과목만)
            for (EnrolledCourseInfo courseInfo : myEnrolledCourses) {
                if ("수강 중".equals(courseInfo.getStatus())) {
                    // CourseDAO에서 과목의 상세 정보(특히 majorName)를 다시 가져옴
                    Course fullCourseInfo = courseDAO.getCourseById(courseInfo.getCourseID());
                    // 내 전공 과목인 경우에만 집계
                    if (fullCourseInfo != null && studentMajor.equals(fullCourseInfo.getMajorName())) {
                        currentTotalCredits += courseInfo.getCredit();
                        if ("전공필수".equals(courseInfo.getCourseType())) {
                            currentMajorMandatoryCredits += courseInfo.getCredit();
                            currentMajorTotalCredits += courseInfo.getCredit();
                        } else if ("전공선택".equals(courseInfo.getCourseType())) {
                            currentMajorTotalCredits += courseInfo.getCredit();
                        }
                    }
                    // TODO: '교양' 학점도 있다면 여기에 추가 로직 (CourseType에 따라 분기)
                    // else if ("교양".equals(courseInfo.getCourseType())) {
                    //     currentTotalCredits += courseInfo.getCredit();
                    // }
                }
            }

            // UI Label 업데이트
            majorMandatoryCreditsLabel.setText(currentMajorMandatoryCredits + "/" + REQUIRED_MAJOR_MANDATORY_CREDITS);
            majorTotalCreditsLabel.setText(currentMajorTotalCredits + "/" + REQUIRED_MAJOR_TOTAL_CREDITS);
            totalCreditsLabel.setText(currentTotalCredits + "/" + REQUIRED_TOTAL_CREDITS);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "오류", "졸업 요건 로딩 실패", e.getMessage());
            System.err.println("updateGraduationRequirements() SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 수강 신청/취소 시 시간표 UI를 업데이트합니다.
     */
    private void updateScheduleGridUI() {
        // 모든 셀 초기화 (배경색 흰색, 테두리 회색)
        for (int row = 1; row <= times.length; row++) {
            for (int col = 1; col <= days.length; col++) {
                if (schedulePanes[row][col] != null) {
                    schedulePanes[row][col].setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0.5;");
                    // 기존에 추가된 자식 노드가 있다면 제거 (강의 정보가 더 이상 표시되지 않으므로)
                    schedulePanes[row][col].getChildren().clear();
                }
            }
        }

        if (myEnrolledCourses == null) return;

        myEnrolledCourses.stream()
                .filter(info -> "수강 중".equals(info.getStatus()))
                .forEach(info -> {
                    Color courseColor = getCourseColor(info.getCourseID());
                    List<Pair<Integer, Integer>> timeSlots = parseTimeString(info.getTime());
                    for (Pair<Integer, Integer> slot : timeSlots) {
                        int periodRowIndex = slot.getKey(); // 교시 (1부터 시작)
                        int dayColumnIndex = slot.getValue(); // parseTimeString에서 이미 +1 된 값 (1부터 시작하는 GridPane 열 인덱스)

                        // 배열 인덱스 유효성 검사 (0번 인덱스는 헤더용이므로 1부터 시작)
                        // dayColumnIndex는 이미 1부터 시작하는 GridPane 열 인덱스이므로, days.length로 비교
                        if (periodRowIndex >= 1 && periodRowIndex <= times.length &&
                                dayColumnIndex >= 1 && dayColumnIndex <= days.length) {

                            // 해당 셀의 Pane 가져오기
                            // parseTimeString에서 이미 +1 되어 있으므로, 여기서 다시 +1 하지 않습니다.
                            Pane cellPane = schedulePanes[periodRowIndex][dayColumnIndex];
                            if (cellPane != null) {
                                // 배경색 설정 (강의 이름은 표시하지 않으므로 배경색만 변경)
                                cellPane.setStyle("-fx-background-color: " + toHexString(courseColor) + "; -fx-border-color: #cccccc; -fx-border-width: 0.5;");
                                // 강의 이름을 표시하지 않으므로, 이 셀에는 추가적인 Label을 넣지 않습니다.
                            }
                        }
                    }
                });
    }

    private Color getCourseColor(String courseID) {
        if (!courseColorMap.containsKey(courseID)) {
            Color newColor = colorPalette.get(currentColorIndex % colorPalette.size());
            courseColorMap.put(courseID, newColor);
            currentColorIndex++;
        }
        return courseColorMap.get(courseID);
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    private List<Pair<Integer, Integer>> parseTimeString(String timeString) {
        List<Pair<Integer, Integer>> timeSlots = new ArrayList<>();
        if (timeString == null || timeString.isEmpty()) return timeSlots;
        String[] parts = timeString.split("[,/]");
        for (String part : parts) {
            part = part.trim();
            if (part.length() < 2) continue;
            String dayStr = part.substring(0, 1);
            String periodStr = part.substring(1);
            Integer dayIndex = dayMap.get(dayStr); // dayMap에서 0-indexed 요일 인덱스 가져옴
            if (dayIndex == null) continue;

            // finalDayColumn을 parseTimeString 내에서 바로 계산하여 Pair에 담습니다.
            // GridPane의 1부터 시작하는 열 인덱스로 변환
            int finalDayColumn = dayIndex + 1; // 0부터 시작하는 dayIndex를 1부터 시작하는 GridPane 컬럼 인덱스로 변환

            try {
                if (periodStr.contains("-") || periodStr.contains("~")) {
                    String[] periodRange = periodStr.split("[-~]");
                    int startPeriod = Integer.parseInt(periodRange[0]);
                    int endPeriod = Integer.parseInt(periodRange[1]);
                    for (int period = startPeriod; period <= endPeriod; period++) {
                        timeSlots.add(new Pair<>(period, finalDayColumn));
                    }
                } else {
                    int singlePeriod = Integer.parseInt(periodStr);
                    timeSlots.add(new Pair<>(singlePeriod, finalDayColumn));
                }
            } catch (NumberFormatException e) {
                // Ignore parsing errors
            }
        }
        return timeSlots;
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmation(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    @FXML
    private void handleSearch() { /* Placeholder */ }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            MainApplication.showLoginScreen();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "로그아웃 오류", "로그아웃 중 오류 발생", e.getMessage());
        }
    }
}