package org.beeblos.bpm.core.model;

/**
 * User collection
 * 
 * Interfase para agrupar las tablas de relacion de un objeto con un user.
 * Por ahora tenemos relacionados Process, Step y User.
 * 
 * @author nestor
 * @since 20141029
 *
 */
import org.joda.time.DateTime;

public interface WUserCol {

	public abstract WUserDef getUser();

	public abstract void setUser(WUserDef user);

	public abstract boolean isAdmin();

	public abstract void setAdmin(boolean admin);

	public abstract Integer getIdObject();

	public abstract void setIdObject(Integer idObject);

	public abstract String getIdObjectType();

	public abstract void setIdObjectType(String idObjectType);

	public abstract Integer getInsertUser();

	public abstract void setInsertUser(Integer insertUser);

	public abstract DateTime getInsertDate();

	public abstract void setInsertDate(DateTime insertDate);

}