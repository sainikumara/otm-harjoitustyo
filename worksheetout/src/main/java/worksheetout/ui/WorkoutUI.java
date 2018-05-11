package worksheetout.ui;

import com.google.api.services.sheets.v4.Sheets;
import java.io.FileInputStream;
import java.io.File;
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
import java.util.Arrays;

import worksheetout.dao.FileUserDao;
import worksheetout.dao.SheetRoutineDao;
import worksheetout.dao.SheetWorkoutSessionDao;
import worksheetout.dao.SheetsServiceUtil;
import worksheetout.domain.DoneExercise;
import worksheetout.domain.Exercise;
import worksheetout.domain.Routine;
import worksheetout.domain.WorkoutSession;
import worksheetout.domain.WorkoutService;

public class WorkoutUI extends Application {
    private WorkoutService workoutService;
    private Sheets sheetsService;
    
    private Scene routinesScene;
    private Scene sessionsScene;
    private Scene addSessionScene;
    private Scene modifyRoutineScene;
    private Scene oneSessionScene;
    private Scene newUserScene;
    private Scene loginScene;
    
    private VBox routineNodes;
    private VBox exerciseNodes;
    private VBox sessionNodes;
    private VBox doneExerciseNodes;
    private VBox addSessionContentNodes;
    
    private Label menuLabel = new Label();
    
    @Override
    public void init() throws Exception {
        Properties properties = new Properties();
        
        File config = new File("config.properties");
        
        if (!config.exists()) {
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
        this.sheetsService = SheetsServiceUtil.getSheetsService();
    }
    
    public Node oneRoutineNode(Routine routine, Stage primaryStage) {
        HBox box = new HBox(10);
        Label label  = new Label(routine.getName());
        label.setMinHeight(28);
        Button sessionButton = new Button("View sessions");
        sessionButton.setOnAction(e -> {
            listSessionsView(routine, primaryStage);
            primaryStage.setScene(this.sessionsScene);
        });
        
        Button routineButton = new Button("Add exercises");
        routineButton.setOnAction(e -> {
            modifyRoutineView(routine, primaryStage);
            primaryStage.setScene(this.modifyRoutineScene);
        });
                
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));
        
