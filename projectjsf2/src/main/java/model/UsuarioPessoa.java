package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
	/*Nameds querys podem ser chamados em qualquer lugar do sistema de uma maneira muito simples
	 * são usados as querys mais simples ou que englobam todos os dados do banco ex: select u */
	@NamedQuery(name = "UsuarioPessoa.Listar", query = "select u from UsuarioPessoa u"),
	@NamedQuery(name = "UsuarioPessoa.buscaPorNome", query = "select u from UsuarioPessoa u where u.nome = :nome")
})
public class UsuarioPessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)/*Faz o hibernate incrementar automaticamente o id no banco*/
	private Long id;
	
	private String nome,sobrenome;
	private String login,senha;
	private String sexo;
	private String idade;
	private Double salario;
	
	@Column(columnDefinition = "text")
	private String imagem;
	
	
	/*@OneToMany: Um para muitos
	 * Do lado dos telefones, qual é o atributo responsável por identificar os telefones?
	 * o atributo usuarioPessoa, então colocamos no mapeamento
	 * (fetch = FetchType.EAGER) Serve para trazer o telefone sempre que consultar o usuario
	 * ou seja, amarra as tabelas tambem na consulta
	 * (cascade = CascadeType.REMOVE, orphanRemoval = true) Ao excluir o pai, tambem exclui os filhos(remove em cascata)*/
	@OneToMany(mappedBy = "usuarioPessoa", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.REMOVE)
	
	private List<TelefoneUser> telefoneuser = new ArrayList<TelefoneUser>();
	
	/*(fetch = FetchType.LAZY) só tras os emails quando for chamado o getEmails e nao quendo
	 * for chamado a classe*/
	@OneToMany(mappedBy = "usuarioPessoa", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
	private List<EmailUser> emails = new ArrayList<EmailUser>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
		
	public List<TelefoneUser> getTelefoneuser() {
		return telefoneuser;
	}
	public void setTelefoneuser(List<TelefoneUser> telefoneuser) {
		this.telefoneuser = telefoneuser;
	}
	
	public String getIdade() {
		return idade;
	}
	
	public void setIdade(String idade) {
		this.idade = idade;
	}
	
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public void setSalario(Double salario) {
		this.salario = salario;
	}
	
	public Double getSalario() {
		return salario;
	}
	
	public void setEmails(List<EmailUser> emails) {
		this.emails = emails;
	}
	
	public List<EmailUser> getEmails() {
		return emails;
	}
	
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
	public String getImagem() {
		return imagem;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public String toString() {
		return "UsuarioPessoa [id=" + id + ", nome=" + nome + ", sobrenome=" + sobrenome + ", login=" + login
				+ ", senha=" + senha + ", sexo=" + sexo + ", idade=" + idade + ", salario=" + salario
				+ ", telefoneuser=" + telefoneuser + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioPessoa other = (UsuarioPessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
