package org.beeblos.bpm.wc.taglib.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beeblos.bpm.wc.taglib.util.CoreManagedBean;
import com.sp.common.jsf.util.UtilsVs;
import org.beeblos.security.auxiliar.bl.PaisBL;
import org.beeblos.security.auxiliar.bl.TerritorioBL;
import org.beeblos.security.auxiliar.bl.TipoTerritorioBL;
import org.beeblos.security.auxiliar.error.PaisException;
import org.beeblos.security.auxiliar.error.TerritorioException;
import org.beeblos.security.auxiliar.error.TipoTerritorioException;
import org.beeblos.security.auxiliar.model.Territorio;
import org.beeblos.security.auxiliar.model.TipoTerritorio;

public class TerritorioBean extends CoreManagedBean {
	/**
	 * 
	 * Nota importante: este bean se encarga de la gestión de los territorios en relación con una dirección.
	 * Permite manejar un conjunto de desplegalbes en cascada mostrando cada nivel territorial ( label y dato ) y 
	 * que el usuario vaya eligiendo cada nivel.
	 * 
	 * En caso que el país indicado no tenga estructura territorial, se utilizará la genérica, que tiene que estar dada
	 * de alta en la tabla tipo_territorio y es requerido que el país esté en "null" para cada tipo de territorio de esa
	 * estructura genérica. De esa forma este sistema entienderá que se manejará manualmente y que los datos ( localidad
	 * y provincia en este caso ) se insertarán en la tabla territorio para el registro correspondiente a 1 dirección en 
	 * particular
	 * 
	 * Cuando el país tiene la estructura ingresada entonces solamente se dejará elegir cada nivel ( localidad, provincia
	 * , etc ) del desplegable.
	 * 
	 * 
	 */

	private static final long serialVersionUID = 1L;	
	private static final Log logger = LogFactory.getLog(TerritorioBean.class);


	private Integer idTerritorioCorriente; // el territorio de menor nivel ( si existe o si viene en modo "modificacion" )
	private Integer idTerritorioAnterior; // para que se pueda hacer "cancel" cuando se modifica un registro y no se pierdan los datos ...
	private Territorio territorioCorriente; // para guardar el territorio corriente ( no c pa q pero lo guardo ... )
	
	
	private String codigoPostal;
	
	private String idPais;
	
	private Integer nivelActual; // para recibir el nivel del combo que seleccionó
	private Integer idTerritorioSeleccionado; // el id del territorio que selecciona en el desplegable ...
	private boolean territorioManual; // flag que se prende cuando no hay estructura territorial del país por lo que se van a manejar los territorios en forma manual ( quedan asociados directamente al registro que se ingrese )
	private List<String> jerarquiaTerritorioManual; // para manejar los territorios manuales ( localidad y provincia para cada dirección )
	
	private List<SelectItem> listaPaises;

	private List<Territorio> localizacion; // lista de territorios con la localizacion ( Localidad / Provincia / CC.AA. ) 

	private List<TipoTerritorio> jerarquiaTerritorial; // la jerarquia territorial de un país
	
	private List<List<SelectItem>> jerarquiaParaDesplegables;
	

	
	public TerritorioBean(){ 

		init();
	}

	public void resetBean() {
		
		this.idPais=null;
		this.idTerritorioCorriente=null;
		jerarquiaTerritorial= new ArrayList<TipoTerritorio>();
		localizacion = new ArrayList<Territorio>();
		jerarquiaParaDesplegables= new ArrayList<List<SelectItem>>();
		jerarquiaTerritorioManual=null;
		territorioManual=false;
		
	}
	
