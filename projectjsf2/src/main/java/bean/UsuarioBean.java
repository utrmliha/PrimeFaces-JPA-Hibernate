package bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import dao.DaoEmail;
import dao.DaoUsuario;
import datatablelazy.LazyDataTableModelUserPessoa;
import model.EmailUser;
import model.UsuarioPessoa;

@ManagedBean(name="usuarioBean")
@ViewScoped
public class UsuarioBean {
	
	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	private LazyDataTableModelUserPessoa<UsuarioPessoa> listaUsuarios = new LazyDataTableModelUserPessoa<UsuarioPessoa>();
	private BarChartModel barChartModel = new BarChartModel();
	private EmailUser emailuser = new EmailUser();
    private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();
	private String campoPesquisa;
    
    
	/*Carrega a lista no banco somente uma vez ao abrir a tela, exclusões e adição de um
	 * novo usuario será feito no banco e na List(listaUsuarios)
	 * para otimização e correção de bugs*/
	@PostConstruct
	public void init() {
		listaUsuarios.load(0, 5, null, null, null);
		montarGrafico();		
	}
	
	public void Upload(FileUploadEvent image) {
		String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setImagem(imagem);
	}
	
	@SuppressWarnings("static-access")
	public void download() throws Exception{
			Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			String fileDownloadID = params.get("fileDownloadId");
			
			UsuarioPessoa pessoa = daoGeneric.pesquisar(Long.parseLong(fileDownloadID), UsuarioPessoa.class);
			
			if(pessoa.getImagem() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ", "Não existe imagem cadastrada para esse usuário"));
				return;
			}
			byte[] imagem = new Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);

			HttpServletResponse response = (HttpServletResponse)
					FacesContext.getCurrentInstance().getExternalContext().getResponse();
			
			response.addHeader("Content-Disposition","attachment; filename=download.png");
			response.setContentType("application/octet-stream");
			response.setContentLength(imagem.length);
			response.getOutputStream().write(imagem);
			response.getOutputStream().flush();
			FacesContext.getCurrentInstance().responseComplete();		
	}
	
	public void Pesquisar() {
		listaUsuarios.pesquisar(campoPesquisa);
		montarGrafico();
	}
	
	private void montarGrafico() {
		barChartModel = new BarChartModel();
		ChartSeries userSalario = new ChartSeries();
		
		for (UsuarioPessoa usuarioPessoa : listaUsuarios.list) {
			//Adiciona valores na tabela(Nome da coluna, Valor)
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario());
		}
		
		barChartModel.addSeries(userSalario);
		barChartModel.setTitle("Gráfico de salários");
	}
	
	public void test() {
		System.out.println("mudou de página");
	}

	public void ExcluirEmail() throws Exception{
		String codEmail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigoEmail");
		
		EmailUser emailRemover = new EmailUser();
		emailRemover.setId(Long.parseLong(codEmail));
		daoEmail.deletarPorId(emailRemover);
		usuarioPessoa.getEmails().remove(emailRemover);
				
	}
	
	public void salvarEmail() {
		emailuser.setUsuarioPessoa(usuarioPessoa);
		emailuser = daoEmail.updateMerge(emailuser);
		usuarioPessoa.getEmails().add(emailuser);
		emailuser = new EmailUser();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado...", "Salvo com sucesso!"));
	}
	
	public String Salvar() {
		daoGeneric.salvar(usuarioPessoa);
		listaUsuarios.list.add(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		init();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
		return "";
	}
	public String Novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	public String Excluir() throws Exception {
		daoGeneric.removerUsuario(usuarioPessoa);
		listaUsuarios.list.remove(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		init();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Usuario: Excluido com sucesso!"));

		return "";
	}
	
	/*Tratando exceções*/
	/*
	public String salvar() {
		try {
			daoGeneric.salvar(usuarioPessoa);//as exceções devem ser jogadas para cima no método do dao generic para capturar aki
			listaUsuarios.add(usuarioPessoa);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
			usuarioPessoa = new UsuarioPessoa();
		} catch (Exception e) {
			//org.hibernate.exception.ConstraintViolationException nome da exception que estourou no console
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Erro ao excluir do banco"));
			}else {
				e.printStackTrace();
			}
		}
		return "";
	}*/
	
	/*------------------Getters e Setters------------------*/
	
	public BarChartModel getBarChartModel() {
		return barChartModel;
	}
	
	public void setBarChartModel(BarChartModel barChartModel) {
		this.barChartModel = barChartModel;
	}
	
	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}
	
	public LazyDataTableModelUserPessoa<UsuarioPessoa> getListaUsuarios() {
		montarGrafico();
		return listaUsuarios;
	}
	
	public void setListaUsuarios(LazyDataTableModelUserPessoa<UsuarioPessoa> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
	public EmailUser getEmailuser() {
		return emailuser;
	}
	
	public void setEmailuser(EmailUser emailuser) {
		this.emailuser = emailuser;
	}
	
	public String getCampoPesquisa() {
		return campoPesquisa;
	}
	
	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}

}
