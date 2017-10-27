package blog.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import blog.model.manager.ManagerUsuariosCRUD;
import blog.view.util.JSFUtil;
import blog.model.entities.Articulo;
import blog.model.entities.Usuario;

@ManagedBean
@SessionScoped
public class ControllerUsuarioCRUD {
	/**
	 * 
	 */

	private String idUsuario;
	private String clave;
	private String correo;
	private String confirmacionClave;
	private List<Usuario> lista;
	private List<Articulo> listaArt;
	private int idArticulo;
	@EJB
	private ManagerUsuariosCRUD managerUsuarios;

	@PostConstruct
	private void cargar() {
		lista = managerUsuarios.findAllUsuarios();
		listaArt = managerUsuarios.findAllArticulos();
	}

	public void actionRegistrarBloggerAdmin() {
		try {
			managerUsuarios.registrarNuevoBlogger(idUsuario, clave, confirmacionClave, correo);
			lista = managerUsuarios.findAllUsuarios();
			JSFUtil.crearMensajeInfo("Nuevo blogger registrado.");

		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}

	}

	public void actionListenerEliminarUsuarioAdmin(String idUsuario) {
		try {
			managerUsuarios.eliminarUsuario(idUsuario);
			lista = managerUsuarios.findAllUsuarios();
			JSFUtil.crearMensajeInfo("Usuario " + idUsuario + " eliminado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerCargarUsuarioAdmin(Usuario usuario) {

		idUsuario = usuario.getIdUsuario();
		clave = usuario.getClave();
		correo = usuario.getCorreo();
		clave = usuario.getClave();

	}

	public void actionListenerActualizarUsuarioAdmin() {
		try {
			managerUsuarios.editarUsuario(idUsuario, clave, confirmacionClave, correo);
			lista = managerUsuarios.findAllUsuarios();
			JSFUtil.crearMensajeInfo("Actualización correcta.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerCesnsurar(Articulo articulo) {
		try {
			managerUsuarios.censurarArticulo(articulo.getIdArticulo());
			listaArt = managerUsuarios.findAllArticulos();
			JSFUtil.crearMensajeInfo("cambio de estado realizado.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}

	public String getConfirmacionClave() {
		return confirmacionClave;
	}

	public void setConfirmacionClave(String confirmacionClave) {
		this.confirmacionClave = confirmacionClave;
	}

	public List<Usuario> getLista() {
		return lista;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
	}

	public List<Articulo> getListaArt() {
		return listaArt;
	}

	public void setListaArt(List<Articulo> listaArt) {
		this.listaArt = listaArt;
	}

}