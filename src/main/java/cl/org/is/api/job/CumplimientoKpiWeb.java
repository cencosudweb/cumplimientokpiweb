/**
 *@name Consulta.java
 * 
 *@version 1.0 
 * 
 *@date 30-03-2017
 * 
 *@author EA7129
 * 
 *@copyright Cencosud. All rights reserved.
 */
package cl.org.is.api.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @description 
 */
public class CumplimientoKpiWeb {
	
	private static final int DIFF_HOY_FECHA_INI = 1;
	private static final int DIFF_HOY_FECHA_FIN = 1;
	//private static final int FORMATO_FECHA_0 = 0;
	//private static final int FORMATO_FECHA_1 = 1;
	//private static final int FORMATO_FECHA_3 = 3;
	private static final String RUTA_ENVIO = "C:/Share/Inbound/CumplimientoKpiWeb";

	private static BufferedWriter bw;
	private static String path;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map <String, String> mapArguments = new HashMap<String, String>();
		String sKeyAux = null;

		for (int i = 0; i < args.length; i++) {

			if (i % 2 == 0) {

				sKeyAux = args[i];
			}
			else {

				mapArguments.put(sKeyAux, args[i]);
			}
		}

		try {
			
			

			File info              = null;
			File miDir             = new File(".");
			path                   =  miDir.getCanonicalPath();
			info                   = new File(path+"/info.txt");
			bw = new BufferedWriter(new FileWriter(info));
			info("El programa se esta ejecutando...");
			crearTxt(mapArguments);
			System.out.println("El programa finalizo.");
			info("El programa finalizo.");
			bw.close();
		}
		catch (Exception e) {

			System.out.println(e.getMessage());
		}
	}
	
	private static void crearTxt(Map<String, String> mapArguments) {
		// TODO Auto-generated method stub
		Connection dbconnOracle = crearConexionOracle();
		File file1              = null;
		BufferedWriter bw       = null;
		PreparedStatement pstmt = null;
		StringBuffer sb         = null;
		String sFechaIni        = null;
		String sFechaFin        = null;
		
		
		Date now2 = new Date();
		SimpleDateFormat ft2 = new SimpleDateFormat ("dd/MM/YY hh:mm:ss");
		String currentDate2 = ft2.format(now2);
		info("Inicio Programa: " + currentDate2 + "\n");
		
		
		

		try {

			try {

				sFechaIni = restarDias(mapArguments.get("-f"), DIFF_HOY_FECHA_INI);
				sFechaFin = restarDias(mapArguments.get("-f"), DIFF_HOY_FECHA_FIN);
				//sFechaIni = "29-03-2017";
				//sFechaFin = "29-03-2017";
				info("sFechaIni: " + sFechaIni + "\n");
				info("sFechaFin: " + sFechaFin + "\n");
			}
			catch (Exception e) {

				info("error: " + e);
			}
			//file1                   = new File(path + "/" + sFechaIni + "_" + sFechaFin + ".txt");
			file1                   = new File(RUTA_ENVIO + "/" + sFechaIni + "_" + sFechaFin + ".txt");
			sb = new StringBuffer();
			
			
			sb.append("SELECT ID_CUMPLE,ID_CUMPLE_RESUMEN,ID_CUMPLIMIENTO_2,ID_RESPONSABLE,ID_TIPO_ORDEN,ID_TIPO_VENTA,ID_CLIENTE_RETIRA,ID_DESC_EMP,ID_TIPOGUI,ID_HORARIO,ID_VENTA_EMPRESA,FECHA_COMPROMISO_EOM,TIPO_CUMPLIMIENTO ");
			sb.append("FROM cumplimiento_kpiweb ");
			sb.append("WHERE 1 = 1 ");
			sb.append("AND fecha_compromiso_eom >= '");
			sb.append(sFechaIni);
			sb.append("' ");
			sb.append("AND fecha_compromiso_eom <= '");
			sb.append(sFechaFin);
			sb.append("' ");
			sb.append(" ");
			
			
			info("Query : " + sb + "\n");
			
			pstmt = dbconnOracle.prepareStatement(sb.toString());
			ResultSet rs = pstmt.executeQuery();
			bw = new BufferedWriter(new FileWriter(file1));
			//bw.write("ID;");
			bw.write("ID_CUMPLE;");
			bw.write("ID_CUMPLE_RESUMEN;");
			bw.write("ID_CUMPLIMIENTO_2;");
			bw.write("ID_RESPONSABLE;");
			bw.write("ID_TIPO_ORDEN;");
			bw.write("ID_TIPO_VENTA;");
			bw.write("ID_CLIENTE_RETIRA;");
			bw.write("ID_DESC_EMP;");
			bw.write("ID_TIPOGUI;");
			bw.write("ID_HORARIO;");
			bw.write("ID_VENTA_EMPRESA;");
			bw.write("FECHA_COMPROMISO_EOM;");
			bw.write("TIPO_CUMPLIMIENTO\n");
			//bw.write("VENTA_EMPRESA\n");
			sb = new StringBuffer();
			
			while (rs.next()){

				//bw.write(rs.getString("ID") + ";");
				bw.write(rs.getString("ID_CUMPLE") + ";");
				bw.write(rs.getString("ID_CUMPLE_RESUMEN")  + ";");
				bw.write(rs.getString("ID_CUMPLIMIENTO_2")  + ";");
				bw.write(rs.getString("ID_RESPONSABLE")  + ";");
				bw.write(rs.getString("ID_TIPO_ORDEN")  + ";");
				bw.write(rs.getString("ID_TIPO_VENTA")  + ";");
				bw.write(rs.getString("ID_CLIENTE_RETIRA")  + ";");
				bw.write(rs.getString("ID_DESC_EMP")  + ";");
				bw.write(rs.getString("ID_TIPOGUI")  + ";");
				bw.write(rs.getString("ID_HORARIO")  + ";");
				bw.write(rs.getString("ID_VENTA_EMPRESA")  + ";");
				bw.write(rs.getString("FECHA_COMPROMISO_EOM")  + ";");
				bw.write(rs.getString("TIPO_CUMPLIMIENTO") + "\n");
				//bw.write(rs.getString("venta_empresa") + "\n");
			}
			bw.write(sb.toString());
			info("Archivos creados." + "\n");
		}
		catch (Exception e) {

			info("[crearTxt1]Exception:"+e.getMessage());
		}
		finally {

			cerrarTodo(dbconnOracle, pstmt, bw);
		}
		info("Fin Programa: " + currentDate2 + "\n");
	}

	/**
	 * Metodo de conexion para MEOMCLP 
	 * 
	 * @return void,  no tiene valor de retorno
	 */
	private static Connection crearConexionOracle() {

		Connection dbconnection = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//Shareplex
			//dbconnection = DriverManager.getConnection("jdbc:oracle:thin:@g500603svcr9.cencosud.corp:1521:MEOMCLP","REPORTER","RptCyber2015");
			//El servidor g500603sv0zt corresponde a ProducciÃ³n. por el momento
			//dbconnection = DriverManager.getConnection("jdbc:oracle:thin:@g500603sv0zt.cencosud.corp:1521:MEOMCLP","ca14","Manhattan1234");
			dbconnection = DriverManager.getConnection("jdbc:oracle:thin:@172.18.163.15:1521/XE", "kpiweb", "kpiweb");
		}
		catch (Exception e) {

			info("[crearConexionOracle]error: " + e);
		}
		return dbconnection;
	}



	/**
	 * Metodo que cierra la conexion, Procedimintos,  BufferedWriter
	 * 
	 * @param Connection,  Objeto que representa una conexion a la base de datos
	 * @param PreparedStatement, Objeto que representa una instrucción SQL precompilada. 
	 * @return retorna
	 * 
	 */
	private static void cerrarTodo(Connection cnn, PreparedStatement pstmt, BufferedWriter bw){

		try {

			if (cnn != null) {

				cnn.close();
				cnn = null;
			}
		}
		catch (Exception e) {

			info("[cerrarTodo]Exception:"+e.getMessage());
		}
		try {

			if (pstmt != null) {

				pstmt.close();
				pstmt = null;
			}
		}
		catch (Exception e) {

			info("[cerrarTodo]Exception:"+e.getMessage());
		}
		try {

			if (bw != null) {

				bw.flush();
				bw.close();
				bw = null;
			}
		}
		catch (Exception e) {

			info("[cerrarTodo]Exception:"+e.getMessage());
		}
	}


	/**
	 * Metodo que muestra informacion 
	 * 
	 * @param String, texto a mostra
	 * @param String, cantidad para restar dias
	 * @return String retorna los dias a restar
	 * 
	 */
	private static void info(String texto){

		try {

			bw.write(texto+"\n");
			bw.flush();
		}
		catch (Exception e) {

			System.out.println("Exception:" + e.getMessage());
		}
	}


	/**
	 * Metodo que resta dias 
	 * 
	 * @param String, dia que se resta
	 * @param String, cantidad para restar dias
	 * @return String retorna los dias a restar
	 * 
	 */
	private static String restarDias(String sDia, int iCantDias) {

		String sFormatoIn = "yyyyMMdd";
		String sFormatoOut = "yyyy-MM-dd";
		Calendar diaAux = null;
		String sDiaAux = null;
		SimpleDateFormat df = null;

		try {

			diaAux = Calendar.getInstance();
			df = new SimpleDateFormat(sFormatoIn);
			diaAux.setTime(df.parse(sDia));
			diaAux.add(Calendar.DAY_OF_MONTH, -iCantDias);
			df.applyPattern(sFormatoOut);
			sDiaAux = df.format(diaAux.getTime());
		}
		catch (Exception e) {

			info("[restarDias]error: " + e);
		}
		return sDiaAux;
	}
	
	/**
	 * Metodo que formatea una fecha 
	 * 
	 * @param String, fecha a formatear
	 * @param String, formato de fecha
	 * @return String retorna el formato de fecha a un String
	 * 
	 */
	/*
	private static String formatDate(Date fecha, int iOptFormat) {

		String sFormatedDate = null;
		String sFormat = null;

		try {

			SimpleDateFormat df = null;

			switch (iOptFormat) {

			case 0:
				sFormat = "dd/MM/yy HH:mm:ss,SSS";
				break;
			case 1:
				sFormat = "dd/MM/yy";
				break;
			case 2:
				sFormat = "dd/MM/yy HH:mm:ss";
				break;
			case 3:
				sFormat = "yyyy-MM-dd HH:mm:ss,SSS";
				break;
			}
			df = new SimpleDateFormat(sFormat);
			sFormatedDate = df.format(fecha != null ? fecha:new Date(0));

			if (iOptFormat == 0 && sFormatedDate != null) {

				sFormatedDate = sFormatedDate + "000000";
			}
		}
		catch (Exception e) {

			info("[formatDate]Exception:"+e.getMessage());
		}
		return sFormatedDate;
	}
	*/

}
