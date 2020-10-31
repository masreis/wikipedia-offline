package net.marcoreis.wikipedia.dump;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GerarDumpCSV {

    private static Logger logger = LoggerFactory.getLogger(GerarDumpCSV.class);
    private Connection conexao;
    private String pwd = "root";
    private String user = "root";
    private String url = "jdbc:mysql://localhost:3306/db_wikipedia";
    private String driver = "com.mysql.jdbc.Driver";

    public GerarDumpCSV() {
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void iniciar() {
        String sql = "select id, title, timestamp, username, text, model, format, comment from PaginaWikipedia ";
        // sql += "where text like '%star trek%'";
        sql += "limit 100000 ";
        try {
            Statement stmt = conexao.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            conexao.setAutoCommit(false);
            stmt.setFetchSize(Integer.MIN_VALUE);
            ResultSet rs = stmt.executeQuery(sql);
            String caminho = System.getProperty("user.home") + "/dados/dump-wikipedia-parcial.csv";
            FileWriter fw = new FileWriter(caminho);
            while (rs.next()) {
                fw.write(rs.getString(1));
                fw.write("^|^");
                fw.write(rs.getString(2));
                fw.write("^|^");
                fw.write(rs.getString(3));
                fw.write("^|^");
                fw.write(rs.getString(4) + "");
                fw.write("^|^");
                fw.write(rs.getString(5));
                fw.write("^|^");
                fw.write(rs.getString(6));
                fw.write("^|^");
                fw.write(rs.getString(7));
                fw.write("\n");
            }
            rs.close();
            stmt.close();
            fw.close();
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new GerarDumpCSV().iniciar();
    }
}
