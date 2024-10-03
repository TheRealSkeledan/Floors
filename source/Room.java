public class Room {
	private String name;
	private int floorNum;
	private int maxFurniture, maxClosets;
	private boolean isBossRoom;
	
	public Room() {
		floorNum = 0000;
	}

	public static int[][] createTestRoom() {
		int[][] testMap = {
			{1, 1, 1, 1, 1, 1, 1},
			{1, 0, 0, 0, 0, 0, 0},
			{1, 0, 0, 0, 0, 0, 0},
			{1, 0, 0, 1, 1, 1, 1},
			{1, 0, 0, 1, 0, 0, 0},
			{1, 0, 0, 1, 0, 0, 0},
			{1, 0, 0, 1, 0, 0, 0}
		};

		return testMap;
	}

	public void floorUp() {
		floorNum++;
	}

	public int getFloor() {
		return floorNum;
	}

	public String getFloorNumber() {
		if(floorNum >= 10)
			return "00" + floorNum;
		if(floorNum == 100) 
			return "0" + floorNum;
		return "000" + floorNum;
	}
}