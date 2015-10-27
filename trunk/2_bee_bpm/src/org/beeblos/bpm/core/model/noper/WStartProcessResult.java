package org.beeblos.bpm.core.model.noper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.beeblos.bpm.core.model.enumerations.StartProcessResult;
import org.joda.time.DateTime;

import com.sp.common.util.StringPair;

/**
 * Object to return the start process result.
 * Data to return will be:
 * - start success / failed
 * - datetime created
 * - qty wStepWork created
 * - id list for wStepWorkCreated
 * - Error List (String)
 * 
 * 
 * @author nestor
 *
 */
public class WStartProcessResult {
	
	/**
	 * the result of the start process event
	 */
	StartProcessResult startProcessResult;
	
	/**
	 * creation datetime persisted
	 */
	DateTime creationDate;
	
	/**
	 * qty created stepWorks
	 */
	Integer qtyStepWorkCreated;
	
	/**
	 * idStepwork list
	 */
	List<Integer> stepWorkIdList;
	
	/**
	 * Error / Warning list...
	 */
	List<StringPair> errorList;

	public WStartProcessResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * create element with empty arrays already created...
	 * @param vacio
	 */
	public WStartProcessResult(boolean vacio) {
		super();
		this.errorList=new ArrayList<StringPair>();
		this.stepWorkIdList= new ArrayList<Integer>();
	}

	public WStartProcessResult(StartProcessResult startProcessResult,
			DateTime creationDate, Integer qtyStepWorkCreated) {
		this.startProcessResult = startProcessResult;
		this.creationDate = creationDate;
		this.qtyStepWorkCreated = qtyStepWorkCreated;
	}

	public WStartProcessResult(StartProcessResult startProcessResult,
			DateTime creationDate, Integer qtyStepWorkCreated,
			List<Integer> stepWorkIdList, List<StringPair> errorList) {
		this.startProcessResult = startProcessResult;
		this.creationDate = creationDate;
		this.qtyStepWorkCreated = qtyStepWorkCreated;
		this.stepWorkIdList = stepWorkIdList;
		this.errorList = errorList;
	}

	public void setParams(StartProcessResult startProcessResult,
			DateTime creationDate, Integer qtyStepWorkCreated,
			List<Integer> stepWorkIdList, List<StringPair> errorList) {
		this.startProcessResult = startProcessResult;
		this.creationDate = creationDate;
		this.qtyStepWorkCreated = qtyStepWorkCreated;
		this.stepWorkIdList = stepWorkIdList;
		this.errorList = errorList;
	}
	
	/**
	 * @return the startProcessResult
	 */
	public StartProcessResult getStartProcessResult() {
		return startProcessResult;
	}

	/**
	 * @param startProcessResult the startProcessResult to set
	 */
	public void setStartProcessResult(StartProcessResult startProcessResult) {
		this.startProcessResult = startProcessResult;
	}

	/**
	 * @return the creationDate
	 */
	public DateTime getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the qtyStepWorkCreated
	 */
	public Integer getQtyStepWorkCreated() {
		return qtyStepWorkCreated;
	}

	/**
	 * @param qtyStepWorkCreated the qtyStepWorkCreated to set
	 */
	public void setQtyStepWorkCreated(Integer qtyStepWorkCreated) {
		this.qtyStepWorkCreated = qtyStepWorkCreated;
	}

	/**
	 * @return the stepWorkIdList
	 */
	public List<Integer> getStepWorkIdList() {
		return stepWorkIdList;
	}

	/**
	 * @param stepWorkIdList the stepWorkIdList to set
	 */
	public void setStepWorkIdList(List<Integer> stepWorkIdList) {
		this.stepWorkIdList = stepWorkIdList;
	}

	/**
	 * @return the errorList
	 */
	public List<StringPair> getErrorList() {
		return errorList;
	}

	/**
	 * @param errorList the errorList to set
	 */
	public void setErrorList(List<StringPair> errorList) {
		this.errorList = errorList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((errorList == null) ? 0 : errorList.hashCode());
		result = prime
				* result
				+ ((qtyStepWorkCreated == null) ? 0 : qtyStepWorkCreated
						.hashCode());
		result = prime
				* result
				+ ((startProcessResult == null) ? 0 : startProcessResult
						.hashCode());
		result = prime * result
				+ ((stepWorkIdList == null) ? 0 : stepWorkIdList.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WStartProcessResult))
			return false;
		WStartProcessResult other = (WStartProcessResult) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (errorList == null) {
			if (other.errorList != null)
				return false;
		} else if (!errorList.equals(other.errorList))
			return false;
		if (qtyStepWorkCreated == null) {
			if (other.qtyStepWorkCreated != null)
				return false;
		} else if (!qtyStepWorkCreated.equals(other.qtyStepWorkCreated))
			return false;
		if (startProcessResult != other.startProcessResult)
			return false;
		if (stepWorkIdList == null) {
			if (other.stepWorkIdList != null)
				return false;
		} else if (!stepWorkIdList.equals(other.stepWorkIdList))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 2;
		return "WStartProcessResult ["
				+ (startProcessResult != null ? "startProcessResult="
						+ startProcessResult + ", " : "")
				+ (creationDate != null ? "creationDate=" + creationDate + ", "
						: "")
				+ (qtyStepWorkCreated != null ? "qtyStepWorkCreated="
						+ qtyStepWorkCreated + ", " : "")
				+ (stepWorkIdList != null ? "stepWorkIdList="
						+ stepWorkIdList.subList(0,
								Math.min(stepWorkIdList.size(), maxLen)) + ", "
						: "")
				+ (errorList != null ? "errorList="
						+ errorList.subList(0,
								Math.min(errorList.size(), maxLen)) : "") + "]";
	}
	
	

}
