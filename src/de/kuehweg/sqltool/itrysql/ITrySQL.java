/*
 * Copyright (c) 2013, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.kuehweg.sqltool.itrysql;

import de.kuehweg.sqltool.database.ServerManager;
import de.kuehweg.sqltool.dialog.images.ImagePack;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Zentrale Klasse der "iTry SQL" Applikation
 *
 * @author Michael Kühweg
 */
public class ITrySQL extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMono.ttf").
                toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMoIt.ttf").
                toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMoBd.ttf").
                toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMoBI.ttf").
                toExternalForm(), 12);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("iTrySQL.fxml"),
                ResourceBundle.getBundle("dictionary"));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(ITrySQL.class.getResource("itrysql.css").
                toExternalForm());

        stage.setScene(scene);
        stage.getIcons().add(ImagePack.APP_ICON.getAsImage());
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            ServerManager.getSharedInstance().shutdownServer();
        } catch (Throwable ex) {
            Logger.getLogger(ITrySQL.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}