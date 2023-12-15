package com.example.demo;
/**************************************************************

 Jalen Early

Mini-Project - Apr 16, 2023

 I wrote this code myself, And By turning in this code, I Pledge:
 1. That I have completed the programming assignment independently.
 2. I have not copied the code from a student or any source.
 3. I have not given my code to any student.

 **************************************************************/
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends Application {
    private DataInputStream input;
    private DataOutputStream output;

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea messageArea = new TextArea();
        TextField messageField = new TextField();
        messageField.setPromptText("Type your message here.");
        Button sendButton = new Button("Send");
        VBox vbox = new VBox(messageArea, messageField, sendButton);

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            Socket socket = new Socket("localhost", 8000);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            Thread readThread = new Thread(() -> {
                while (true) {
                    try {
                        String message = input.readUTF();
                        Platform.runLater(() -> messageArea.appendText(message + "\n"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendButton.setOnAction(event -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                try {
                    output.writeUTF(message);
                    messageField.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
