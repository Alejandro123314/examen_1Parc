package examen_1Parc;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class EstanciasDB {

	private final Connection conn;
	private PreparedStatement stmInsert;

	public EstanciasDB() throws Exception {
		Properties conexion = new Properties();
		conexion.load(new InputStreamReader(new FileInputStream(Principal.getDirectorioBase()+"/recursosExternos/conexion.prop")));		
		String url = conexion.getProperty("protocolo") + Principal.getDirectorioBase() + conexion.getProperty("ficheroSQLite");	
		Class.forName(conexion.getProperty("driver"));

		conn = DriverManager.getConnection(url, conexion.getProperty("user"), conexion.getProperty("password"));
		conn.createStatement().execute("PRAGMA foreign_keys = true;");
		stmInsert = conn.prepareStatement(
				"INSERT INTO Estancias (nombre, fechaInicio, fechaFin, numHabitacion, codHotel) VALUES (?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
	}

	public Estancia insert(Estancia est) throws SQLException {
		Estancia resultado=null;
		ResultSet rs;
		Integer id;
		
		stmInsert.setString(1, est.getNombre());
		stmInsert.setString(2, est.getFechaInicio());
		stmInsert.setString(3, est.getFechaFin());
		stmInsert.setString(4, est.getNumHabitacion());
		stmInsert.setString(5, est.getCodHotel());
		stmInsert.executeUpdate();
		rs = stmInsert.getGeneratedKeys();
		if(rs != null && rs.next()) {
			id = rs.getInt(1);
			est.setId(id);
			resultado = est;
		}
		return resultado;
	}
}
