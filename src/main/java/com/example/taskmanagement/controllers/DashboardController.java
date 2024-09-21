package com.example.taskmanagement.controllers;
import com.example.taskmanagement.Utils.CSVfile;
import com.example.taskmanagement.Utils.User;
import com.example.taskmanagement.dao.DBconnection;
import com.example.taskmanagement.Utils.Tasks;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;


public class DashboardController implements Initializable {
    @FXML
    public Button logOutButton;
    public Button viewPendingTasks;
    public AnchorPane homepage;
    public AnchorPane pendingTaskPage;
    public AnchorPane completedTasksPage;
    public AnchorPane ongoingTasksPage;
    public Button viewOngoingTasks;
    public Button ViewCompletedTasks;
    public Button homeButton;
    public Button userNameButton;
    public Label welcomeUser;
    public Button addTaskButton;
    public Button myTaskButton;
    public AnchorPane myTasksPage;
    @FXML
    public TableView<Tasks> myTasksTable;
    @FXML
    public TableColumn<Tasks, Integer> taskno;
    @FXML
    public TableColumn<Tasks,String> taskname;
    @FXML
    public TableColumn<Tasks,String> descriptionn;
    @FXML
    public TableColumn<Tasks,String> priorityy;
    @FXML
    public TableColumn<Tasks,String> statuss;
    @FXML
    public TableColumn<Tasks, Date> startDate;
    @FXML
    public TableColumn<Tasks,Date> dueDate;

    public TextField taskNametextField;
    public TextArea descriptionTextField;
    public ComboBox priorityChooseField;
    public ComboBox statusChooseField;
    public DatePicker startDateField;
    public DatePicker dueDateField;
    public Button updateTaskButton;
    public Button deleteTaskButton;
    public Button clearTaskButton;
    public TextField searchBar;
    public TableView<Tasks> pendingTable;
    public TableColumn<Tasks,Integer> pendingTaskNo;
    public TableColumn<Tasks,String> pendingTaskName;
    public TableColumn<Tasks,String> pendingDescription;
    public TableColumn<Tasks,String> pendingPriority;
    public TableColumn<Tasks,String> pendingStatus;
    public TableColumn<Tasks,Date> pendingStartDate;
    public TableColumn<Tasks,Date> pendingDueDate;
    public TableView<Tasks> ongoingTable;
    public TableColumn<Tasks,Integer> ongoingTaskNo;
    public TableColumn<Tasks,String> ongoingTaskName;
    public TableColumn<Tasks,String> ongoingDescription;
    public TableColumn<Tasks,String> ongoingPriority;
    public TableColumn<Tasks,String> ongoingStatus;
    public TableColumn<Tasks,Date> ongoingStartDate;
    public TableColumn<Tasks,Date> ongoingDueDate;
    public TableView<Tasks> completetedTaskTable;
    public TableColumn<Tasks,Integer> completedTaskNo;
    public TableColumn<Tasks,String> completedTaskName;
    public TableColumn<Tasks,String> completedDescription;
    public TableColumn<Tasks,String> completedPriority;
    public TableColumn<Tasks,String> completedStatus;
    public TableColumn<Tasks,String> completedStartDate;
    public TableColumn<Tasks,String> completedDueDate;
    public Label HighTaskPriority;
    public Label pendingTaskCount;
    public Label ongoingTaskCount;
    public Label CompletedTaskCount;
    public Button downloadFile;


