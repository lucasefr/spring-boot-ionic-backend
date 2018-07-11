package com.lucasefr.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.lucasefr.cursomc.domain.Cidade;
import com.lucasefr.cursomc.domain.Cliente;
import com.lucasefr.cursomc.domain.Endereco;
import com.lucasefr.cursomc.domain.enums.TipoCliente;
import com.lucasefr.cursomc.dto.ClienteDTO;
import com.lucasefr.cursomc.dto.ClienteNewDTO;
import com.lucasefr.cursomc.repositories.CidadeRepository;
import com.lucasefr.cursomc.repositories.ClienteRepository;
import com.lucasefr.cursomc.repositories.EnderecoRepository;
import com.lucasefr.cursomc.services.exceptions.DataIntegrityException;
import com.lucasefr.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente  find(Integer id) {
		Cliente obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " 
		+ id + "Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	//metodo para inserção de Cliente
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}
	
	//metodo para fazer update
		public Cliente update(Cliente obj) {
			Cliente newObj = find(obj.getId());
			updateData(newObj, obj);
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
				throw new DataIntegrityException("Não é possivel excluir porque ha entidades relacionadas");
			}
			
		}
		
		//Metodo para listagem de Clientes
		public List<Cliente> findAll(){
			return repo.findAll();
		}
		
		//Metodo para controle de numero de Cliente
		public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
			PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
			return repo.findAll(pageRequest);
		}
		
		//Metodo auxiliar para validação de dados
		public Cliente fromDTO(ClienteDTO objDto) {
			return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
		}
								
		public Cliente fromDTO(ClienteNewDTO objDto) {													//aula 40
			Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
			Cidade cid = cidadeRepository.findOne(objDto.getCidadeId());
			Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
			cli.getEnderecos().add(end);
			cli.getTelefones().add(objDto.getTelefone1());
			if (objDto.getTelefone2()!=null) {
				cli.getTelefones().add(objDto.getTelefone2());
			}
			if (objDto.getTelefone3()!=null) {
				cli.getTelefones().add(objDto.getTelefone3());
			}
			return cli;
		}
		
		private void updateData( Cliente newObj, Cliente obj) {
			newObj.setNome(obj.getNome());
			newObj.setEmail(obj.getEmail());
		}
	
	

}