        box.getChildren().addAll(label, spacer, routineButton, sessionButton);
        return box;
    }
    
    public void redrawRoutineList(Stage primaryStage) {
        if (this.workoutService.getLoggedUser() == null) {
            return;
        }
        this.routineNodes.getChildren().clear();

        List<Routine> routines = this.workoutService.getRoutines();
        routines.forEach(routine -> {
            this.routineNodes.getChildren().add(oneRoutineNode(routine, primaryStage));
        });
    }
    
    public Node oneExerciseNode(Exercise exercise) {
        HBox box = new HBox(20);
        Label exerciseName  = new Label(exercise.getName());
        exerciseName.setMinHeight(28);
        exerciseName.setWrapText(true);
         
        Label firstParameter  = new Label(exercise.getParameters().get(0));
        firstParameter.setMinHeight(28);
        firstParameter.setWrapText(true);
        
        Label secondParameter  = new Label(exercise.getParameters().get(1));
        secondParameter.setMinHeight(28);
        secondParameter.setWrapText(true);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));
        
        box.getChildren().addAll(exerciseName, spacer, firstParameter, secondParameter);
        return box;
    }
    
    public void redrawExerciseList(Routine routine) {
        if (this.workoutService.getLoggedUser() == null) {
            return;
        }
        List<Exercise> exercisesOnSheet = this.workoutService.getExercisesOfRoutine(routine);
        List<Exercise> exercisesInRoutine = routine.getExercises();

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
        this.exerciseNodes.getChildren().clear();
        
        exercisesOnSheet.forEach(exercise -> {
            this.exerciseNodes.getChildren().add(oneExerciseNode(exercise));
        });
    }
    
    public void modifyRoutineView(Routine routine, Stage primaryStage) {        
        ScrollPane workoutScrollbar = new ScrollPane();       
        BorderPane mainPane = new BorderPane(workoutScrollbar);
        this.modifyRoutineScene = new Scene(mainPane, 800, 500);
                
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Label routineNameLabel = new Label(routine.getName());
        VBox menuLabelAndRoutineLabel = new VBox(2);
        menuLabelAndRoutineLabel.getChildren().addAll(menuLabel, routineNameLabel);
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
            this.workoutService.logout();
            primaryStage.setScene(this.loginScene);
        });
        saveButton.setOnAction(e -> {
            this.workoutService.routineToSheet(routine, this.workoutService.getLoggedUser().getSpreadsheetId());
        });
        backButton.setOnAction(e -> {
            this.workoutService.routineToSheet(routine, this.workoutService.getLoggedUser().getSpreadsheetId());
            primaryStage.setScene(this.routinesScene);
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
       
        this.exerciseNodes = new VBox(10);
        this.exerciseNodes.setMaxWidth(780);
        this.exerciseNodes.setMinWidth(780);
        this.exerciseNodes.setPadding(new Insets(10));
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
        workoutScrollbar.setContent(this.exerciseNodes);
        mainPane.setBottom(formAndLabel);
        mainPane.setTop(menuPane);
        
        createExerciseButton.setOnAction(e -> {
            String exerciseName = newExerciseNameInput.getText();
            String firstParameter = firstParameterInput.getText();
            String secondParameter = secondParameterInput.getText();
   
            if (exerciseName.length() < 2) {
                exerciseCreationMessage.setText("exercise name too short");
                exerciseCreationMessage.setTextFill(Color.RED);
            } else if (this.workoutService.addExerciseToRoutine(this.workoutService.createExercise(exerciseName, firstParameter, secondParameter), routine)) {
                exerciseCreationMessage.setText("new exercise created");
                exerciseCreationMessage.setTextFill(Color.GREEN);
                redrawExerciseList(routine);
                newExerciseNameInput.setText("");
            } else {
                exerciseCreationMessage.setText("the exercise takes a name and two parameters");
                exerciseCreationMessage.setTextFill(Color.RED);
            }
        });
        
    }
    
    public Node oneSessionNode(Routine routine, WorkoutSession session, Stage primaryStage) {
        HBox box = new HBox(20);
        Label sessionDate  = new Label(this.workoutService.dateToString(session.getDate()));
        sessionDate.setMinHeight(28);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));
        
        Button viewSessionButton = new Button("View session");
        viewSessionButton.setPadding(new Insets(10));
        viewSessionButton.setOnAction(e -> {
            listDoneExercisesView(routine, session, primaryStage);
            primaryStage.setScene(this.oneSessionScene);
        });
        
        box.getChildren().addAll(sessionDate, spacer, viewSessionButton);
        return box;
    }
    
    public void redrawSessionsList(Routine routine, Stage primaryStage) {
        if (this.workoutService.getLoggedUser() == null) {
            return;
        }
        List<WorkoutSession> sessionsOnSheet = this.workoutService.getWorkoutSessions(routine);

        if ((sessionsOnSheet == null || sessionsOnSheet.isEmpty())) {
            return;
        }
        this.sessionNodes.getChildren().clear();
        
        sessionsOnSheet.forEach(session -> {
            this.sessionNodes.getChildren().add(oneSessionNode(routine, session, primaryStage));
        });
    }
    
    public void listSessionsView(Routine routine, Stage primaryStage) {
        ScrollPane workoutScrollbar = new ScrollPane();       
        BorderPane mainPane = new BorderPane(workoutScrollbar);
        this.sessionsScene = new Scene(mainPane, 800, 500);
                
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Label routineNameLabel = new Label(routine.getName());
        VBox menuLabelAndRoutineLabel = new VBox(2);
        menuLabelAndRoutineLabel.getChildren().addAll(menuLabel, routineNameLabel);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout");
        logoutButton.setPadding(new Insets(10));
        Button backButton = new Button("Back");
        backButton.setPadding(new Insets(10));
        menuPane.getChildren().addAll(menuLabelAndRoutineLabel, menuSpacer, logoutButton, backButton);
        
        logoutButton.setOnAction(e -> {
            this.workoutService.logout();
            primaryStage.setScene(this.loginScene);
        });
        backButton.setOnAction(e -> {
            primaryStage.setScene(this.routinesScene);
            redrawRoutineList(primaryStage);
        });
        
        Label createSessionLabel = new Label();
        Label sessionTimeLabel = new Label("Session date ('yyyy-mm-dd')");
        TextField sessionTimeInput = new TextField();
        VBox labelAndForm = new VBox(5);
        HBox addNewSessionForm = new HBox(10);
        labelAndForm.setPadding(new Insets(10));
        Button createSessionButton = new Button("Add a new session");
        createSessionButton.setPadding(new Insets(10));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
       
        this.sessionNodes = new VBox(10);
        this.sessionNodes.setMaxWidth(780);
        this.sessionNodes.setMinWidth(780);
        this.sessionNodes.setPadding(new Insets(10));
        redrawSessionsList(routine, primaryStage);

        mainPane.setTop(menuPane);
        addNewSessionForm.getChildren().addAll(sessionTimeLabel, sessionTimeInput, spacer, createSessionButton);
        labelAndForm.getChildren().addAll(createSessionLabel, addNewSessionForm);
        
        workoutScrollbar.setContent(this.sessionNodes);
        mainPane.setBottom(labelAndForm);
        mainPane.setTop(menuPane);
        
        createSessionButton.setOnAction(e -> {
            String timesString = sessionTimeInput.getText();
            WorkoutSession newSession = this.workoutService.createWorkoutSession(timesString, routine);
            if (newSession != null) {
                createSessionLabel.setText("");
                sessionTimeInput.setText("");
                this.addSessionView(routine, newSession, primaryStage);
                this.redrawAddDoneExerciseList(routine, newSession);
                primaryStage.setScene(this.addSessionScene);
            } else {
                createSessionLabel.setText("Not a valid date");
                createSessionLabel.setTextFill(Color.RED);
            }
        });
        
    }
    
    public Node oneDoneExerciseNode(Exercise exercise, DoneExercise doneExercise) {
        HBox box = new HBox(20);
        Label exerciseName  = new Label(exercise.getName());
        exerciseName.setMinHeight(28);
        exerciseName.setWrapText(true);

        Label firstParameter  = new Label(exercise.getParameters().get(0));
        firstParameter.setMinHeight(28);
        firstParameter.setWrapText(true);
        
        Label firstParameterValue  = new Label(Double.toString(doneExercise.getParameterValue(exercise.getParameters().get(0))));
        firstParameterValue.setMinHeight(28);
        firstParameterValue.setWrapText(true);
        
        Label secondParameter  = new Label(exercise.getParameters().get(1));
        secondParameter.setMinHeight(28);
        secondParameter.setWrapText(true);
        
        Label secondParameterValue  = new Label(Double.toString(doneExercise.getParameterValue(exercise.getParameters().get(1))));
        secondParameterValue.setMinHeight(28);
        secondParameterValue.setWrapText(true);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));
        
        box.getChildren().addAll(exerciseName, spacer, firstParameter, firstParameterValue, secondParameter, secondParameterValue);
        return box;
    }
    
    public void redrawDoneExerciseList(Routine routine, WorkoutSession session, Stage primaryStage) {
        if (this.workoutService.getLoggedUser() == null) {
            return;
        }
        List<DoneExercise> sessionContents = session.getSessionContents();
        List<Exercise> exercisesInRoutine = routine.getExercises();

        if ((sessionContents == null || sessionContents.isEmpty()) || (exercisesInRoutine == null || exercisesInRoutine.isEmpty())) {
            return;
        }
        this.doneExerciseNodes.getChildren().clear();
        
        for (int i = 0; i < exercisesInRoutine.size(); i++) {
            this.doneExerciseNodes.getChildren().add(oneDoneExerciseNode(exercisesInRoutine.get(i), sessionContents.get(i)));
        }
    }
    
    public void listDoneExercisesView(Routine routine, WorkoutSession session, Stage primaryStage) {
        ScrollPane workoutScrollbar = new ScrollPane();       
        BorderPane mainPane = new BorderPane(workoutScrollbar);
        this.oneSessionScene = new Scene(mainPane, 800, 500);
                
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Label routineNameLabel = new Label(routine.getName() + ": " + this.workoutService.dateToString(session.getDate()));
        VBox menuLabelAndRoutineLabel = new VBox(2);
        menuLabelAndRoutineLabel.getChildren().addAll(menuLabel, routineNameLabel);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout");
        logoutButton.setPadding(new Insets(10));
        Button backButton = new Button("Back");
        backButton.setPadding(new Insets(10));
        menuPane.getChildren().addAll(menuLabelAndRoutineLabel, menuSpacer, logoutButton, backButton);
        
        logoutButton.setOnAction(e -> {
            this.workoutService.logout();
            primaryStage.setScene(this.loginScene);
        });
        backButton.setOnAction(e -> {
            primaryStage.setScene(this.sessionsScene);
            redrawSessionsList(routine, primaryStage);
        });
        
        Label createSessionLabel = new Label();
        Label sessionTimeLabel = new Label("Session date ('yyyy-mm-dd')");
        TextField sessionTimeInput = new TextField();
        VBox labelAndForm = new VBox(5);
        HBox addNewSessionForm = new HBox(10);
        labelAndForm.setPadding(new Insets(10));
        Button createSessionButton = new Button("Add a new session");
        createSessionButton.setPadding(new Insets(10));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
       
        this.doneExerciseNodes = new VBox(10);
        this.doneExerciseNodes.setMaxWidth(780);
        this.doneExerciseNodes.setMinWidth(780);
        this.doneExerciseNodes.setPadding(new Insets(10));
        redrawDoneExerciseList(routine, session, primaryStage);
        
        addNewSessionForm.getChildren().addAll(sessionTimeLabel, sessionTimeInput, spacer, createSessionButton);
        labelAndForm.getChildren().addAll(createSessionLabel, addNewSessionForm);
        
        workoutScrollbar.setContent(this.doneExerciseNodes);
        mainPane.setBottom(labelAndForm);
        mainPane.setTop(menuPane);
        
        createSessionButton.setOnAction(e -> {
            String timeString = sessionTimeInput.getText();

            WorkoutSession newSession = this.workoutService.createWorkoutSession(timeString, routine);
            if (newSession != null) {
                this.addSessionView(routine, newSession, primaryStage);
                this.redrawAddDoneExerciseList(routine, newSession);
                primaryStage.setScene(this.addSessionScene);
            } else {
                createSessionLabel.setText("Not a valid date");
                createSessionLabel.setTextFill(Color.RED);
            }
        });
    }
    
    public Node addDoneExerciseNode(Exercise exercise, WorkoutSession session) {     
        VBox box = new VBox(2);
        Label createDoneExerciseMessage = new Label();
        HBox addDoneExerciseForm = new HBox(10);
        Label exerciseName  = new Label(exercise.getName());
        exerciseName.setMinHeight(28);
        exerciseName.setWrapText(true);
         
        Label firstParameter  = new Label(exercise.getParameters().get(0));
        firstParameter.setMinHeight(28);
        firstParameter.setWrapText(true);

        TextField firstParameterInput = new TextField();

        Label secondParameter  = new Label(exercise.getParameters().get(1));
        secondParameter.setMinHeight(28);
        secondParameter.setWrapText(true);

        TextField secondParameterInput = new TextField();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));
        
        Button addDoneExerciseButton = new Button("Add");
        addDoneExerciseButton.setPadding(new Insets(10));

        addDoneExerciseForm.getChildren().addAll(exerciseName, spacer, firstParameter, firstParameterInput, secondParameter, secondParameterInput, addDoneExerciseButton);
        box.getChildren().addAll(createDoneExerciseMessage, addDoneExerciseForm);
        
        addDoneExerciseButton.setOnAction(e -> {
            String firstParameterValue = firstParameterInput.getText();
            String secondParameterValue = secondParameterInput.getText();
            
            if (this.workoutService.addDoneExerciseToSession(session, exercise, firstParameterValue, secondParameterValue)) {
                createDoneExerciseMessage.setText("The values has been added to the workout session");
                createDoneExerciseMessage.setTextFill(Color.GREEN);
            } else {
                createDoneExerciseMessage.setText("Please add values as real numbers");
                createDoneExerciseMessage.setTextFill(Color.RED);
            }
        });
        
        return box;
    }
    
    public void redrawAddDoneExerciseList(Routine routine, WorkoutSession session) {
        if (this.workoutService.getLoggedUser() == null) {
            return;
        }
        List<Exercise> exercisesOnSheet = this.workoutService.getExercisesOfRoutine(routine);

        List<Exercise> exercisesInRoutine = routine.getExercises();

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
        this.addSessionContentNodes.getChildren().clear();
        
        exercisesOnSheet.forEach(exercise -> {
            this.addSessionContentNodes.getChildren().add(addDoneExerciseNode(exercise, session));
        });
    }
    
    public void addSessionView(Routine routine, WorkoutSession session, Stage primaryStage) {        
        ScrollPane workoutScrollbar = new ScrollPane();       
        BorderPane mainPane = new BorderPane(workoutScrollbar);
        this.addSessionScene = new Scene(mainPane, 800, 500);
                
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Label routineNameLabel = new Label(routine.getName() + ": " + this.workoutService.dateToString(session.getDate()));
        VBox menuLabelAndRoutineLabel = new VBox(2);
        menuLabelAndRoutineLabel.getChildren().addAll(menuLabel, routineNameLabel);
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
            this.workoutService.logout();
            primaryStage.setScene(this.loginScene);
        });
        saveButton.setOnAction(e -> {
            this.workoutService.workoutSessionToSheet(session, this.workoutService.getLoggedUser().getSpreadsheetId());
        });
        backButton.setOnAction(e -> {
            this.workoutService.workoutSessionToSheet(session, this.workoutService.getLoggedUser().getSpreadsheetId());
            primaryStage.setScene(this.sessionsScene);
            redrawSessionsList(routine, primaryStage);
        });
        
