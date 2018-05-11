package worksheetout.ui;

import com.google.api.services.sheets.v4.Sheets;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import java.util.Properties;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import worksheetout.dao.FileUserDao;
import worksheetout.dao.SheetRoutineDao;
import worksheetout.dao.SheetWorkoutSessionDao;
import worksheetout.dao.SheetsServiceUtil;
import worksheetout.domain.DoneExercise;
import worksheetout.domain.Exercise;
import worksheetout.domain.Routine;
import worksheetout.domain.WorkoutSession;
import worksheetout.domain.User;
import worksheetout.domain.WorkoutService;

public class WorkoutUI extends Application {
    private WorkoutService workoutService;
    private Sheets sheetsService;
    private Scene routinesScene;
    private Scene sessionsScene;
    private Scene modifyRoutineScene;
    private Scene addSessionScene;
    private Scene newUserScene;
    private Scene loginScene;
    
    private VBox routineNodes;
    private VBox exerciseNodes;
    private Label menuLabel = new Label();
    
    @Override
    public void init() throws Exception {
        Properties properties = new Properties();
        
        File config = new File("config.properties");
        
        if(!config.exists()) {
            config.createNewFile();
            Path path = Paths.get("config.properties");
            Files.write(path, Arrays.asList("userFile=users.txt"), Charset.forName("UTF-8"));
        }

        properties.load(new FileInputStream("config.properties"));
        String userFile = properties.getProperty("userFile");
        FileUserDao userDao = new FileUserDao(userFile);
        
        try {
            setupSheetsService();
        } catch (Exception e) {
            System.out.println("Could not set up Sheets service. Error message: " + e);
        }

        SheetRoutineDao routineDao = new SheetRoutineDao(this.sheetsService);
        SheetWorkoutSessionDao workoutSessionDao = new SheetWorkoutSessionDao(this.sheetsService);
        this.workoutService = new WorkoutService(userDao, routineDao, workoutSessionDao);
    }
    
    /**
     * Setting up Google Sheets Service
     * @throws GeneralSecurityException
     * @throws IOException 
     */
    
