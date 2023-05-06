package interfaces;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import launcher.Launcher;
import utils.FileUtil;
import utils.ScreenUtil;

public class FreeBlockInterface {
	private JButton[][] btnBlockList = new JButton[9][5];
	public static JButton btnBack, btnConfirm;
	public static String[] dateList = new String[5];

	private JPanel freeBlockPanel, daysPanel, blockPanel, timePanel;

	private String[] days = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri" };

	public FreeBlockInterface() {
	}

	public FreeBlockInterface(ScreenUtil screenUtil) {
		freeBlockPanel = new JPanel();
		freeBlockPanel.setBounds(0, 0, screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		freeBlockPanel.setLayout(null);

		daysPanel = new JPanel();
		daysPanel.setBounds(30 + screenUtil.getBtnWidth(), 0,
				screenUtil.getScreenWidth() - 60 - screenUtil.getBtnWidth() * 2, 80);
		daysPanel.setLayout(new GridLayout(1, 5));
		for (int i = 0; i < days.length; i++) {
			JLabel tempLabel = new JLabel(days[i]);
			tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
			daysPanel.add(tempLabel);
		}
		freeBlockPanel.add(daysPanel);

		timePanel = new JPanel();
		timePanel.setBounds(0, daysPanel.getHeight(), 30 + screenUtil.getBtnWidth(),
				screenUtil.getScreenHeight() - daysPanel.getHeight() * 2 - 60);
		timePanel.setLayout(new GridLayout(9, 1));
		for (int i = 1; i <= 9; i++) {
			JLabel tempLabel = new JLabel("Block " + i);
			tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
			timePanel.add(tempLabel);
		}
		freeBlockPanel.add(timePanel);

		blockPanel = new JPanel();
		blockPanel.setBounds(daysPanel.getX(), timePanel.getY(), daysPanel.getWidth(), timePanel.getHeight());
		blockPanel.setLayout(new GridLayout(9, 5));
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				btnBlockList[i][j] = new JButton();
				blockPanel.add(btnBlockList[i][j]);
			}
		}
		freeBlockPanel.add(blockPanel);

		btnBack = new JButton("Back");
		btnBack.setBounds(15, blockPanel.getY() + blockPanel.getHeight() + 20, screenUtil.getScreenWidth() / 2 - 30,
				ScreenUtil.HEIGHT * 2);
		freeBlockPanel.add(btnBack);

		btnConfirm = new JButton("Save");
		btnConfirm.setBounds(screenUtil.getScreenWidth() / 2 + 15, blockPanel.getY() + blockPanel.getHeight() + 20,
				screenUtil.getScreenWidth() / 2 - 30, ScreenUtil.HEIGHT * 2);
		freeBlockPanel.add(btnConfirm);
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				btnBlockList[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String text = ((JButton) e.getSource()).getText();
						if (text.equals("")) {
							((JButton) e.getSource()).setText("Free");
						} else {
							((JButton) e.getSource()).setText("");
						}
					}
				});
			}
		}
	}

	public JPanel getPanel() {
		return freeBlockPanel;
	}

	public void setBlockList() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				btnBlockList[i][j].setText("");
			}
		}
		for (int i = 0; i < Launcher.sessionUtil.getFreeBlockList().size(); i++) {
			String[] temp = Launcher.sessionUtil.getFreeBlockList().get(i).split("_");
			int day = Integer.valueOf(temp[0]).intValue();
			int block = Integer.valueOf(temp[1]).intValue();
			btnBlockList[block][day].setText("Free");
		}
	}
	
	public void save() {
		Launcher.sessionUtil.getFreeBlockList().clear();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 5; j++) {
				if (!btnBlockList[i][j].getText().equals("")) {
					Launcher.sessionUtil.getFreeBlockList().add(j+"_"+i);
				}
			}
		}
		FileUtil.saveFreeBlockList();
		JOptionPane.showMessageDialog(null, "Free blocks are saved!");
	}
}