//        HBox labelBox = new HBox(10);
//        labelBox.setPadding(new Insets(10));
//        Label exerciseCreationMessage = new Label();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
       
        this.addSessionContentNodes = new VBox(10);
        this.addSessionContentNodes.setMaxWidth(780);
        this.addSessionContentNodes.setMinWidth(780);
        this.addSessionContentNodes.setPadding(new Insets(10));
        redrawAddDoneExerciseList(routine, session);
        
        workoutScrollbar.setContent(this.addSessionContentNodes);
        mainPane.setTop(menuPane);
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
            if (this.workoutService.login(username)) {
                loginMessage.setText("");
                redrawRoutineList(primaryStage);
                primaryStage.setScene(this.routinesScene);  
                usernameInput.setText("");
            } else {
                loginMessage.setText("User does not exist");
                loginMessage.setTextFill(Color.RED);
            }      
        });  
        
        createButton.setOnAction(e -> {
            usernameInput.setText("");
            primaryStage.setScene(this.newUserScene);   
        });  
        
        loginPane.getChildren().addAll(loginMessage, inputPane, loginButton, createButton);       
        
        this.loginScene = new Scene(loginPane, 800, 550);    
   
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
            } else if (this.workoutService.createUser(username, spreadsheetId)) {
                userCreationMessage.setText("");
                loginMessage.setText("new user created");
                loginMessage.setTextFill(Color.GREEN);               
                primaryStage.setScene(this.loginScene);
            } else {
                userCreationMessage.setText("username has to be unique");
                userCreationMessage.setTextFill(Color.RED);
            }
        });
        
        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane, spreadsheetIdPane, createNewUserButton); 
        this.newUserScene = new Scene(newUserPane, 800, 500);
        
        // main scene
        
        ScrollPane workoutScrollbar = new ScrollPane();       
        BorderPane mainPane = new BorderPane(workoutScrollbar);
        this.routinesScene = new Scene(mainPane, 800, 500);
                
        HBox menuPane = new HBox(10);
        menuPane.setPadding(new Insets(10));
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout"); 
        logoutButton.setPadding(new Insets(10));
        menuPane.getChildren().addAll(menuLabel, menuSpacer, logoutButton);
        
        logoutButton.setOnAction(e -> {
            this.workoutService.logout();
            primaryStage.setScene(this.loginScene);
        });        
        
        HBox createRoutineForm = new HBox(10);
        createRoutineForm.setPadding(new Insets(10));
        Button createRoutineButton = new Button("Add new routine");
        createRoutineButton.setPadding(new Insets(10));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label newRoutineInputLabel = new Label("Name your new exercise routine: ");
        TextField newRoutineInput = new TextField();
        createRoutineForm.getChildren().addAll(newRoutineInputLabel, newRoutineInput, spacer, createRoutineButton);
        
        this.routineNodes = new VBox(10);
        this.routineNodes.setMaxWidth(780);
        this.routineNodes.setMinWidth(780);
        this.routineNodes.setPadding(new Insets(10));
        redrawRoutineList(primaryStage);
        
        workoutScrollbar.setContent(this.routineNodes);
        mainPane.setBottom(createRoutineForm);
        mainPane.setTop(menuPane);
        
        createRoutineButton.setOnAction(e -> {
            this.workoutService.createRoutine(newRoutineInput.getText());
            newRoutineInput.setText("");
            redrawRoutineList(primaryStage);
        });
        
        // seutp primary stage
        
        primaryStage.setTitle("worksheetout");
        primaryStage.setScene(this.loginScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("closing");
        });
    }

    @Override
    public void stop() {
        System.out.println("the program is closing");
    }    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
