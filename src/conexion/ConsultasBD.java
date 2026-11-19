/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

/**
 *
 * @author Danny
 */
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConsultasBD {
    
    Conexion cone = new Conexion();
    Connection con = cone.getConnection();
    
    // Método auxiliar para crear modelos de tabla donde el usuario no pueda editar celdas
    private DefaultTableModel crearModeloNoEditable(String[] columnas) {
        return new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    // ==================== INSERTS ====================
    public boolean insertCliente(String name, String telf, String Dire){
        String sql = "INSERT INTO Clientes (nombre_c, tel_c, dire_c) values (?,?,?)";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setString(1, name);
            star.setString(2, telf);
            star.setString(3, Dire);
            star.executeUpdate();
            return true;          
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error al insertar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean insertProducto(String name, double precio, String FCaducidad){
        String sql = "insert into Producto (Name_Pr, Precio, Fecha_Cad) values (?, ?, ?)";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setString(1, name);
            star.setDouble(2, precio);
            star.setString(3, FCaducidad);
            star.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean insertProveedor (String nombreProv, int idProducto){
        String sql = "INSERT INTO Proveedor (Name_Prov, ID_Pr) values (?, ?)";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setString(1, nombreProv);
            star.setInt(2, idProducto);
            star.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public int insertVenta (double totalVenta, int idProducto, int idCliente) {
        String sql = "INSERT INTO Ventas (Total_ven, ID_Pr, ID_Cl) values (?, ?, ?)";
        try {
            PreparedStatement star = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            star.setDouble(1, totalVenta);
            star.setInt(2, idProducto);
            star.setInt(3, idCliente);

            int filas = star.executeUpdate();
            if (filas > 0) {
                ResultSet rs = star.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // ==================== UPDATES ====================
    public boolean updateCliente(int idCliente, String name, String telf, String Dire) {
        String sql = "UPDATE Clientes SET nombre_c = ?, tel_c = ?, dire_c = ? WHERE ID_Cl = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setString(1, name);
            star.setString(2, telf);
            star.setString(3, Dire);
            star.setInt(4, idCliente);
            star.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateProducto(int idProducto, String name, double precio, String FCaducidad, String estatus) {
        String sql = "UPDATE Producto SET Name_Pr = ?, Precio = ?, Fecha_Cad = ?, est_Pr = ? WHERE ID_Pr = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setString(1, name);
            star.setDouble(2, precio);
            star.setString(3, FCaducidad);
            star.setString(4, estatus);
            star.setInt(5, idProducto);
            star.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateProveedor(int idProveedor, String nombreProv, String estatus, int idProducto) {
        String sql = "UPDATE Proveedor SET Name_Prov = ?, est_Prov = ?, ID_Pr = ? WHERE ID_Prov = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setString(1, nombreProv);
            star.setString(2, estatus);
            star.setInt(3, idProducto);
            star.setInt(4, idProveedor);
            star.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateVenta(int folioVenta, double totalVenta, int idProducto, int idCliente) {
        String sql = "UPDATE Ventas SET Total_ven = ?, ID_Pr = ?, ID_Cl = ? WHERE Folio = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setDouble(1, totalVenta);
            star.setInt(2, idProducto);
            star.setInt(3, idCliente);
            star.setInt(4, folioVenta);
            star.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // ==================== SELECTS ====================
    public DefaultTableModel mostrarClientes(String idBusqueda) {
        String[] nombresColumnas = {"ID Cliente", "Nombre", "Teléfono", "Dirección"};
        DefaultTableModel modelo = crearModeloNoEditable(nombresColumnas);
        String sql = (idBusqueda == null || idBusqueda.trim().isEmpty()) 
                     ? "SELECT * FROM Clientes" 
                     : "SELECT * FROM Clientes WHERE ID_Cl = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            if (idBusqueda != null && !idBusqueda.trim().isEmpty()) {
                star.setInt(1, Integer.parseInt(idBusqueda.trim()));
            }
            ResultSet rs = star.executeQuery();
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ID_Cl"),
                    rs.getString("nombre_c"),
                    rs.getString("tel_c"),
                    rs.getString("dire_c")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    }

    public DefaultTableModel mostrarProductos(String idBusqueda) {
        String[] nombresColumnas = {"ID Prod", "Nombre", "Precio", "Fecha Ingreso", "Fecha Caducidad", "Estatus"};
        DefaultTableModel modelo = crearModeloNoEditable(nombresColumnas);
        String sql = (idBusqueda == null || idBusqueda.trim().isEmpty()) 
                     ? "SELECT * FROM Producto" 
                     : "SELECT * FROM Producto WHERE ID_Pr = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            if (idBusqueda != null && !idBusqueda.trim().isEmpty()) {
                star.setInt(1, Integer.parseInt(idBusqueda.trim()));
            }
            ResultSet rs = star.executeQuery();
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ID_Pr"),
                    rs.getString("Name_Pr"),
                    rs.getDouble("Precio"),
                    rs.getTimestamp("Fecha_Ing"),
                    rs.getString("Fecha_Cad"),
                    rs.getString("est_Pr")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    }

    // NUEVO: Consulta específica para mostrar productos disponibles al abrir el módulo de Ventas
    public DefaultTableModel mostrarProductosParaVenta() {
        String[] nombresColumnas = {"ID Prod", "Producto", "Precio", "Estatus"};
        DefaultTableModel modelo = crearModeloNoEditable(nombresColumnas);
        String sql = "SELECT ID_Pr, Name_Pr, Precio, est_Pr FROM Producto WHERE est_Pr = 'Activo'";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            ResultSet rs = star.executeQuery();
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ID_Pr"),
                    rs.getString("Name_Pr"),
                    rs.getDouble("Precio"),
                    rs.getString("est_Pr")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar catálogo de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    }

    public DefaultTableModel mostrarProveedores(String idBusqueda) {
        String[] nombresColumnas = {"ID Prov", "Nombre Proveedor", "Estatus", "ID Producto"};
        DefaultTableModel modelo = crearModeloNoEditable(nombresColumnas);
        String sql = (idBusqueda == null || idBusqueda.trim().isEmpty()) 
                     ? "SELECT * FROM Proveedor" 
                     : "SELECT * FROM Proveedor WHERE ID_Prov = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            if (idBusqueda != null && !idBusqueda.trim().isEmpty()) {
                star.setInt(1, Integer.parseInt(idBusqueda.trim()));
            }
            ResultSet rs = star.executeQuery();
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ID_Prov"),
                    rs.getString("Name_Prov"),
                    rs.getString("est_Prov"),
                    rs.getInt("ID_Pr")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    }

    public DefaultTableModel mostrarVentas(String idBusqueda) {
        String[] nombresColumnas = {"Folio", "Fecha Venta", "Total", "ID Prod", "ID Cliente"};
        DefaultTableModel modelo = crearModeloNoEditable(nombresColumnas);
        String sql = (idBusqueda == null || idBusqueda.trim().isEmpty()) 
                     ? "SELECT * FROM Ventas" 
                     : "SELECT * FROM Ventas WHERE Folio = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            if (idBusqueda != null && !idBusqueda.trim().isEmpty()) {
                star.setInt(1, Integer.parseInt(idBusqueda.trim()));
            }
            ResultSet rs = star.executeQuery();
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("Folio"),
                    rs.getTimestamp("fecha_ven"),
                    rs.getDouble("Total_ven"),
                    rs.getInt("ID_Pr"),
                    rs.getInt("ID_Cl")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar ventas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    } 

    // ==================== DELETES ====================
    public boolean deleteCliente(int idCliente) {
        String sql = "DELETE FROM Clientes WHERE ID_Cl = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setInt(1, idCliente);
            return star.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean deleteProducto(int idProducto) {
        String sql = "DELETE FROM Producto WHERE ID_Pr = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setInt(1, idProducto);
            return star.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto (Verifica que no esté en una Venta o Proveedor): " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean deleteProveedor(int idProveedor) {
        String sql = "DELETE FROM Proveedor WHERE ID_Prov = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setInt(1, idProveedor);
            return star.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean deleteVenta(int folioVenta) {
        String sql = "DELETE FROM Ventas WHERE Folio = ?";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            star.setInt(1, folioVenta);
            return star.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public javax.swing.DefaultComboBoxModel<String> obtenerComboProductos() {
        javax.swing.DefaultComboBoxModel<String> modelo = new javax.swing.DefaultComboBoxModel<>();
        String sql = "SELECT ID_Pr, Name_Pr FROM Producto";
        try {
            PreparedStatement star = con.prepareStatement(sql);
            ResultSet rs = star.executeQuery();
            while (rs.next()) {
                // Formato: "1 - Sabritas" para poder extraer el ID fácil
                modelo.addElement(rs.getInt("ID_Pr") + " - " + rs.getString("Name_Pr"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos en combo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    }
}