    public void setupSheetsService() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }
    
    public Node oneRoutineNode(Routine routine, Stage primaryStage) {
        HBox box = new HBox(10);
        Label label  = new Label(routine.getName());
        label.setMinHeight(28);
        System.out.println("Routine in oneRoutineNode " + routine.getName());
        Button sessionButton = new Button("Add a session");
        sessionButton.setOnAction(e -> {
            primaryStage.setScene(this.addSessionScene);
        });
        
        Button routineButton = new Button("Add exercises");
        routineButton.setOnAction(e -> {
            modifyRoutineView(routine, primaryStage);
            primaryStage.setScene(this.modifyRoutineScene);
        });
                
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0,5,0,5));
        
        box.getChildren().addAll(label, spacer, routineButton, sessionButton);
        return box;
    }
    
    public void redrawRoutineList(Stage primaryStage) {
        if (workoutService.getLoggedUser() == null) {
            return;
        }
        routineNodes.getChildren().clear();

        List<Routine> routines = workoutService.getRoutines();
        routines.forEach(routine -> {
            routineNodes.getChildren().add(oneRoutineNode(routine, primaryStage));
        });
    }
    
    public Node oneExerciseNode(Exercise exercise) {
        HBox box = new HBox(20);
        Label exerciseName  = new Label(exercise.getName());
        exerciseName.setMinHeight(28);
        exerciseName.setWrapText(true);
        System.out.println("Exercise in oneExerciseNode " + exercise.getName());
         
        Label firstParameter  = new Label(exercise.getParameters().get(0));
        firstParameter.setMinHeight(28);
        firstParameter.setWrapText(true);
        
        Label secondParameter  = new Label(exercise.getParameters().get(1));
        secondParameter.setMinHeight(28);
        secondParameter.setWrapText(true);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0,5,0,5));
        
        box.getChildren().addAll(exerciseName, spacer, firstParameter, secondParameter);
        return box;
    }
    
    public void redrawExerciseList(Routine routine) {
        if (workoutService.getLoggedUser() == null) {
            return;
        }
        List<Exercise> exercisesOnSheet = workoutService.getExercisesOfRoutine(routine);
        List<Exercise> exercisesInRoutine = routine.getExercises();

        System.out.println("Exercises in redrawExerciseList: " + exercisesOnSheet);
        if ((exercisesOnSheet == null || exercisesOnSheet.isEmpty()) && exercisesInRoutine.isEmpty()) {
            return;
        } else if ((exercisesOnSheet == null || exercisesOnSheet.isEmpty()) && !exercisesInRoutine.isEmpty()) {
            exercisesOnSheet = new ArrayList<>();
            exercisesOnSheet.addAll(exercisesInRoutine);
        } else if (exercisesOnSheet != null && !exercisesOnSheet.containsAll(exercisesInRoutine)) {
            for (Exercise newExercise : exercisesInRoutine) {
                if (!exercisesOnSheet.contains(newExercise)) {
                    exercisesOnSheet.add(newExercise);
                }
            }
        }
        System.out.println("Exercises after adding possible new ones: " + exercisesOnSheet);
        exerciseNodes.getChildren().clear();
        
        exercisesOnSheet.forEach(exercise -> {
            exerciseNodes.getChildren().add(oneExerciseNode(exercise));
        });
    }
    
    public void modifyRoutineView(Routine routine, Stage primaryStage) {
        System.out.println("Starting modifyRoutineView");
        
        ScrollPane workoutScrollbar = new ScrollPane();       
        BorderPane mainPane = new BorderPane(workoutScrollbar);
        modifyRoutineScene = new Scene(mainPane, 800, 500);
                
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Label routineNameLabel = new Label(routine.getName());
        VBox menuLabelAndRoutineLabel = new VBox(2);
        menuLabelAndRoutineLabel.getChildren().addAll(menuLabel,routineNameLabel);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout");
        logoutButton.setPadding(new Insets(10));
        Button saveButton = new Button("Save");
        saveButton.setPadding(new Insets(10));
        Button backButton = new Button("Back");
        backButton.setPadding(new Insets(10));
        menuPane.getChildren().addAll(menuLabelAndRoutineLabel, menuSpacer, logoutButton, backButton, saveButton);
        
        logoutButton.setOnAction(e -> {
            workoutService.logout();
            primaryStage.setScene(loginScene);
        });
        saveButton.setOnAction(e -> {
            workoutService.routineToSheet(routine, workoutService.getLoggedUser().getSpreadsheetId());
        });
        backButton.setOnAction(e -> {
            workoutService.routineToSheet(routine, workoutService.getLoggedUser().getSpreadsheetId());
            primaryStage.setScene(routinesScene);
            redrawRoutineList(primaryStage);
        });
        
        VBox formAndLabel = new VBox(5);
        formAndLabel.setPadding(new Insets(10));
        Label exerciseCreationMessage = new Label();
        HBox addNewExerciseForm = new HBox(10);    
        Button createExerciseButton = new Button("Add new exercise");
        createExerciseButton.setPadding(new Insets(10));
        createExerciseButton.setWrapText(true);
        createExerciseButton.setPrefWidth(80);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
       
        exerciseNodes = new VBox(10);
        exerciseNodes.setMaxWidth(780);
        exerciseNodes.setMinWidth(780);
        exerciseNodes.setPadding(new Insets(10));
        redrawExerciseList(routine);
        
        VBox newExerciseNamePane = new VBox(10);
        TextField newExerciseNameInput = new TextField();
        Label newExerciseNameLabel = new Label("Exercise name");
        newExerciseNameLabel.setPrefWidth(80);
        newExerciseNameLabel.setWrapText(true);
        newExerciseNamePane.getChildren().addAll(newExerciseNameLabel, newExerciseNameInput);
     
        VBox firstParameterPane = new VBox(10);
        TextField firstParameterInput = new TextField();
        Label firstParameterLabel = new Label("first parameter (eg. kg, speed)");
        firstParameterLabel.setPrefWidth(110);
        firstParameterLabel.setWrapText(true);
        firstParameterPane.getChildren().addAll(firstParameterLabel, firstParameterInput);        
        
        VBox secondParameterPane = new VBox(10);
        TextField secondParameterInput = new TextField();
        Label secondParameterLabel = new Label("second parameter (eg. repetitions, minutes)");
        secondParameterLabel.setPrefWidth(150);
        secondParameterLabel.setWrapText(true);
        secondParameterPane.getChildren().addAll(secondParameterLabel, secondParameterInput);        

        addNewExerciseForm.getChildren().addAll(newExerciseNamePane, firstParameterPane, secondParameterPane, spacer, createExerciseButton);

        formAndLabel.getChildren().addAll(exerciseCreationMessage, addNewExerciseForm);
        workoutScrollbar.setContent(exerciseNodes);
        mainPane.setBottom(formAndLabel);
        mainPane.setTop(menuPane);
        
        createExerciseButton.setOnAction(e -> {
            String exerciseName = newExerciseNameInput.getText();
            String firstParameter = firstParameterInput.getText();
            String secondParameter = secondParameterInput.getText();
   
            if (exerciseName.length() < 2) {
                exerciseCreationMessage.setText("exercise name too short");
                exerciseCreationMessage.setTextFill(Color.RED);
            } else if (workoutService.addExerciseToRoutine(workoutService.createExercise(exerciseName, firstParameter, secondParameter), routine)) {
                exerciseCreationMessage.setText("new exercise created");
                exerciseCreationMessage.setTextFill(Color.GREEN);
                System.out.println("Routine after adding an exercise: " + routine);
                redrawExerciseList(routine);
                newExerciseNameInput.setText("");
            } else {
                exerciseCreationMessage.setText("the exercise takes a name and two parameters");
                exerciseCreationMessage.setTextFill(Color.RED);
            }
        });
        
    }
    
    @Override
    public void start(Stage primaryStage) {               
        // login scene
        
        VBox loginPane = new VBox(10);
        HBox inputPane = new HBox(10);
        loginPane.setPadding(new Insets(10));
        Label loginLabel = new Label("Username");
        TextField usernameInput = new TextField();
        
        inputPane.getChildren().addAll(loginLabel, usernameInput);
        
        Label loginMessage = new Label();
        Button loginButton = new Button("Login");
        loginButton.setPadding(new Insets(10));
        Button createButton = new Button("Create new user");
        createButton.setPadding(new Insets(10));
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            menuLabel.setText("Logged in: " + username);
            if (workoutService.login(username)){
                loginMessage.setText("");
                redrawRoutineList(primaryStage);
                primaryStage.setScene(routinesScene);  
                usernameInput.setText("");
            } else {
                loginMessage.setText("User does not exist");
                loginMessage.setTextFill(Color.RED);
            }      
        });  
        
        createButton.setOnAction(e -> {
            usernameInput.setText("");
            primaryStage.setScene(newUserScene);   
        });  
        
        loginPane.getChildren().addAll(loginMessage, inputPane, loginButton, createButton);       
        
        loginScene = new Scene(loginPane, 800, 550);    
   
        // new createNewUserScene
        
        VBox newUserPane = new VBox(10);
        
        HBox newUsernamePane = new HBox(10);
        newUsernamePane.setPadding(new Insets(10));
        TextField newUsernameInput = new TextField(); 
        Label newUsernameLabel = new Label("Username");
        newUsernameLabel.setPrefWidth(100);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUsernameInput);
     
        HBox spreadsheetIdPane = new HBox(10);
        spreadsheetIdPane.setPadding(new Insets(10));
        TextField spreadsheetIdInput = new TextField();
        Label newSpreadsheetIdLabel = new Label("Spreadsheet id");
        newSpreadsheetIdLabel.setPrefWidth(100);
        spreadsheetIdPane.getChildren().addAll(newSpreadsheetIdLabel, spreadsheetIdInput);        
        
        Label userCreationMessage = new Label();
        
        Button createNewUserButton = new Button("Create new user");
        createNewUserButton.setPadding(new Insets(10));

        createNewUserButton.setOnAction(e -> {
            String username = newUsernameInput.getText();
            String spreadsheetId = spreadsheetIdInput.getText();
   
            if (username.length() == 2 || spreadsheetId.length() < 2) {
                userCreationMessage.setText("username or name too short");
                userCreationMessage.setTextFill(Color.RED);
            } else if (workoutService.createUser(username, spreadsheetId)) {
                userCreationMessage.setText("");
                loginMessage.setText("new user created");
                loginMessage.setTextFill(Color.GREEN);               
                primaryStage.setScene(loginScene);
            } else {
                userCreationMessage.setText("username has to be unique");
                userCreationMessage.setTextFill(Color.RED);
            }
        });
        
        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane, spreadsheetIdPane, createNewUserButton); 
        newUserScene = new Scene(newUserPane, 800, 500);
        
        // main scene
        
        ScrollPane workoutScrollbar = new ScrollPane();       
        BorderPane mainPane = new BorderPane(workoutScrollbar);
        routinesScene = new Scene(mainPane, 800, 500);
                
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout"); 
        logoutButton.setPadding(new Insets(10));
        menuPane.getChildren().addAll(menuLabel, menuSpacer, logoutButton);
        
        logoutButton.setOnAction(e->{
            workoutService.logout();
            primaryStage.setScene(loginScene);
        });        
        
        HBox createRoutineForm = new HBox(10);
        createRoutineForm.setPadding(new Insets(10));
        Button createRoutineButton = new Button("Add new routine");
        createRoutineButton.setPadding(new Insets(10));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        TextField newRoutineInput = new TextField();
        createRoutineForm.getChildren().addAll(newRoutineInput, spacer, createRoutineButton);
        
        routineNodes = new VBox(10);
        routineNodes.setMaxWidth(780);
        routineNodes.setMinWidth(780);
        routineNodes.setPadding(new Insets(10));
        redrawRoutineList(primaryStage);
        
        workoutScrollbar.setContent(routineNodes);
        mainPane.setBottom(createRoutineForm);
        mainPane.setTop(menuPane);
        
        createRoutineButton.setOnAction(e -> {
            workoutService.createRoutine(newRoutineInput.getText());
            newRoutineInput.setText("");
            redrawRoutineList(primaryStage);
        });
        
        // seutp primary stage
        
        primaryStage.setTitle("worksheetout");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("closing");
            System.out.println(workoutService.getLoggedUser());
        });
    }

    @Override
    public void stop() {
      // tee lopetustoimenpiteet täällä
      System.out.println("the program is closing");
    }    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
