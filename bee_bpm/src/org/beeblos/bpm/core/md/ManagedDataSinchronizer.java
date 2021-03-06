package org.beeblos.bpm.core.md;

import org.beeblos.bpm.core.error.ManagedDataSynchronizerException;
import org.beeblos.bpm.core.model.WProcessDataField;
import org.beeblos.bpm.core.model.WProcessWork;
import org.beeblos.bpm.core.model.enumerations.ProcessStage;

public interface ManagedDataSinchronizer {

	public abstract Object syncrhonizeField(WProcessWork work, 
			WProcessDataField mdf, ProcessStage stage ) throws ManagedDataSynchronizerException;

}