	public void init() {

		try {

			// cargo la lista de paises. Si el primer país es diferente de null, inicializo el bean con él ...
			listaPaises = new ArrayList<SelectItem>();
			listaPaises = UtilsVs
					.castStringPairToSelectitem(
							new PaisBL().obtenerPaisesParaCombo("Seleccionar ...", "") );
			
			if ( listaPaises.get(0)!=null && listaPaises.get(0).getValue()!=null ) {
				if ( this.idPais==null || "".equals(this.idPais) ) {
					this.idPais=listaPaises.get(0).getValue().toString();
				}
			}
			
			// carga la jerarquia territorial para el país corriente y la lista de desplegables para cada nivel ...
			if ( this.idPais!=null && !"".equals(this.idPais)) {
				_cargaJerarquias();
				// determina si hay estructura de pais o si se maneja manualmente
				determinaManejoTerritorios();

			}
			
			// si hay un id territorio carga su localizacion ( lista con cc.aa. / provincia / localidad )
			// ( esto deberia ocurrir cuando se edita 1 registro que ya tiene 1 territorio seleccionado
			if ( idTerritorioCorriente!=null && idTerritorioCorriente!=0) {
				localizacion = new ArrayList<Territorio>();
				localizacion = new TerritorioBL().obtenerLocalizacion(idTerritorioCorriente);
			}

			
		} catch (TerritorioException e) {  // no pudo cargar la localización
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PaisException e) { // si da exception la lista de paises
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void determinaManejoTerritorios(){
		
		TipoTerritorio tt=jerarquiaTerritorial.get(0);
		if ( 	tt.getTipoTerritorioPais()==null || 
				tt.getTipoTerritorioPais().getIdPais()==null || 
				"".equals(tt.getTipoTerritorioPais().getIdPais()) ) {

			territorioManual=true;
			// inicializo los string para cargar localidad y provincia
			Integer size=jerarquiaTerritorial.size();
			jerarquiaTerritorioManual = 
					new ArrayList<String>( size );
			for (int i=0; i<size;i++) {
				jerarquiaTerritorioManual.add("");
			}
		
		} else {
			territorioManual=false;
			jerarquiaTerritorioManual = null;
		}
		
	}
	
	public void changePais() {
		
		// si cambia el país hay que cambiar toda la jerarquía ( a menos que la nueva sea igual a la anterior ( genérica ... ) )
		if ( this.idPais!=null && !"".equals(this.idPais) ) {
			if ( this.idTerritorioCorriente!=null && this.idTerritorioCorriente!=0) { 
					this.idTerritorioAnterior=this.idTerritorioCorriente;
					this.idTerritorioCorriente=null;
					this.territorioCorriente=new Territorio();
			}
			_cargaJerarquias();
			determinaManejoTerritorios();
		}

	}
	
	public void changeNivel() {
		
		// si cambia 1 desplegable, cojo el id elegido y cargo los hijos ...
		if ( this.idPais!=null && !"".equals(this.idPais) &&
				this.idTerritorioSeleccionado!=null && this.idTerritorioSeleccionado!=0 ) {
			
			try {
			
				Territorio ts = new TerritorioBL().obtenerTerritorioPorPK(this.idTerritorioSeleccionado);

				this.nivelActual = ts.getTipoTerritorio().getTipoTerritorioNivel();
				
				_cargaJerarquiaDesplegableHijos(ts);

			
			} catch (TerritorioException e) {

				e.printStackTrace();
			}
			
			
		
		}

	}
	
	public void changeTerritorioManual() {
		
		System.out.println("-------->> cambia territorio manual .....");
		for (String t: jerarquiaTerritorioManual) {
			System.out.println(" --->>"+t+"<<----");
		}
		System.out.println("--------------------------------------------------");
	
	}

	private void _cargaJerarquias() {
		
		// si cambian el país, cargamos la jerarquia territorial del país
		// y el desplegable de nivel 0
		jerarquiaTerritorial= new ArrayList<TipoTerritorio>();
		jerarquiaParaDesplegables= new ArrayList<List<SelectItem>>();
		
		try {
			
			jerarquiaTerritorial = new TipoTerritorioBL().obtenerJerarquiaTerritorialPais(this.idPais);
			
			if ( this.idTerritorioCorriente!=null && this.idTerritorioCorriente!=0 ) {
				
				_cargaJerarquiaExistente(); // carga todos los niveles ( solo el dato correspondiente - no arma combos ... )
				
			} else {
				
				_cargaJerarquiaParaCombos(); // carga combo del primer nivel
			}
			
			
		} catch (TipoTerritorioException e) {
			
			e.printStackTrace();

		} 
	}
	
	private void _cargaJerarquiaParaCombos() {
		
		try {
			
			for ( TipoTerritorio tt: jerarquiaTerritorial) {
				if ( tt.getTipoTerritorioPais()!=null && tt.getTipoTerritorioPais().getIdPais()!=null ) {
					if ( tt.getTipoTerritorioNivel()==0 ) {
						jerarquiaParaDesplegables
							.add(
								UtilsVs
								.castStringPairToSelectitem(
									new TerritorioBL()
										.obtenerListaTerritoriosParaCombo(
												tt.getTipoTerritorioPais().getIdPais(), 
												tt.getIdTipoTerritorio())) );
					}
				} else {
					jerarquiaParaDesplegables
						.add(
								new ArrayList<SelectItem>(0));
				}
			}

		} catch (TerritorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	private void _cargaJerarquiaExistente() {
		
		try {
			
				// si hay territorio corriente quiere decir que está identificado el territorio y se
				// pueden cargar todos los niveles a partir de él
				
				TerritorioBL tbl = new TerritorioBL();
				
				List<Territorio> localizacion = tbl.obtenerLocalizacion(this.idTerritorioCorriente);
				List<SelectItem> litem;

				for ( Territorio t: localizacion) {
					
					litem = new ArrayList<SelectItem>();
					
					litem.add( new SelectItem(t.getIdTerritorio(), t.getTerritorioNombre()) );
					
					jerarquiaParaDesplegables.add(litem);
				
				}
				
		} catch (TerritorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void _cargaJerarquiaDesplegableHijos( Territorio ts ) {

		try {	

			if ( this.nivelActual < jerarquiaTerritorial.size()-1 ) { // si hay mas niveles que el actual, recargo todo

				List<List<SelectItem>> nl = new ArrayList<List<SelectItem>>();
	
				for (int i=0; i<this.nivelActual; i++) {
					nl.add(jerarquiaParaDesplegables.get(i));
				}
				
				List<SelectItem> ltmp = new ArrayList<SelectItem>(); // me creo 1 lista para poner solamente el elemento seleccionado en su desplegable y desecho el resto de elementos ...
				ltmp.add( new SelectItem( ts.getIdTerritorio(), ts.getTerritorioNombre() ) );
				nl.add(	ltmp ) ;
					
				// y cargo el nivel siguiente ...
				nl.add(
						UtilsVs
						.castStringPairToSelectitem(
								new TerritorioBL()
							.obtenerTerritoriosHijosParaCombo(ts.getIdTerritorio())) );
	
				jerarquiaParaDesplegables=nl;

			} else { // si es el último, seteo 
				
				this.idTerritorioCorriente=this.idTerritorioSeleccionado;

				this.territorioCorriente = new TerritorioBL().obtenerTerritorioPorPK(this.idTerritorioCorriente);
				
			}
			
		} catch (TerritorioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			
	
	}
	
	public Territorio getTerritorioSeleccionado() {
		
		if ( this.idTerritorioCorriente!=null && this.idTerritorioCorriente!=0 ) {
			try {
				return new TerritorioBL().obtenerTerritorioPorPK(this.idTerritorioCorriente);
			} catch (TerritorioException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
		
	}

	public Integer getIdTerritorioCorriente() {
		return idTerritorioCorriente;
	}
	public void setIdTerritorioCorriente(Integer idTerritorioCorriente) {
		this.idTerritorioCorriente = idTerritorioCorriente;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getIdPais() {
		return idPais;
	}
	public void setIdPais(String idPais) {
		this.idPais = idPais;
	}
	public List<SelectItem> getListaPaises() {
		return listaPaises;
	}
	public void setListaPaises(List<SelectItem> listaPaises) {
		this.listaPaises = listaPaises;
	}
	public List<Territorio> getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(List<Territorio> localizacion) {
		this.localizacion = localizacion;
	}
	public List<TipoTerritorio> getJerarquiaTerritorial() {
		return jerarquiaTerritorial;
	}
	public void setJerarquiaTerritorial(List<TipoTerritorio> jerarquiaTerritorial) {
		this.jerarquiaTerritorial = jerarquiaTerritorial;
	}
	public List<List<SelectItem>> getJerarquiaParaDesplegables() {
		return jerarquiaParaDesplegables;
	}
	public void setJerarquiaParaDesplegables(
			List<List<SelectItem>> jerarquiaParaDesplegables) {
		this.jerarquiaParaDesplegables = jerarquiaParaDesplegables;
	}
	public Integer getNivelActual() {
		return nivelActual;
	}
	public void setNivelActual(Integer nivelActual) {
		this.nivelActual = nivelActual;
	}
	public Integer getIdTerritorioSeleccionado() {
		return idTerritorioSeleccionado;
	}
	public void setIdTerritorioSeleccionado(Integer idTerritorioSeleccionado) {
		this.idTerritorioSeleccionado = idTerritorioSeleccionado;
	}
	public Territorio getTerritorioCorriente() {
		return territorioCorriente;
	}
	public void setTerritorioCorriente(Territorio territorioCorriente) {
		this.territorioCorriente = territorioCorriente;
	}
	public Integer getIdTerritorioAnterior() {
		return idTerritorioAnterior;
	}
	public void setIdTerritorioAnterior(Integer idTerritorioAnterior) {
		this.idTerritorioAnterior = idTerritorioAnterior;
	}
	public List<String> getJerarquiaTerritorioManual() {
		return jerarquiaTerritorioManual;
	}
	public void setJerarquiaTerritorioManual(List<String> jerarquiaTerritorioManual) {
		this.jerarquiaTerritorioManual = jerarquiaTerritorioManual;
	}
	public boolean isTerritorioManual() {
		return territorioManual;
	}
	public void setTerritorioManual(boolean territorioManual) {
		this.territorioManual = territorioManual;
	}

	
}

