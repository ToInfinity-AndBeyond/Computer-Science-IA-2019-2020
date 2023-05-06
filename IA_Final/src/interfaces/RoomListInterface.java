package interfaces;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import launcher.Launcher;
import utils.ScreenUtil;

public class RoomListInterface {
	public static JButton btnBack, btnCheck;
	public static JButton[] btnRoomList;

	private JPanel mainPanel, upperPanel, lowerPanel;

	public RoomListInterface() {
	}

	public RoomListInterface(ScreenUtil screenUtil) {
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		mainPanel.setLayout(null);

		upperPanel = new JPanel();
		upperPanel.setBounds(20, 20, screenUtil.getScreenWidth() - 40, (screenUtil.getScreenHeight() - 40) / 10 * 7);

		upperPanel.setLayout(new GridLayout(2, 4, 30, 80));
		btnRoomList = new JButton[8];
		for (int i = 0; i < 8; i++) {
			btnRoomList[i] = new JButton("Room " + (i + 1));
			upperPanel.add(btnRoomList[i]);
		}
		mainPanel.add(upperPanel);

		lowerPanel = new JPanel();
		lowerPanel.setBounds(0, upperPanel.getY() + upperPanel.getHeight() + 20, screenUtil.getScreenWidth(),
				screenUtil.getScreenHeight() - (upperPanel.getY() + upperPanel.getHeight() + 20));
		lowerPanel.setLayout(null);

		btnBack = new JButton("Back");
		btnBack.setBounds(10, lowerPanel.getHeight() / 2 - ScreenUtil.HEIGHT / 2, 80, ScreenUtil.HEIGHT);
		lowerPanel.add(btnBack);

		btnCheck = new JButton("Check");
		btnCheck.setBounds(screenUtil.getScreenWidth() - screenUtil.getBtnWidth() - 10, btnBack.getY(),
				screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		lowerPanel.add(btnCheck);

		mainPanel.add(lowerPanel);
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public void setPanel() {
		btnCheck.setVisible(!Launcher.sessionUtil.getType().equals("librarian"));
		for (int i = 0; i < 8; i++) {
			btnRoomList[i].setEnabled(true);
		}
		if (Launcher.sessionUtil.getType().equals("parent")) {
			for (int i = 2; i < 8; i++) {
				btnRoomList[i].setEnabled(false);
			}
		}
	}
}
