package net.marcoreis.wikipedia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.marcoreis.wikipedia.vo.CategoriaWikipedia;

public class DAOCategoriaWikipedia {
    private static Logger logger = LoggerFactory.getLogger(DAOCategoriaWikipedia.class);
    private Connection conexao;
    private String pwd = "root";
    private String user = "root";
    private String url = "jdbc:mysql://localhost:3306/db_wikipedia";
    private String driver = "com.mysql.jdbc.Driver";
    private String sql = "insert into CategoriaWikipedia (descricao) values (?)";
    private PreparedStatement pstmt;
    private int batchSize;
    private int counter;

    public DAOCategoriaWikipedia() {
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, pwd);
            pstmt = conexao.prepareStatement(sql);
            conexao.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DAOCategoriaWikipedia(int batchSize) {
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, pwd);
            pstmt = conexao.prepareStatement(sql);
            conexao.setAutoCommit(false);
            this.batchSize = batchSize;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void inserir(CategoriaWikipedia categoria) {
        if (batchSize == 0) {
            inserirSequencial(categoria);
        } else {
            inserirBatch(categoria);
        }
    }

    private void inserirSequencial(CategoriaWikipedia categoria) {
        try {
            pstmt.setString(1, categoria.getDescricao());
            pstmt.executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    private void inserirBatch(CategoriaWikipedia categoria) {
        try {
            pstmt.setString(1, categoria.getDescricao());
            pstmt.addBatch();
            counter++;
            if (counter == batchSize) {
                pstmt.executeBatch();
                pstmt.clearBatch();
                conexao.commit();
                counter = 0;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public void fechar() {
        try {
            pstmt.close();
            conexao.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
