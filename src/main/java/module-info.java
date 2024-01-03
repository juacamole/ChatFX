module com.example.chatfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens com.example.chatfx to javafx.fxml;
    exports com.example.chatfx;
}