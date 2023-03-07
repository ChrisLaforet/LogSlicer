package com.chrislaforetsoftware.logslicer.display;


import javafx.beans.value.ObservableValue;
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
public class ButtonFactory implements IntFunction<Node> {
	private final ObservableValue<Integer> shownLine;
	private final Font font;

	public ButtonFactory(ObservableValue<Integer> shownLine) {
		this.shownLine = shownLine;
		this.font = new Font("Arial", 8.0);
	}

	@Override
	public Node apply(int lineNumber) {
		Button button = new Button();
		button.setFont(font);
		//button.setStyle("-fx-background-color: #0000aa");
		button.setStyle("-fx-border-color: green");
		//button.setPrefHeight(5.0);
		//button.setPrefHeight();
		button.setText("XML");

		button.setVisible(lineNumber % 2 == 0);
		return button;


//		Polygon triangle = new Polygon(0.0, 0.0, 10.0, 5.0, 0.0, 10.0);
//		triangle.setFill(Color.GREEN);
//
//		ObservableValue<Boolean> visible = Val.map(shownLine, sl -> sl == lineNumber);
//
//		triangle.visibleProperty().bind(
//				Val.flatMap(triangle.sceneProperty(), scene -> {
//					return scene != null ? visible : Val.constant(false);
//				}));
//
//		return triangle;
	}
}