package org.beeblos.bpm.core.util.castor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.beeblos.bpm.core.model.ObjectM;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WProcessRole;
import org.beeblos.bpm.core.model.WProcessUser;
import org.beeblos.bpm.core.model.WRoleDef;
import org.beeblos.bpm.core.model.WStepAssignedDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepResponseDef;
import org.beeblos.bpm.core.model.WStepRole;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.model.WStepUser;
import org.beeblos.bpm.core.model.WStepWork;
import org.beeblos.bpm.core.model.WStepWorkAssignment;
import org.beeblos.bpm.core.model.WTimeUnit;
import org.beeblos.bpm.core.model.WTrackWork;
import org.beeblos.bpm.core.model.WUserDef;
import org.beeblos.bpm.core.model.WUserRole;
import org.beeblos.bpm.core.model.WUseridmapper;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.tools.MappingTool;

public class CastorMappingToolUtil {
	
	@SuppressWarnings("unchecked")
	public static void generate(Class clazz) throws MappingException, FileNotFoundException {
		
	    MappingTool tool = new MappingTool();
	    tool.setInternalContext(new org.castor.xml.BackwardCompatibilityContext());
	    
	    tool.addClass(clazz);
	    
	    String className = clazz.getName().substring( clazz.getName().lastIndexOf(".") + 1 );
	    OutputStream file = new FileOutputStream("/home/u097/workspace/bee_bpm/autogenerate-xmlcastor/"+className+"_castor.xml"); 
	
	    // genera mapping XML del objeto
	    Writer writer = new OutputStreamWriter(file);
	    try {
		    tool.write(writer);
		    
		    System.out.println(writer.toString());
	
		    writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    // motrar mapping XML generado
		FileReader fr = new FileReader("/home/u097/workspace/bee_bpm/autogenerate-xmlcastor/"+className+"_castor.xml");
		BufferedReader br = new BufferedReader(fr);
		String s;
		try {
			while((s = br.readLine()) != null) {
				System.out.println(s);
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    try {
	
	    	System.out.println("... Iniciando PROCESO");
	    	
	    	// genera mapping de estas clases de package org.beeblos.bpm.core.model;
	    	CastorMappingToolUtil.generate(ObjectM.class);
	    	CastorMappingToolUtil.generate(WProcessDef.class);
	    	CastorMappingToolUtil.generate(WProcessRole.class);
	    	CastorMappingToolUtil.generate(WProcessUser.class);
	    	CastorMappingToolUtil.generate(WRoleDef.class);
	    	CastorMappingToolUtil.generate(WStepAssignedDef.class);
	    	CastorMappingToolUtil.generate(WStepDef.class);
	    	CastorMappingToolUtil.generate(WStepResponseDef.class);
	    	CastorMappingToolUtil.generate(WStepRole.class);
	    	CastorMappingToolUtil.generate(WStepSequenceDef.class);
	    	CastorMappingToolUtil.generate(WStepUser.class);
	    	CastorMappingToolUtil.generate(WStepWork.class);
	    	CastorMappingToolUtil.generate(WStepWorkAssignment.class);
	    	CastorMappingToolUtil.generate(WTimeUnit.class);
	    	CastorMappingToolUtil.generate(WTrackWork.class);
	    	CastorMappingToolUtil.generate(WUserDef.class);
	    	CastorMappingToolUtil.generate(WUseridmapper.class);
	    	CastorMappingToolUtil.generate(WUserRole.class);    	
	        
	    	System.out.println("... Fin del PROCESO");
	        
	    } catch (MappingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
