package com.example.universitycoursesystemui;

import com.example.universitycoursesystemui.controller.StudentDashboardController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

// MainApplication 클래스는 JavaFX 애플리케이션의 진입점입니다.
// Application 클래스를 상속받아 UI 초기화 및 화면 전환을 담당합니다.
public class MainApplication extends Application {

    // primaryStage는 애플리케이션의 메인 윈도우를 의미하며,
    // 여러 화면 간 전환 시 공통으로 사용하기 위해 static 변수로 선언합니다.
    private static Stage primaryStage;

    // JavaFX 애플리케이션 시작 시 호출되는 start() 메서드입니다.
    // 전달받은 Stage 객체를 primaryStage에 할당하고 프로그램 종료 시 complete 종료를 위해 설정합니다.
    // 초기 화면으로 로그인 화면을 표시합니다.
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        // 윈도우 창이 닫힐 때 System.exit(0)을 호출해 프로그램을 종료합니다.
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        showLoginScreen();
    }

    // 로그인 화면을 로드하고 primaryStage에 Scene을 설정하여 창에 화면을 표시하는 메서드입니다.
    public static void showLoginScreen() throws IOException {
        // Login.fxml 파일을 로드하여 UI 구성 요소를 가져옵니다.
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("/com/example/universitycoursesystemui/Login.fxml")
        );
        // 로드한 FXML 파일로부터 새로운 Scene 객체를 생성합니다.
        Scene scene = new Scene(fxmlLoader.load());
        // 생성된 Scene에 CSS 스타일을 적용합니다.
        applyStyles(scene);
        // 윈도우의 제목을 설정합니다.
        primaryStage.setTitle("수강신청 시스템 로그인");
        // Scene을 primaryStage에 설정한 후 화면을 표시합니다.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 학생 대시보드 화면을 로드하는 메서드입니다.
    // 로그인한 학생의 ID를 전달받아 화면에 반영합니다.
    public static void showStudentDashboard(String studentId) throws IOException {
        // StudentDashboard.fxml 파일을 로드합니다.
        FXMLLoader loader = new FXMLLoader(
                MainApplication.class.getResource("/com/example/universitycoursesystemui/StudentDashboard.fxml")
        );
        Parent root = loader.load();
        // FXML에 정의된 컨트롤러를 가져와 로그인된 학생 ID를 설정합니다.
        StudentDashboardController controller = loader.getController();
        controller.setLoggedInStudentID(studentId);
        // 로드한 FXML을 기반으로 Scene 객체를 생성하고 스타일을 적용합니다.
        Scene scene = new Scene(root);
        applyStyles(scene);
        // 생성된 Scene을 primaryStage에 설정하고 제목을 "학생 대시보드"로 변경합니다.
        primaryStage.setScene(scene);
        primaryStage.setTitle("학생 대시보드");
        // primaryStage.setFullScreen(true); // 전체화면 모드를 사용하려면 이 주석을 해제하세요.
    }

    // 회원가입 화면(SignUp 화면)을 로드하는 메서드입니다.
    public static void showSignUpScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("/com/example/universitycoursesystemui/SignUp.fxml")
        );
        // 회원가입 화면의 UI를 구성하는 Scene 객체를 생성합니다.
        Scene scene = new Scene(fxmlLoader.load());
        // 생성된 Scene에 CSS 스타일을 적용합니다.
        applyStyles(scene);
        // 윈도우의 제목을 "회원가입"으로 설정한 후 Scene을 할당합니다.
        primaryStage.setTitle("회원가입");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Scene에 CSS 스타일을 적용하는 도우미 메서드입니다.
    // style.css 파일이 존재하면 해당 스타일시트를 Scene에 추가하고,
    // 파일을 찾지 못하면 오류 메시지를 출력합니다.
    private static void applyStyles(Scene scene) {
        URL cssUrl = MainApplication.class.getResource("/com/example/universitycoursesystemui/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("경고: style.css 파일을 찾을 수 없습니다.");
        }
    }

    // main() 메서드는 애플리케이션을 실행하는 진입점(entry point)으로,
    // launch() 메서드를 호출하여 JavaFX 애플리케이션의 시작을 알립니다.
    public static void main(String[] args) {
        launch(args);
    }
}