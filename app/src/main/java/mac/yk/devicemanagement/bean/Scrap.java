package mac.yk.devicemanagement.bean;

import java.util.Date;

public class Scrap {
      int Did,Dname;
      String user,remark;
		Date scrapDate;
      public Scrap(){

	  }
	public Scrap(int did, int dname, String user, String remark) {
		super();
		Did = did;
		Dname = dname;
		this.user = user;
		this.remark = remark;
	}
	public int getDid() {
		return Did;
	}
	public void setDid(int did) {
		Did = did;
	}
	public int getDname() {
		return Dname;
	}
	public void setDname(int dname) {
		Dname = dname;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getScrapDate() {
		return scrapDate;
	}

	public void setScrapDate(Date scrapDate) {
		this.scrapDate = scrapDate;
	}
}