package launcher;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import interfaces.EnqueueInterface;
import interfaces.FreeBlockInterface;
import interfaces.LoginInterface;
import interfaces.MainInterface;
import interfaces.ManageInterface;
import interfaces.ReservationInterface;
import interfaces.RoomListInterface;
import interfaces.SignUpInterface;
import managers.ReservationManager;
import models.EnqueueModel;
import models.ReservationModel;
import models.UserModel;
import utils.FileUtil;
import utils.ScreenUtil;
import utils.SessionUtil;

public class Launcher extends JFrame {
	private static final long serialVersionUID = 3189130355195067920L;
	private ScreenUtil screenUtil;

	private LoginInterface loginInterface;
	private SignUpInterface signupInterface;
	private MainInterface mainInterface;
	private RoomListInterface roomListInterface;
	private ReservationInterface reservationInterface;
	private FreeBlockInterface freeBlockInterface;
	private EnqueueInterface enqueueInterface;
	private ManageInterface manageInterface;

	private JPanel loginPanel, signupPanel, mainPanel, roomListPanel, reservationPanel, freeBlockPanel, enqueuePanel,
			managePanel;

	public static SessionUtil sessionUtil = new SessionUtil();

	public static ArrayList<UserModel> userList = new ArrayList<>();
	public static ArrayList<ReservationModel> reservationList = new ArrayList<>();
	public static ArrayList<EnqueueModel> enqueueList = new ArrayList<>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Launcher();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Launcher() {
		init();

		setSize(screenUtil.getScreenWidth(), screenUtil.getScreenHeight());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(loginPanel);
		setVisible(true);
	}

	private void init() {
		screenUtil = new ScreenUtil(Toolkit.getDefaultToolkit().getScreenSize());

		loginInterface = new LoginInterface(screenUtil);
		signupInterface = new SignUpInterface(screenUtil);
		mainInterface = new MainInterface(screenUtil);
		roomListInterface = new RoomListInterface(screenUtil);
		reservationInterface = new ReservationInterface(screenUtil);
		freeBlockInterface = new FreeBlockInterface(screenUtil);
		enqueueInterface = new EnqueueInterface(screenUtil);
		manageInterface = new ManageInterface(screenUtil);

		loginPanel = loginInterface.getPanel();
		signupPanel = signupInterface.getPanel();
		mainPanel = mainInterface.getPanel();
		roomListPanel = roomListInterface.getPanel();
		reservationPanel = reservationInterface.getPanel();
		freeBlockPanel = freeBlockInterface.getPanel();
		enqueuePanel = enqueueInterface.getPanel();
		managePanel = manageInterface.getPanel();

		initEvents();

		FileUtil.readUserFile();
		FileUtil.readReservedFile();
		FileUtil.readQueueFile();
	}

	private void initEvents() {
		addLoginEvent();
		addSignupEvent();
		addMainEvent();
		addRoomListEvent();
		addReservationEvent();
		addFreeBlockEvent();
		addEnqueueEvent();
		addManageEvent();
	}

