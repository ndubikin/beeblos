package org.beeblos.bpm.wc.taglib.security;

import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;


public class ContextoSeguridadPropertyResolver extends PropertyResolver {

	private static final String ADMON_MODE = "admon";
	private static final String ID_AREA = "idArea";
	private static final String ID_DEPTO = "idDepto";
	private static final String ID_USUARIO = "idUsuario";
	private static final String NO_PERMITIDO = "noPermitido";
	private static final String PERMITE_CUALQUIERA = "permiteCualquiera";
	private static final String USUARIO_PAGINA_INICIO = "usuarioPaginaInicio"; // nes 20110903
	private static final String TITULO_PRINCIPAL = "tituloPrincipal";                           // rrl 20111108
	private static final String TITULO_PRINCIPAL_DEPARTAMENTO = "tituloPrincipalDepartamento";  // rrl 20111108
	private static final String PERMITIDO = "permitido";
	private static final String SOLO_LECTURA = "suLectura";
	private static final String USUARIO_LOGIN = "usuarioLogin";
	private static final String USUARIO_NOMBRE = "usuarioNombre";

	private static String getMessage(Object base, String name) {
		return "Bean: " + base.getClass().getName() + ", property: " + name;
	}

	private PropertyResolver originalResolver;

	public ContextoSeguridadPropertyResolver(PropertyResolver propertyresolver) {
		originalResolver = propertyresolver;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getType(Object base, int index) throws EvaluationException,
			PropertyNotFoundException {
		return originalResolver.getType(base, index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getType(Object base, Object property)
			throws EvaluationException, PropertyNotFoundException {
		if (base instanceof ContextoSeguridad)
			return ContextoSeguridad.class;
		else
			return originalResolver.getType(base, property);
	}

	@Override
	public Object getValue(Object base, int index) throws EvaluationException,
			PropertyNotFoundException {
		return originalResolver.getValue(base, index);
	}

	@SuppressWarnings("static-access")
	@Override
	public Object getValue(Object base, Object property)
			throws EvaluationException, PropertyNotFoundException {

		Object retorno = null;

		if (base instanceof ContextoSeguridad) {
			ContextoSeguridad contextoSeguridad = (ContextoSeguridad) base;

			if (property.equals(ID_USUARIO)) {
				retorno = contextoSeguridad.getIdUsuario();
			} else if (property.equals(USUARIO_NOMBRE)) {
				retorno = contextoSeguridad.getUsuarioLogin();
			} else if (property.equals(ID_DEPTO)) {
				retorno = contextoSeguridad.getIdDepto();
			} else if (property.equals(ADMON_MODE)) {
				retorno = contextoSeguridad.getAdmon();
			} else if (property.equals(USUARIO_LOGIN)) {
				retorno = contextoSeguridad.getUsuarioLogin();

			// nes 20110903
			} else if (property.equals(USUARIO_PAGINA_INICIO)) {
				retorno = contextoSeguridad.getUsuarioPaginaInicio();
			// rrl 20111108
			} else if (property.equals(TITULO_PRINCIPAL)) {
				retorno = contextoSeguridad.getTituloPrincipal();
			} else if (property.equals(TITULO_PRINCIPAL_DEPARTAMENTO)) {
				retorno = contextoSeguridad.getTituloPrincipalDepartamento();
				
				
				
			} else if (property.equals(PERMITIDO)) {
				contextoSeguridad
						.setAuthMode(contextoSeguridad.AUTH_MODE_SINGLE);
				retorno = contextoSeguridad;
			} else if (property.equals(PERMITE_CUALQUIERA)) {
				contextoSeguridad.setAuthMode(contextoSeguridad.AUTH_MODE_ANY);
				retorno = contextoSeguridad;
			
			} else if (property.equals(SOLO_LECTURA)) {
				contextoSeguridad.setAuthMode(ContextoSeguridad.AUTH_MODE_SU);
				retorno = contextoSeguridad;
			} else if (property.equals(NO_PERMITIDO)) {
				contextoSeguridad.setAuthMode(contextoSeguridad.AUTH_MODE_NOT);
				retorno = contextoSeguridad;
			} else if (contextoSeguridad.inAuthMode()) {

				int authMode = contextoSeguridad.getAuthMode();

				if (authMode == contextoSeguridad.AUTH_MODE_SINGLE) {
					retorno = Boolean.valueOf(contextoSeguridad
							.permitido(property.toString()));
				} else if (authMode == contextoSeguridad.AUTH_MODE_SU) { //Para el manejo de Solo Lectura RRG 01/10/2008
					retorno = Boolean.valueOf(contextoSeguridad
							.habilitaSuLectura(property.toString()));

				} else if (authMode == contextoSeguridad.AUTH_MODE_ANY)
					retorno = Boolean.valueOf(contextoSeguridad
							.permiteCualquiera(property.toString()));
				

				else
					retorno = Boolean.valueOf(contextoSeguridad
							.noPermitido(property.toString()));
			} else {
				throw new PropertyNotFoundException(getMessage(base,
						(String) property));
			}
		} else
			retorno = originalResolver.getValue(base, property);

		return retorno;
	}

	@Override
	public boolean isReadOnly(Object base, int index)
			throws EvaluationException, PropertyNotFoundException {
		return originalResolver.isReadOnly(base, index);
	}

	@Override
	public boolean isReadOnly(Object base, Object property)
			throws EvaluationException, PropertyNotFoundException {
		if (base instanceof ContextoSeguridad)
			return true;
		else
			return originalResolver.isReadOnly(base, property);
	}

	@Override
	public void setValue(Object base, int index, Object value)
			throws EvaluationException, PropertyNotFoundException {
		originalResolver.setValue(base, index, value);
	}

	@Override
	public void setValue(Object base, Object property, Object value)
			throws EvaluationException, PropertyNotFoundException {
		originalResolver.setValue(base, property, value);
	}

}
