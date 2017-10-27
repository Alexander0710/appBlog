package blog.model.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import blog.model.entities.Articulo;
import blog.model.entities.Blog;
import blog.model.entities.Usuario;

/**
 * Session Bean implementation class ManagerBlog
 */
@Stateless
@LocalBean
public class ManagerBlog {
	@PersistenceContext(unitName = "blogDS")
	private EntityManager em;
	@EJB
	private ManagerUsuarios managerUsuarios;

	public ManagerBlog() {
	}

	@SuppressWarnings("unchecked")
	public List<Blog> findBlogByUsuario(String idUsuario) {
		String JPQL = "SELECT o FROM Blog o WHERE o.usuario.idUsuario='" + idUsuario + "'";
		Query q;
		List<Blog> lista;
		q = em.createQuery(JPQL);
		lista = q.getResultList();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Blog> findAllBlogs() {
		String sentenciaSQL = "SELECT o FROM Blog o ";
		Query q;
		List<Blog> listaBlog;
		q = em.createQuery(sentenciaSQL);
		listaBlog = q.getResultList();
		return listaBlog;
	}

	@SuppressWarnings("unchecked")
	public List<Articulo> finArticuloByBlog(long idBlog) {
		String JPQLj = "SELECT i FROM Articulo i WHERE i.blog.idBlog='" + idBlog + "'";
		Query q1;
		List<Articulo> listaArt;
		q1 = em.createQuery(JPQLj);
		listaArt = q1.getResultList();
		return listaArt;
	}

	public Blog findBlogById(long idBlog) {
		Blog u = em.find(Blog.class, idBlog);
		return u;
	}

	public Articulo findArticuloById(long idArticulo) {
		Articulo a = em.find(Articulo.class, idArticulo);
		return a;
	}

	public void crearBLog(String idUsuario, String nombreBlog, String descripcion, String tituloArticulo,
			String contenido) throws Exception {
		Usuario u = managerUsuarios.findUsuarioById(idUsuario);
		if (u == null)
			throw new Exception("No existe el usuario " + idUsuario);
		Blog blog = new Blog();
		blog.setDescripcion(descripcion);
		blog.setNombreBlog(nombreBlog);
		blog.setUsuario(u);
		blog.setArticulos(new ArrayList<Articulo>());
		Articulo articulo = new Articulo();
		articulo.setTitulo(tituloArticulo);
		articulo.setContenido(contenido);
		articulo.setLikes(0);
		articulo.setRecaudado(BigDecimal.ZERO);
		articulo.setBlog(blog);
		articulo.setVisible(true);
		blog.addArticulo(articulo);
		em.persist(blog);
	}

	public void insertarArticulo(Blog blog, String titulo, String contenido) {
		Articulo articulo = new Articulo();
		articulo.setTitulo(titulo);
		articulo.setContenido(contenido);
		articulo.setBlog(blog);
		articulo.setLikes(0);
		articulo.setRecaudado(BigDecimal.ZERO);
		articulo.setVisible(true);
		em.persist(articulo);
	}

	public void eliminarArticulo(Articulo articulo) {
		em.remove(articulo);
	}

	public void eliminarBlog(long idBlog) throws Exception {
		Blog b = findBlogById(idBlog);

		if (b == null) {
			throw new Exception("No existe el Blog " + idBlog);
		} else if (finArticuloByBlog(b.getIdBlog()).size() == 0) {
			em.remove(b);
		} else {
			throw new Exception("El Blog " + idBlog + "tiene blogs publicados no es posible eliminar");
		}

	}

	public void editarBlog(long idBlog, String idUsuario, String nombreBlog, String descripcion) throws Exception {

		Blog b = findBlogById(idBlog);
		if (b == null)
			throw new Exception("No existe el Blog especificado.");
		b.setDescripcion(descripcion);
		b.setNombreBlog(nombreBlog);
		em.merge(b);
	}

	public void likes(long idArticulo) throws Exception {
		Articulo a = findArticuloById(idArticulo);
		if (a == null)
			throw new Exception("no existe el articulo");
		a.setLikes(a.getLikes() + 1);
		a.setRecaudado(a.getRecaudado().add(new BigDecimal(0.25)));

	}
}
