package org.furniture.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBConnect {
    private static Connection conn;

    public static void loadDriver() {
        conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/furniture-selling-system?" + "user=root");
            System.out.println("Connection Is Successful");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static void closeDriver() throws SQLException {
        conn.close();
        System.out.println("Connection Is Closed");
    }

    private static void printResults(ResultSet rs) {
        try {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();

            //print head
            for (int i = 0; i < cols; i++) {
                String name = md.getColumnLabel(i + 1);
                System.out.print(name + "\t");
            }
            System.out.println();

            //print data
            while (rs.next()) {
                for (int i = 0; i < cols; i++) {
                    String value = rs.getString(i + 1);
                    System.out.print(value + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error printing results: " + e.getMessage());
        }

    }

    public static ResultSet query(String codeSQL) {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(codeSQL);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return rs;
    }

    public static void queryUpdate(String codeSQL){
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(codeSQL);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void checkStock() throws SQLException {
        ResultSet rs = null;
        String SQL_0 = "SELECT id FROM sale_order so WHERE so.furniture_status=0";
        rs = query(SQL_0);
        List<Integer> idSaleOrderStatus0 = new ArrayList<>();
        while (rs.next()) {
            idSaleOrderStatus0.add(rs.getInt("id"));
        }
        for (int id : idSaleOrderStatus0) {
            rs = query("SELECT *,sum(IF(quantity > sum_spend,0,-1)) status FROM (SELECT so.id so_id,m.id m_id,SUM(spend) sum_spend,m.quantity\n" +
                    "FROM sale_order_list AS ol\n" +
                    "   INNER JOIN sale_order AS so\n" +
                    "       ON so.id = ol.fk_sale_order_id\n" +
                    "   INNER JOIN furniture AS f\n" +
                    "       ON f.id = ol.fk_furniture_id\n" +
                    "   INNER JOIN bill_of_material AS bom\n" +
                    "       ON bom.fk_furniture_id = f.id\n" +
                    "   INNER JOIN material AS m\n" +
                    "       ON m.id = bom.fk_material_id\n" +
                    "WHERE so.id=" + id + "\n" +
                    "GROUP BY so_id,m.id\n" +
                    "ORDER BY m.id) temp"
            );
            rs.next();
            System.out.println(rs.getInt("status"));
            if (rs.getInt("status") == 0) {
                query("UPDATE sale_order\n" +
                        "SET furniture_status=1\n" +
                        "WHERE id=" + id);
                rs = query("SELECT so.id so_id,m.id m_id,SUM(spend) sum_spend,m.quantity\n" +
                        "FROM sale_order_list AS ol\n" +
                        "   INNER JOIN sale_order AS so\n" +
                        "       ON so.id = ol.fk_sale_order_id\n" +
                        "   INNER JOIN furniture AS f\n" +
                        "       ON f.id = ol.fk_furniture_id\n" +
                        "   INNER JOIN bill_of_material AS bom\n" +
                        "       ON bom.fk_furniture_id = f.id\n" +
                        "   INNER JOIN material AS m\n" +
                        "       ON m.id = bom.fk_material_id\n" +
                        "WHERE so.id=" + id + "\n" +
                        "GROUP BY so_id,m.id\n" +
                        "ORDER BY m.id"
                );

                while (rs.next()) {
                    query("UPDATE material\n" +
                            "SET quantity=" + (rs.getInt("quantity") - rs.getInt("sum_spend")) +
                            "WHERE id=" + rs.getInt("m_id"));
                }
            }
        }
    }

    public void insertData(String tableName, String[] columnName, String[] data) throws SQLException {
        ResultSet rs = null;
        rs = query("INSERT INTO " + tableName + " (" + columnName + ")\n" +
                "VALUES (" + data + ")");
        printResults(rs);
    }

    public void deleteData(String tableName, String id) {
        ResultSet rs = null;
        rs = query("DELETE FROM " + tableName + " WHERE id=" + id);
        printResults(rs);
    }

    public void updateData(String tableName, String id, String[] columnName, String[] data) {
        ResultSet rs = null;
        for (int i = 0; i < columnName.length; i++) {
            rs = query("UPDATE " + tableName + "\n" +
                    "SET " + columnName[i] + " = " + data[i] + "\n" +
                    "WHERE id=" + id);
            printResults(rs);
        }
    }

        public static Collection<String> customerNameCollection() throws SQLException {
        ResultSet rs = null;
        rs = query("SELECT c.name FROM customer c");
        ArrayList<String> arrayList = new ArrayList<>();
        while (rs.next()){
            arrayList.add(rs.getString("name"));
        }
        return arrayList;
    }

    public static Collection<String> furnitureNameCollection() throws SQLException {
        ResultSet rs = null;
        rs = query("SELECT f.name FROM furniture f");
        ArrayList<String> arrayList = new ArrayList<>();
        while (rs.next()){
            arrayList.add(rs.getString("name"));
        }
        return arrayList;
    }

    public static Collection<String> materialNameCollection() throws SQLException {
        ResultSet rs = null;
        rs = query("SELECT m.name FROM material m");
        ArrayList<String> arrayList = new ArrayList<>();
        while (rs.next()){
            arrayList.add(rs.getString("name"));
        }
        return arrayList;
    }
}
