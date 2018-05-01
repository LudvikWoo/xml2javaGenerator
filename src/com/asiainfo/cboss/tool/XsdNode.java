package com.asiainfo.cboss.tool;


public class XsdNode implements Comparable{
	
	public static final String ROOT_NAME = "SvcCont";
	
	public static final String CON_ONLYONE = "1";
	public static final String CON_ONEMORE = "+,£«,£«";
	public static final String CON_ONEMORE2 = "£«";
	public static final String CON_ZEROONE = "?,£¿";
	public static final String CON_ZEROONE2 = "£¿";
	public static final String CON_CHIOCE = "^,¡­¡­";
	public static final String CON_CHIOCE2 = "¡­¡­";
	public static final String CON_ZEROMORE = "*,£ª";
	public static final String CON_ZEROMORE2 = "£ª";	
	
	public static final String TYPE_STRING = "String";
	public static final String TYPE_NUMBER = "NUM";
	public static final String TYPE_COMPLX = "-,¡ª,£­,¡ª¡ª";
	public static final String TYPE_COMPLX2 = "£­";
	
	public static final String LENGTH_FIX = "F";
	public static final String LENGTH_VAR = "V";
	public static final String LENGTH_COMPLX = "-,¡ª,£­,¡ª¡ª";
	public static final String LENGTH_COMPLX2 = "£­";
	
	
	private int fileNo;//  FILE_NO     NUMBER not null,
	private String fileName;//  FILE_NAME   VARCHAR2(100) not null,
	private String nodeNo;//  NODE_NO     VARCHAR2(50) not null,
	private String pNodeName;//  P_NODE_NAME VARCHAR2(50) not null,
	private String nodeName;//  NODE_NAME   VARCHAR2(50) not null,
	private String constrain;//  CONSTRAIN   VARCHAR2(1) not null,
	private String dataType;//  DATA_TYPE   VARCHAR2(20) not null,
	private String length;//  LENGTH      VARCHAR2(10) not null,
	private String descript;//  DESCRIPT    VARCHAR2(50) not null,
	private String notes;//  NOTES

	public XsdNode() {
		super();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

	public String getConstrain() {
		return constrain;
	}

	public void setConstrain(String constrain) {
		this.constrain = constrain;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileNo() {
		return fileNo;
	}

	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeNo() {
		return nodeNo;
	}

	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPNodeName() {
		return pNodeName;
	}

	public void setPNodeName(String nodeName) {
		pNodeName = nodeName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if(!( o instanceof XsdNode)){
			return -1;
		}
		XsdNode other = (XsdNode) o;
//		String[] sNos = this.getNodeNo().split(".");
//		String[] oNos = other.getNodeNo().split(".");
//		int len = sNos.length;
//		if(oNos.length < len) len = oNos.length;
//		for(int i = 0; i < len; i ++){
//			if(sNos[i].equals(oNos[i])){
//				continue;
//			}
//			return sNos[i].compareTo(oNos[i]);
//		}
//		return this.getNodeNo().length() - other.getNodeNo().length();
		int ret = this.getNodeNo().length() - other.getNodeNo().length();
		if(ret != 0) return ret;
		return this.getNodeNo().compareTo(other.getNodeNo());
	}	

}



