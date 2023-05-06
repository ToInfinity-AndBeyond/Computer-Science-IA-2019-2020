package interfaces;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import launcher.Launcher;
import managers.UserManager;
import models.UserModel;
import utils.FileUtil;
import utils.ScreenUtil;
import utils.Security;

public class SignUpInterface {
	private JLabel lbUsername, lbPw, lbPwRe, lbName, lbType;
	public static JTextField tfUsername, tfName;
	public static JPasswordField pfPasswd, pfPasswdRe;
	public static JComboBox<String> cbType;
	public static JButton btnBack, btnSignup;

	private JPanel signupPanel;

	private String typeList[] = { "Select", "student", "teacher", "parent" };

	public SignUpInterface() {
	}

	public SignUpInterface(ScreenUtil screenUtil) {
		signupPanel = new JPanel();
		signupPanel.setBounds(0, 0, screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		signupPanel.setLayout(null);

		lbUsername = new JLabel("ID");
		lbUsername.setBounds(screenUtil.getScreenWidth() / 2 - screenUtil.getLabelWidth() - 10,
				screenUtil.getScreenHeight() / 2 - ScreenUtil.HEIGHT - 20, screenUtil.getLabelWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(lbUsername);

		lbPw = new JLabel("PW");
		lbPw.setBounds(lbUsername.getX(), lbUsername.getY() + ScreenUtil.HEIGHT + 10, screenUtil.getLabelWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(lbPw);

		lbPwRe = new JLabel("Re Enter PW");
		lbPwRe.setBounds(lbUsername.getX(), lbPw.getY() + ScreenUtil.HEIGHT + 10, screenUtil.getLabelWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(lbPwRe);

		lbName = new JLabel("Name");
		lbName.setBounds(lbUsername.getX(), lbPwRe.getY() + ScreenUtil.HEIGHT + 10, screenUtil.getLabelWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(lbName);

		lbType = new JLabel("Type");
		lbType.setBounds(lbUsername.getX(), lbName.getY() + ScreenUtil.HEIGHT + 10, screenUtil.getLabelWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(lbType);

		tfUsername = new JTextField(15);
		tfUsername.setBounds(lbUsername.getX() + lbUsername.getWidth() + 10, lbUsername.getY(), screenUtil.getTfWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(tfUsername);

		pfPasswd = new JPasswordField(15);
		pfPasswd.setBounds(lbPw.getX() + lbPw.getWidth() + 10, lbPw.getY(), screenUtil.getTfWidth(), ScreenUtil.HEIGHT);
		signupPanel.add(pfPasswd);

		pfPasswdRe = new JPasswordField(15);
		pfPasswdRe.setBounds(lbPwRe.getX() + lbPwRe.getWidth() + 10, lbPwRe.getY(), screenUtil.getTfWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(pfPasswdRe);

		tfName = new JTextField(15);
		tfName.setBounds(lbName.getX() + lbName.getWidth() + 10, lbName.getY(), screenUtil.getTfWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(tfName);

		cbType = new JComboBox<String>(typeList);
		cbType.setBounds(lbType.getX() + lbType.getWidth() + 10, lbType.getY(), screenUtil.getTfWidth(),
				ScreenUtil.HEIGHT);
		signupPanel.add(cbType);

		btnSignup = new JButton("Sign Up");
		btnSignup.setBounds(screenUtil.getScreenWidth() / 2 - screenUtil.getBtnWidth() - 5,
				lbType.getY() + ScreenUtil.HEIGHT + 15, screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		signupPanel.add(btnSignup);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(screenUtil.getScreenWidth() / 2 + 5, lbType.getY() + ScreenUtil.HEIGHT + 15,
				screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		signupPanel.add(btnBack);
	}

	public JPanel getPanel() {
		return signupPanel;
	}

	public boolean signup() {
		String username = tfUsername.getText();
		String passwd = Security.encrypt(pfPasswd.getText().toString());
		String name = tfName.getText();
		String type = (String) cbType.getSelectedItem();
		if (username.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter your username!");
		} else if (UserManager.findUser(username) > -1) {
			JOptionPane.showMessageDialog(null, "The username is already in use!");
		} else if (pfPasswd.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter your password!");
		} else if (!pfPasswd.getText().toString().equals(pfPasswdRe.getText().toString())) {
			JOptionPane.showMessageDialog(null, "Please confirm your password!");
		} else if (name.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter your name!");
		} else if (cbType.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(null, "Please select your account type!");
		} else {
			JOptionPane.showMessageDialog(null, String.format("Thank you for registration, %s!", name));

			Launcher.userList.add(new UserModel(username, passwd, name, type));
			FileUtil.saveUserList();

			tfUsername.setText("");
			pfPasswd.setText("");
			pfPasswdRe.setText("");
			tfName.setText("");

			return true;
		}
		return false;
	}
}
