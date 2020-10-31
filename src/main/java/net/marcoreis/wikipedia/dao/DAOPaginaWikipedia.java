package net.marcoreis.wikipedia.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.marcoreis.wikipedia.vo.PaginaWikipedia;

public class DAOPaginaWikipedia {
    private static Logger logger = LoggerFactory.getLogger(DAOPaginaWikipedia.class);
    private Connection conexao;
    private String pwd = "root";
    private String user = "root";
    private String url = "jdbc:mysql://localhost:3306/db_wikipedia";
    private String driver = "com.mysql.jdbc.Driver";
    private int batchSize;
    private int counter = 0;
    private String sql = "insert into PaginaWikipedia "
            + "(id, title, timestamp, username, text, model, format, comment) values (?,?,?,?,?,?,?,?)";
    private PreparedStatement pstmt;

    public DAOPaginaWikipedia() {
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, pwd);
            pstmt = conexao.prepareStatement(sql);
            conexao.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DAOPaginaWikipedia(int batchSize) {
        try {
            this.batchSize = batchSize;
            Class.forName(driver);
            url += "?rewriteBatchedStatements=true";
            conexao = DriverManager.getConnection(url, user, pwd);
            pstmt = conexao.prepareStatement(sql);
            conexao.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void inserirSequencial(PaginaWikipedia pagina) {
        try {
//            pstmt.clearParameters();
            pstmt.setLong(1, pagina.getId());
            pstmt.setString(2, pagina.getTitle());
            Date data = new Date(pagina.getTimeStamp().getTime());
            pstmt.setTimestamp(3, new Timestamp(data.getTime()));
            pstmt.setString(4, pagina.getUserName());
            pstmt.setString(5, pagina.getText());
            pstmt.setString(6, pagina.getModel());
            pstmt.setString(7, pagina.getFormat());
            pstmt.setString(8, pagina.getComment());
            int qtd = pstmt.executeUpdate();
            if (qtd != 1) {
                logger.error("Registro nao incluido -> " + pagina.getId());
            }
            conexao.commit();
//            pstmt.close();
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void inserir(PaginaWikipedia pagina) {
        if (batchSize == 0) {
            inserirSequencial(pagina);
        } else {
            inserirBatch(pagina);
        }
    }

    private void inserirBatch(PaginaWikipedia pagina) {
        try {
            Date data = new Date(pagina.getTimeStamp().getTime());
            pstmt.setLong(1, pagina.getId());
            pstmt.setString(2, pagina.getTitle());
            pstmt.setTimestamp(3, new Timestamp(data.getTime()));
            pstmt.setString(4, pagina.getUserName());
            pstmt.setString(5, pagina.getText());
            pstmt.setString(6, pagina.getModel());
            pstmt.setString(7, pagina.getFormat());
            pstmt.setString(8, pagina.getComment());
            pstmt.addBatch();
            counter++;
            if (counter == batchSize) {
                pstmt.executeBatch();
                pstmt.clearBatch();
                counter = 0;
                conexao.commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public ResultSet findAll() {
        try {
            String sql = "select id, text from PaginaWikipedia";
            PreparedStatement pstmt = conexao.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            pstmt.setFetchSize(Integer.MIN_VALUE);
            ResultSet rs = pstmt.executeQuery();
            return rs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws SQLException {
        pstmt.close();
        conexao.close();
    }
}
