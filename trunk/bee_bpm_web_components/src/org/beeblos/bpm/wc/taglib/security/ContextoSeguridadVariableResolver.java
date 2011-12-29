package org.beeblos.bpm.wc.taglib.security;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;
import javax.servlet.http.HttpSession;



public class ContextoSeguridadVariableResolver extends VariableResolver  {
    
    private static final String SECURITY_CONTEXT = "contextoSeguridad";
    
    private VariableResolver originalResolver;

  
    public ContextoSeguridadVariableResolver(VariableResolver variableresolver) 
    {
        originalResolver = variableresolver;
    }

    @Override
    public Object resolveVariable(FacesContext facesContext, String variableName) throws EvaluationException 
    {
        if (SECURITY_CONTEXT.equals(variableName)) {            
            return getSecurityContext(facesContext);
        } else {
            return originalResolver.resolveVariable(facesContext, variableName);
        }
    }
    

    private ContextoSeguridad getSecurityContext(FacesContext facesContext)
    {        
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session.getAttribute(SECURITY_CONTEXT) != null) {
            return (ContextoSeguridad)session.getAttribute(SECURITY_CONTEXT);
        } else {
            return new ContextoSeguridad();
        }
    }
}