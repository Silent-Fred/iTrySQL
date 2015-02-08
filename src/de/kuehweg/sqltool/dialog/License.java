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
package de.kuehweg.sqltool.dialog;

import de.kuehweg.sqltool.common.DialogDictionary;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Dialog mit Angaben zum Programm, Lizenzen,...
 *
 * @author Michael Kühweg
 */
public class License extends Stage {

    public License() {
        super();
        Scene scene;
        try {
            final Parent root;
            root = FXMLLoader.load(
                    getClass().getResource("/resources/fxml/License.fxml"),
                    ResourceBundle.getBundle("dictionary"));
            root.getStylesheets().add(
                    getClass().getResource("/resources/css/itrysql.css")
                    .toExternalForm());
            scene = new Scene(root);
        } catch (final Exception ex) {
            scene = FallbackSceneFactory.createNewInstance();
        }
        setScene(scene);
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);

        centerOnScreen();
        setResizable(false);
        setTitle(DialogDictionary.APPLICATION.toString());
    }
}
