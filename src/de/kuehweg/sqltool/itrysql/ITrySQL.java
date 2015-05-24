/*
 * Copyright (c) 2013-2015, Michael Kühweg
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

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.dialog.images.ImagePack;
import de.kuehweg.sqltool.dialog.util.StageSizerUtil;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Zentrale Klasse der "iTry SQL" Applikation
 *
 * @author Michael Kühweg
 */
public class ITrySQL extends Application {

    private iTrySQLController controller;

    @Override
    public void init() throws Exception {
        super.init();
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMono.ttf")
                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMoIt.ttf")
                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMoBd.ttf")
                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/resources/fonts/VeraMoBI.ttf")
                .toExternalForm(), 12);
        // spezieller Icon-Font
        Font.loadFont(getClass().getResource("/resources/fonts/iTryIcons-Roman.ttf")
                .toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/resources/fonts/iTryIcons-Bold.ttf")
                .toExternalForm(), 12);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setResources(ResourceBundle.getBundle("dictionary"));
        fxmlLoader.setLocation(getClass().getResource(
                "/resources/fxml/iTrySQL.fxml"));
        final Parent root = (Parent) fxmlLoader.load();
        root.getStylesheets().add(
                getClass().getResource("/resources/css/itrysql.css")
                .toExternalForm());

        controller = (iTrySQLController) fxmlLoader.getController();

        final Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        final Rectangle2D calculatedSize = StageSizerUtil
                .calculateSizeDependingOnScreenSize();
        primaryStage.setX(calculatedSize.getMinX());
        primaryStage.setY(calculatedSize.getMinY());
        primaryStage.setWidth(calculatedSize.getWidth());
        primaryStage.setHeight(calculatedSize.getHeight());
        primaryStage.getIcons().add(ImagePack.APP_ICON.getAsImage());
        primaryStage.setTitle(DialogDictionary.APPLICATION.toString());
        primaryStage.setOnCloseRequest((WindowEvent ev) -> {
            if (!controller.quit()) {
                ev.consume();
            }
        });
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            if (controller != null) {
                controller.getConnectionHolder().disconnect();
            }
        } catch (final Throwable ex) {
            Logger.getLogger(ITrySQL.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main()
     * serves only as fallback in case the application can not be launched through
     * deployment artifacts, e.g., in IDEs with limited FX support. NetBeans ignores
     * main().
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