	//한 창에서 다 되는거고, changePanel 계속 panel만바꿔줌; 계속 생성하는데는 계산이나 작업이 많이 들어가므로 기본적인 내용만 갖고 이게 하되 불필요한걸 최대한 줄인 
	private void addLoginEvent() {
		LoginInterface.btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loginInterface.login()) {
					mainInterface.setPanel(screenUtil);
					changePanel(mainPanel);
				}
			}
		});

		LoginInterface.btnSignup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(signupPanel);
			}
		});
	}

	private void addSignupEvent() {
		SignUpInterface.btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(loginPanel);
			}
		});

		SignUpInterface.btnSignup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (signupInterface.signup()) {
					changePanel(loginPanel);
				}
			}
		});
	}

	private void addMainEvent() {
		MainInterface.btnReservation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(roomListPanel);
				roomListInterface.setPanel();
			}
		});

		MainInterface.btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sessionUtil.logout();
				reservationInterface.resetDate();
				changePanel(loginPanel);
			}
		});

		MainInterface.btnAddFreeBlock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freeBlockInterface.setBlockList();
				changePanel(freeBlockPanel);
			}
		});

		MainInterface.btnManageReservation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(roomListPanel);
				roomListInterface.setPanel();
			}
		});
	}

	private void addRoomListEvent() { //interfaces에 대한 것. RoomListInterface에 대한 Event 저
		RoomListInterface.btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainInterface.setPanel(screenUtil);
				changePanel(mainPanel);
			}
		});

		for (int i = 0; i < RoomListInterface.btnRoomList.length; i++) {
			RoomListInterface.btnRoomList[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (sessionUtil.getType().equals("librarian")) {
						ManageInterface.roomNum = Integer.valueOf(((JButton) e.getSource()).getText().substring(5))
								.intValue();
						manageInterface.setBlockList(); // roomlist가 있을 때 librarin일 경우랑 일반 유저일 경우에 차이나는 것. reservationlist / manageinterface로 다르게 들어감 (다른 기능)
						changePanel(managePanel);
					} else {
						ReservationInterface.roomNum = Integer.valueOf(((JButton) e.getSource()).getText().substring(5))
								.intValue();
						reservationInterface.setBlockList();
						changePanel(reservationPanel);
					}
				}
			});
		}

		RoomListInterface.btnCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enqueueInterface.setBlockList();
				enqueueInterface.resetDate();
				changePanel(enqueuePanel);
			}
		});
	}

	private void addReservationEvent() {
		ReservationInterface.btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reservationInterface.resetDate();
				changePanel(roomListPanel);
				roomListInterface.setPanel();
			}
		});

		ReservationInterface.btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reservationInterface.nextWeek();
			}
		});

		ReservationInterface.btnPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reservationInterface.lastWeek();
			}
		});

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 6; j++) {
				ReservationInterface.btnBlockList[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] temp = ((JButton) e.getSource()).getName().split("-");
						int block = Integer.valueOf(temp[0]).intValue();
						int day = Integer.valueOf(temp[1]).intValue();
						if (((JButton) e.getSource()).getText().equals("Reserved")) {
							int result = JOptionPane.showConfirmDialog(null, "Will you force students to leave?",
									"Confirm", JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION) {
								String reason = "";
								int numPeople = 0;
								do {
									reason = JOptionPane.showInputDialog("Please enter the purpose.");
								} while (reason != null && reason.equals(""));
								if (reason != null) {
									do {
										try {
											String temp2 = JOptionPane.showInputDialog(
													"Please enter the number of people using this room includes you.");
											numPeople = Integer.valueOf(temp2).intValue();
										} catch (Exception ex) {
											JOptionPane.showMessageDialog(null, "Please enter the number!");
											ex.printStackTrace();
										}
									} while (numPeople == 0);
									if (numPeople > 0) {
										int index = reservationInterface.getIndex(day, block);
										reservationList.remove(index);
										reservationInterface.save(block + 1, day, numPeople, reason); //
										((JButton) ReservationInterface.btnBlockList[block][day])
												.setText(sessionUtil.getName()); //kickout하면 지가 들어
										((JButton) ReservationInterface.btnBlockList[block][day]).setEnabled(false);
									} // kickout --> 이건 teacher이 하는 
								}
							}
						} else {
							String reason = "";
							int numPeople = 0;
							do {
								reason = JOptionPane.showInputDialog("Please enter the purpose.");
							} while (reason != null && reason.equals(""));
							if (reason != null) {
								do {
									try {
										String temp2 = JOptionPane.showInputDialog(
												"Please enter the number of people using this room includes you.");
										numPeople = Integer.valueOf(temp2).intValue();
									} catch (Exception ex) {
										JOptionPane.showMessageDialog(null, "Please enter the number!");
										ex.printStackTrace();
									}
								} while (numPeople == 0);
								if (numPeople > 0) {
									reservationInterface.save(block + 1, day, numPeople, reason);
									((JButton) ReservationInterface.btnBlockList[block][day])
											.setText(sessionUtil.getName());
									((JButton) ReservationInterface.btnBlockList[block][day]).setEnabled(false);
								}
							}//일반적인 경우 (학생들도 부모님드ㅗ,쌤들도)
						}
					}
				});
			}
		}
	}

	private void addFreeBlockEvent() {
		FreeBlockInterface.btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(mainPanel);
			}
		});

		FreeBlockInterface.btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				freeBlockInterface.save();
				changePanel(mainPanel);
			}
		});
	}

	private void addEnqueueEvent() {
		EnqueueInterface.btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changePanel(roomListPanel);
			}
		});

		EnqueueInterface.btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enqueueInterface.nextWeek();
			}
		});

		EnqueueInterface.btnPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enqueueInterface.lastWeek();
			}
		});

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 6; j++) {
				EnqueueInterface.btnBlockList[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] temp = ((JButton) e.getSource()).getName().split("-");
						int block = Integer.valueOf(temp[1]).intValue();
						int day = Integer.valueOf(temp[2]).intValue();
						if (EnqueueInterface.isBlockFull(block + 1, day)) {
							int result = -1;
							int roomIndex = EnqueueInterface.isStudentExists(block + 1, day);
							int index = EnqueueInterface.getIndex(day, block + 1);
							System.out.println(index);
							if (sessionUtil.getType().equals("teacher") && roomIndex > -1) {
								result = JOptionPane.showConfirmDialog(null,
										"The rooms are full. Do you want to proceed after the queue occurs?", "Confirm",
										JOptionPane.YES_NO_OPTION);
							} else {
								result = JOptionPane.showConfirmDialog(null,
										"The rooms are full. Do you want to proceed after the queue occurs?", "Confirm",
										JOptionPane.YES_NO_OPTION);
							}
							if (result == JOptionPane.YES_OPTION) {
								String reason = "";
								int numPeople = 0;
								do {
									reason = JOptionPane.showInputDialog("Please enter the purpose.");
								} while (reason != null && reason.equals(""));
								if (reason != null) {
									do {
										try {
											String temp2 = JOptionPane.showInputDialog(
													"Please enter the number of people using this room includes you.");
											numPeople = Integer.valueOf(temp2).intValue();
										} catch (Exception ex) {
											JOptionPane.showMessageDialog(null, "Please enter the number!");
											ex.printStackTrace();
										}
									} while (numPeople == 0);
									if (numPeople > 0) {
										if (index > -1) {
											enqueueList.add(new EnqueueModel(sessionUtil.getUsername(), sessionUtil.getName(), sessionUtil.getType(),
													EnqueueInterface.dateList[day], block + 1, numPeople, reason, 1, 'N'));
											FileUtil.saveQueueList();
										} else {
											reservationList.add(new ReservationModel(sessionUtil.getUsername(),
													sessionUtil.getName(), sessionUtil.getType(),
													EnqueueInterface.dateList[day], roomIndex + 1, block + 1, numPeople,
													reason));
											FileUtil.saveReservationList();
										}
										((JButton) EnqueueInterface.btnBlockList[block][day]).setText("Reserved");
										((JButton) EnqueueInterface.btnBlockList[block][day]).setEnabled(false);
									}
								}
							}
						} else { // one이 비었으면 One으로 가고 등등.. numerical order
							int result = JOptionPane.showConfirmDialog(null,
									"Do you want to continue with the reservation process?", "Confirm",
									JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION) {
								String reason = "";
								int numPeople = 0;
								do {
									reason = JOptionPane.showInputDialog("Please enter the purpose.");
								} while (reason != null && reason.equals(""));
								if (reason != null) {
									do {
										try {
											String temp2 = JOptionPane.showInputDialog(
													"Please enter the number of people using this room includes you.");
											numPeople = Integer.valueOf(temp2).intValue();
										} catch (Exception ex) {
											JOptionPane.showMessageDialog(null, "Please enter the number!");
											ex.printStackTrace();
										}
									} while (numPeople == 0);
									if (numPeople > 0) {
										enqueueInterface.save(block + 1, day, numPeople, reason);
										enqueueInterface.setBlockList();
										((JButton) EnqueueInterface.btnBlockList[block][day]).setText("Reserved");
										((JButton) EnqueueInterface.btnBlockList[block][day]).setEnabled(false);
									}
								}
							}
						}
					}
				});
			}
		}
	}

	private void addManageEvent() {
		ManageInterface.btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manageInterface.resetDate();
				changePanel(roomListPanel);
				roomListInterface.setPanel();
			}
		});

		ManageInterface.btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manageInterface.nextWeek();
			}
		});

		ManageInterface.btnPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manageInterface.lastWeek();
			}
		});

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 6; j++) {
				ManageInterface.btnBlockList[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] temp = ((JButton) e.getSource()).getName().split("-");
						int block = Integer.valueOf(temp[0]).intValue();
						int day = Integer.valueOf(temp[1]).intValue();
						int result = JOptionPane.showConfirmDialog(null,
								ManageInterface.reservationList[block][day].getName() + " | "
										+ ManageInterface.reservationList[block][day].getNumPeople() + " | "
										+ ManageInterface.reservationList[block][day].getReason()
										+ " |\nAre you sure you want to force the student to leave?",
								"Confirm", JOptionPane.YES_NO_OPTION); // alarm 할거면 여기다 추가하 s 누르면 해당 학생 usernam 퇴실당한 이유 등.
						if (result == JOptionPane.YES_OPTION) {ㅌ
							int index = manageInterface.getIndex(day, block);
							reservationList.remove(index);
							ReservationModel reservation = manageInterface.enqueue(day, block + 1);
							if (reservation != null) {
								((JButton) ManageInterface.btnBlockList[block][day]).setText(reservation.getName());
								ManageInterface.reservationList[block][day] = reservation;
							} else {
								((JButton) ManageInterface.btnBlockList[block][day]).setText("");
								((JButton) ManageInterface.btnBlockList[block][day]).setEnabled(false);
								ManageInterface.reservationList[block][day] = null;
								FileUtil.saveReservationList(); //librarian 부
							}
						}
					}
				});
			}
		}
	}

	private void changePanel(JPanel panel) {
		setContentPane(panel);
		validate();
		repaint();
	}
}
