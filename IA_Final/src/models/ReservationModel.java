package models;

public class ReservationModel {
	private String username;
	private String name;
	private String type;
	private String date;
	private int roomNum;
	private int block;
	private int numPeople;
	private String reason;

	public ReservationModel() {
	}

	public ReservationModel(String username, String name, String type, String date, int roomNum, int block,
			int numPeople, String reason) {
		super();
		this.username = username;
		this.name = name;
		this.type = type;
		this.date = date;
		this.roomNum = roomNum;
		this.block = block;
		this.numPeople = numPeople;
		this.reason = reason;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(int roomNum) {
		this.roomNum = roomNum;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}
	
	public int getNumPeople() {
		return numPeople;
	}
	
	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
}
