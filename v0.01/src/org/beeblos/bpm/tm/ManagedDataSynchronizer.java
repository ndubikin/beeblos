package org.beeblos.bpm.tm;

import org.beeblos.bpm.core.error.ManagedDataSynchronizerException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.enumerations.ProcessStage;

public interface ManagedDataSynchronizer {

	public void synchronizeProcessWorkManagedData(
			WProcessWork processWork, ManagedData md, ProcessStage stage, Integer externalUserId )  // nes 20140707
					throws ManagedDataSynchronizerException;
	
	public Object syncrhonizeField(
			WProcessWork work, WProcessDataField mdf, ProcessStage stage, ManagedData md, Integer externalUserId ) // nes 20140707
					throws ManagedDataSynchronizerException;

}