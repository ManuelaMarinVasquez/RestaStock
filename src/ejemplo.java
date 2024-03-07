import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ejemplo {

		private static final String CONTROLADOR = "com.mysql.jdbc.Driver";
		private static final String URL = "jdbc:mysql://umjcgdodsccpmssm:G4ViZSPibiMdxzc4PwnU@blwpqxft8vdtgncehmbh-mysql.services.clever-cloud.com:3306/blwpqxft8vdtgncehmbh";
		private static final String USUARIO = "umjcgdodsccpmssm";
		private static final String CLAVE = "G4ViZSPibiMdxzc4PwnU";
		Connection conexion;
		ResultSet resultado = null;
		Statement stm = null;

		public Connection conectar() {
			conexion = null;
			try {
				conexion = DriverManager.getConnection(URL,USUARIO,CLAVE);
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

}
