package net.marcoreis.wikipedia.jdbc;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.mysql.jdbc.StringUtils;

public class GerarDumpCSV {

    private static Logger logger = Logger.getLogger(GerarDumpCSV.class);
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
        String sql = "select id, title, timestamp, username, text, model, format, comment from PaginaWikipedia";
        try {
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String caminho = System.getProperty("user.home")
                    + "/dados/dump-wikipedia.csv";
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
            logger.error(e);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new GerarDumpCSV().iniciar();
    }
}
