package org.furniture.services;

import org.furniture.enums.OrderStatus;
import org.furniture.models.Customer;
import org.furniture.models.Furniture;
import org.furniture.models.Material;
import org.furniture.models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnect {
    private static Connection conn;

    //-------------------------------- Open And Close DBConnect --------------------------------
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

    //-------------------------------- Print Result --------------------------------
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

    //-------------------------------- Normal Query --------------------------------
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

    //-------------------------------- CRUD Query --------------------------------
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

    //-------------------------------- Get Data From Material Table  --------------------------------

    private static Material createMaterial(String query) {
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

    private static List<Material> createMaterialsList(String query) {
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

    public static List<Material> getMaterialsList() {
        return createMaterialsList("SELECT m.id,m.name,m.quantity,m.minimum FROM material m");
    }

    public static HashMap<Material, Integer> getMaterialsByOrderId(String id) {
        HashMap<Material, Integer> materialIntegerHashMap = new HashMap<>();
        try {
            ResultSet rs = query("SELECT m.id m_id,SUM(bom.spend * ol.quantity) sum_spend FROM sale_order_list AS ol\n" +
                    "                           INNER JOIN sale_order AS so\n" +
                    "                               ON so.id = ol.fk_sale_order_id\n" +
                    "                           INNER JOIN furniture AS f\n" +
                    "                               ON f.id = ol.fk_furniture_id\n" +
                    "                           INNER JOIN bill_of_material AS bom\n" +
                    "                               ON bom.fk_furniture_id = f.id\n" +
                    "                           INNER JOIN material AS m\n" +
                    "                               ON m.id = bom.fk_material_id\n" +
                    "                          WHERE so.id = " + id + "\n" +
                    "                          GROUP BY m.id");
            while (rs.next()) {
                materialIntegerHashMap.put(getMaterialByID(rs.getString("m_id")),
                        rs.getInt("sum_spend"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return materialIntegerHashMap;
    }

    public static List<Material> getMaterialsListByName(String name) {
        return createMaterialsList("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                "WHERE m.name LIKE '%" + name + "%'");
    }

    public static List<Material> getMaterialsByFurnitureID(String furnitureID) {
        return createMaterialsList("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                "INNER JOIN bill_of_material bom\n" +
                "ON bom.fk_material_id = m.id\n" +
                "WHERE bom.fk_furniture_id=" + furnitureID);
    }

    public static Material getMaterialByID(String id) {
        return createMaterial("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                "WHERE m.id=" + id);
    }

    public static List<Material> checkMaterialUnderMinimum() {
        return createMaterialsList("SELECT m.id,m.name,m.quantity,m.minimum FROM material m\n" +
                "WHERE m.quantity < m.minimum");
    }

    public static HashMap<Material, Integer> getAmountMaterialNeedAddToStock() {
        HashMap<Material, Integer> materialIntegerHashMap = new HashMap<>();
        try {
            ResultSet rs = null;
            List<String> idMaterialFirstTable = new ArrayList<>();
            rs = query("SELECT m_id,minimum - (quantity - sum_spend) need  FROM (SELECT m.id m_id,SUM(bom.spend * ol.quantity) sum_spend,m.quantity,m.minimum FROM sale_order_list AS ol\n" +
                    "                           INNER JOIN sale_order AS so\n" +
                    "                               ON so.id = ol.fk_sale_order_id\n" +
                    "                           INNER JOIN furniture AS f\n" +
                    "                               ON f.id = ol.fk_furniture_id\n" +
                    "                           INNER JOIN bill_of_material AS bom\n" +
                    "                               ON bom.fk_furniture_id = f.id\n" +
                    "                           INNER JOIN material AS m\n" +
                    "                               ON m.id = bom.fk_material_id\n" +
                    "                          WHERE so.furniture_status = 0\n" +
                    "                          GROUP BY m.id) product_spend_table\n" +
                    "                          WHERE minimum - (quantity - sum_spend) > 0 ");
            while (rs.next()) {
                materialIntegerHashMap.put(getMaterialByID(rs.getString("m_id")),
                        rs.getInt("need"));
                idMaterialFirstTable.add(rs.getString("m_id"));
            }

            rs = query("SELECT m.id,m.minimum - m.quantity need\n" +
                    "FROM material m\n" +
                    "WHERE m.minimum > m.quantity");

            while (rs.next()) {
                if (!idMaterialFirstTable.contains(rs.getString("id")))
                    materialIntegerHashMap.put(getMaterialByID(rs.getString("id")), rs.getInt("need"));
            }
            return materialIntegerHashMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getIDMaterailNeedToAddToStock() {
        List<String> listID = new ArrayList<>();
        try {
            ResultSet rs = null;
            rs = query("SELECT m_id  FROM (SELECT m.id m_id,SUM(bom.spend * ol.quantity) sum_spend,m.quantity,m.minimum FROM sale_order_list AS ol\n" +
                    "                           INNER JOIN sale_order AS so\n" +
                    "                               ON so.id = ol.fk_sale_order_id\n" +
                    "                           INNER JOIN furniture AS f\n" +
                    "                               ON f.id = ol.fk_furniture_id\n" +
                    "                           INNER JOIN bill_of_material AS bom\n" +
                    "                               ON bom.fk_furniture_id = f.id\n" +
                    "                           INNER JOIN material AS m\n" +
                    "                               ON m.id = bom.fk_material_id\n" +
                    "                          WHERE so.furniture_status = 0\n" +
                    "                          GROUP BY m.id) product_spend_table\n" +
                    "                          WHERE minimum - (quantity - sum_spend) > 0 \n" +
                    "                          UNION\n" +
                    "                          SELECT m.id\n" +
                    "                          FROM material m\n" +
                    "                          WHERE m.minimum > m.quantity");
            while (rs.next()) {
                listID.add(rs.getString("m_id"));
            }
            return listID;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getMaterialLastID() {
        try {
            ResultSet rs = query("SELECT max(id) id FROM material;");
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //-------------------------------- Get Data From Customer Table  --------------------------------
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

    public static Customer getCustomerByID(String id) {
        return createCustomer("SELECT c.id,c.name,c.address,c.phone FROM customer c\n" +
                "WHERE c.id=" + id);
    }

    public static int getCustomerLastID() {
        try {
            ResultSet rs = query("SELECT max(id) id FROM customer;");
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //-------------------------------- Get Data From Furniture Table  --------------------------------
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

    private static Furniture createFurniture(String condition) {
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

    public static Furniture getFurnitureByID(String id) {
        return createFurniture("WHERE f.id =" + id);
    }

    public static List<Furniture> getFurnitureList() {
        return createFurnitureList("SELECT f.id FROM furniture f");
    }

    public static List<Furniture> getFurnituresListByName(String name) {
        return createFurnitureList("SELECT f.id FROM furniture f\n" +
                "WHERE f.name LIKE '%" + name + '%');
    }

    public static List<Furniture> getFurnitureListByOrderID(String orderID) {
        return createFurnitureList("SELECT f.id FROM furniture f\n" +
                "INNER JOIN sale_order_list sl\n" +
                "ON sl.fk_furniture_id = f.id\n" +
                "WHERE sl.fk_sale_order_id=" + orderID);
    }

    public static HashMap<Furniture, Integer> getFurnitureAmountByOrderID(String orderID) {
        HashMap<Furniture, Integer> furnitureIntegerHashMap = new HashMap<>();
        ResultSet rs = null;
        rs = query("SELECT f.id,sl.quantity FROM furniture f\n" +
                "INNER JOIN sale_order_list sl\n" +
                "ON sl.fk_furniture_id = f.id\n" +
                "WHERE sl.fk_sale_order_id=" + orderID);
        try {
            while (rs.next()) {
                furnitureIntegerHashMap.put(getFurnitureByID(rs.getString("id")),
                        rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return furnitureIntegerHashMap;
    }

    public static HashMap<Furniture, Integer> getFurnitureTreeMapByTime(int year, int quarter) {
        if (year <= 0 || quarter <= 0 || quarter > 4) return null;

        HashMap<Furniture, Integer> furnitureIntegerTreeMap = new HashMap<Furniture, Integer>();
        ResultSet rs = null;
        try {
            rs = query("SELECT ol.fk_furniture_id f_id,sum(ol.quantity) sum_quantity FROM sale_order so\n" +
                    "INNER JOIN sale_order_list ol\n" +
                    "ON ol.fk_sale_order_id = so.id\n" +
                    "WHERE YEAR(so.create_date) =" + year + "\n" +
                    "AND QUARTER(so.create_date) =" + quarter + "\n" +
                    "AND so.furniture_status=" + OrderStatus.DONE.getStatus() + "\n" +
                    "GROUP BY ol.fk_furniture_id");
            while (rs.next()) {
                furnitureIntegerTreeMap.put(getFurnitureByID(rs.getString("f_id")),
                rs.getInt("sum_quantity"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return furnitureIntegerTreeMap;
    }

    public static int getFurnitureLastID() {
        try {
            ResultSet rs = query("SELECT max(id) id FROM furniture;");
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //-------------------------------- Get Data From Sale Order Table  --------------------------------
    private static List<Order> createSaleOrdersList(String condition) {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        try {
            ResultSet rs = null;
            rs = query(condition);

            while (rs.next()) {
                orderArrayList.add(createOrderByID(rs.getString("id")));
            }
        } catch (Exception ignored) {

        }
        return orderArrayList;
    }

    public static Order createOrderByID(String orderID) {
        Order order = null;
        try {
            ResultSet rs = null;
            rs = query("SELECT so.id,so.fk_customer_id,so.c_name," +
                    "so.c_address,so.cost_total,so.create_date,so.furniture_status," +
                    "fk_furniture_id fk_id,ol.quantity FROM sale_order so\n" +
                    "INNER JOIN sale_order_list ol\n" +
                    "ON ol.fk_sale_order_id = so.id\n" +
                    "WHERE so.id=" + orderID);
            while (rs.next()) {
                if (order == null) {
                    order = new Order(rs.getString("id"),
                            rs.getString("c_name"),
                            rs.getInt("cost_total"),
                            rs.getString("c_address"),
                            rs.getDate("create_date"),
                            OrderStatus.findStatus(rs.getInt("furniture_status")),
                            getCustomerByID(rs.getString("ID")));
                }
                order.addFurniture(getFurnitureByID(rs.getString("fk_id")),
                        rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    public static List<Order> getSaleOrdersByStatus(OrderStatus orderStatus) {
        return createSaleOrdersList("SELECT so.id FROM sale_order so\n" +
                "WHERE so.furniture_status=" + orderStatus.getStatus());
    }

    public static List<Order> getSaleOrdersList() {
        return createSaleOrdersList("SELECT so.id,so.fk_customer_id,so.c_name,so.c_address," +
                "so.cost_total,so.create_date,so.furniture_status FROM sale_order so\n");
    }

    public static List<Order> getSaleOrdersByCName(String name) {
        return createSaleOrdersList("SELECT so.id,so.fk_customer_id,so.c_name,so.c_address,so.cost_total,so.create_date,so.furniture_status FROM sale_order so\n" +
                "WHERE c_name LIKE '%" + name + "'%");
    }

    public static boolean checkOrderCanBeConstruction(String id) {
        try {
            ResultSet rs = query("SELECT SUM(IF(quantity > sum_spend,0,-1)) check_status \n" +
                    "   FROM (SELECT m.id m_id,SUM(bom.spend * ol.quantity) sum_spend,m.quantity FROM sale_order_list AS ol\n" +
                    "                           INNER JOIN sale_order AS so\n" +
                    "                               ON so.id = ol.fk_sale_order_id\n" +
                    "                           INNER JOIN furniture AS f\n" +
                    "                               ON f.id = ol.fk_furniture_id\n" +
                    "                           INNER JOIN bill_of_material AS bom\n" +
                    "                               ON bom.fk_furniture_id = f.id\n" +
                    "                           INNER JOIN material AS m\n" +
                    "                               ON m.id = bom.fk_material_id\n" +
                    "                          WHERE so.id = " + id + "\n" +
                    "                          GROUP BY m.id) statusTable");
            rs.next();
            return rs.getInt("check_status") >= 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeSaleOrderToConstruction(String orderID) {
        if (checkOrderCanBeConstruction(orderID)) {
            try {
                query("UPDATE sale_order\n" +
                        "SET furniture_status=" + OrderStatus.CONSTURCTING + "\n" +
                        "WHERE id=" + orderID);
                ResultSet rs = query("SELECT m.id m_id,SUM(bom.spend * ol.quantity) sum_spend,m.quantity FROM sale_order_list AS ol\n" +
                        "                           INNER JOIN sale_order AS so\n" +
                        "                               ON so.id = ol.fk_sale_order_id\n" +
                        "                           INNER JOIN furniture AS f\n" +
                        "                               ON f.id = ol.fk_furniture_id\n" +
                        "                           INNER JOIN bill_of_material AS bom\n" +
                        "                               ON bom.fk_furniture_id = f.id\n" +
                        "                           INNER JOIN material AS m\n" +
                        "                               ON m.id = bom.fk_material_id\n" +
                        "                          WHERE so.id = " + orderID + "\n" +
                        "                          GROUP BY m.id"
                );

                while (rs.next()) {
                    query("UPDATE material\n" +
                            "SET quantity=" + (rs.getInt("quantity") - rs.getInt("sum_spend")) +
                            "WHERE id=" + rs.getInt("m_id"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static int getOrderLastID() {
        try {
            ResultSet rs = query("SELECT max(id) id FROM sale_order;");
            rs.next();
            return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //-------------------------------- Insert Data To Table --------------------------------
    public static void insertOrder(Order order) {
        try {
            queryUpdate("INSERT INTO sale_order(fk_customer_id,c_name,c_address,cost_total) VALUES\n" +
                    "('" + order.getCustomer().getId() + "','" +
                    order.getName() + "','" +
                    order.getAddress() + "','" +
                    order.getTotalPrice() + "')");

            for (Map.Entry<Furniture, Integer> furnitureIntegerEntry :
                    order.getFurnitures().entrySet()) {
                Furniture furniture = furnitureIntegerEntry.getKey();
                int quantity = furnitureIntegerEntry.getValue();
                queryUpdate("INSERT INTO sale_order_list(fk_sale_order_id,fk_furniture_id,quantity,cost_withholding) VALUES\n" +
                        "('" + order.getId() + "','" +
                        furniture.getId() + "','" +
                        quantity + "','" +
                        furniture.getPrice() + "')");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertFurniture(Furniture furniture) {
        try {
            queryUpdate("INSERT INTO furniture(name,cost) VALUES\n" +
                    "('" + furniture.getName() + "','" +
                    furniture.getPrice() + "')");
            for (Map.Entry<Material, Integer> materialIntegerEntry :
                    furniture.getMaterials().entrySet()) {
                Material material = materialIntegerEntry.getKey();
                int quantity = materialIntegerEntry.getValue();
                queryUpdate("INSERT INTO bill_of_material(fk_furniture_id,fk_material_id,spend) VALUES\n" +
                        "('" + furniture.getId() + "','" +
                        material.getId() + "','" +
                        quantity + "')");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertMaterial(Material material) {
        try {
            queryUpdate("INSERT INTO material(name,quantity,minimum) VALUES\n" +
                    "('" + material.getName() + "','" +
                    material.getQuantity() + "','" +
                    material.getMinimum() + "')");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertCustomer(Customer customer) {
        try {
            queryUpdate("INSERT INTO customer(name,address,phone)VALUES\n" +
                    "('" + customer.getName() + "','" +
                    customer.getAddress() + "','" +
                    customer.getPhone() + "')");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //-------------------------------- Update Data To Table --------------------------------
    public static void updateOrderStatus(Order order) {
        try {
            queryUpdate("UPDATE sale_order\n" +
                    "SET furniture_status='" + order.getStatus().getStatus() + "'\n" +
                    "WHERE id=" + order.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateCustomer(Customer customer) {
        try {
            queryUpdate("UPDATE customer\n" +
                    "SET    name='" + customer.getName() + "'," +
                    "       address='" + customer.getAddress() + "'," +
                    "       phone='" + customer.getPhone() + "'\n" +
                    "WHERE id=" + customer.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateMaterial(Material material) {
        try {
            queryUpdate("UPDATE material\n" +
                    "SET    quantity='" + material.getQuantity() + "'," +
                    "       minimum='" + material.getMinimum() + "'\n" +
                    "WHERE id=" + material.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateFurniture(Furniture furniture) {
        try {
            queryUpdate("UPDATE furniture\n" +
                    "SET    cost='" + furniture.getPrice() + "'\n" +
                    "WHERE id=" + furniture.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getOldestYearQuarter() {
        Statement stmt;
        ResultSet rs;
        String str = "";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT min(sale_order.create_date) FROM sale_order;");
            rs.next();
            str = rs.getString("min(sale_order.create_date)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] strArr = str.substring(0, 7).split("-");

        List<Integer> yQ = new ArrayList<>();
        yQ.add(Integer.parseInt(strArr[0]));
        yQ.add(Integer.parseInt(strArr[1]));

        return yQ;
    }

    public static void test() throws SQLException {
//        HashMap<Material,Integer> materialIntegerHashMap = getAmountMaterialNeedAddToStock();
//        for(Material m : materialIntegerHashMap.keySet()) {
//            System.out.println("M : " + m.toString());
//            System.out.println("Need : " + materialIntegerHashMap.get(m));
//        }
//        System.out.println(materialIntegerHashMap.size());

        System.out.println(getFurnitureTreeMapByTime(2022, 4));
    }
}

