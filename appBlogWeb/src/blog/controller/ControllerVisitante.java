package blog.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import blog.model.entities.Articulo;
import blog.model.entities.Blog;
import blog.model.manager.ManagerBlog;
import blog.view.util.JSFUtil;

@ManagedBean
@SessionScoped
public class ControllerVisitante {
	private List<Blog> listaBlogs;
	private List<Articulo> listaArticulo;
	private String nombreBlog;
	private String descripcionBlog;
	private String tituloArticulo;
	private String contenidoArticulo;
	private Blog blogActual;
	private long idBlog1;
	private String idUser;

	@EJB
	private ManagerBlog managerBlog;

	@PostConstruct
	public void iniciar() {
		listaBlogs = managerBlog.findAllBlogs();
		JSFUtil.crearMensajeInfo("Blogs: " + listaBlogs.size());
	}

	public void actionListenerConsultarBlogs() {
		listaBlogs = managerBlog.findAllBlogs();
		JSFUtil.crearMensajeInfo("Blogs: " + listaBlogs.size());
	}

	public String actionListaArticulos(long idBlog) {
		listaArticulo = managerBlog.finArticuloByBlog(idBlog);
		return "ArticulosVisitante";
	}

	public void actionListenerLikes(long idArticulo) {
		try {
			managerBlog.likes(idArticulo);
			JSFUtil.crearMensajeInfo("LIKE REGISTRADO");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<Blog> getListaBlogs() {
		return listaBlogs;
	}

	public void setListaBlogs(List<Blog> listaBlogs) {
		this.listaBlogs = listaBlogs;
	}

	public String getNombreBlog() {
		return nombreBlog;
	}

	public void setNombreBlog(String nombreBlog) {
		this.nombreBlog = nombreBlog;
	}

	public String getDescripcionBlog() {
		return descripcionBlog;
	}

	public void setDescripcionBlog(String descripcionBlog) {
		this.descripcionBlog = descripcionBlog;
	}

	public String getTituloArticulo() {
		return tituloArticulo;
	}

	public void setTituloArticulo(String tituloArticulo) {
		this.tituloArticulo = tituloArticulo;
	}

	public String getContenidoArticulo() {
		return contenidoArticulo;
	}

	public void setContenidoArticulo(String contenidoArticulo) {
		this.contenidoArticulo = contenidoArticulo;
	}

	public Blog getBlogActual() {
		return blogActual;
	}

	public void setBlogActual(Blog blogActual) {
		this.blogActual = blogActual;
	}

	public long getIdBlog1() {
		return idBlog1;
	}

	public void setIdBlog1(long idBlog1) {
		this.idBlog1 = idBlog1;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public List<Articulo> getListaArticulo() {
		return listaArticulo;
	}

	public void setListaArticulo(List<Articulo> listaArticulo) {
		this.listaArticulo = listaArticulo;
	}

}
