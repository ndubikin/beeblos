package org.beeblos.bpm.core.test.bl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.beeblos.bpm.core.bl.WProcessDefBL;
import org.beeblos.bpm.core.error.WProcessDefException;
import org.beeblos.bpm.core.error.WStepSequenceDefException;
import org.beeblos.bpm.core.graph.Layer;
import org.beeblos.bpm.core.graph.MxCell;
import org.beeblos.bpm.core.graph.MxGraphModel;
import org.beeblos.bpm.core.graph.Symbol;
import org.beeblos.bpm.core.graph.ElementWrapper;
import org.beeblos.bpm.core.graph.Workflow;
import org.beeblos.bpm.core.model.WProcessDef;
import org.beeblos.bpm.core.model.WStepDef;
import org.beeblos.bpm.core.model.WStepSequenceDef;
import org.beeblos.bpm.core.util.Constants;
import org.junit.Test;

public class XmlTest {

	@Test
	public void testeame() throws WStepSequenceDefException{

		try {

			WProcessDef pro = new WProcessDefBL().getWProcessDefByPK(1, 1000);
			pro.setWorkflowObj(new Workflow(
										pro.getComments(),
										pro.getXmlId(),
										pro.getId().toString(),
										pro.getProcess().getName()));
			
			JAXBContext jaxbContext = JAXBContext.newInstance(org.beeblos.bpm.core.graph.MxGraphModel.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Iterator<WStepDef> i = pro.getSteps().iterator();
			
			while(i.hasNext()){
				
				WStepDef wsd = i.next();
				if(wsd.getMxCellString() != null) {
					StringReader reader = new StringReader(wsd.getMxCellString());
					MxCell m = (MxCell)jaxbUnmarshaller.unmarshal(reader);
					wsd.setMxCellObject(m);
				}
			}

			Iterator<WStepSequenceDef> i2 = pro.getStepSequenceList().iterator();
			
			while(i2.hasNext()){
				
				WStepSequenceDef ssd = i2.next();
				
				JAXBContext jc = JAXBContext.newInstance(org.beeblos.bpm.core.graph.MxCell.class);
				Unmarshaller ju = jc.createUnmarshaller();

				StringReader r = new StringReader(ssd.getXmlMxCellString() == null || ssd.getXmlMxCellString().equals("") ? 
															Constants.DEFAULT_MXCELL_XML :
															ssd.getXmlMxCellString() );
				
				MxCell m = (MxCell)ju.unmarshal(r);
				
				ssd.setMxCell(m);
				
			}
			
			JAXBContext jaxbContext2 = JAXBContext.newInstance(org.beeblos.bpm.core.graph.ElementWrapper.class);
			Unmarshaller jaxbUnmarshaller2 = jaxbContext2.createUnmarshaller();
	
			StringReader sr = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ElementWrapper>"+pro.getXmlSymbolsString()+"</ElementWrapper>");
			ElementWrapper sw = (ElementWrapper)jaxbUnmarshaller2.unmarshal(sr);

			pro.setSymbolObjectList(sw.getsList());
			pro.setLayerObjectList(sw.getlList());
			
			MxGraphModel m = new MxGraphModel(pro);
		
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 
			jaxbMarshaller.marshal(m, System.out);

			
		} catch (WProcessDefException e) {

			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
