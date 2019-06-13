package com;

import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class JFX extends Application {
    
    LinearProbing hashTable = new LinearProbing();
    
    Scene scene;
    BorderPane root;
    Pane wrapperPane;
    
    Canvas canvas = new Canvas(970, 700);
    GraphicsContext gc = canvas.getGraphicsContext2D();   
      
    Button insert;
    Button remove;
    Button search;
    Button reset;
    Button create;
    
    Label valueL;
    Label keyL;
    Label tableSize;
    Label loadFactor;
    
    TextField keyTF;
    TextField valueTF;
    TextField tableSizeTF;
    TextField loadFactorTF;
    
    HBox commands;
    HBox newTable;
    VBox menu;   
    
    @Override
    public void start(Stage primaryStage) {
        
        draw(" ");
        drawHeader();
              
        insert = new Button("Insert");
        remove = new Button("Remove");
        search = new Button("Search");
        reset = new Button("Reset");
        create = new Button("Create new table");
              
        keyL = new Label("Enter a key: ");
        keyL.setTextFill(Color.WHITE);
        valueL = new Label("Enter a value: ");
        valueL.setTextFill(Color.WHITE);
        tableSize = new Label("Table size: ");
        tableSize.setTextFill(Color.WHITE);
        loadFactor = new Label("Load factor: ");
        loadFactor.setTextFill(Color.WHITE);
               
        keyTF = new TextField();
        keyTF.setPrefWidth(60);
        valueTF = new TextField();
        valueTF.setPrefWidth(60);       
        tableSizeTF = new TextField();
        tableSizeTF.setPrefWidth(50);
        tableSizeTF.setText("4");
        loadFactorTF = new TextField();
        loadFactorTF.setPrefWidth(50);
        loadFactorTF.setText("0.5");
        
        root = new BorderPane();       
        wrapperPane = new Pane();       
        wrapperPane.getChildren().add(canvas);
        wrapperPane.setStyle("-fx-background-color: black; -fx-border-color: yellow; -fx-border-width: 2px;");
        root.setCenter(wrapperPane);
            
        commands = new HBox();
        commands.setAlignment(Pos.CENTER);
        newTable = new HBox();
        newTable.setAlignment(Pos.CENTER);
        menu = new VBox();
        
        commands.getChildren().addAll(keyL, keyTF, valueL, valueTF, insert, remove, search);
        newTable.getChildren().addAll(tableSize, tableSizeTF, loadFactor, loadFactorTF, create, reset);   
        menu.getChildren().addAll(commands, newTable);
        root.setBottom(menu);

        commands.setPadding(new Insets(15));
        commands.setSpacing(10);
        commands.setStyle("-fx-background-color: #336699; -fx-border-color: yellow; -fx-border-width: 2px; -fx-border-style: hidden solid solid solid;");
       
        newTable.setPadding(new Insets(15));    
        newTable.setSpacing(10);
        newTable.setStyle("-fx-background-color: #336699; -fx-border-color: yellow; -fx-border-width: 2px; -fx-border-style: hidden solid solid solid;");
                    
        insert.setOnAction((ActionEvent event) -> {
            if(hashTable.getCurrentSize() == 21) {
                JOptionPane.showMessageDialog(null, "The table is full!", "FULL TABLE!", JOptionPane.WARNING_MESSAGE);
                keyTF.clear();
                valueTF.clear();
                return;
            }
            
            String key = keyTF.getText().replaceAll("\\s+","");
            String value = valueTF.getText().replaceAll("\\s+","");
            
            if (key.equals("") || value.equals("")) {
                JOptionPane.showMessageDialog(null, "Enter a key and a value!", "Enter key and value!", JOptionPane.WARNING_MESSAGE);
                keyTF.clear();
                valueTF.clear();
                return;
            }
            
            if(key.length() > 4) {
                key = key.substring(0, 4);
            }
            
            if (value.length() > 4) {
                value = value.substring(0, 4);
            }
            
            hashTable.insert(key, value);
            draw(" ");
            drawHeader();
            keyTF.clear();
            valueTF.clear();
        });
        
        remove.setOnAction((ActionEvent event) -> {
            String key = keyTF.getText().replaceAll("\\s+","");
            if (key.equals("")) {
                keyTF.clear();
                valueTF.clear();
                return;
            }
            
            if(key.length() > 4) {
                key = key.substring(0, 4);
            }

            boolean containsKey = hashTable.contains(key);
           
            if(containsKey) {              
                hashTable.delete(key);
            } else  {
                JOptionPane.showMessageDialog(null, key + " is not in the table!", "Not found!", JOptionPane.INFORMATION_MESSAGE);
            }
            
            draw(" ");
            drawHeader();
            keyTF.clear();
            valueTF.clear();
            
        });
        
        search.setOnAction((ActionEvent event) -> {
            String key = keyTF.getText().replaceAll("\\s+","");
            if (key.equals("")) {
                keyTF.clear();
                valueTF.clear();
                return;
            }
            
            if(key.length() > 4) {
                key = key.substring(0, 4);
            }
            
            boolean containsKey = hashTable.contains(key);  
            
            if (!containsKey) {
                JOptionPane.showMessageDialog(null, key + " is not in the table!", "Not found!", JOptionPane.INFORMATION_MESSAGE);
                draw(" ");
            } else {
                JOptionPane.showMessageDialog(null, key + " has been found!", "Success!", JOptionPane.OK_OPTION);
                draw(key);
            }
            
            drawHeader();
            keyTF.clear();
            valueTF.clear();
        }); 
        
        reset.setOnAction((ActionEvent event) -> {
            hashTable = new LinearProbing();
            draw(" ");
            drawHeader();
            keyTF.clear();
            valueTF.clear();
        });
        
        create.setOnAction((ActionEvent event) -> {
            int ts;
            double lf;
            
            if(tableSizeTF.getText().matches("^\\d+") && loadFactorTF.getText().matches("^\\d\\.\\d+")) {
                ts = Integer.parseInt(tableSizeTF.getText());
                lf = Double.parseDouble(loadFactorTF.getText());
                
                if((ts > 1 && ts < 22) && (lf > 0.0 && lf < 1.0)) {
                    hashTable = new LinearProbing(ts, lf);                    
                } 
                else {
                    JOptionPane.showMessageDialog(null, "Table size must be 1 < x < 22 !\nLoad factor must be 0 < x < 1 !", "Table not functional!", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Table size must be 1 < x < 22 !\nLoad factor must be 0 < x < 1 !", "Table not functional!", JOptionPane.WARNING_MESSAGE);
            }
            
            draw(" ");
            drawHeader();    
            tableSizeTF.clear();
            loadFactorTF.clear();
        });
        
        
        scene = new Scene(root, 960, 800);
        
        primaryStage.setTitle("Linear Probing Hash Table!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }   
       
    private void draw(String str) {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();
           
        gc.clearRect(0, 0, width, height);
        gc.setFont(Font.font ("Verdana", 25));
        gc.setFill(Color.WHITE);
        
        int row = 0, n = 0, x = 1, y = 4;
        for (String key : hashTable.getKeys()) {
            if(row == 7) {
                x += 300;
                y = 4;
                row = 0;
            }
            
            if(key.equals(str)) {
                gc.setFill(Color.RED);              
            } else {
                gc.setFill(Color.WHITE);                
            }
            
            gc.fillText("(" + String.valueOf(n) + ")", x + 60, y * 60);
            gc.fillText("[" + key + "] -> " + String.valueOf(hashTable.getValue(key)), x + 135, y * 60);
            
            row++;
            y++;            
            n++;                   
        }
        
    }
    
    public void drawHeader() {
        gc.setFont(Font.font ("Verdana", 25));
      
        gc.setFill(Color.LIGHTBLUE);
                             
        gc.fillText("Table Size: ", 161, 1*50);
        gc.fillText("Current Size: ", 161, 2*50);
        
        gc.fillText("Load Factor: ", 561, 1*50);
        gc.fillText("Current LF: ", 561, 2*50);
        
        gc.setFill(Color.GREEN);
                        
        gc.fillText(String.valueOf(new DecimalFormat("#.##").format(hashTable.getLoadFactor())), 770, 1*50);  
        gc.fillText(String.valueOf(hashTable.getMaxSize()), 370, 1*50);          
        
        gc.setFill(Color.YELLOW);
        
        gc.fillText(String.valueOf(new DecimalFormat("#.##").format(hashTable.getCurrentLoadFactor())), 770, 2*50);
        gc.fillText(String.valueOf(hashTable.getCurrentSize()), 370, 2*50);
                      
        gc.setStroke(Color.WHITE);
        
        gc.strokeLine(0, 150, 970, 150);
        
    }

}


