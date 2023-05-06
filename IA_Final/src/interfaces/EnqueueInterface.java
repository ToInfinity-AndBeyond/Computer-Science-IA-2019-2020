package interfaces;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import launcher.Launcher;
import managers.ReservationManager;
import models.EnqueueModel;
import models.ReservationModel;
import utils.FileUtil;
import utils.ScreenUtil;

public class EnqueueInterface {
	public static JButton[][] btnBlockList = new JButton[9][7];
	public static JButton btnPrevious, btnNext, btnBack, btnEnque;
	public static String[] dateList = new String[7];

	private JLabel[] lbDayList = new JLabel[7];

	private JPanel enqueuePanel, daysPanel, blockPanel, timePanel;

	private String[] days = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

	private Calendar calendar;
	private String today;
	private SimpleDateFormat dateFormat;

	public static HashMap<String, ArrayList<Integer>> hashMap = new HashMap<>();
	public static ArrayList<String> checkList = new ArrayList<>();

	public EnqueueInterface() {
	}

	public EnqueueInterface(ScreenUtil screenUtil) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		today = dateFormat.format(Calendar.getInstance().getTime());
		calendar = Calendar.getInstance();

		enqueuePanel = new JPanel();
		enqueuePanel.setBounds(0, 0, screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		enqueuePanel.setLayout(null);

		daysPanel = new JPanel();
		daysPanel.setBounds(30 + screenUtil.getBtnWidth(), 0,
				screenUtil.getScreenWidth() - 60 - screenUtil.getBtnWidth() * 2, 80);
		daysPanel.setLayout(new GridLayout(1, 7));
		for (int i = 0; i < days.length; i++) {
			lbDayList[i] = new JLabel(days[i]);
			lbDayList[i].setHorizontalAlignment(SwingConstants.CENTER);
			daysPanel.add(lbDayList[i]);
		}
		enqueuePanel.add(daysPanel);

		timePanel = new JPanel();
		timePanel.setBounds(0, daysPanel.getHeight(), 30 + screenUtil.getBtnWidth(),
				screenUtil.getScreenHeight() - daysPanel.getHeight() * 2 - 60);
		timePanel.setLayout(new GridLayout(9, 1));
		for (int i = 1; i <= 9; i++) {
			JLabel tempLabel = new JLabel("Block " + i);
			tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
			timePanel.add(tempLabel);
		}
		enqueuePanel.add(timePanel);

		blockPanel = new JPanel();
		blockPanel.setBounds(daysPanel.getX(), timePanel.getY(), daysPanel.getWidth(), timePanel.getHeight());
		blockPanel.setLayout(new GridLayout(9, 7));
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 7; j++) {
				btnBlockList[i][j] = new JButton();
				btnBlockList[i][j].setName("enqueue-" + i + "-" + j);
				blockPanel.add(btnBlockList[i][j]);
			}
		}
		enqueuePanel.add(blockPanel);

		btnBack = new JButton("Back");
		btnBack.setBounds(15, blockPanel.getY() + blockPanel.getHeight() + 20, screenUtil.getScreenWidth() - 30,
				ScreenUtil.HEIGHT * 2);
		enqueuePanel.add(btnBack);

		btnPrevious = new JButton("Last Week");
		btnPrevious.setBounds(15, daysPanel.getHeight() / 2 - ScreenUtil.HEIGHT / 2, screenUtil.getBtnWidth(),
				ScreenUtil.HEIGHT);
		enqueuePanel.add(btnPrevious);

		btnNext = new JButton("Next Week");
		btnNext.setBounds(daysPanel.getX() + daysPanel.getWidth() + 15,
				daysPanel.getHeight() / 2 - ScreenUtil.HEIGHT / 2, screenUtil.getBtnWidth(), ScreenUtil.HEIGHT);
		enqueuePanel.add(btnNext);
	}

	public JPanel getPanel() {
		return enqueuePanel;
	}

	public void setBlockList() {
		hashMap.clear();
		checkList.clear();
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		for (int i = 0; i < 7; i++) {
			calendar.add(Calendar.DATE, 1);
			dateList[i] = dateFormat.format(calendar.getTime());
			lbDayList[i].setText(days[i] + "\n" + dateList[i]);
		} //date조
		calendar.add(Calendar.DATE, -7);
		ReservationManager.setLeftRooms();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 7; j++) {
				btnBlockList[i][j].setEnabled(true);
				btnBlockList[i][j].setText("");
				String temp = dateList[j] + "_" + (i + 1);
				if (dateList[j].compareTo(today) < 0 || j == 6 || (Launcher.sessionUtil.getType().equals("student")
						&& !Launcher.sessionUtil.getFreeBlockList().contains(j + "_" + (i + 1)))) {
					btnBlockList[i][j].setEnabled(false); //오늘 날짜랑 비교해서 날짜 지나가면 setEnabled false
				}
				if (checkList.contains(temp)) {
					if (Launcher.sessionUtil.getType().equals("parent")
							&& hashMap.get(dateList[j] + "_" + (i + 1)).contains(Integer.valueOf(1))
							&& hashMap.get(dateList[j] + "_" + (i + 1)).contains(Integer.valueOf(2))
							|| hashMap.get(temp).size() == 8) {
						btnBlockList[i][j].setText("FULL");
					} else {
						btnBlockList[i][j].setText(String.valueOf(8 - hashMap.get(temp).size()) + " LEFT");
					}
				} else { //parent일 경우에는 roomNo1, roomNo2가 모든 블럭마다방이 가득 찼
					btnBlockList[i][j].setText("8 LEFT");
				}//방이비어있는지 체크하는 것 
			}
		}
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			if (reservation.getDate().compareTo(today) < 0 || reservation.getDate().compareTo(dateList[6]) > 0) {
				continue;
			}
			if (reservation.getUsername().equals(Launcher.sessionUtil.getUsername())) {
				for (int j = 0; j < 7; j++) {
					if (reservation.getDate().equals(dateList[j])) {
						if (btnBlockList[reservation.getBlock() - 1][j].isEnabled()) {
							btnBlockList[reservation.getBlock() - 1][j].setText("Reserved");
							btnBlockList[reservation.getBlock() - 1][j].setEnabled(false);
						} //한마디로 예약상태보여주는 
					}
				}
			}
		}
		enqueuePanel.validate();
		enqueuePanel.repaint();
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
		int roomNum = 0;
		String temp = dateList[day] + "_" + block;
		if (checkList.contains(temp)) {
			for (int i = 1; i <= 8; i++) {
				if (checkList.contains(temp) && !hashMap.get(temp).contains(Integer.valueOf(i))) {
					roomNum = i;
					break;
				}//예약한 정보 save되는 
			}
		} else {
			roomNum = 1;
		}
		Launcher.reservationList
				.add(new ReservationModel(Launcher.sessionUtil.getUsername(), Launcher.sessionUtil.getName(),
						Launcher.sessionUtil.getType(), dateList[day], roomNum, block, numPeople, reason));
		FileUtil.saveReservationList();

		JOptionPane.showMessageDialog(null, "Your reservation is successful!");
	}

	public void enqueue(int block, int day, int numPeople, String reason) {
		Launcher.enqueueList.add(new EnqueueModel(Launcher.sessionUtil.getUsername(), Launcher.sessionUtil.getName(),
				Launcher.sessionUtil.getType(), dateList[day], block, numPeople, reason,
				ReservationManager.countQueue(dateList[day], block + 1), 'N'));
		FileUtil.saveQueueList();

		JOptionPane.showMessageDialog(null, "Your reservation is successful!");
	}

	public static boolean isBlockFull(int block, int day) {
		String temp = dateList[day] + "_" + block;
		if (checkList.contains(temp)) {
			return Launcher.sessionUtil.getType().equals("parent")
					&& hashMap.get(dateList[day] + "_" + block).contains(Integer.valueOf(1))
					&& hashMap.get(dateList[day] + "_" + block).contains(Integer.valueOf(2))
					|| hashMap.get(temp).size() == 8;
		} else {
			return false;
		}
	}

	public static int isStudentExists(int block, int day) {
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			if (reservation.getDate().equals(dateList[day]) && reservation.getBlock() == block
					&& reservation.getType().equals("student")) {
				return reservation.getRoomNum(); // 학생이 있는지를 조사하는 것, 선생이 enque할 때대기가 아니라 바로 Kick 해버리
			}
		}
		return -1;
	}

	public static int getIndex(int day, int block) {
		int index = isStudentExists(block, day);
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			System.out.println(reservation.getDate() + " | " + dateList[day] + " | " + reservation.getBlock() + " | "
					+ reservation.getRoomNum() + " | " + index);
			if (reservation.getBlock() == block && reservation.getDate().equals(dateList[day])
					&& reservation.getRoomNum() == index) {
				return i; //student가 있을 경우에 해당 날짜 해당 블럭에, 그 student에 대한 Index가져와서 (이거 쓰이는데 찾아보기) --> kick 하고 나서 학생에 대한 정를 Remove할 때 그 inde찾아서 remove
			}
		}
		return -1;
	}
}
