package interfaces;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import launcher.Launcher;
import managers.UserManager;
import utils.ScreenUtil;
import utils.Security;

public class LoginInterface {
	private JLabel lbId, lbPw;
	public static JTextField tfUsername;
	public static JPasswordField pfPasswd;
	public static JButton btnSignup;
	public static JButton btnLogin;

	private JPanel loginPanel;

	public LoginInterface() {
	}

	public LoginInterface(ScreenUtil screenUtil) {
		loginPanel = new JPanel();
		loginPanel.setBounds(0, 0, screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		loginPanel.setLayout(null);

		lbId = new JLabel("ID");
		lbId.setBounds(screenUtil.getScreenWidth() / 2 - screenUtil.getLabelWidth() - 10,
				screenUtil.getScreenHeight() / 2 - ScreenUtil.HEIGHT - 20, screenUtil.getLabelWidth(),
				ScreenUtil.HEIGHT);
		loginPanel.add(lbId);

		lbPw = new JLabel("PW");
		lbPw.setBounds(screenUtil.getScreenWidth() / 2 - screenUtil.getLabelWidth() - 10,
				lbId.getY() + ScreenUtil.HEIGHT + 10, screenUtil.getLabelWidth(), ScreenUtil.HEIGHT);
		loginPanel.add(lbPw);

		tfUsername = new JTextField(15);
		tfUsername.setBounds(lbId.getX() + lbId.getWidth() + 10, lbId.getY(), screenUtil.getTfWidth(),
				ScreenUtil.HEIGHT);
		loginPanel.add(tfUsername);

		pfPasswd = new JPasswordField(15);
		pfPasswd.setBounds(lbPw.getX() + lbPw.getWidth() + 10, lbPw.getY(), screenUtil.getTfWidth(), ScreenUtil.HEIGHT);
		loginPanel.add(pfPasswd);

		btnSignup = new JButton("Sign Up");
		btnSignup.setBounds(screenUtil.getScreenWidth() / 2 - screenUtil.getBtnWidth() - 5,
				lbPw.getY() + ScreenUtil.HEIGHT + 15, screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		loginPanel.add(btnSignup);

		btnLogin = new JButton("Login");
		btnLogin.setBounds(screenUtil.getScreenWidth() / 2 + 5, lbPw.getY() + ScreenUtil.HEIGHT + 15,
				screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		loginPanel.add(btnLogin);
		tfUsername.setText("librarian");
		pfPasswd.setText("1234");
	}

	public JPanel getPanel() {
		return loginPanel;
	}

	public boolean login() {
		String username = tfUsername.getText();
		String passwd = Security.encrypt(pfPasswd.getText().toString());
		if (username.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter your username!");
		} else if (pfPasswd.getPassword().length == 0) {
			JOptionPane.showMessageDialog(null, "Please enter your password!");
		} else if (!UserManager.isMatched(username, passwd)) {
			JOptionPane.showMessageDialog(null, "Your username or password is not validated!");
		} else {
			tfUsername.setText("");
			pfPasswd.setText("");
			return true;
		}
		return false;
	}
}
