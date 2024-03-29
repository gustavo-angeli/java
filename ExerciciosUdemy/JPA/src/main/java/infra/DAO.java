package infra;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DAO<E> {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private Class<E> classe;

	static {
		try {
			emf = Persistence.createEntityManagerFactory("JPA");

		} catch (Exception e) {
			// logar -> log4j
		}
	}

	// Construtores
	public DAO() {
		this(null);
	}
	public DAO(Class<E> classe) {
		this.classe = classe;
		em = emf.createEntityManager();
	}

	// M�todos para incluir
	public DAO<E> abrirTransacao() {
		em.getTransaction().begin();
		return this;
	}
	public DAO<E> fecharTransacao() {
		em.getTransaction().commit();
		return this;
	}
	public DAO<E> incluirTransacao(E entidade) {
		em.persist(entidade);
		return this;
	}
	public DAO<E> incluirTudo(E entidade) {
		return this.abrirTransacao().incluirTransacao(entidade).fecharTransacao();
	}

	// M�todos de busca
	public List<E> obterTodos() {
		return this.obterAlguns(10, 0);
	}
	public List<E> obterAlguns(int qtde, int deslocamento) {
		if (classe == null) {
			throw new UnsupportedOperationException("Classe nula.");
		}

		String jpql = "SELECT e FROM " + classe.getName() + " e";
		TypedQuery<E> query = em.createQuery(jpql, classe);
		query.setMaxResults(qtde);
		query.setFirstResult(deslocamento);
		return query.getResultList();
	}
	public E buscarPorID(Object o) {
		return em.find(classe, o);
	}
	
	public List<E> consultar (String nomeConsulta, Object... params) {
		TypedQuery<E> query = em.createNamedQuery(nomeConsulta, classe);
		
		for (int i = 0; i < params.length; i += 2) {
			query.setParameter(params[i].toString(), params[i + 1]);
		}
		return query.getResultList();
	}
	
	public E consultarUm(String nomeConsulta, Object... params) {
		List<E> lista = consultar(nomeConsulta, params);
		return lista.isEmpty() ? null : lista.get(0);
	}
	
	public void fechar() {
		em.close();
	}
	
}
