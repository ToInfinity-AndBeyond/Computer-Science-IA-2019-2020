package interfaces;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import launcher.Launcher;
import utils.ScreenUtil;

public class MainInterface {
	public static JLabel lbName, lbType;
	public static JPasswordField pfPasswd;
	public static JButton btnReservation, btnAddFreeBlock, btnManageReservation, btnLogout;

	private JPanel mainPanel;

	public MainInterface() {
	}

	public MainInterface(ScreenUtil screenUtil) {
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		mainPanel.setLayout(null);

		lbName = new JLabel();
		lbName.setBounds(screenUtil.getScreenWidth() - 170, 10, 160, ScreenUtil.HEIGHT);
		lbName.setHorizontalAlignment(SwingConstants.RIGHT);
		mainPanel.add(lbName);

		lbType = new JLabel();
		lbType.setBounds(lbName.getX(), lbName.getY() + ScreenUtil.HEIGHT + 3, 160, ScreenUtil.HEIGHT);
		lbType.setHorizontalAlignment(SwingConstants.RIGHT);
		mainPanel.add(lbType);

		btnReservation = new JButton("Reservation");
		btnReservation.setBounds(screenUtil.getScreenWidth() / 2 - 80, screenUtil.getScreenHeight() / 2 - 50, 160, 100);
		mainPanel.add(btnReservation);

		btnManageReservation = new JButton("Manage Reservations");
		btnManageReservation.setBounds(screenUtil.getScreenWidth() / 2 - 80, screenUtil.getScreenHeight() / 2 - 50, 160,
				100);
		mainPanel.add(btnManageReservation);

		btnAddFreeBlock = new JButton("Add Free Blocks");
		btnAddFreeBlock.setBounds(screenUtil.getScreenWidth() / 2 + 5, screenUtil.getScreenHeight() / 2 - 50, 160, 100);
		mainPanel.add(btnAddFreeBlock);

		btnLogout = new JButton("Logout");
		btnLogout.setBounds(screenUtil.getScreenWidth() - screenUtil.getBtnWidth() - 10,
				lbType.getY() + ScreenUtil.HEIGHT + 10, screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		mainPanel.add(btnLogout);
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	public void setPanel(ScreenUtil screenUtil) {
		btnAddFreeBlock.setVisible(Launcher.sessionUtil.getType().equals("student"));
		btnManageReservation.setVisible(Launcher.sessionUtil.getType().equals("librarian"));
		if (Launcher.sessionUtil.getType().equals("librarian")) {
			btnReservation.setVisible(false);
		} else {
			btnReservation.setVisible(true);
			if (Launcher.sessionUtil.getType().equals("student")) {
				btnReservation.setBounds(screenUtil.getScreenWidth() / 2 - 165, screenUtil.getScreenHeight() / 2 - 50,
						160, 100);
			} else {
				btnReservation.setBounds(screenUtil.getScreenWidth() / 2 - 80, screenUtil.getScreenHeight() / 2 - 50,
						160, 100);
			}
		}
		mainPanel.validate();
		mainPanel.repaint();
	}
} //로그인할 때 나오는 
