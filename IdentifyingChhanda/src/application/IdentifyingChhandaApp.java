package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class IdentifyingChhandaApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load background image
        Image backgroundImage = new Image("Untitled-2 copy.jpg");

        // Create background
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
        Background backgroundLayout = new Background(background);

//        // Create logo
        ImageView logoImageView = new ImageView(new Image("Maharaja_Sriram_Chandra_Bhanja_Deo_University_logo (1).png"));
        logoImageView.setFitWidth(80); // Set width of the logo
        logoImageView.setPreserveRatio(true);
//
//        // Create top logo HBox
        HBox bottomRightLogoHBox = new HBox();
        bottomRightLogoHBox.setAlignment(Pos.CENTER);
        bottomRightLogoHBox.getChildren().add(logoImageView);
        
        ImageView topPhotoImageView = new ImageView(new Image("Untitled-1 copy4.jpg"));
        topPhotoImageView.setFitWidth(800); // Set width of the photo
        topPhotoImageView.setPreserveRatio(true);

        // Create top photo HBox
        HBox topPhotoHBox = new HBox();
        topPhotoHBox.setAlignment(Pos.CENTER);
        topPhotoHBox.getChildren().add(topPhotoImageView);

        // Create UI components
        TextArea inputTextArea = new TextArea();
        inputTextArea.setPromptText("Enter Odia Poems...");
        inputTextArea.setPrefRowCount(15);
        inputTextArea.setStyle("-fx-font-size: 20px;");

        Button analyzeButton = new Button("Analyze Chhanda");
        Button clearButton = new Button("Clear");
        Label resultLabel = new Label();
        TextArea outputTextArea = new TextArea();
        outputTextArea.setPromptText("");
        outputTextArea.setStyle("-fx-font-size: 20px;");
        outputTextArea.setPrefRowCount(1);
        outputTextArea.setEditable(false); // Make output text area read-only

        // Attach event handler to the analyzeButton
        analyzeButton.setOnAction(event -> {
        	String[] lines = inputTextArea.getText().split("\n");
            int[] lengths = new int[lines.length];
            for (int i = 0; i < lines.length; i++) {
                lengths[i] = strlen(lines[i]);
            }
            outputTextArea.setText(identifyChhanda(lengths));
        });

        // Attach event handler to the clearButton
        clearButton.setOnAction(event -> {
            // Clear the input and output text areas
            inputTextArea.clear();
            outputTextArea.clear();
        });
        
     // Create Analyze and Clear buttons HBox
        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.getChildren().addAll(analyzeButton, clearButton);

        // Create layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(inputTextArea, analyzeButton, clearButton, resultLabel, outputTextArea);
        vbox.setPadding(new Insets(10));

        // Create BorderPane layout
        BorderPane root = new BorderPane();
        root.setBackground(backgroundLayout);
        root.setTop(topPhotoHBox); 
        root.setBottom(bottomRightLogoHBox);
        root.setCenter(vbox); // Set UI components in the center

        // Create scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.resizableProperty().setValue(Boolean.FALSE);

        // Set the scene
        primaryStage.setScene(scene);
        primaryStage.setTitle("Identifying Chhandas in Odia Poetry");
        primaryStage.show();
    }


    public static int strlen(String text) {
        int len = 0;

        StringBuilder syllable = new StringBuilder();
        String reverseStr = "";
        String maatraas = "ାିୀୁୂୃେୈୋୌଂଁଃ";
        boolean isEnd = false;
        text = text.replaceAll("'", "");
        for (int index = 0; index < text.length(); index++) {
            char ch = text.charAt(index);
            char nextChar = index < text.length() - 1 ? text.charAt(index + 1) : '\0';
            char nextToNextChar = index < text.length() - 2 ? text.charAt(index + 2) : '\0';

            if (Character.isWhitespace(ch) || !(ch >= '\u0B01' && ch <= '\u0B63') || ch == '\u0B71') {
                continue;
            }
            syllable.append(ch);

            if (maatraas.indexOf(nextChar) != -1) {
                syllable.append(nextChar);
                index++;
                isEnd = true;

                if (maatraas.indexOf(nextToNextChar) != -1) {
                    syllable.append(nextToNextChar);
                    index++;
                    isEnd = true;
                }
            } else if (nextChar == '\u200D' && nextToNextChar == '\u0B4D') {
                syllable.append(nextChar);
                index++;
                syllable.append(nextToNextChar);
                index++;
                isEnd = false;
            } else if (nextChar == '\u0B4D') {
                isEnd = false;
                syllable.append(nextChar);
                index++;

                if (nextToNextChar == '\0') {
                    syllable.append('\u200C');
                    isEnd = true;
                }
            } else {
                isEnd = true;
            }

            if (isEnd) {
                reverseStr = syllable + reverseStr;
                syllable.setLength(0);
                len++;
            }
        }
        //System.out.println("preprocessed text of [" + text + "] is [" + reverseStr + "]");
        return len;
    }

    public static String identifyChhanda(int[] lengths) {
        if (lengths.length == 4 && lengths[0] == 14 && lengths[1] == 14 && lengths[2] == 8 && lengths[3] == 14) {
            return "ଶଙ୍କରାଭରଣ";
        } else if (lengths.length == 2 && lengths[0] == 16 && lengths[1] == 16) {
            return "ରାମକେରୀ";
        } else if (lengths.length == 2 && lengths[0] == 11 && lengths[1] == 11) {
            return "ଚକ୍ରକେଳି";
        } else if (lengths.length == 2 && lengths[0] == 14 && lengths[1] == 14) {
            return "ଆଶାବରୀ";
        } else if (lengths.length == 2 && lengths[0] == 9 && lengths[1] == 9) {
            return "ଗୁଜ୍ଜରୀ ବା ଭାଗବତ ବାଣୀ";
        } else if (lengths.length == 4 && lengths[0] == 10 && lengths[1] == 10 && lengths[2] == 10 && lengths[3] == 10) {
            return "ମୁଖାରୀ";
        } else if (lengths.length == 4 && lengths[0] == 12 && lengths[1] == 12 && lengths[2] == 12 && lengths[3] == 12) {
            return "କଳହଂସ କେଦାର";
        } else if (lengths.length == 4 && lengths[0] == 17 && lengths[1] == 17 && lengths[2] == 17 && lengths[3] == 17) {
            return "କାଳୀ";
        } else if (lengths.length == 2 && lengths[0] == 20 && lengths[1] == 20) {
            return "ବଙ୍ଗଳାଶ୍ରୀ";
        } else if (lengths.length == 2 && lengths[0] == 24 && lengths[1] == 24) {
            return "ଭୂପାଳ";
        } else if (lengths.length == 2 && lengths[0] == 26 && lengths[1] == 26) {
            return "ବିଭାସଗୁଜ୍ଜରୀ";
        } else if (lengths.length == 2 && lengths[0] == 12 && lengths[1] == 12) {
            return "ନଟବାଣୀ/ ବିଭାସ କେଦାର";
        } else if (lengths.length == 6 && lengths[0] == 11 && lengths[1] == 11 && lengths[2] == 11 && lengths[3] == 11 && lengths[4] == 8 && lengths[5] == 20) {
            return "ରସକୁଲ୍ୟା";
        } else if (lengths.length == 6 && lengths[0] == 11 && lengths[1] == 11 && lengths[2] == 11 && lengths[3] == 11 && lengths[4] == 6 && lengths[5] == 11) {
            return "ଆଷାଢ଼ ଶୁକ୍ଳ";
        } else if (lengths.length == 4 && lengths[0] == 29 && lengths[1] == 29 && lengths[2] == 9 && lengths[3] == 13) {
            return "ଚୋଖୁ";
        } else if (lengths.length == 2 && lengths[0] == 13 && lengths[1] == 13) {
            return "ଚିନ୍ତାଦେଶାକ୍ଷ";
        } else if (lengths.length == 2) {
            return "ଦାଣ୍ଡିବୃତ୍ତ";
        } else if (lengths.length == 2 && lengths[0] == 17 && lengths[1] == 17) {
            return "ଖଣ୍ଡ କୁମ୍ଭକାମୋଦୀ";
        } else if (lengths.length == 2 && lengths[0] == 18 && lengths[1] == 18) {
            return "କୁମ୍ଭକାମୋଦୀ";
        } else if (lengths.length == 4 && lengths[0] == 20 && lengths[1] == 20 && lengths[1] == 7 && lengths[2] == 10) {
            return "ଚିନ୍ତାକାମୋଦୀ";
        } else if (lengths.length == 5 && lengths[0] == 22 && lengths[1] == 22 && lengths[2] == 6 && lengths[3] == 10 && lengths[4] == 22) {
            return "ଶୋକ କାମୋଦୀ";
        } else if (lengths.length == 6 && lengths[0] == 23 && lengths[1] == 24 && lengths[2] == 4 && lengths[3] == 9 && lengths[4] == 9 && lengths[5] == 24) {
            return "କାମୋଦୀ";
        } else if (lengths.length == 3 && lengths[0] == 8 && lengths[1] == 8 && lengths[2] == 20) {
            return "ଘଣ୍ଟାରବ";
        } else if (lengths.length == 3 && lengths[0] == 14 && lengths[1] == 14 && lengths[2] == 6) {
            return "ଜୟନ୍ତ";
        } else if (lengths.length == 4 && lengths[0] == 15 && lengths[1] == 15 && lengths[2] == 15 && lengths[3] == 15) {
            return "ଲଳିତକାମୋଦୀ";
        } else if (lengths.length == 5 && lengths[0] == 21 && lengths[1] == 21 && lengths[2] == 16 && lengths[3] == 16 && lengths[4] == 21) {
            return "କୌଶିକ";
        } else if (lengths.length == 5 && lengths[0] == 35 && lengths[1] == 35 && lengths[2] == 12 && lengths[3] == 12 && lengths[4] == 35) {
            return "ପାହାଡ଼ିଆ କେଦାର";
        } else if (lengths.length == 6 && lengths[0] == 14 && lengths[1] == 14 && lengths[2] == 14 && lengths[3] == 14 && lengths[4] == 14 && lengths[5] == 14) {
            return "ବସନ୍ତ";
        } else if (lengths.length == 2 && lengths[0] == 14 && lengths[1] == 14) {
            return "କଳସା";
        } else if (lengths.length == 4 && lengths[0] == 8 && lengths[1] == 8 && lengths[2] == 8 && lengths[3] == 5) {
            return "ମୁନିବରବାଣୀ";
        } else if (lengths.length == 8 && lengths[0] == 21 && lengths[1] == 11 && lengths[2] == 20 && lengths[3] == 3 && lengths[4] == 11 && lengths[5] == 11 && lengths[6] == 20 && lengths[7] == 3) {
            return "କନଡ଼ା";
        } else if (lengths.length == 4 && lengths[0] == 10 && lengths[1] == 10 && lengths[2] == 22 && lengths[3] == 3) {
            return "ଭୈରବ";
        } else if (lengths.length == 5 && lengths[0] == 21 && lengths[1] == 21 && lengths[2] == 3 && lengths[3] == 9 && lengths[4] == 21) {
            return "କଲ୍ୟାଣ-ଆହାରୀ";
        } else{
            return "Please Enter Correct Odia text...It's not a Chhanda!";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
