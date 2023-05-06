package managers;

import java.util.ArrayList;

import interfaces.EnqueueInterface;
import launcher.Launcher;
import models.EnqueueModel;
import models.ReservationModel;
import utils.FileUtil;

public class ReservationManager {
	public static void setLeftRooms() {
		for (int i = 0; i < Launcher.reservationList.size(); i++) {
			ReservationModel reservation = Launcher.reservationList.get(i);
			String temp = reservation.getDate() + "_" + reservation.getBlock();
			if (!EnqueueInterface.checkList.contains(temp)) { // block마다 남은 방의 개수; reservaton된 데이터가 있는지 없는지 체
				ArrayList<Integer> tempArray = new ArrayList<>();
				tempArray.add(Integer.valueOf(reservation.getRoomNum()));
				EnqueueInterface.checkList.add(temp);
				EnqueueInterface.hashMap.put(temp, tempArray); // 이미 없으면 이렇게 해주고 else
			} else {
				EnqueueInterface.hashMap.get(temp).add(Integer.valueOf(reservation.getRoomNum())); 
			}
		}
	}

	public static int countQueue(String date, int block) {
		int count = 1;
		for (int i = 0; i < Launcher.enqueueList.size(); i++) {
			EnqueueModel enqueue = Launcher.enqueueList.get(i);
			if (enqueue.getDate().equals(date) && enqueue.getBlock() == block) {
				if (count < enqueue.getPriority()) {
					count = enqueue.getPriority();
				}
			}//특정한 날짜에서 특정한 block을 parameter로 받을 때 여기서도 priority가 있음. teacher의 경우에는 가장 우선순위) priority를 가져오기 (현재큐의 개수)
		}
		return count;
	}

	public static EnqueueModel getNextQueue(String date, int block) {
		for (int i = 0; i < Launcher.enqueueList.size(); i++) {
			EnqueueModel queue = Launcher.enqueueList.get(i);
			System.out.println(queue.getDate() + " | " + date + " | " + queue.getBlock() + " | " + queue.getStatus());
			if (queue.getDate().equals(date) && queue.getBlock() == block && queue.getStatus() == 'N') {
				Launcher.enqueueList.get(i).setStatus('Y');
				FileUtil.saveQueueList();
				return queue; // 앞의 누군가가 librarian에 의해서 강제로 퇴출당했을 때 다음 사람이 오게 됌. 퇴출당한 사람은 n, 들어올 사람은 y가 
			}
		}
		return null;
	}
}
