package blog.model.manager;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import blog.model.entities.Articulo;
import blog.model.entities.Blog;
import blog.model.entities.Usuario;
import blog.model.util.ModelUtil;

/** * Session Bean implementation class ManagerUsuarios */
@Stateless
@LocalBean
public class ManagerUsuariosCRUD {

	@PersistenceContext(unitName = "blogDS")
	private EntityManager em;

	public ManagerUsuariosCRUD() {
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

	public Articulo findArticuloById(long id_Articulo) {
		Articulo a = em.find(Articulo.class, id_Articulo);
		return a;
	}

	public List<Usuario> findAllUsuarios() {
		Query q;
		List<Usuario> listado;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Usuario o ";
		q = em.createQuery(sentenciaSQL);
		listado = q.getResultList();
		return listado;
	}

	public List<Articulo> findAllArticulos() {
		Query q;
		List<Articulo> listaArticulo;
		String sentenciaSQL;
		sentenciaSQL = "SELECT o FROM Articulo o ";
		q = em.createQuery(sentenciaSQL);
		listaArticulo = q.getResultList();
		return listaArticulo;
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

	public void eliminarUsuario(String idUsuario) throws Exception {

		Usuario u = findUsuarioById(idUsuario);
		if (u == null) {
			throw new Exception("No existe el Usuario " + idUsuario);
		} else if (findBlogByUsuario(u.getIdUsuario()).size() == 0) {
			em.remove(u);
		} else {
			throw new Exception("El Usuario " + idUsuario + "tiene blogs publicados no es posible eliminar");
		}

	}

	public void editarUsuario(String idUsuario, String clave, String confirmacionClave, String correo)
			throws Exception {
		Usuario u = findUsuarioById(idUsuario);
		if (u == null)
			throw new Exception("No existe el usuario especificado.");
		u.setIdUsuario(idUsuario);
		u.setClave(clave);
		u.setCorreo(correo);

		em.merge(u);
	}

	public List<Blog> findBlogByUsuario(String idUsuario) {
		String JPQL = "SELECT o FROM Blog o WHERE o.usuario.idUsuario='" + idUsuario + "'";
		Query q;
		List<Blog> lista;
		q = em.createQuery(JPQL);
		lista = q.getResultList();
		return lista;
	}

	public void censurarArticulo(long id_Articulo) throws Exception {
		Articulo a = findArticuloById(id_Articulo);
		if (a == null)
			throw new Exception("No existe el Articulo especificado.");

		if (a.getVisible()) {
			a.setVisible(false);
			em.merge(a);
		} else {
			a.setVisible(true);
			em.merge(a);
		}
	}

}