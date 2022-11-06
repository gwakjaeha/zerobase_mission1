package data;

public class HistoryDTO {
	private int id;
	private double lnt;
	private double lat;
	private String viewDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLnt() {
		return lnt;
	}
	public void setLnt(double lnt) {
		this.lnt = lnt;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getViewDate() {
		return viewDate;
	}
	public void setViewDate(String viewDate) {
		this.viewDate = viewDate;
	}
	
	
}
