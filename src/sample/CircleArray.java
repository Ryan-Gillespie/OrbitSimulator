package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleArray {
	private CartVector[] array;
	private int current;

	/**
	 * Constructor that initializes an empty CircleArray.
	 * @param initialCapacity is the capacity of the array
	 */
	public CircleArray(int initialCapacity) {
		array = new CartVector[initialCapacity];
		current = 0;
	}

	/**
	 * Adds a vector to the least recently used index in the array and continues the circle
	 * @param v is the vector to add, the position of the planet
	 */
	public void add(CartVector v) {
		array[current] = v;
		current++;
		if (current == array.length)
			current = 0;
	}

	/**
	 * clears out the whole array, sets every cartVector to null
	 */
	public void clear() {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null)
				break;
			array[i] = null;
		}
		current = 0;
	}

	/**
	 * Draws all tracers to the screen
	 * @param gc is the GraphicsContext to draw to
	 */
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		for (CartVector v : array) {
			if (v == null) {
				break;
			}
			gc.fillOval(v.getX(), v.getY(), 2, 2);
		}
	}
}
