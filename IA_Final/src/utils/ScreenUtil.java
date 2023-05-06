package utils;

import java.awt.Dimension;

public class ScreenUtil {
	private int screenHeight, screenWidth;
	private int labelWidth, tfWidth, btnWidth;
	public static final int HEIGHT = 30;

	public ScreenUtil(Dimension screenSize) {
		screenHeight = screenSize.height;
		screenWidth = screenSize.width;
		labelWidth = 100;
		tfWidth = 200;
		btnWidth = 100;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getLabelWidth() {
		return labelWidth;
	}

	public int getTfWidth() {
		return tfWidth;
	}

	public int getBtnWidth() {
		return btnWidth;
	}
}
