package fag.arquivo;

import java.io.IOException;

import fag.dados.UsuarioDoSistema;

// Esta interface define o que qualquer repositorio precisa fazer para salvar e carregar dados.

// Interface RepositorioDeDadosDoSistema: define o contrato de persistencia do sistema.
public interface RepositorioDeDadosDoSistema {

	//  recupera o usuario salvo no arquivo.
	UsuarioDoSistema carregarDados() throws IOException;

	// recupera ou cria os dados de um usuario especifico.
	UsuarioDoSistema carregarDadosDoUsuario(String apelido) throws IOException;

	//  grava o usuario e suas listas em JSON.
	void armazenarDados(UsuarioDoSistema usuario) throws IOException;
}

