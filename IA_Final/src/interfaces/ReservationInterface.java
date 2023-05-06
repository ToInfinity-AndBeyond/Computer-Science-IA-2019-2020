package interfaces;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import launcher.Launcher;
import models.ReservationModel;
import utils.FileUtil;
import utils.ScreenUtil;

public class ReservationInterface {
	public static JButton[][] btnBlockList = new JButton[9][7];
	public static JButton btnPrevious, btnNext, btnBack;
	public static String[] dateList = new String[7];

	private JLabel[] lbDayList = new JLabel[7];

	public static int roomNum;

	private JPanel reservationPanel, daysPanel, blockPanel, timePanel;

	private String[] days = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

	private Calendar calendar;
	private String today;
	private SimpleDateFormat dateFormat;

	public ReservationInterface() {
	}

	public ReservationInterface(ScreenUtil screenUtil) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		today = dateFormat.format(Calendar.getInstance().getTime());
		calendar = Calendar.getInstance();

		reservationPanel = new JPanel();
		reservationPanel.setBounds(0, 0, screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		reservationPanel.setLayout(null);

		daysPanel = new JPanel();
		daysPanel.setBounds(30 + screenUtil.getBtnWidth(), 0,
				screenUtil.getScreenWidth() - 60 - screenUtil.getBtnWidth() * 2, 80);
		daysPanel.setLayout(new GridLayout(1, 7));
		for (int i = 0; i < days.length; i++) {
			lbDayList[i] = new JLabel(days[i]);
			lbDayList[i].setHorizontalAlignment(SwingConstants.CENTER);
			daysPanel.add(lbDayList[i]);
		}
		reservationPanel.add(daysPanel);

		timePanel = new JPanel();
		timePanel.setBounds(0, daysPanel.getHeight(), 30 + screenUtil.getBtnWidth(),
				screenUtil.getScreenHeight() - daysPanel.getHeight() * 2 - 60);
		timePanel.setLayout(new GridLayout(9, 1));
		for (int i = 1; i <= 9; i++) {
			JLabel tempLabel = new JLabel("Block " + i);
			tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
			timePanel.add(tempLabel);
		}
		reservationPanel.add(timePanel);

		blockPanel = new JPanel();
		blockPanel.setBounds(daysPanel.getX(), timePanel.getY(), daysPanel.getWidth(), timePanel.getHeight());
		blockPanel.setLayout(new GridLayout(9, 7));
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 7; j++) {
				btnBlockList[i][j] = new JButton();
				btnBlockList[i][j].setName(i + "-" + j);
				blockPanel.add(btnBlockList[i][j]);
			}
		}
		reservationPanel.add(blockPanel);

		btnPrevious = new JButton("Last Week");
		btnPrevious.setBounds(15, daysPanel.getHeight() / 2 - ScreenUtil.HEIGHT / 2, screenUtil.getBtnWidth(),
				ScreenUtil.HEIGHT);
		reservationPanel.add(btnPrevious);

		btnNext = new JButton("Next Week");
		btnNext.setBounds(daysPanel.getX() + daysPanel.getWidth() + 15,
				daysPanel.getHeight() / 2 - ScreenUtil.HEIGHT / 2, screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		reservationPanel.add(btnNext);

		btnBack = new JButton("Back");
		btnBack.setBounds(15, blockPanel.getY() + blockPanel.getHeight() + 20, screenUtil.getScreenWidth() - 30,
				ScreenUtil.HEIGHT * 2);
		reservationPanel.add(btnBack);
	}

	public JPanel getPanel() {
		return reservationPanel;
	}

	public void setBlockList() {
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		for (int i = 0; i < 7; i++) {
			calendar.add(Calendar.DATE, 1);
			dateList[i] = dateFormat.format(calendar.getTime());
			lbDayList[i].setText(days[i] + "\n" + dateList[i]);
		}
		calendar.add(Calendar.DATE, -7);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 7; j++) {
				btnBlockList[i][j].setEnabled(true);
				btnBlockList[i][j].setText("");
				if (dateList[j].compareTo(today) < 0) {
					btnBlockList[i][j].setEnabled(false);
					continue;
				}
				if (j == 6 || (Launcher.sessionUtil.getType().equals("student")
						&& !Launcher.sessionUtil.getFreeBlockList().contains(j + "_" + (i + 1)))) {
					if (btnBlockList[i][j].isEnabled()) {
						btnBlockList[i][j].setEnabled(false);
					} //일요일이거 학생이면서 freeblock에 해당하지 않을 경
				}
			}
		}
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			if (reservation.getDate().compareTo(today) < 0 || reservation.getDate().compareTo(dateList[6]) > 0) {
				continue; // 넘어간다 
			} else {
				if (reservation.getRoomNum() == roomNum) {
					for (int j = 0; j < dateList.length; j++) {
						if (dateList[j].equals(reservation.getDate())) {
							if (reservation.getUsername().equals(Launcher.sessionUtil.getUsername())) {
								btnBlockList[reservation.getBlock() - 1][j].setText(reservation.getName()); //예약한 사람 넣어주
							} else {
								btnBlockList[reservation.getBlock() - 1][j].setText("Reserved");
								if (Launcher.sessionUtil.getType().equals("teacher")
										&& reservation.getType().equals("student")) {
									btnBlockList[reservation.getBlock() - 1][j].setEnabled(true);
								} // 본인이 선님인데 학생인 경우에는 Kick하고 들어갈 수 있어야
							}
							if (!(Launcher.sessionUtil.getType().equals("teacher")
									&& reservation.getType().equals("student"))
									&& btnBlockList[reservation.getBlock() - 1][j].isEnabled()) {
								btnBlockList[reservation.getBlock() - 1][j].setEnabled(false);
							} //학생은 다른 학생 못
							break;
						}
					}
				}
			}
		}
		reservationPanel.validate();
		reservationPanel.repaint();
	}

	public void nextWeek() {
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		setBlockList();
	}

	public void lastWeek() {
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		setBlockList();
	}

	public void resetDate() {
		today = dateFormat.format(Calendar.getInstance().getTime());
		calendar = Calendar.getInstance();
	}

	public void save(int block, int day, int numPeople, String reason) {
		Launcher.reservationList
				.add(new ReservationModel(Launcher.sessionUtil.getUsername(), Launcher.sessionUtil.getName(),
						Launcher.sessionUtil.getType(), dateList[day], roomNum, block, numPeople, reason));
		FileUtil.saveReservationList();

		JOptionPane.showMessageDialog(null, "Your reservation is successful!");
	}

	public int getIndex(int day, int block) {
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			if (reservation.getBlock() == block + 1 && reservation.getDate().equals(dateList[day])
					&& reservation.getRoomNum() == roomNum) {
				return i;
			}
		}
		return -1;
	}
}
