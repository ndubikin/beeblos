package org.beeblos.bpm.tm;

import org.beeblos.bpm.core.error.ManagedDataSynchronizerException;
import org.beeblos.bpm.core.model.ManagedData;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.enumerations.ProcessStage;

public interface ManagedDataSynchronizer {

	public Object syncrhonizeField(
			WProcessWork work, WProcessDataField mdf, ProcessStage stage, ManagedData md) 
					throws ManagedDataSynchronizerException;

}