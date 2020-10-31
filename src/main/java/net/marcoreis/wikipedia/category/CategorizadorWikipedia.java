package net.marcoreis.wikipedia.category;

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.marcoreis.wikipedia.dao.DAOCategoriaWikipedia;
import net.marcoreis.wikipedia.dao.DAOPaginaWikipedia;
import net.marcoreis.wikipedia.vo.CategoriaWikipedia;

public class CategorizadorWikipedia {
    private static Logger logger = LoggerFactory.getLogger(CategorizadorWikipedia.class);
    private DAOCategoriaWikipedia daoCategoria = new DAOCategoriaWikipedia(10000);
    private DAOPaginaWikipedia daoPagina = new DAOPaginaWikipedia();
    private CategoryExtractor extractor = new CategoryExtractor();

    public void carregarCategorias() {
        try {
            int quantidadePaginasAnalisadas = 0;
            ResultSet rs = daoPagina.findAll();
            while (rs.next()) {
                extractor.extractAndAddText(rs.getString("text"));
                quantidadePaginasAnalisadas++;
            }
            logger.info("Quantidade de paginas analisados: " + quantidadePaginasAnalisadas);
            rs.close();
            daoPagina.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        CategorizadorWikipedia c = new CategorizadorWikipedia();
        c.carregarCategorias();
        c.inserirCategorias();
    }

    private void inserirCategorias() {
        int quantidadeCategoriasCriadas = 0;
        for (String categoria : extractor.getCategories()) {
            daoCategoria.inserir(new CategoriaWikipedia(categoria));
            quantidadeCategoriasCriadas++;
        }
        logger.info("Quantidade de categorias criadas: " + quantidadeCategoriasCriadas);
        daoCategoria.fechar();
    }
}
