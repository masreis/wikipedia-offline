package net.marcoreis.wikipedia.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.marcoreis.wikipedia.vo.CategoriaWikipedia;

import org.apache.log4j.Logger;

public class DAOCategoriaWikipedia {
    private static Logger logger = Logger
	    .getLogger(DAOCategoriaWikipedia.class);
    private Connection conexao;
    private String pwd = "";
    private String user = "root";
    private String url = "jdbc:mysql://localhost:3306/db_wikipedia";
    private String driver = "com.mysql.jdbc.Driver";

    public DAOCategoriaWikipedia() {
	try {
	    Class.forName(driver);
	    conexao = DriverManager.getConnection(url, user, pwd);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void inserir(CategoriaWikipedia categoria) {
	try {
	    String sql = "insert into Categoria (descricao) values (?)";
	    PreparedStatement pstmt = conexao.prepareStatement(sql);
	    pstmt.setString(1, categoria.getDescricao());
	    pstmt.executeUpdate();
	    pstmt.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }

    public void fechar() {
	try {
	    conexao.close();
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}
    }

}
