package com.chrislaforetsoftware.logslicer.display;


import com.chrislaforetsoftware.logslicer.controller.MainViewController;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.reactfx.value.Val;

import java.util.function.IntFunction;

/**
 * Given the line number, return a node (graphic) to display to the left of a line.
 */
public class ArrowFactory implements IntFunction<Node> {
	private final ObservableValue<Integer> shownLine;
	private final MainViewController controller;

	public ArrowFactory(ObservableValue<Integer> shownLine, MainViewController controller) {
		this.shownLine = shownLine;
		this.controller = controller;
	}

	@Override
	public Node apply(int lineNumber) {
		Polygon triangle = new Polygon(0.0, 0.0, 10.0, 5.0, 0.0, 10.0);
		triangle.setFill(Color.DARKRED);

		ObservableValue<Boolean> visible = Val.map(shownLine, sl -> sl == lineNumber);

		triangle.visibleProperty().bind(
				Val.flatMap(triangle.sceneProperty(), scene -> {
					return controller.getLogContent() != null && scene != null ? visible : Val.constant(false);
				}));

		return triangle;
	}
}