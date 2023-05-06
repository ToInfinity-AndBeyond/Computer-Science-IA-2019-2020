package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import launcher.Launcher;
import models.EnqueueModel;
import models.ReservationModel;
import models.UserModel;

public class FileUtil {
	public static void saveUserList() {
		ArrayList<String> dataList = new ArrayList<>();
		for (int i = 0; i < Launcher.userList.size(); i++) {
			UserModel user = Launcher.userList.get(i);
			String line = user.getUsername() + "|" + user.getPasswd() + "|" + user.getName() + "|" + user.getType();
			dataList.add(line);
		}
		fileWriter(dataList, "user.data");
	}

	public static void saveReservationList() {
		ArrayList<String> dataList = new ArrayList<>();
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			String temp = reservation.getUsername() + "|" + reservation.getName() + "|" + reservation.getType() + "|"
					+ reservation.getDate() + "|" + reservation.getRoomNum() + "|" + reservation.getBlock() + "|"
					+ reservation.getNumPeople() + "|" + reservation.getReason();
			dataList.add(temp);
		}
		fileWriter(dataList, "reservation.data");
	}

	public static void saveQueueList() {
		ArrayList<String> dataList = new ArrayList<>();
		for (int i = 0; i < Launcher.enqueueList.size(); i++) {
			EnqueueModel enqueue = Launcher.enqueueList.get(i);
			String temp = enqueue.getUsername() + "|" + enqueue.getName() + "|" + enqueue.getType() + "|"
					+ enqueue.getDate() + "|" + enqueue.getBlock() + "|" + enqueue.getNumPeople() + "|"
					+ enqueue.getReason() + "|" + enqueue.getPriority() + "|" + enqueue.getStatus();
			dataList.add(temp);
		}
		fileWriter(dataList, "queue.data");
	}

	public static void saveFreeBlockList() {
		String username = Launcher.sessionUtil.getUsername();
		ArrayList<String> dataList = new ArrayList<>();
		try {
			File file = new File("raw/free_block.data");
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			String line = "";
			while ((line = bf.readLine()) != null) {
				String[] temp = line.split("\\|");
				String tempUsername = temp[0];
				if (!username.equals(tempUsername)) {
					dataList.add(line);
				}
			}
			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
		}
		for (int i = 0; i < Launcher.sessionUtil.getFreeBlockList().size(); i++) {
			String[] temp = Launcher.sessionUtil.getFreeBlockList().get(i).split("_");
			String freeBlock = username + "|" + temp[1] + "|" + temp[0];
			dataList.add(freeBlock);
		}
		fileWriter(dataList, "free_block.data");
	}

	public static void fileWriter(ArrayList<String> dataList, String filename) {
		FileOutputStream out;
		try {
			out = new FileOutputStream("raw/" + filename);
			String data = "";
			for (int i = 0; i < dataList.size(); i++) {
				data += dataList.get(i) + "\n";
			}
			if (data.length() > 0) {
				data = data.substring(0, data.length() - 1);
			}
			byte[] totalBytes = data.getBytes();
			out.write(totalBytes);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readUserFile() {
		try {
			File file = new File("raw/user.data");
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			String line = "";
			while ((line = bf.readLine()) != null) {
				String[] temp = line.split("\\|");
				String username = temp[0];
				String passwd = temp[1];
				String name = temp[2];
				String type = temp[3];
				UserModel user = new UserModel(username, passwd, name, type);
				Launcher.userList.add(user);
			}
			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void readFreeBlockFile(String username) {
		ArrayList<String> freeBlockList = new ArrayList<>();
		try {
			File file = new File("raw/free_block.data");
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			String line = "";
			while ((line = bf.readLine()) != null) {
				String[] temp = line.split("\\|");
				String tempUsername = temp[0];
				int block = Integer.valueOf(temp[1]).intValue();
				int day = Integer.valueOf(temp[2]).intValue();
				if (username.equals(tempUsername)) {
					freeBlockList.add(day + "_" + block);
				}
			}
			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
		}
		Launcher.sessionUtil.setFreeBlockList(freeBlockList);
	}

	public static void readReservedFile() {
		try {
			File file = new File("raw/reservation.data");
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			String line = "";
			while ((line = bf.readLine()) != null) {
				String[] temp = line.split("\\|");
				String username = temp[0];
				String name = temp[1];
				String type = temp[2];
				String date = temp[3];
				int roomNum = Integer.valueOf(temp[4]).intValue();
				int block = Integer.valueOf(temp[5]).intValue();
				int numPeople = Integer.valueOf(temp[6]).intValue();
				String reason = temp[7];
				Launcher.reservationList
						.add(new ReservationModel(username, name, type, date, roomNum, block, numPeople, reason));
			}
			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void readQueueFile() {
		try {
			File file = new File("raw/queue.data");
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);
			String line = "";
			while ((line = bf.readLine()) != null) {
				String[] temp = line.split("\\|");
				String username = temp[0];
				String name = temp[1];
				String type = temp[2];
				String date = temp[3];
				int block = Integer.valueOf(temp[4]).intValue();
				int numPeople = Integer.valueOf(temp[5]).intValue();
				String reason = temp[6];
				int priority = Integer.valueOf(temp[7]).intValue();
				char status = temp[8].charAt(0);
				Launcher.enqueueList
						.add(new EnqueueModel(username, name, type, date, block, numPeople, reason, priority, status));
			}
			bf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