    Connection connect;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    CSVfile csv = null;
    String username = null;
    String firstName = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getUser_id();
        displayUserName();
        displayFirstName();
        addTasksShowListData();
        pendingTasksShowListData();
        ongoingTasksShowListData();
        completedTasksShowListData();
        displayHighPriorityTask();
        addTaskPriorityList();
        addTaskStatusList();
        downloadFile.setOnAction(event -> {
            csv = new CSVfile();
            csv.generateTaskListCSVFile(tasksList);
        });

    }

    public int getUser_id(){
        return User.user_id;
    }
    public void displayUserName(){
        String sql = "SELECT User.username, User.first_name " +
                "FROM User " +
                "INNER JOIN Tasks ON User.user_id = Tasks.user_id " +
                "WHERE Tasks.user_id = ?";
        connect = DBconnection.getConnection();
        try{

            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, getUser_id());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        userNameButton.setText(username);
    }

    public void displayFirstName(){
        String sql = "SELECT User.username, User.first_name " +
                "FROM User " +
                "INNER JOIN Tasks ON User.user_id = Tasks.user_id " +
                "WHERE Tasks.user_id = ?";
        connect = DBconnection.getConnection();

        try {
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, getUser_id());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                firstName = resultSet.getString("first_name");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        welcomeUser.setText("Welcome " + firstName + "!!");
    }

    @FXML
    public void displayHighPriorityTask(){
        PriorityQueue<Tasks> taskQueue = loadTasks();
        for (Tasks task : taskQueue) {
            if (task.getStatus().equals("Pending") || task.getStatus().equals("Ongoing")) {
                Tasks highestPriorityTask = taskQueue.peek();
                if (highestPriorityTask != null) {
                    String taskName = highestPriorityTask.getTask_name();
                    // Set the text to the task name in main home page
                    HighTaskPriority.setText(taskName);
                } else {
                    HighTaskPriority.setText("No Task Available");
                }
            }
        }

    }

    @FXML
    public void logOutOnClick(ActionEvent e) {
        Parent parent;
        try {
            //close dashboard
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();

            //go back to login page
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/pages/login.fxml")));
            Scene scene = new Scene(parent);
            Stage login = new Stage();
            login.setScene(scene);
            login.setTitle("Login");
            login.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    @FXML
    public void viewPendingTasksOnClick(){
        homepage.setVisible(false);
        pendingTaskPage.setVisible(true);
        pendingTasksShowListData();
    }

    @FXML
    public void viewOngoingTasksOnClick(){
        homepage.setVisible(false);
        ongoingTasksPage.setVisible(true);
        ongoingTasksShowListData();
    }

    @FXML
    public void ViewCompletedTasksOnClick(){
        homepage.setVisible(false);
        completedTasksPage.setVisible(true);
        completedTasksShowListData();
    }

    @FXML
    public void homeButtonOnClick(){
        myTasksPage.setVisible(false);
        ongoingTasksPage.setVisible(false);
        completedTasksPage.setVisible(false);
        pendingTaskPage.setVisible(false);
        homepage.setVisible(true);
        displayHighPriorityTask();
        pendingTasksShowListData();
        completedTasksShowListData();
        ongoingTasksShowListData();
    }

    @FXML
    public void MyTaskButtonOnClick(){
        homepage.setVisible(false);
        ongoingTasksPage.setVisible(false);
        completedTasksPage.setVisible(false);
        pendingTaskPage.setVisible(false);
        myTasksPage.setVisible(true);
        addTaskPriorityList();
        addTaskStatusList();
    }

    @FXML
    private String[] priority = {"High", "Medium", "Low"};

    @FXML
    public void addTaskPriorityList(){
        List<String> priorirtyList = new ArrayList<>(Arrays.asList(priority));
        ObservableList listPriority = FXCollections.observableArrayList(priorirtyList);
        priorityChooseField.setItems(listPriority);
    }

    @FXML
    private String[] status = {"Pending", "Ongoing", "Completed"};
    @FXML
    public void addTaskStatusList(){
        List<String> statusList = new ArrayList<>(Arrays.asList(status));
        ObservableList<String> listStatus = FXCollections.observableArrayList(statusList);

        statusChooseField.setItems(listStatus);


    }
    public ObservableList<Tasks> addTaskListData() {
        ObservableList<Tasks> listData = FXCollections.observableArrayList();
        String sql = "Select * from Tasks where user_id = " + getUser_id();
        connect = DBconnection.getConnection();
        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            Tasks tasks;
            while (resultSet.next()){
                tasks = new Tasks(resultSet.getInt("task_id"),
                                  resultSet.getString("task_name"),
                                  resultSet.getString("description"),
                                  resultSet.getString("priority"),
                                  resultSet.getString("status"),
                                  resultSet.getDate("start_date"),
                                  resultSet.getDate("due_date"),
                                  resultSet.getInt("user_id"));
                listData.add(tasks);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listData;

    }

    private FilteredList<Tasks> tasksList;
    public void addTasksShowListData() {
        taskno.setCellValueFactory(new PropertyValueFactory<>("task_id"));
        taskname.setCellValueFactory(new PropertyValueFactory<>("task_name"));
        descriptionn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priorityy.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statuss.setCellValueFactory(new PropertyValueFactory<>("status"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
        tasksList = new FilteredList<>(addTaskListData() , task -> task.getStatus().equals("Pending") || task.getStatus().equals("Ongoing"));
        searchBar.textProperty().addListener((observable, oldValue , newValue) ->
                tasksList.setPredicate(tasks -> {
                    if (tasks.getStatus().equals("Completed")) {
                        return false;
                    }
                    
                    if (newValue == null || newValue.isBlank()) {
                        return true;
                    }

                    String searchKey = newValue.toLowerCase();
                    
                    if (tasks.getTask_id().toString().toLowerCase().contains(searchKey)) {
                        return true;
                    } else if (tasks.getTask_name().toLowerCase().contains(searchKey)) {
                        return true;
                    } else if (tasks.getDescription().toLowerCase().contains(searchKey)) {
                        return true;
                    } else {
                        return false;
                    }
                })

        );
        myTasksTable.setItems(tasksList);
    }



    @FXML
    public void pendingTasksShowListData() {
        PriorityQueue<Tasks> taskQueue = loadTasks();

        pendingTaskNo.setCellValueFactory(new PropertyValueFactory<>("task_id"));
        pendingTaskName.setCellValueFactory(new PropertyValueFactory<>("task_name"));
        pendingDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        pendingPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        pendingStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        pendingStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        pendingDueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
        //sort by priority in pending task list
        FilteredList<Tasks> pendingfilteredData = new FilteredList<>(FXCollections.observableArrayList(taskQueue), task -> task.getStatus().equals("Pending"));
        int count = pendingfilteredData.size();
        pendingTaskCount.setText(String.valueOf(count));
        pendingTable.setItems(pendingfilteredData);
    }

    public void ongoingTasksShowListData() {
        PriorityQueue<Tasks> taskQueue = loadTasks();
        ongoingTaskNo.setCellValueFactory(new PropertyValueFactory<>("task_id"));
        ongoingTaskName.setCellValueFactory(new PropertyValueFactory<>("task_name"));
        ongoingDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        ongoingPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        ongoingStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ongoingStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        ongoingDueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
        
        //sort by priority in ongoing task list
        FilteredList<Tasks> ongoingFilteredData = new FilteredList<>(FXCollections.observableArrayList(taskQueue), task -> task.getStatus().equals("Ongoing"));
        int count = ongoingFilteredData.size();
        ongoingTaskCount.setText(String.valueOf(count));
        ongoingTable.setItems(ongoingFilteredData);
    }

    public void completedTasksShowListData() {
        PriorityQueue<Tasks> taskQueue = loadTasks();
        completedTaskNo.setCellValueFactory(new PropertyValueFactory<>("task_id"));
        completedTaskName.setCellValueFactory(new PropertyValueFactory<>("task_name"));
        completedDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        completedPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        completedStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        completedStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        completedDueDate.setCellValueFactory(new PropertyValueFactory<>("due_date"));
        
        //sort by priority in completed task list
        FilteredList<Tasks> completedFilteredData = new FilteredList<>(FXCollections.observableArrayList(taskQueue), task -> task.getStatus().equals("Completed"));
        int count = completedFilteredData.size();
        CompletedTaskCount.setText(String.valueOf(count));
        completetedTaskTable.setItems(completedFilteredData);
    }

    private PriorityQueue<Tasks> loadTasks() {
        PriorityQueue<Tasks> taskQueue = new PriorityQueue<>();
        String sql = "Select * from Tasks where user_id = " + getUser_id();
        connect = DBconnection.getConnection();
        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            Tasks tasks;
            while (resultSet.next()){
                tasks = new Tasks(resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("description"),
                        resultSet.getString("priority"),
                        resultSet.getString("status"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("due_date"),
                        resultSet.getInt("user_id"));
                taskQueue.add(tasks);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskQueue;
    }
    @FXML
    public void AddTaskButtonOnClick(){
        String sql = "INSERT INTO `Tasks`( `task_name`, `description`, `priority`, `status`, `start_date`, `due_date`, `user_id`) VALUES (?,?,?,?,?,?,?);";
        connect = DBconnection.getConnection();
        try {

            if(taskNametextField.getText().isEmpty() ||
               descriptionTextField.getText().isEmpty()||
               priorityChooseField.getSelectionModel().getSelectedItem() == null||
               statusChooseField.getSelectionModel().getSelectedItem() == null ||
               startDateField.getValue() == null ||
               dueDateField.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the details of task");
                alert.showAndWait();
            }
            else {
                    preparedStatement = connect.prepareStatement(sql);
                    preparedStatement.setString(1, taskNametextField.getText());
                    preparedStatement.setString(2, descriptionTextField.getText());
                    preparedStatement.setString(3, (String) priorityChooseField.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(4, (String) statusChooseField.getSelectionModel().getSelectedItem());
                    preparedStatement.setDate(5, java.sql.Date.valueOf(startDateField.getValue()));
                    preparedStatement.setDate(6, java.sql.Date.valueOf(dueDateField.getValue()));
                    preparedStatement.setInt(7, getUser_id());
                    preparedStatement.executeUpdate();
                    addTasksShowListData();
                    clearTaskButtonOnClick();
                    //change the high priority task if new added task is of high priority
                    displayHighPriorityTask();

            }
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            String message = e.getMessage();

            if ("45000".equals(sqlState)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText("Task could not be added");
                alert.setContentText(message);  // Display the error message from the trigger
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while trying to insert the task. Please try again.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void clearTaskButtonOnClick(){
        taskNametextField.setText("");
        descriptionTextField.setText("");
        priorityChooseField.getSelectionModel().clearSelection();
        statusChooseField.getSelectionModel().clearSelection();
        startDateField.setValue(null);
        dueDateField.setValue(null);
    }

    Integer task_id;

    private Integer getTask_id() {
        return task_id;
    }

    @FXML
    public void SelectTask(){
        Tasks task = myTasksTable.getSelectionModel().getSelectedItem();
        if (task == null) {
            return;
        }

        priorityChooseField.setValue(task.getPriority());
        statusChooseField.setValue(task.getStatus());

        taskNametextField.setText(task.getTask_name());
        descriptionTextField.setText(task.getDescription());
        startDateField.setValue(task.getStart_date().toLocalDate());
        dueDateField.setValue(task.getDue_date().toLocalDate());


        // Store the task ID for future updates
        task_id = task.getTask_id();
    }

    @FXML
    public void UpdateTaskButtonOnClick(){
        if(taskNametextField.getText().isEmpty() ||
                descriptionTextField.getText().isEmpty()||
                priorityChooseField.getSelectionModel().getSelectedItem() == null||
                statusChooseField.getSelectionModel().getSelectedItem() == null ||
                startDateField.getValue() == null ||
                dueDateField.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Select the Task you want to Update");
                alert.showAndWait();
        }
        else {
                String sql = "UPDATE Tasks SET task_name = ? , description = ? , priority = ? , status = ?, start_date = ? ,due_date = ? WHERE task_id =" + getTask_id();
                connect = DBconnection.getConnection();
                try {
                    preparedStatement = connect.prepareStatement(sql);
                    preparedStatement.setString(1, taskNametextField.getText());
                    preparedStatement.setString(2, descriptionTextField.getText());
                    preparedStatement.setString(3, (String) priorityChooseField.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(4, (String) statusChooseField.getSelectionModel().getSelectedItem());
                    preparedStatement.setDate(5, java.sql.Date.valueOf(startDateField.getValue()));
                    preparedStatement.setDate(6, java.sql.Date.valueOf(dueDateField.getValue()));
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to update task with id : " + getTask_id());
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get().equals(ButtonType.OK)) {
                        preparedStatement.executeUpdate();
                        addTasksShowListData();
                        clearTaskButtonOnClick();
                        //change the high priority task if new updated task is of high priority
                        displayHighPriorityTask();
                    }


                } catch (SQLException e) {
                    String sqlState = e.getSQLState();
                    String message = e.getMessage();

                    if ("45000".equals(sqlState)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText("Task could not be updated");
                        alert.setContentText(message);  // Display the error message from the trigger
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("SQL Error");
                        alert.setHeaderText(null);
                        alert.setContentText("An error occurred while trying to update the task. Please try again.");
                        alert.showAndWait();
                        e.printStackTrace();
                    }
                }

        }
    }


    @FXML
    public void DeleteTaskButtonOnClick(){
        if(taskNametextField.getText().isEmpty() ||
                descriptionTextField.getText().isEmpty()||
                startDateField.getValue() == null ||
                dueDateField.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error message");
            alert.setHeaderText(null);
            alert.setContentText("Select the Task you want to Delete");
            alert.showAndWait();
        }
        else {
            String sql = "DELETE FROM `Tasks` WHERE task_id ="+getTask_id();
            connect = DBconnection.getConnection();
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Delete task with id : " + getTask_id());
                Optional<ButtonType> option =  alert.showAndWait();
                if (option.get().equals(ButtonType.OK)){
                    Statement statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    addTasksShowListData();
                    clearTaskButtonOnClick();
                    //change the high priority task if high priority task is deleted
                    displayHighPriorityTask();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
