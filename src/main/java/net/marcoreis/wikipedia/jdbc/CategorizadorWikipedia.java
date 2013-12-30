package net.marcoreis.wikipedia.jdbc;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.marcoreis.wikipedia.vo.CategoriaWikipedia;

import org.apache.log4j.Logger;

public class CategorizadorWikipedia {
    private static Logger logger = Logger
	    .getLogger(CategorizadorWikipedia.class);
    private Pattern pattern = Pattern.compile("\\[\\[Categoria\\:(.*?)\\]\\]");
    private DAOCategoriaWikipedia daoCategoria = new DAOCategoriaWikipedia();
    private DAOPaginaWikipedia daoPagina = new DAOPaginaWikipedia();
    private Collection<String> categorias = new HashSet<String>();

    public void criarCategorias() {
	try {
	    int quantidadePaginasAnalisadas = 0;
	    ResultSet rs = daoPagina.findAll();
	    while (rs.next()) {
		String texto = rs.getString("text");
		Matcher matcher = pattern.matcher(texto);
		//
		while (matcher.find()) {
		    String categoria = matcher.group(1);
		    //
		    if (categoria.contains("|")) {
			String subCategorias[] = categoria.split("\\|");
			for (String subCategoria : subCategorias) {
			    if (subCategoria.length() > 0)
				categorias.add(subCategoria.trim());
			}
		    } else {
			categorias.add(categoria.trim());
		    }
		}
		quantidadePaginasAnalisadas++;
	    }
	    logger.info("Quantidade de paginas analisados: "
		    + quantidadePaginasAnalisadas);
	    rs.close();
	    daoPagina.fechar();
	} catch (Exception e) {
	    logger.error(e);
	}
    }

    public static void main(String[] args) {
	CategorizadorWikipedia c = new CategorizadorWikipedia();
	c.criarCategorias();
	c.inserirCategorias();
    }

    private void inserirCategorias() {
	int quantidadeCategoriasCriadas = 0;
	for (String categoria : categorias) {
	    daoCategoria.inserir(new CategoriaWikipedia(categoria));
	    quantidadeCategoriasCriadas++;
	}
	logger.info("Quantidade de categorias criadas: "
		+ quantidadeCategoriasCriadas);
	daoCategoria.fechar();
    }
}
