package org.beeblos.bpm.wc.taglib.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.core.model.noper.BeeblosAttachment;
import org.beeblos.bpm.core.util.HibernateSessionParameters;
import org.beeblos.bpm.core.util.HibernateUtil;
import org.beeblos.bpm.wc.taglib.security.ContextoSeguridad;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import org.beeblos.bpm.wc.taglib.util.FGPException;
import org.beeblos.bpm.wc.taglib.util.HelperUtil;
import org.hibernate.HibernateException;

public class ChangeHibernateConfigurationBean extends CoreManagedBean {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory
			.getLog(ChangeHibernateConfigurationBean.class);

	private static ResourceBundle rb = ResourceBundle
			.getBundle("hibernateDefaultConfiguration");

	private static final String MANAGED_BEAN_NAME = "changeHibernateConfigurationBean";

	private Integer currentUserId;

	private HibernateSessionParameters currentHibernateSessionParameters;

	private TimeZone timeZone;

	private BeeblosAttachment attachment;
	private String documentLink;

	public static ChangeHibernateConfigurationBean getCurrentInstance() {
		return (ChangeHibernateConfigurationBean) FacesContext
				.getCurrentInstance().getExternalContext().getRequestMap()
				.get(MANAGED_BEAN_NAME);
	}

	public ChangeHibernateConfigurationBean() {
		super();

		init();
	}

	public void init() {
		super.init();

		setShowHeaderMessage(false);

		_reset();

	}

	public void _reset() {

		ContextoSeguridad cs = (ContextoSeguridad) getSession().getAttribute(
				SECURITY_CONTEXT);
		if (cs != null) 
			currentHibernateSessionParameters = cs.getHibernateParameters();

		attachment = new BeeblosAttachment();

		documentLink = null;

		HelperUtil.recreateBean("documentacionBean",
				"com.softpoint.taglib.common.DocumentacionBean");

	}

	public HibernateSessionParameters getCurrentHibernateSessionParameters() {
		return currentHibernateSessionParameters;
	}

	public void setCurrentHibernateSessionParameters(
			HibernateSessionParameters currentHibernateSessionParameters) {
		this.currentHibernateSessionParameters = currentHibernateSessionParameters;
	}

	public BeeblosAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(BeeblosAttachment attachment) {
		this.attachment = attachment;
	}

	public String getDocumentLink() {
		return documentLink;
	}

	public void setDocumentLink(String documentLink) {
		this.documentLink = documentLink;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getCurrentUserId() {
		if (currentUserId == null) {
			ContextoSeguridad cs = (ContextoSeguridad) getSession()
					.getAttribute(SECURITY_CONTEXT);
			if (cs != null)
				currentUserId = cs.getIdUsuario();
		}
		return currentUserId;
	}

	public TimeZone getTimeZone() {
		// Si se pone GMT+1 pone mal el dia
		return java.util.TimeZone.getDefault();
	}

	public void changeParameters() {

		List exc = new ArrayList();
		
		if (currentHibernateSessionParameters != null) {

			if (parametersNotEmpty()) {

				try {
					HibernateUtil.getNewSession(currentHibernateSessionParameters);

					ContextoSeguridad cs = (ContextoSeguridad) getSession()
							.getAttribute(SECURITY_CONTEXT);
					
					cs.setHibernateParameters(currentHibernateSessionParameters);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {

				String message = "You cannot insert empty values";
				String params[] = { message + ",",
						".Please confirm input values." };
				agregarMensaje("209", message, params, FGPException.ERROR);

			}
					
		}

	}

	public void setDefaultParameters() {

		this.currentHibernateSessionParameters = new HibernateSessionParameters(
				rb.getString("hibernate.connection.driver_class"),
				rb.getString("hibernate.connection.password"),
				rb.getString("hibernate.connection.username"),
				rb.getString("hibernate.connection.url"),
				rb.getString("hibernate.connection.default_catalog"));

	}

	private boolean parametersNotEmpty() {

		if ("".equals(currentHibernateSessionParameters
				.getConnectionDriverClass())) {
			return false;
		} else if ("".equals(currentHibernateSessionParameters
				.getConnectionPassword())) {
			return false;
		} else if ("".equals(currentHibernateSessionParameters
				.getConnectionUsername())) {
			return false;
		} else if ("".equals(currentHibernateSessionParameters
				.getConnectionUrl())) {
			return false;
		} else if ("".equals(currentHibernateSessionParameters
				.getConnectionDefaultCatalog())) {
			return false;
		}
		return true;
	}

}
