package model;

public class ImageScale {
	private final String x;
	private final String y;
	private final String scale;

	public ImageScale(String x, String y, String scale) {
		this.x = x;
		this.y = y;
		this.scale = scale;
	}

	public String getX() {
		return x;
	}

	public String getY() {
		return y;
	}

	public String getScale() {
		return scale;
	}

}
