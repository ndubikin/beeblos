package org.beeblos.bpm.core.model.userType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.beeblos.bpm.core.model.enumerations.DeviceType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.sp.hibernate.common.usertype.EnumIntegerUserType;

/**
 * An Hibernate {@link UserType} that allows persisting an array of strings in one single column in the table.
 * Based in code from http://javadata.blogspot.com.ar/2011/07/hibernate-and-user-types.html.
 * 
 * In order to use this as the mapper for a column, use the following in the field of your entity:
 *  @Type(type=SniModalidadPostulacionUserType.NAME)
 *   List<Integer> tracks;
 *   
 *   PROBLEMA: al serializar para meterlo en el string, se separan los campos con pipe, por lo que habrá que ver como resolvemos
 *   la posibilidad de meter pipes en el string ...
 *  
 * @author dds
 * */
/**
 * UserType para mapear Enum tipo EstadoContratoPlantillaVersion con campo STRING de una db
 * @author nes 20151020
 *
 */
public class DeviceTypeUserType extends EnumIntegerUserType<DeviceType> {


    public DeviceTypeUserType() { 
        super(DeviceType.class); 
    } 

    /**
     * para que mapee el id del userType en vez de string...
     */
	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names,
			SessionImplementor arg2, Object arg3) throws HibernateException,
			SQLException {
        String name = resultSet.getString(names[0]);   
        DeviceType result = null;   
        if (!resultSet.wasNull()) { 
        	Integer code = new Integer(name);
            result = DeviceType.findByKey(code);   
        }   
        return result;
	}
	
	/**
     * para que persista el id en vez del string
     */
	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index,
			SessionImplementor arg3) throws HibernateException, SQLException {
		if (null == value) {   
            preparedStatement.setNull(index, Types.INTEGER);   
        } else {   
            preparedStatement.setString(index, ((DeviceType)value).getCode().toString());   
        }  
		
	}   

}
