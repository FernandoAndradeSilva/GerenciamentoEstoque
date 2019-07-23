package br.com.estoque.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.estoque.exception.NegocioException;
import br.com.estoque.model.Item;
import br.com.estoque.model.Usuario;

public class ItemDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;


    public Item salvar(Item item) {
		return manager.merge(item);		
	}
	
	public void excluir(Item item) {
		try {
			item = porId(item.getId());
			manager.remove(item);
			manager.flush();

		} catch (Exception e) {
			throw new NegocioException("ITEM NÃO PODE SER EXCLUÍDO");
		}
	}

	public Item porId(Long id) {
		return manager.find(Item.class, id);	
	}
	
	public boolean verificaCodigoCadastrado(String codigo) {
		Query query = manager.createNativeQuery(
				"SELECT * FROM Item where codigo = :bindCodigo",
				Item.class);
		query.setParameter("bindCodigo", codigo);
		try {
			query.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}	
	}
	
	public List<Item> listAll() {
		return manager.createNativeQuery("SELECT * FROM Item", Item.class).getResultList();
	}



	public List<Item> listAll(String tipo) {
		return manager.createNativeQuery("SELECT * FROM Item "
				+ "inner join tipo on item.tipo = tipo.id "
				+ "where item.totalEntradas >0 "
				+ "and tipo.descricao = :bindTipo ", Item.class)
				.setParameter("bindTipo" , tipo)
				.getResultList();

	}

    public List<Item> listAll(Long idUsuario) {

        return manager.createNativeQuery("SELECT * FROM Item "
                + "inner join tipo on item.tipo = tipo.id "
                + "inner join categoria  on tipo.categoria = categoria.id "
                + "where categoria.id in ("
                + "select categoria_id from usuario_categoria "
                + "where usuario_id = :paramId)", Item.class).setParameter("paramId", idUsuario).getResultList();

    }

	public List<Item> listAll(Long idUsuario, String tipoItemNavegacao) {
		return manager.createNativeQuery("SELECT * FROM Item "
				+ "inner join tipo on item.tipo = tipo.id "
				+ "inner join categoria  on tipo.categoria = categoria.id "
				+ "where categoria.id in ("
				+ "select categoria_id from usuario_categoria "
				+ "where usuario_id = :paramId) "
				+ "and tipo.descricao = :paramTipo", Item.class)
				.setParameter("paramId", idUsuario)
				.setParameter("paramTipo", tipoItemNavegacao)
				.getResultList();
	}

//	/*
//	 * Método para listar todas as movimenteções
//	 * Recebe o id do usuário por parâmetro para
//	 * verificar as categorias do usuário
//	 * */
//	public List<Object> listAllHash(Long id) {
//		HashMap<String , Object> map = new HashMap<>();
//		List<Object> itens = new ArrayList<>();
//
//		List<Object[]> resultList = manager.createNativeQuery("SELECT item.id, item.codigo , item.descricao , "
//				+ " item.especificacao, unidadeDeMedida.sigla, unidadeDeMedida.descricao as d,  sum(estoque.entradas) , "
//				+ " sum(estoque.saidas) , sum(estoque.saldo) , estoque.custo, item.estoqueMinimo FROM Item "
//				+ "inner join tipo on item.tipo = tipo.id "
//				+ "inner join categoria  on tipo.categoria = categoria.id "
//				+ "inner join estoque on estoque.item = item.id "
//				+ "inner join unidadedemedida on unidadeDeMedida.id = item.unidadeDeMedida "
//				+ "where categoria.id in ("
//				+ "select categoria_id from usuario_categoria "
//				+ "where usuario_id = :paramId group by item.id) ")
//				//+ "and estoque.unidade = :paramIdUnidade ")
//				.setParameter("paramId", id).getResultList();
//		//.setParameter("paramIdUnidade" , this.retornaIdUnidadeUsuario())
//
//		for (Object[] array : resultList) {
//			map.put("id" , array[0]);
//			map.put("codigo" , array[1]);
//			map.put("descricao" , array[2]);
//			map.put("especificacao" , array[3]);
//			map.put("siglaUnDeMedida" , array[4]);
//			map.put("nomeUnDeMedida" , array[5]);
//			map.put("entradas" , array[6]);
//			map.put("saidas" , array[7]);
//			map.put("saldo" , array[8]);
//			map.put("custo" , array[9]);
//			map.put("estoqueMinimo" , array[10]);
//			itens.add(map);
//			map = new HashMap<>();
//		}
//		return itens;
//	}
//
//	/*
//	 * Método para listar todas as movimenteções
//	 * Recebe o id do usuário por parâmetro para
//	 * verificar as categorias do usuário
//	 * Recebe o tipo da movimentação por parâmetro
//	 * */
//	public List<Object> listAllHash(Long id , String tipoItemNavegacao) {
//
//		HashMap<String , Object> map = new HashMap<>();
//		List<Object> itens = new ArrayList<>();
//
//		List<Object[]> resultList = manager.createNativeQuery("SELECT item.id, item.codigo , item.descricao , "
//				+ " item.especificacao, unidadeDeMedida.sigla, unidadeDeMedida.descricao as d, sum(estoque.entradas) , "
//				+ " sum(estoque.saidas) , sum(estoque.saldo) , estoque.custo, estoque.unidade item.estoqueMinimo FROM Item "
//				+ "inner join tipo on item.tipo = tipo.id "
//				+ "inner join categoria  on tipo.categoria = categoria.id "
//				+ "inner join estoque on estoque.item = item.id "
//				+ "inner join unidadedemedida on unidadeDeMedida.id = item.unidadeDeMedida "
//				+ "where categoria.id in ("
//				+ "select categoria_id from usuario_categoria "
//				+ "where usuario_id = :paramId) "
//				+ "and tipo.descricao = :paramTipo group by item.id")
//				//+ "and estoque.unidade = :paramIdUnidade ")
//				.setParameter("paramId", id)
//				.setParameter("paramTipo", tipoItemNavegacao).getResultList();
//		//.setParameter("paramIdUnidade" , this.retornaIdUnidadeUsuario())
//
//		for (Object[] array : resultList) {
//			map.put("id" , array[0]);
//			map.put("codigo" , array[1]);
//			map.put("descricao" , array[2]);
//			map.put("especificacao" , array[3]);
//			map.put("siglaUnDeMedida" , array[4]);
//			map.put("nomeUnDeMedida" , array[5]);
//			map.put("entradas" , array[6]);
//			map.put("saidas" , array[7]);
//			map.put("saldo" , array[8]);
//			map.put("custo" , array[9]);
//			map.put("estoqueMinimo" , array[10]);
//			itens.add(map);
//			map = new HashMap<>();
//		}
//
//		return itens;
//	}




}
