package interfaces;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import launcher.Launcher;
import managers.ReservationManager;
import models.EnqueueModel;
import models.ReservationModel;
import utils.FileUtil;
import utils.ScreenUtil;

public class ManageInterface {
	public static JButton[][] btnBlockList = new JButton[9][7];
	public static JButton btnPrevious, btnNext, btnBack;
	public static String[] dateList = new String[7];

	private JLabel[] lbDayList = new JLabel[7];

	public static ReservationModel[][] reservationList = new ReservationModel[9][7];

	public static int roomNum;

	private JPanel reservationPanel, daysPanel, blockPanel, timePanel;

	private String[] days = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

	private Calendar calendar;
	private String today;
	private SimpleDateFormat dateFormat;

	public ManageInterface() {
	}

	public ManageInterface(ScreenUtil screenUtil) {
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
				btnBlockList[i][j].setEnabled(false);
				btnBlockList[i][j].setText("");
				if (j < 6) {
					reservationList[i][j] = null;
				}
			}
		}
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			if (reservation.getDate().compareTo(today) < 0 || reservation.getDate().compareTo(dateList[6]) > 0) {
				continue;
			} else {
				if (reservation.getRoomNum() == roomNum) {
					for (int j = 0; j < dateList.length; j++) {
						if (dateList[j].equals(reservation.getDate())) {
							btnBlockList[reservation.getBlock() - 1][j].setText(reservation.getName());
							btnBlockList[reservation.getBlock() - 1][j].setEnabled(true);
							reservationList[reservation.getBlock() - 1][j] = reservation;
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



	public ReservationModel enqueue(int day, int block) {
		EnqueueModel queue = ReservationManager.getNextQueue(dateList[day], block);
		ReservationModel reservation = null;
		if (queue != null) {
			reservation = new ReservationModel(queue.getUsername(), queue.getName(), queue.getType(), queue.getDate(),
					roomNum, queue.getBlock(), queue.getNumPeople(), queue.getReason());
			Launcher.reservationList.add(reservation);
			FileUtil.saveReservationList();
		}
		return reservation;
	}

	public int getIndex(int day, int block) {
		System.out.println(Launcher.reservationList.size());
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			System.out.println(reservation.getBlock() + " | " + (block + 1) + " | " + reservation.getDate() + " | "
					+ dateList[day] + " | " + reservation.getRoomNum() + " | " + roomNum);
			if (reservation.getBlock() == block + 1 && reservation.getDate().equals(dateList[day])
					&& reservation.getRoomNum() == roomNum) {
				return i;
			}
		}
		return -1;
	}
}
