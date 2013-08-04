/**
 * 
 */
package org.beeblos.security.st.bl;

/**
 * @author rr
 *
 */

import java.util.List;
import com.sp.common.util.StringPair;
import org.beeblos.security.st.dao.DepartamentoDao;
import org.beeblos.security.st.error.DepartamentoException;
import org.beeblos.security.st.model.Departamento;


public class DepartamentoBL {

	public DepartamentoBL() {

	}

	public Integer agregar(Departamento departamento) throws DepartamentoException {

		// martin - 20100714 - A�adido retorno id
		
		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.agregar(departamento);

	}

	public void actualizar(Departamento departamento) throws DepartamentoException {
		new DepartamentoDao().actualizar(departamento);
		//aDao.actualizar(departamento);
	}

	public void borra(Departamento departamento) throws DepartamentoException {

		DepartamentoDao aDao = new DepartamentoDao();
		aDao.borrar(departamento);

	}

	public List<Departamento> obtenerDepartamentos() throws DepartamentoException {

		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.obtenerDepartamentos();

	}
	

	public List<Departamento> obtenerDepartamentosPorNombre() throws DepartamentoException {

		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.obtenerDepartamentosPorNombre();

	}

	public List<StringPair> obtenerDepartamentosNombreParaCombo(
			String textoPrimeraLinea, String separacion
	) throws DepartamentoException {

		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.obtenerDepartamentosNombreParaCombo(textoPrimeraLinea, separacion);

	}
	
	//rrl 20111216
	public List<StringPair> obtenerDepartamentosNombreYAbreviaturaParaCombo(
			String textoPrimeraLinea, String separacion
	) throws DepartamentoException {

		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.obtenerDepartamentosNombreYAbreviaturaParaCombo(textoPrimeraLinea, separacion);

	}
	
	public List<StringPair> obtenerDepartamentosAbreviadosParaCombo (
			String textoPrimeraLinea, String separacion
	) throws DepartamentoException {

		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.obtenerDepartamentosAbreviadosParaCombo(textoPrimeraLinea, separacion);

	}

	public Departamento obtenerDepartamentoPorNombre(String nombre) throws DepartamentoException {

		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.obtenerDepartamentoPorNombre(nombre);

	}

	public Departamento obtenerDepartamentoPorPK(Integer pk) throws DepartamentoException {
		DepartamentoDao aDao = new DepartamentoDao();
		return aDao.obtenerDepartamentoPorPK(pk);
	}

}
