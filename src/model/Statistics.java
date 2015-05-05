package model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;

public class Statistics extends Observable{

	private int xmlTime, sqlTime, couchDbTime, txtTime, totalTime, mergeTime;
	private String xmlTimeTxt, sqlTimeTxt, couchDbTimeTxt, txtTimeTxt, totalTimeTxt, mergeTimeTxt;
	private int xmlNumber, sqlNumber, couchDbNumber, txtNumber;
	private Calendar xmlBegin, xmlEnd, sqlBegin, sqlEnd, couchDbBegin, couchDbEnd, txtBegin, txtEnd;
	private Calendar mergeBegin, mergeEnd;
	private Calendar totalBegin, totalEnd;
	private int totalNumber;
	
	public Statistics() {
		init();
	}
	
	private void init(){
		this.xmlTime = 0;
		this.sqlTime = 0;
		this.couchDbTime = 0;
		this.txtTime = 0;
		this.xmlNumber = 0;
		this.sqlNumber = 0;
		this.couchDbNumber = 0;
		this.txtNumber = 0;
		this.totalNumber = 0;
		this.totalTime = 0;
		this.xmlBegin = GregorianCalendar.getInstance();
		this.xmlEnd = GregorianCalendar.getInstance();
		this.sqlBegin = GregorianCalendar.getInstance();
		this.sqlEnd = GregorianCalendar.getInstance();
		this.couchDbBegin = GregorianCalendar.getInstance();
		this.couchDbEnd = GregorianCalendar.getInstance();
		this.txtBegin = GregorianCalendar.getInstance();
		this.txtEnd = GregorianCalendar.getInstance();
		this.totalBegin = GregorianCalendar.getInstance();
		this.totalEnd = GregorianCalendar.getInstance();
		this.mergeBegin = GregorianCalendar.getInstance();
		this.mergeEnd = GregorianCalendar.getInstance();
//		this.xmlTimeTxt = "";
//		this.sqlTimeTxt = "";
//		this.couchDbTimeTxt = "";
//		this.txtTimeTxt = "";
		getText();
	}

	public void execute(){
		xmlTime = (int) (xmlEnd.getTimeInMillis() - xmlBegin.getTimeInMillis());
		sqlTime = (int) (sqlEnd.getTimeInMillis() - sqlBegin.getTimeInMillis());
		couchDbTime = (int) (sqlEnd.getTimeInMillis() - couchDbBegin.getTimeInMillis());
		txtTime = (int) (txtEnd.getTimeInMillis() - txtBegin.getTimeInMillis());
		totalTime = (int) (totalEnd.getTimeInMillis() - totalBegin.getTimeInMillis());
		mergeTime = (int) (mergeEnd.getTimeInMillis() - mergeBegin.getTimeInMillis());
		getText();
		setChanged();
		notifyObservers();
	}
	
	private void getText(){
		xmlTimeTxt = xmlTime / 1000 + "s "+ xmlTime % 1000+ "ms";
		sqlTimeTxt = sqlTime/1000 + "s "+ sqlTime%1000+"ms";
		couchDbTimeTxt = couchDbTime/1000+"s "+couchDbTime%1000+"ms";
		txtTimeTxt = txtTime/1000+"s "+txtTime%1000+"ms";
		totalTimeTxt = totalTime/1000+"s "+totalTime%1000+"ms";
		mergeTimeTxt = mergeTime/1000+"s "+mergeTime%1000+"ms";
	}
	
	public int getXmlTime() {
		return xmlTime;
	}

	public void setXmlTime(int xmlTime) {
		this.xmlTime = xmlTime;
	}

	public int getSqlTime() {
		return sqlTime;
	}

	public void setSqlTime(int sqlTime) {
		this.sqlTime = sqlTime;
	}

	public int getCouchDbTime() {
		return couchDbTime;
	}

	public void setCouchDbTime(int couchDbTime) {
		this.couchDbTime = couchDbTime;
	}

	public int getTxtTime() {
		return txtTime;
	}

	public void setTxtTime(int txtTime) {
		this.txtTime = txtTime;
	}

	public int getXmlNumber() {
		return xmlNumber;
	}

	public void setXmlNumber(int xmlNumber) {
		this.xmlNumber = xmlNumber;
	}

