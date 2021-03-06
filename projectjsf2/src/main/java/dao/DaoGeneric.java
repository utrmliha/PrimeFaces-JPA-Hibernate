package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import hibernate.util.HibernateUtil;

public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();/*resgata a conexão*/
	
	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);/*Salva no banco*/
		transaction.commit();/*Comita a alteração*/
	}
	
	public E updateMerge(E entidade) {/*Salva ou atualiza*/
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);/*Salva no banco*/
		transaction.commit();/*Comita a alteração*/
		
		return entidadeSalva;
	}
	
	@SuppressWarnings("unchecked")
	public E pesquisar(E entidade) {		
		Object id = HibernateUtil.getPrimaryKey(entidade);/*usa o método getPrimaryKey para se obter o id de uma entidade*/
		
		E e = (E) entityManager.find(entidade.getClass(), id);
		return e;
		
	}
	
	@SuppressWarnings("unchecked")
	public E pesquisar(Long id, Class<E> entidade) {/*Segunda forma de salvar, reduz 1 linha de código e não nescessita do método getPrimaryKey do HibernateUtil*/
		entityManager.clear();
		E e = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where id= " +id).getSingleResult();
		return e;
	}
	
	public void deletarPorId(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		/* entidade.getClass().getSimpleName().toLowerCase() retorna o nome da tabela em minusculo*/
		entityManager.createNativeQuery(
				"delete from " + entidade.getClass().getSimpleName().toLowerCase()+
				" where id ="+id).executeUpdate();
		transaction.commit();//comita alteração no banco
	}
	
	public void deletarPorId(Long id, E entidade) {/*Segunda forma de deletar*/
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		/* entidade.getClass().getSimpleName().toLowerCase() retorna o nome da tabela em minusculo*/
		entityManager.createNativeQuery(
				"delete from " + entidade.getClass().getSimpleName().toLowerCase()+
				" where id ="+id).executeUpdate();
		transaction.commit();//comita alteração no banco
	}
	
	@SuppressWarnings("unchecked")
	public List<E> listar(Class<E> entidade){
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		/* entidade.getClass().getSimpleName().toLowerCase() retorna o nome da tabela em minusculo*/
		List<E> lista = entityManager.createQuery("from "+ entidade.getName()).getResultList();
		
		transaction.commit();
		return lista;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
