package com.example.taskmanagement.Utils;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVfile {
    public void generateTaskListCSVFile(FilteredList<Tasks> filteredTasks) {
        String csvFilePath = "tasks.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {

            writer.write("Task ID,Task Name,Description,Priority,Status,Start Date,Due Date");
            writer.newLine();

            for (Tasks task : filteredTasks) {
                writer.write(
                        task.getTask_id() + "," +
                                escapeSpecialCharacters(task.getTask_name()) + "," +
                                escapeSpecialCharacters(task.getDescription()) + "," +
                                task.getPriority() + "," +
                                task.getStatus() + "," +
                                task.getStart_date() + "," +
                                task.getDue_date()
                );
                writer.newLine();
            }

            System.out.println("CSV file created: " + csvFilePath);


            // Show a confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText(null);
            alert.setContentText("Tasks have been successfully exported to tasks.csv");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();

            // Show an error message if something goes wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while exporting tasks.");
            alert.showAndWait();
        }
    }
    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\"", "\"\"");

        if (data.contains(",") || data.contains("\n") || data.contains("\r")) {
            escapedData = "\"" + escapedData + "\"";
        }

        return escapedData;
    }

}