	public int getSqlNumber() {
		return sqlNumber;
	}

	public void setSqlNumber(int sqlNumber) {
		this.sqlNumber = sqlNumber;
	}

	public int getCouchDbNumber() {
		return couchDbNumber;
	}

	public void setCouchDbNumber(int couchDbNumber) {
		this.couchDbNumber = couchDbNumber;
	}

	public int getTxtNumber() {
		return txtNumber;
	}

	public void setTxtNumber(int txtNumber) {
		this.txtNumber = txtNumber;
	}

	public Calendar getXmlBegin() {
		return xmlBegin;
	}

	public void setXmlBegin(Calendar xmlBegin) {
		this.xmlBegin = xmlBegin;
	}

	public Calendar getXmlEnd() {
		return xmlEnd;
	}

	public void setXmlEnd(Calendar xmlEnd) {
		this.xmlEnd = xmlEnd;
	}

	public Calendar getSqlBegin() {
		return sqlBegin;
	}

	public void setSqlBegin(Calendar sqlBegin) {
		this.sqlBegin = sqlBegin;
	}

	public Calendar getSqlEnd() {
		return sqlEnd;
	}

	public void setSqlEnd(Calendar sqlEnd) {
		this.sqlEnd = sqlEnd;
	}

	public Calendar getCouchDbBegin() {
		return couchDbBegin;
	}

	public void setCouchDbBegin(Calendar couchDbBegin) {
		this.couchDbBegin = couchDbBegin;
	}

	public Calendar getCouchDbEnd() {
		return couchDbEnd;
	}

	public void setCouchDbEnd(Calendar couchDbEnd) {
		this.couchDbEnd = couchDbEnd;
	}

	public Calendar getTxtBegin() {
		return txtBegin;
	}

	public void setTxtBegin(Calendar txtBegin) {
		this.txtBegin = txtBegin;
	}

	public Calendar getTxtEnd() {
		return txtEnd;
	}

	public void setTxtEnd(Calendar txtEnd) {
		this.txtEnd = txtEnd;
	}

	public String getXmlTimeTxt() {
		return xmlTimeTxt;
	}

	public void setXmlTimeTxt(String xmlTimeTxt) {
		this.xmlTimeTxt = xmlTimeTxt;
	}

	public String getSqlTimeTxt() {
		return sqlTimeTxt;
	}

	public void setSqlTimeTxt(String sqlTimeTxt) {
		this.sqlTimeTxt = sqlTimeTxt;
	}

	public String getCouchDbTimeTxt() {
		return couchDbTimeTxt;
	}

	public void setCouchDbTimeTxt(String couchDbTimeTxt) {
		this.couchDbTimeTxt = couchDbTimeTxt;
	}

	public String getTxtTimeTxt() {
		return txtTimeTxt;
	}

	public void setTxtTimeTxt(String txtTimeTxt) {
		this.txtTimeTxt = txtTimeTxt;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public String getTotalTimeTxt() {
		return totalTimeTxt;
	}

	public void setTotalTimeTxt(String totalTimeTxt) {
		this.totalTimeTxt = totalTimeTxt;
	}

	public Calendar getTotalBegin() {
		return totalBegin;
	}

	public void setTotalBegin(Calendar totalBegin) {
		this.totalBegin = totalBegin;
	}

	public Calendar getTotalEnd() {
		return totalEnd;
	}

	public void setTotalEnd(Calendar totalEnd) {
		this.totalEnd = totalEnd;
	}

	public Calendar getMergeBegin() {
		return mergeBegin;
	}

	public void setMergeBegin(Calendar mergeBegin) {
		this.mergeBegin = mergeBegin;
	}

	public Calendar getMergeEnd() {
		return mergeEnd;
	}

	public void setMergeEnd(Calendar mergeEnd) {
		this.mergeEnd = mergeEnd;
	}

	public int getMergeTime() {
		return mergeTime;
	}

	public void setMergeTime(int mergeTime) {
		this.mergeTime = mergeTime;
	}

	public String getMergeTimeTxt() {
		return mergeTimeTxt;
	}

	public void setMergeTimeTxt(String mergeTimeTxt) {
		this.mergeTimeTxt = mergeTimeTxt;
	}
}
