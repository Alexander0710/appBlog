package blog.model.manager;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import blog.model.entities.Usuario;
import blog.model.util.ModelUtil;

/** * Session Bean implementation class ManagerUsuarios */
@Stateless
@LocalBean
public class ManagerUsuarios {

	@PersistenceContext(unitName = "blogDS")
	private EntityManager em;

	public ManagerUsuarios() {
	}

	public boolean comprobarUsuario(String idUsuario, String clave) throws Exception {
		Usuario u = em.find(Usuario.class, idUsuario);
		if (u == null)
			throw new Exception("No existe el usuario " + idUsuario);
		if (!u.getActivo())
			throw new Exception("El usuario no está activo.");
		if (u.getClave().equals(clave))
			return true;
		throw new Exception("Contraseña no válida.");
	}

	public Usuario findUsuarioById(String idUsuario) {
		Usuario u = em.find(Usuario.class, idUsuario);
		return u;
	}

	public void registrarNuevoBlogger(String idUsuario, String clave, String confirmacionClave, String correo)
			throws Exception {
		if (ModelUtil.isEmpty(idUsuario))
			throw new Exception("Debe especificar un ID de usuario.");
		if (ModelUtil.isEmpty(clave))
			throw new Exception("Debe especificar una clave.");
		if (!clave.equals(confirmacionClave))
			throw new Exception("No coinciden la clave y la confirmación.");
		if (ModelUtil.isEmpty(correo))
			throw new Exception("Debe especificar un correo válido.");
		Usuario u = findUsuarioById(idUsuario);
		if (u != null)
			throw new Exception("Ya existe un usuario " + idUsuario);
		u = new Usuario();
		u.setIdUsuario(idUsuario);
		u.setClave(clave);
		u.setCorreo(correo);
		u.setActivo(true);
		em.persist(u);
	}

}