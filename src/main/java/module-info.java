module itrysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;

    requires java.sql;

    requires java.prefs;
    requires java.desktop;

    requires jdk.jsobject;

    opens de.kuehweg.sqltool.itrysql to javafx.fxml;
    opens de.kuehweg.sqltool.dialog to javafx.fxml;

    exports de.kuehweg.sqltool.itrysql;
}
