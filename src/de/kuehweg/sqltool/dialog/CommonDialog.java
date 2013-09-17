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
package de.kuehweg.sqltool.dialog;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.dialog.images.ImagePack;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Michael Kühweg
 */
public abstract class CommonDialog extends Stage {

    private String activatedButton;

    public CommonDialog(final String message) {
        super();
        try {
            final Parent root = FXMLLoader.load(CommonDialog.class.getResource("CommonDialog.fxml"), ResourceBundle.getBundle("dictionary"));
            initStyle(StageStyle.UTILITY);
            setScene(new Scene(root));
            Label messageLabel = (Label) getScene().lookup("#message");
            if (messageLabel != null) {
                messageLabel.setText(message);
            }
            centerOnScreen();
            setResizable(false);
            initModality(Modality.APPLICATION_MODAL);
        } catch (Exception ex) {
            initStyle(StageStyle.UTILITY);
            initModality(Modality.APPLICATION_MODAL);
            setScene(new Scene(
                    VBoxBuilder.create().
                    children(new Text(DialogDictionary.APPLICATION.toString()),
                    new Text(DialogDictionary.ERR_LOAD_FXML.toString())).alignment(Pos.CENTER).padding(new Insets(50)).build()));
            centerOnScreen();
            setResizable(false);
            setTitle("Alert");
        }
    }

    public void specializeDialogTitle(final String titleText) {
        Label titleLabel = (Label) getScene().lookup("#title");
        if (titleLabel != null) {
            titleLabel.setText(titleText);
        }
    }

    public void specializeDialogIcon(final ImagePack image) {
        ImageView icon = (ImageView) getScene().lookup("#icon");
        if (icon != null) {
            icon.setImage(image.getAsImage());
        }
    }

    public void addDialogButtons(final String... buttons) {
        HBox buttonBox = (HBox) getScene().lookup("#buttonBox");
        if (buttonBox != null) {
            boolean first = true;
            for (final String button : buttons) {
                Button buttonNode = new Button(button);
                buttonNode.setDefaultButton(first);
                first = false;
                buttonNode.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        activatedButton = button;
                        close();
                    }
                });
                buttonBox.getChildren().add(0, buttonNode);
            }
        }
    }

    public String askUserFeedback() {
        showAndWait();
        return activatedButton;
    }
}
