package agh.ics.oop;

import java.util.Objects;

/**
 * The map visualizer converts the {@link IMap} map into a string
 * representation.
 *
 * @author apohllo
 */
public class MapVisualizer {
	private static final String EMPTY_CELL = "  ";
	private static final String FRAME_SEGMENT = "--";
	private static final String CELL_SEGMENT = "|";
	private final IMap map;

	/**
	 * Initializes the MapVisualizer with an instance of map to visualize.
	 */
	public MapVisualizer(IMap map) {
		this.map = map;
	}

	/**
	 * Convert selected region of the map into a string. It is assumed that the
	 * indices of the map will have no more than two characters (including the
	 * sign).
	 *
	 * @param lowerLeft  The lower left corner of the region that is drawn.
	 * @param upperRight The upper right corner of the region that is drawn.
	 * @return String representation of the selected region of the map.
	 */
	public String draw(Vector2d lowerLeft, Vector2d upperRight) {
		StringBuilder builder = new StringBuilder();
		for (int i = upperRight.y + 1; i >= lowerLeft.y - 1; i--) {
			if (i == upperRight.y + 1) {
				builder.append(drawHeader(lowerLeft, upperRight));
			}
			builder.append(String.format("%3d: ", i));
			for (int j = lowerLeft.x; j <= upperRight.x + 1; j++) {
				if (i < lowerLeft.y || i > upperRight.y) {
					builder.append(drawFrame(j <= upperRight.x));
				} else {
					builder.append(CELL_SEGMENT);
					if (j <= upperRight.x) {
						builder.append(drawObject(new Vector2d(j, i)));
					}
				}
			}
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}

	private String drawFrame(boolean innerSegment) {
		if (innerSegment) {
			return FRAME_SEGMENT + FRAME_SEGMENT;
		} else {
			return FRAME_SEGMENT;
		}
	}

	private String drawHeader(Vector2d lowerLeft, Vector2d upperRight) {
		StringBuilder builder = new StringBuilder();
		builder.append(" y\\x ");
		for (int j = lowerLeft.x; j < upperRight.x + 1; j++) {
			builder.append(String.format("%2d ", j));
		}
		builder.append(System.lineSeparator());
		return builder.toString();
	}

	private String drawObject(Vector2d currentPosition) {
		String result;
		if (this.map.isOccupied(currentPosition)) {
			String tileRepresentation = this.map.positionRepresentation(currentPosition);
			result = Objects.requireNonNullElse(tileRepresentation, EMPTY_CELL);
		} else {
			result = EMPTY_CELL;
		}
		return result;
	}
}