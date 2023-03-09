package com.chrislaforetsoftware.logslicer.display;


import com.chrislaforetsoftware.logslicer.controller.MainViewController;
import com.chrislaforetsoftware.logslicer.log.LogContent;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import org.reactfx.value.Val;

import java.util.function.IntFunction;

/**
 * Given the line number, return a node (graphic) to display to the left of a line.
 */
public class ButtonFactory {
	private final ObservableValue<Integer> shownLine;
	private final MainViewController controller;
	private final Font font;

	public ButtonFactory(ObservableValue<Integer> shownLine, MainViewController controller) {
		this.shownLine = shownLine;
		this.controller = controller;
		this.font = new Font("Arial", 8.0);
	}

	public Node apply(LogContent content, int lineNumber) {
		Button button = new Button();
		button.setFont(font);
		button.setText("JSON");

		if (content != null
				&& (content.hasXml(lineNumber) || content.hasJson(lineNumber))) {
			//button.setStyle("-fx-background-color: #0000aa");

			//button.setPrefHeight(5.0);
			//button.setPrefHeight();
			if (content.hasXml(lineNumber)) {
				button.setText(" XML  ");
				button.setStyle("-fx-border-color: lightgreen");
				button.setStyle("-fx-text-base-color: green");

				final EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e)
					{
						controller.handleXmlButton(e, lineNumber);
					}
				};

				button.setOnAction(event);

			} else {
				button.setText(" JSON ");
				button.setStyle("-fx-border-color: red");
				button.setStyle("-fx-text-base-color: darkred");

				final EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e)
					{
						controller.handleJsonButton(e, lineNumber);
					}
				};

				button.setOnAction(event);
			}

			button.setVisible(true);
		} else {
			button.setVisible(false);
		}
		return button;
	}
}