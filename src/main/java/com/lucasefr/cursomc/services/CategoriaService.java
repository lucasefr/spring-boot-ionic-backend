package com.lucasefr.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lucasefr.cursomc.domain.Categoria;
import com.lucasefr.cursomc.repositories.CategoriaRepository;
import com.lucasefr.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria  buscar(Integer id) {
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

}
