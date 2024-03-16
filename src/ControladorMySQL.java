import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.DatabaseMetaData;
import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class ControladorMySQL {
	private static final String CONTROLADOR = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://umjcgdodsccpmssm:G4ViZSPibiMdxzc4PwnU@blwpqxft8vdtgncehmbh-mysql.services.clever-cloud.com:3306/blwpqxft8vdtgncehmbh";
	private static final String USUARIO = "umjcgdodsccpmssm";
	private static final String CLAVE = "G4ViZSPibiMdxzc4PwnU";
	Connection conexion;
	ResultSet resultado = null;
	Statement stm = null;
	
	
	public Connection conectar() {// ACA ESTOY GENERANDO LA CONEXION CON LA BASE DE DATOS, METODO CONECTAR
		conexion = null;
		try {
			conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
			System.out.println("Conexion a base de datos exitosa.");
		} catch (SQLException e) {
			System.out.println("Error en la conexion a base de datos.");
			e.printStackTrace();
		}
		return conexion;
	}
	
	
	
	// Cierro conexion
	public Connection cerrarConexion() {
		if (conexion != null) {
			try {
				conexion.close();
				System.out.println("Conexión a la base de datos cerrada correctamente.");
			} catch (SQLException e) {
				System.out.println("Error al cerrar la conexión a la base de datos.");
				e.printStackTrace();
			}
		}
		return conexion;
	}
	
	
	
	// metodo para insertar a todas las tablas. Paso por parametro la tabla y el
		// arraylist de valores que voy a insertar.
		public void Insertar(String tabla, ArrayList<String> valores) {
			Scanner entrada = new Scanner(System.in);
			try {
				conectar();
				// Obtiene el metadata de la base de datos
				DatabaseMetaData metaData = (DatabaseMetaData) conexion.getMetaData();

				ResultSet resultSet = metaData.getColumns(null, null, tabla, null);

				// Obtiene el metadata del ResultSet
				ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();

				int numeroColumnas = 0;
				String valor;
				// arraylist de valores a ingresar.
				// valores = new ArrayList<>();
				ArrayList<String> columnas = new ArrayList<>();

				// while que recorre y obtiene nombre de columna y tipo de datos para imprimirlo
				// al usuario
				while (resultSet.next()) {
					numeroColumnas++;
					String nombreColumna = resultSet.getString("COLUMN_NAME");
					columnas.add(nombreColumna);
					String tipoDato = resultSet.getString("TYPE_NAME");
					System.out.println("Nombre de la columna: " + nombreColumna);
					System.out.println("Tipo de dato: " + tipoDato);
					System.out.println("------------------------");
				}
				System.out.println("Número de columnas en la tabla: " + tabla + " es: " + numeroColumnas);// muestra el
																											// numero de
																											// columnas que
																											// tiene

				System.out.println(columnas);

				System.out.println(valores);

				// consulta que me inserta en la tabla
				String consultaSQL = "INSERT INTO " + tabla
						+ " ("; /*
								 * Construyo la consulta, verifico si la columna actual no es la última en la
								 * lista. Si no es la última, se agrega una coma y un espacio después del nombre
								 * de la columna para separarla de la siguiente columna, y si es la ultima,
								 * cierro la consulta.
								 */

				for (int i = 0; i < columnas.size(); i++) {
					consultaSQL += columnas.get(i);
					if (i < columnas.size() - 1) {
						consultaSQL += ", ";
					} else {
						consultaSQL += ") VALUES (";
					}
				}
				for (int i = 0; i < valores.size(); i++) {
					consultaSQL += "?";
					if (i < valores.size() - 1) {
						consultaSQL += ", ";
					} else {
						consultaSQL += ")";
					}
				}

				try {
					PreparedStatement preparedStatement = (PreparedStatement) conexion.prepareStatement(consultaSQL);

					// Establecer los valores en la consulta
					for (int i = 0; i < valores.size(); i++) {
						preparedStatement.setObject(i + 1, valores.get(i));
					}

					// Ejecutar la inserción
					int filasAfectadas = preparedStatement.executeUpdate();
					if (filasAfectadas > 0) {
						System.out.println("Inserción exitosa");
					} else {
						System.out.println("No se insertaron filas");
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {
				System.out.println("error aqui ");
				e.printStackTrace();
			}

		}
		
		
		
		//para modificar cuando la tabla la estoy mostrando SIN el ID
//paso por parametros el nombre dela tabla, un arraylist con los datos nuevos y el ID del cual quiero modificar.
		public void modificar(String tabla, ArrayList<String> valoresNuevos, String ID) {

			try {// Obtiene el metadata de la base de datos
				conectar();
				DatabaseMetaData metaData = (DatabaseMetaData) conexion.getMetaData();

				ResultSet resultSet = metaData.getColumns(null, null, tabla, null); // obtengo los datos de la tabla que
																					// ingrese

				// Obtiene el metadata del ResultSet
				ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();

				int numeroColumnas = 0; // variable para saber cuantas columnas tiene esa tabla.
				String valor;
				// arraylist de valores a ingresar y de las columnas de la tabla que ingrese.

				ArrayList<String> columnas = new ArrayList<>();

				while (resultSet.next()) { // obtengo el nombre de la columna(este lo meto en mi arraylist de columnas) el
											// tipo de dato.

					String nombreColumna = resultSet.getString("COLUMN_NAME");
					columnas.add(nombreColumna);
					String tipoDato = resultSet.getString("TYPE_NAME");
					System.out.println("Nombre de la columna: " + nombreColumna);
					System.out.println("Tipo de dato: " + tipoDato);
					System.out.println("------------------------");
					numeroColumnas++;
				}

				// Consulta SQL de actualización
				String consultaSQL = "UPDATE " + tabla + " SET ";
				for (int i = 1; i < columnas.size(); i++) {
					consultaSQL += columnas.get(i) + " = ?";
					if (i < columnas.size() - 1) {
						consultaSQL += ", ";
					} else {
						consultaSQL += " WHERE " + columnas.get(0) + " = '" + ID + "';"; // donde el id de
																							// la columna
																							// sea el que
																							// ingreso.
					}
					System.out.println("Consulta UPDATE: " + consultaSQL);
					System.out.println("numero de columnas: " + numeroColumnas);
					System.out.println("numero de columnas size" + columnas.size());
				}

	//System.out.println("Consulta UPDATE: "+consultaSQL);
				PreparedStatement preparedStatement = (PreparedStatement) conexion.prepareStatement(consultaSQL);
				// Establezco los nuevos valores en la consulta
				for (int i = 1; i <= valoresNuevos.size(); i++) {
					System.out.println("Esto es valoresnuevos. get en posicion: " + valoresNuevos.get(i - 1));
					preparedStatement.setObject(i, valoresNuevos.get(i - 1));
				}
				// Ejecutar la actualización
				System.out.println(consultaSQL);
				if (!ID.equals(""))
					System.out.println("La id es: " + ID);
				else
					System.out.println("No hay ID");
				int filasAfectadas = preparedStatement.executeUpdate();
				if (filasAfectadas > 0) {
					System.out.println("Actualización exitosa");
				} else {
					System.out.println("No se actualizaron filas");
				}
			} catch (SQLException e) {
				System.out.println("No se modificó nada.");
				e.printStackTrace();
			}

		}
		
		
	
		
		
		//para modificar cuando la tabla la estoy mostrando con el ID
		public void modificarConID(String tabla, ArrayList<String> valoresNuevos, String ID) {

			try {// Obtiene el metadata de la base de datos
				conectar();
				DatabaseMetaData metaData = (DatabaseMetaData) conexion.getMetaData();

				ResultSet resultSet = metaData.getColumns(null, null, tabla, null); // obtengo los datos de la tabla que
																					// ingrese

				// Obtiene el metadata del ResultSet
				ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();

				int numeroColumnas = 0; // variable para saber cuantas columnas tiene esa tabla.
				String valor;
				// arraylist de valores a ingresar y de las columnas de la tabla que ingrese.

				ArrayList<String> columnas = new ArrayList<>();

				while (resultSet.next()) { // obtengo el nombre de la columna(este lo meto en mi arraylist de columnas) el
											// tipo de dato.

					String nombreColumna = resultSet.getString("COLUMN_NAME");
					columnas.add(nombreColumna);
					String tipoDato = resultSet.getString("TYPE_NAME");
					System.out.println("Nombre de la columna: " + nombreColumna);
					System.out.println("Tipo de dato: " + tipoDato);
					System.out.println("------------------------");
					numeroColumnas++;
				}

				// Consulta SQL de actualización
				String consultaSQL = "UPDATE " + tabla + " SET ";
				for (int i = 0; i < columnas.size(); i++) {
					consultaSQL += columnas.get(i) + " = ?";
					if (i < columnas.size() - 1) {
						consultaSQL += ", ";
					} else {
						consultaSQL += " WHERE " + columnas.get(0) + " = '" + ID + "';"; // donde el id de
																							// la columna
																							// sea el que
																							// ingreso.
					}
					System.out.println("Consulta UPDATE: " + consultaSQL);
					System.out.println("numero de columnas: " + numeroColumnas);
					System.out.println("numero de columnas size" + columnas.size());
				}

				System.out.println("hola si sali        " + valoresNuevos);
	//System.out.println("Consulta UPDATE: "+consultaSQL);
				PreparedStatement preparedStatement = (PreparedStatement) conexion.prepareStatement(consultaSQL);
				// Establezco los nuevos valores en la consulta
				for (int i = 1; i <= valoresNuevos.size(); i++) {
					System.out.println("Esto es valoresnuevos. get en posicion: " + valoresNuevos.get(i - 1));
					preparedStatement.setObject(i, valoresNuevos.get(i - 1));
				}
				// Ejecutar la actualización
				System.out.println(consultaSQL);
				if (!ID.equals(""))
					System.out.println("La id es: " + ID);
				else
					System.out.println("No hay ID");
				int filasAfectadas = preparedStatement.executeUpdate();
				if (filasAfectadas > 0) {
					System.out.println("Actualización exitosa");
				} else {
					System.out.println("No se actualizaron filas");
				}
			} catch (SQLException e) {
				System.out.println("No se modificó nada.");
				e.printStackTrace();
			}

		}
		
		
		
		public void borrar(String tabla, int idAEliminar) throws SQLException {
			ControladorMySQL controlador = new ControladorMySQL();
			Connection cn = null;
			Statement stm = null;
			cn = controlador.conectar();
			stm = cn.createStatement();
			try { // Obtiene el metadata de la base de datos
				DatabaseMetaData metaData = (DatabaseMetaData) cn.getMetaData();

				ResultSet resultSet = metaData.getColumns(null, null, tabla, null); // obtengo los datos de la tabla.

				// Obtiene el metadata del ResultSet
				ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();

				int numeroColumnas = 0;
				String valor; // arraylist de valores a ingresar.
				ArrayList<String> columnas = new ArrayList<>();

				while (resultSet.next()) {
					numeroColumnas++;
					String nombreColumna = resultSet.getString("COLUMN_NAME");
					columnas.add(nombreColumna); // meto en el arraylist los nombres de las columnas.
					String tipoDato = resultSet.getString("TYPE_NAME");
					System.out.println("Nombre de la columna: " + nombreColumna);
					System.out.println("Tipo de dato: " + tipoDato);
					System.out.println("------------------------");
				}

				System.out.println("Inserte el ID de la fila a eliminar.");
				// Consulta SQL de eliminación
				String consultaSQL = "DELETE FROM " + tabla + " WHERE " + columnas.get(0) + " = ?";
				// columnas.get(0) será la primera columna de la tabla que elija, que será el ID

				PreparedStatement preparedStatement = (PreparedStatement) (cn.prepareStatement(consultaSQL));

				// Establecer el ID a eliminar en la consulta
				preparedStatement.setInt(1, idAEliminar); // aquí añado a la consulta el numero de ID de la fila que quiero
															// eliminar.

				// Ejecutar el borrado
				int filasAfectadas = preparedStatement.executeUpdate();
				if (filasAfectadas > 0) {
					System.out.println("Borrado exitoso");
				} else {
					System.out.println("No se eliminaron filas");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		
		
		
		
		
		public void cargartablitaConID(DefaultTableModel tableModel, JTable jTable, String tabla) throws SQLException {
			Connection cn = null;
			Statement stm = null;

			ControladorMySQL controlador = new ControladorMySQL();
			cn = controlador.conectar();
			stm = cn.createStatement();

			ArrayList<String> info;
			ArrayList<String> columnas;
			int numeroColumnas = 0;
			Object[] rowData = new Object[numeroColumnas];

			try {
				// Obtiene el metadata de la base de datos
				DatabaseMetaData metaData = (DatabaseMetaData) cn.getMetaData();
				ResultSet resultSet = metaData.getColumns(null, null, tabla, null);
				// Obtiene el metadata del ResultSet
				ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
				// arraylist de valores a ingresar y de las columnas de la tabla que ingrese.
				columnas = new ArrayList<>();
				while (resultSet.next()) { // obtengo el nombre de la columna(este lo meto en mi arraylist de columnas) el
											// tipo de dato.
					numeroColumnas++;
					String nombreColumna = resultSet.getString("COLUMN_NAME");
					columnas.add(nombreColumna);

					String tipoDato = resultSet.getString("TYPE_NAME");
					System.out.println("Nombre de la columna: " + nombreColumna);
					// System.out.println("Tipo de dato: " + tipoDato);
					// System.out.println("------------------------");
				}

				// establezco el tamaño del array de object que necesito para el tablemodel

				// le mando el array al tableModel.

				info = new ArrayList<String>((numeroColumnas - 1));
				ArrayList<String> fila = new ArrayList<>();

				String consulta = "Select * from " + tabla + ";";
				ResultSet rs = stm.executeQuery(consulta);

				while (rs.next()) {
					rowData = new Object[columnas.size()];

					for (int i = 1; i <= columnas.size(); i++) {
						rowData[i - 1] = rs.getString(i);
					}

					// Agrega la fila al modelo de la tabla
					tableModel.addRow(rowData);
				}

				// lo recorro y le voy insertando mi arraylist de columnas, que son los nombres
				// de las columnas de la tabla
				/*
				 * for (int i = 0; i < rowData.length; i++) { rowData[i] = info.get(i); }
				 * tableModel.addRow(rowData);
				 */
			} catch (Exception h) {
				h.printStackTrace();
			}
		}
		// metodo para cargar tabla sin id

		public void cargartablitaSinID(DefaultTableModel tableModel, JTable jTable, String tabla) throws SQLException {
			Connection cn = null;
			Statement stm = null;

			ControladorMySQL controlador = new ControladorMySQL();
			cn = controlador.conectar();
			stm = cn.createStatement();

			ArrayList<String> info;
			ArrayList<String> columnas;
			int numeroColumnas = 0;
			Object[] rowData = new Object[numeroColumnas];

			try {
				// Obtiene el metadata de la base de datos
				DatabaseMetaData metaData = (DatabaseMetaData) cn.getMetaData();
				ResultSet resultSet = metaData.getColumns(null, null, tabla, null);
				// Obtiene el metadata del ResultSet
				ResultSetMetaData resultSetMetaData = (ResultSetMetaData) resultSet.getMetaData();
				// arraylist de valores a ingresar y de las columnas de la tabla que ingrese.
				columnas = new ArrayList<>();
				while (resultSet.next()) { // obtengo el nombre de la columna(este lo meto en mi arraylist de columnas) el
											// tipo de dato.
					numeroColumnas++;
					String nombreColumna = resultSet.getString("COLUMN_NAME");
					columnas.add(nombreColumna);

					String tipoDato = resultSet.getString("TYPE_NAME");
					System.out.println("Nombre de la columna: " + nombreColumna);
					// System.out.println("Tipo de dato: " + tipoDato);
					// System.out.println("------------------------");
				}

				// establezco el tamaño del array de object que necesito para el tablemodel

				// le mando el array al tableModel.

				info = new ArrayList<String>((numeroColumnas - 1));
				ArrayList<String> fila = new ArrayList<>();

				String consulta = "Select * from " + tabla + ";";
				ResultSet rs = stm.executeQuery(consulta);

				while (rs.next()) {
					rowData = new Object[columnas.size() - 1];

					for (int i = 1; i <= columnas.size() - 1; i++) {
						rowData[i - 1] = rs.getString(i + 1);
					}

					// Agrega la fila al modelo de la tabla
					tableModel.addRow(rowData);
				}

				// lo recorro y le voy insertando mi arraylist de columnas, que son los nombres
				// de las columnas de la tabla
				/*
				 * for (int i = 0; i < rowData.length; i++) { rowData[i] = info.get(i); }
				 * tableModel.addRow(rowData);
				 */
			} catch (Exception h) {
				h.printStackTrace();
			}
		}
		
		
		
}
