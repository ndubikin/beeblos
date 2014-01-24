package org.beeblos.bpm.core.model;



import static org.beeblos.bpm.core.util.Constants.EMPTY_OBJECT;

public class HibernateConfiguration {

	private HibernateConfigurationParameters hcp;
	private Environment environment;
	
	public HibernateConfiguration() {

	}

	public HibernateConfiguration(boolean createEmptyObject) {
		super();

		if (createEmptyObject){

			this.hcp = new HibernateConfigurationParameters();
			this.environment = new Environment(EMPTY_OBJECT);
			
		}
		
	}
	
	public HibernateConfiguration(HibernateConfigurationParameters hcp, Environment environment) {
		super();
		this.hcp = hcp;
		this.environment = environment;
	}

	public HibernateConfigurationParameters getHcp() {
		return hcp;
	}

	public void setHcp(HibernateConfigurationParameters hcp) {
		this.hcp = hcp;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public String toString() {
		return "HibernateConfiguration [hcp=" + hcp + ", environment=" + environment + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((environment == null) ? 0 : environment.hashCode());
		result = prime * result + ((hcp == null) ? 0 : hcp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HibernateConfiguration other = (HibernateConfiguration) obj;
		if (environment == null) {
			if (other.environment != null)
				return false;
		} else if (!environment.equals(other.environment))
			return false;
		if (hcp == null) {
			if (other.hcp != null)
				return false;
		} else if (!hcp.equals(other.hcp))
			return false;
		return true;
	}

	public boolean empty() {

		if (hcp!=null && !hcp.empty()) return false;
		if (environment!=null && !environment.empty()) return false;
		
		return true;
	}
	// dml 20120215
	public boolean hasEmptyFields() {
		
		if (hcp==null || hcp.empty()) return true;
		if (environment==null || environment.empty()) return true;
		
		return false;
	}

}
