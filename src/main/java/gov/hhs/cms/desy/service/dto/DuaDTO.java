/**
 * 
 */
package gov.hhs.cms.desy.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import gov.hhs.cms.desy.service.util.DateFunctions;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuaDTO implements Serializable {
	private static final long serialVersionUID = 797614956650564341L;
	
	private int duaNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date expirationDate;
	@SafeHtml
	private String requestor;
	@SafeHtml
	private String studyName;
	private boolean returnRequired;
	private char encryptionSwitch;
	@SafeHtml
	@JsonIgnore
	private String encryptionDesc;
	@JsonIgnore
	private char ftapeSwitch;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date desyExpirationDate;
	@JsonIgnore
	private boolean isDuaExpired;
	@JsonIgnore
	private boolean isDuaToExpire;
	@JsonIgnore
	private long duaExpDays;
	@SafeHtml
	@JsonIgnore
	private String duaToExpireMessage;
	@JsonIgnore
	private boolean isDuaClosed;
	@JsonIgnore
	private boolean isSupressFlag;

	//the below properties are added for JSON conversion
	@JsonIgnore
	private Date formattedExpirationDate;

	@JsonIgnore
	private Date formattedDesyExpirationDate;
	@JsonIgnore
	private boolean foriegnTape;
	
	/**
	 * @return
	 */
	public Date getDesyExpirationDate() {
		return desyExpirationDate;
	}

	/**
	 * @return
	 */
	public int getDuaNumber() {
		return duaNumber;
	}

	/**
	 * @return
	 */
	public char getEncryptionSwitch() {
		return encryptionSwitch;
	}

	/**
	 * @return
	 */
	public String getEncryptionDesc() {
		return encryptionDesc;
	}
	/**
	 * @return
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @return
	 */
	public char getFtapeSwitch() {
		return ftapeSwitch;
	}

	/**
	 * @return
	 */
	public String getRequestor() {
		return requestor;
	}

	/**
	 * @return
	 */
	public boolean getReturnRequired() {
		return returnRequired;
	}

	/**
	 * @return
	 */
	public String getStudyName() {
		return studyName;
	}

	public String getFormattedExpirationDate() {
		return DateFunctions.formatDate(expirationDate);	
	}
	/**
	 * @return
	 */
	public String getFormattedDesyExpirationDate() {
		return DateFunctions.formatDate(desyExpirationDate);
	}
	/**
	 * @param date
	 */
	public void setDesyExpirationDate(Date date1) {
		setFormattedDesyExpirationDate(date1);		
		if(date1!=null)
		{
			desyExpirationDate = date1;
			Date d= new Date();			
			GregorianCalendar gc = new GregorianCalendar(1900,1,1);			
			if(!(date1.toString().equalsIgnoreCase(gc.toString()))  && date1.before(d) )
	    		isDuaExpired=true;
			
			duaExpDays = (date1.getTime() - d.getTime()) / (1000 * 60 * 60 * 24);
			
			isDuaToExpire = ((duaExpDays>0 && duaExpDays<60))?true:false;
			
		}
	}

	
	
	/**
	 * @param formattedDesyExpirationDate the formattedDesyExpirationDate to set
	 */
	public void setFormattedDesyExpirationDate(Date date1) {
		if(date1!=null)
		{
			desyExpirationDate = date1;
			Date d= new Date();			
			GregorianCalendar gc = new GregorianCalendar(1900,1,1);			
			if(!(date1.toString().equalsIgnoreCase(gc.toString()))  && date1.before(d) )
	    		isDuaExpired=true;
			
			duaExpDays = (date1.getTime() - d.getTime()) / (1000 * 60 * 60 * 24);
			
			if(duaExpDays>0 && duaExpDays<60)
			{
				isDuaToExpire=true;
			}
			
		}
	}

	/**
	 * @param i
	 */
	public void setDuaNumber(int i) {
		duaNumber = i;
	}

	/**
	 * @param char
	 */
	public void setEncryptionSwitch(char c) {
		encryptionSwitch = c;
		
		// set encryption description 
		switch(c) {
			case 'E':
				setEncryptionDesc("ENCRYPTED");	
				break;
			case 'I':
				setEncryptionDesc("IDENTIFIABLE");	
				break;
			case 'L':
				setEncryptionDesc("LIMITED DATASET");
				break;
			// ETS-1503: add description for 'M' type.
			case 'M':
				setEncryptionDesc("MODIFIED LDS");
				break;	
		} // end-switch	
	}

	/**
	 * @param string
	 */
	public void setEncryptionDesc(String s) {
		encryptionDesc = s;
	}
	/**
	 * @param date
	 */
	public void setExpirationDate(Date date) {
		expirationDate = date;
		setFormattedExpirationDate(expirationDate);
	}
	
	/**
	 * @param formattedExpirationDate the formattedExpirationDate to set
	 */
	public void setFormattedExpirationDate(Date formattedExpirationDate) {
		this.formattedExpirationDate = formattedExpirationDate;
	}

	/**
	 * @param string
	 */
	public void setFtapeSwitch(char c) {
		ftapeSwitch = c;
	}

	/**
	 * @param string
	 */
	public void setRequestor(String string) {
		requestor = string;
	}

	/**
	 * @param string
	 */
	public void setReturnRequired(boolean b) {
		returnRequired = b;
	}

	/**
	 * @param string
	 */
	public void setStudyName(String string) {
		studyName = string;
	}

	/**
	 * @return
	 */
	public boolean isDuaExpired() {
		return isDuaExpired;
	}

	/**
	 * @param b
	 */
	public void setDuaExpired(boolean b) {
		isDuaExpired = b;
	}

	public boolean isForiegnTape()
	{
		boolean ftapeFlag=false;
		if(ftapeSwitch=='Y' || ftapeSwitch=='y' )
		{
			ftapeFlag=true;
		}else if (ftapeSwitch=='N' || ftapeSwitch=='n')
		{
			ftapeFlag=false;
		}
		return ftapeFlag;
		
	}

	
	
	/**
	 * @param foriegnTape the foriegnTape to set
	 */
	public void setForiegnTape(boolean foriegnTape) {
		this.foriegnTape = false;
	}

	/**
	 * @return
	 */
	public boolean isDuaToExpire() {
		return isDuaToExpire;
	}

	/**
	 * @param b
	 */
	public void setDuaToExpire(boolean b) {
		isDuaToExpire = b;
	}

	public boolean isDuaClosed() {
		return isDuaClosed;
	}


	public boolean isSupressFlag() {
		return isSupressFlag;
	}


	/**
	 * @param b
	 */

	public void setDuaClosed(boolean b) {
		isDuaClosed = b;
	}

	public void setSupressFlag(boolean b) {
		isSupressFlag = b;
	}

	/**
	 * @return
	 */
	public long getDuaExpDays() {
		return duaExpDays;
	}

	/**
	 * @param i
	 */
	public void setDuaExpDays(long l) {
		duaExpDays = l;
	}

	/**
	 * @return
	 */
	public String getDuaToExpireMessage() {
		return duaToExpireMessage;
	}

	/**
	 * @param string
	 */
	public void setDuaToExpireMessage(String msg) {
		duaToExpireMessage = "";
		if(msg != null) {
			int index = (msg != null) ?msg.indexOf('*'):0;
			
			String dayString = (duaExpDays*1 + 1) + " day";
			if (duaExpDays > 0)
					dayString += 's';
			duaToExpireMessage=msg.substring(0, index) +  dayString + msg.substring(index + 1);			
		}

	}
	
}
