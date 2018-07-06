package com.lucasefr.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.lucasefr.cursomc.domain.Categoria;
import com.lucasefr.cursomc.repositories.CategoriaRepository;
import com.lucasefr.cursomc.services.exceptions.DataIntegrityException;
import com.lucasefr.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria  find(Integer id) {
		Categoria obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " 
		+ id + "Tipo: " + Categoria.class.getName());
		}
		return obj;
	}
	
	//metodo para inserção de categoria
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	//metodo para fazer update
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj);
	}
	
	//metodo para fazer delete
	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		}
		catch (DataIntegrityViolationException e) {
			// TODO: Tratamento de exceção
			throw new DataIntegrityException("Não é possivel excluir uma categoria porque possui produtos");
		}
		
	}
	
	//Metodo para listagem de Categorias
	public List<Categoria> findAll(){
		return repo.findAll();
	}
	
	//Metodo para controle de numero de Categorisa
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	

}
