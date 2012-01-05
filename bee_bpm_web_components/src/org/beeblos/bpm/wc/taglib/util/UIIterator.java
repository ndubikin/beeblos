package org.beeblos.bpm.wc.taglib.util;

import java.util.ArrayList;
import java.util.Set;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.ajax4jsf.component.html.HtmlAjaxRepeat;
import org.ajax4jsf.model.SequenceDataModel;

public class UIIterator extends HtmlAjaxRepeat {

   @SuppressWarnings("unchecked")
   @Override
   protected DataModel getDataModel() {
      Object current = getValue();
      if(current instanceof Set){
          return new SequenceDataModel(new ListDataModel(
                new ArrayList((Set) current)));
      }
      return super.getDataModel();
   }
}
