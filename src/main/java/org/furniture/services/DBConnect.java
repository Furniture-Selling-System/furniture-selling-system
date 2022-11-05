package org.furniture.services;

import org.furniture.enums.OrderStatus;
import org.furniture.models.Customer;
import org.furniture.models.Furniture;
import org.furniture.models.Material;
import org.furniture.models.Order;

import java.sql.*;
import java.util.ArrayList;
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

    public static void queryUpdate(String codeSQL) {
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

    private static List<Customer> createCustomersList(String query) {
        List<Customer> customerArrayList = new ArrayList<>();
        try {
            ResultSet rs = null;
            rs = query(query);
            while (rs.next()) {
                customerArrayList.add(new Customer(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getString("address"),
                                rs.getString("phone")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customerArrayList;
    }

    private static Customer createCustomer(String query) {
        Customer customer = null;
        try {
            ResultSet rs = null;
            rs = query(query);
            while (rs.next()) {
                customer = new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    public static List<Customer> getCustomers() {
        return createCustomersList("SELECT c.id,c.name,c.address,c.phone FROM customer c");
    }

    public static List<Customer> getCustomersByName(String name) {
        return createCustomersList("SELECT c.id,c.name,c.address,c.phone FROM customer c\n" +
                "WHERE c.name LIKE '%" + name + "%'");
    }

    public static Customer getCustomersByID(String id) {
        return createCustomer("SELECT c.id,c.name,c.address,c.phone FROM customer c\n" +
                "WHERE c.id=" + id);
    }

    private static List<Furniture> createFurnitureList(String query) {
        List<Furniture> furnitureArrayList = new ArrayList<>();
        try {
            ResultSet rs = null;

            rs = query(query);
            List<String> idFurnitureList = new ArrayList<>();

            while (rs.next())
                idFurnitureList.add(rs.getString("id"));

            for (String i : idFurnitureList) {
                furnitureArrayList.add(getFurnitureByID(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return furnitureArrayList;
    }

    private static Furniture createFurniture(String condition){
        ResultSet rs = null;
        rs = query("SELECT f.id,f.cost,f.name,bom.spend,m.name m_name,m.id m_id FROM furniture f\n" +
                "INNER JOIN bill_of_material bom\n" +
                "ON bom.fk_furniture_id = f.id\n" +
                "INNER JOIN material m\n" +
                "ON m.id = bom.fk_material_id\n" +
                condition);
        Furniture furniture = null;
        try {
            while (rs.next()) {
                if (furniture == null) {
                    furniture = new Furniture(
                            rs.getString("id"),
                            rs.getString("name"),
                            Integer.parseInt(rs.getString("cost")));
                }
                furniture.addMaterial(getMaterialByID(rs.getString("m_id")),
                        rs.getInt("spend"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return furniture;
    }

    public static Furniture getFurnitureByID(String id){
        return createFurniture("WHERE f.id =" + id);
    }
    public static List<Furniture> getFurnituresList() {
        return createFurnitureList("SELECT f.id FROM furniture f");
    }
    public static List<Furniture> getFurnituresListByName(String name) {
        return createFurnitureList("SELECT f.id FROM furniture f\n" +
                "WHERE f.name LIKE '%" + name + '%');
    }

    public static List<Furniture> getFurnitureListByOrderID(String orderID){
        return createFurnitureList("SELECT f.id FROM furniture f\n" +
                "INNER JOIN sale_order_list sl\n" +
                "ON sl.fk_furniture_id = f.id\n" +
                "WHERE sl.fk_sale_order_id="+ orderID);
    }

    public static List<Material> createMaterialsList(String query){
        List<Material> materialArrayList = new ArrayList<>();
        try {
            ResultSet rs = null;
            rs = query(query);
            while (rs.next()) {
                materialArrayList.add(new Material(
                                rs.getString("id"),
                                rs.getString("name"),
                                rs.getInt("quantity"),
                                rs.getInt("minimum")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materialArrayList;
    }

    public static Material createMaterial(String query){
        Material material = null;

        try {
            ResultSet rs = null;
            rs = query(query);
            rs.next();
            material = new Material(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getInt("minimum")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return material;
    }

    public static List<Material> getMaterialsList() {
        return createMaterialsList("SELECT m.id,m.name,m.quantity,m.minimum FROM material m");
    }

    public static List<Material> getMaterialsListByName(String name) {
        return createMaterialsList("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                "WHERE m.name LIKE '%" + name + "%'");
    }

    public static List<Material> getMaterialsByFurnitureID(String furnitureID){
        return createMaterialsList("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                "INNER JOIN bill_of_material bom\n" +
                "ON bom.fk_material_id = m.id\n"  +
                "WHERE bom.fk_furniture_id=" + furnitureID);
    }

    public static Material getMaterialByID(String id) {
        return createMaterial("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                "WHERE m.id=" + id);
    }

    public static List<Order> getSaleOrdersByStatus(OrderStatus orderStatus) {
        ArrayList<Order> arrayList = new ArrayList<>();
        try {
            ResultSet rs = null;
            rs = query("SELECT so.id,so.fk_customer_id,so.c_name,so.c_address,so.cost_total,so.create_date FROM sale_order so\n" +
                    "WHERE so.furniture_status=" + orderStatus.getStatus());

            while (rs.next()) {
                arrayList.add(
                        new Order(rs.getString("id"),
                                rs.getString("c_name"),
                                rs.getInt("cost_total"),
                                rs.getString("c_address"),
                                rs.getDate("create_date"),
                                orderStatus,
                                getCustomersByID(rs.getString("ID"))
                        ));
            }
        } catch (Exception ignored) {

        }
        return arrayList;
    }

    public static ArrayList<Order> getSaleOrders() {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        try {
            ResultSet rs = null;
            rs = query("SELECT so.id,so.fk_customer_id,so.c_name,so.c_address,so.cost_total,so.create_date,so.furniture_status FROM sale_order so\n");

            while (rs.next()) {
                orderArrayList.add(
                        new Order(rs.getString("id"),
                                rs.getString("c_name"),
                                rs.getInt("cost_total"),
                                rs.getString("c_address"),
                                rs.getDate("create_date"),
                                OrderStatus.findStatus(rs.getInt("furniture_status")),
                                getCustomersByID(rs.getString("ID"))
                        ));
            }
        } catch (Exception ignored) {

        }
        return orderArrayList;
    }

    public static ArrayList<Order> getSaleOrdersByCName(String name) {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        try {
            ResultSet rs = null;
            rs = query("SELECT so.id,so.fk_customer_id,so.c_name,so.c_address,so.cost_total,so.create_date,so.furniture_status FROM sale_order so\n" +
                    "WHERE c_name LIKE '%" + name + "'%");
            while (rs.next()) {
                orderArrayList.add(
                        new Order(rs.getString("id"),
                                rs.getString("c_name"),
                                rs.getInt("cost_total"),
                                rs.getString("c_address"),
                                rs.getDate("create_date"),
                                OrderStatus.findStatus(rs.getInt("furniture_status")),
                                getCustomersByID(rs.getString("ID"))
                        ));
            }
        } catch (Exception ignored) {

        }
        return orderArrayList;
    }

    public static void test() throws SQLException {
        for (Material f : getMaterialsByFurnitureID("1")){
            System.out.println(f);
        }
    }
}

