package blog.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import blog.model.entities.Articulo;
import blog.model.entities.Blog;
import blog.model.entities.Usuario;
import blog.model.manager.ManagerBlog;
import blog.view.util.JSFUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@ManagedBean
@SessionScoped
public class ControllerBlog {
	private List<Blog> listaBlogs;
	private List<Articulo> listaArticulo;
	private String nombreBlog;
	private String descripcionBlog;
	private String tituloArticulo;
	private String contenidoArticulo;
	private Blog blogActual;
	private long idBlog1;
	private String idUser;

	@ManagedProperty(value = "#{controllerUsuario}")
	private ControllerUsuario controllerUsuario;
	@EJB
	private ManagerBlog managerBlog;

	@PostConstruct
	public void iniciar() {
		listaBlogs = managerBlog.findBlogByUsuario(controllerUsuario.getIdUsuario());

		JSFUtil.crearMensajeInfo("Blogs: " + listaBlogs.size());
	}

	public void actionListenerConsultarBlogs() {
		listaBlogs = managerBlog.findBlogByUsuario(controllerUsuario.getIdUsuario());
		JSFUtil.crearMensajeInfo("Blogs: " + listaBlogs.size());
	}

	public void actionListenerCrearBlog() {
		String idUsuario = controllerUsuario.getIdUsuario();
		try {
			managerBlog.crearBLog(idUsuario, nombreBlog, descripcionBlog, tituloArticulo, contenidoArticulo);
			JSFUtil.crearMensajeInfo("Blog creado.");
			listaBlogs = managerBlog.findBlogByUsuario(controllerUsuario.getIdUsuario());
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionListenerEliminarBlog(long idBlog) {
		try {
			managerBlog.eliminarBlog(idBlog);
			listaBlogs = managerBlog.findBlogByUsuario(controllerUsuario.getIdUsuario());
			JSFUtil.crearMensajeInfo("Blog Eliminado");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public String actionListaArticulos(long idBlog) {
		listaArticulo = managerBlog.finArticuloByBlog(idBlog);
		return "Articulos";
	}

	public void actionListenerCargarUsuarioAdmin(Blog blog) {
		idBlog1 = blog.getIdBlog();
		idUser = blog.getUsuario().getIdUsuario();
		nombreBlog = blog.getNombreBlog();
		descripcionBlog = blog.getDescripcion();

	}

	public void actionListenerActualizarBlog() {
		try {
			managerBlog.editarBlog(idBlog1, idUser, nombreBlog, descripcionBlog);
			listaBlogs = managerBlog.findBlogByUsuario(controllerUsuario.getIdUsuario());
			JSFUtil.crearMensajeInfo("Actualización correcta.");
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	public String actionReporte() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		/*
		 * parametros.put("p_titulo_principal",p_titulo_principal);
		 * parametros.put("p_titulo",p_titulo);
		 */ FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("admin/blogs.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=blogs.pdf");
		response.setContentType("application/pdf");
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/blog", "postgres", "postgres");
			JasperPrint impresion = JasperFillManager.fillReport(ruta, parametros, connection);
			JasperExportManager.exportReportToPdfStream(impresion, response.getOutputStream());
			context.getApplication().getStateManager().saveView(context);
			System.out.println("reporte generado.");
			context.responseComplete();
		} catch (Exception e) {
			JSFUtil.crearMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public void actionListenerInsertarArticulo() {
		managerBlog.insertarArticulo(blogActual, tituloArticulo, contenidoArticulo);
	}

	public void actionListenerEliminarArticulo(Articulo articulo) {
		managerBlog.eliminarArticulo(articulo);
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

	public ControllerUsuario getControllerUsuario() {
		return controllerUsuario;
	}

	public void setControllerUsuario(ControllerUsuario controllerUsuario) {
		this.controllerUsuario = controllerUsuario;
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
