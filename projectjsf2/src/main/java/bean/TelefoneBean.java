package bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefone;
import dao.DaoUsuario;
import model.TelefoneUser;
import model.UsuarioPessoa;

@ManagedBean(name = "telefoneBean")
@ViewScoped
public class TelefoneBean {

	private UsuarioPessoa user = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUser = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefone<TelefoneUser> daoTelefone = new DaoTelefone<TelefoneUser>();
	private TelefoneUser telefone = new TelefoneUser();
	private List<TelefoneUser> telefones = new ArrayList<TelefoneUser>();
	
	@PostConstruct
	public void init() {
		telefones = daoTelefone.listar(TelefoneUser.class);
		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codUser");
		System.out.println(codUser);
		user = daoUser.pesquisar(Long.parseLong(codUser), UsuarioPessoa.class);
	}

	public String Salvar() {
		telefone.setUsuarioPessoa(user);
		daoTelefone.salvar(telefone);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Telefone: "+telefone.getNumero()+" Salvo com sucesso!"));
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		telefone = new TelefoneUser();
		return "";
	}
	
	public String Excluir() {
		daoTelefone.deletarPorId(telefone);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Telefone: "+telefone.getNumero()+" Excluido com sucesso!"));
		user = daoUser.pesquisar(user.getId(), UsuarioPessoa.class);
		telefone = new TelefoneUser();
		return "";
	}
	
	public UsuarioPessoa getUser() {
		return user;
	}

	public void setUser(UsuarioPessoa user) {
		this.user = user;
	}
	
	public void setDaoTelefone(DaoTelefone<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}

	public DaoTelefone<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}
	
	public void setTelefone(TelefoneUser telefone) {
		this.telefone = telefone;
	}
	
	public TelefoneUser getTelefone() {
		return telefone;
	}
	
	public List<TelefoneUser> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(List<TelefoneUser> telefones) {
		this.telefones = telefones;
	}
	
}
