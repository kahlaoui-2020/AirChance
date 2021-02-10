import java.sql.*;

//import java.util.Random;
import java.util.ArrayList;
public class Avions {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void GestionAvion(Connection Conn) throws SQLException{
		try {
			// 1. Get a connection to database
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
		}catch (Exception exc) {
				exc.printStackTrace();
			}
		System.out.println("---------------------Gestion des Avions-------------------------");
		System.out.println("----------------Veuillez choisir une op�ration--------------");
		System.out.println("1-Ajouter un Avion");
		System.out.println("2-Afficher tous les Avions");
		System.out.println("4-Supprimer un Avion");
		//System.out.println("3-Gestion des R�servations");

		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			AjouterAvion(conn);
			break;
		case 2:
			AfficherAvion(conn);
			break;
		case 3:
			SupprimerAvion(conn);
			break;
		}
	}
	
	public static void AjouterAvion(Connection Conn)throws SQLException{
		System.out.println("Saisissez le Num�ro d'Avion: ");
		int numAvion= Integer.parseInt(LectureClavier.lireChaine());

		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("select modele from ModelesAvion");
		String modele = new String();
		ArrayList<String> l=new ArrayList<String>();
		if(res2.next()) {
			l.add((res2.getString("modele")));
			while(res2.next()) {
				l.add((res2.getString("modele")));
			}


			System.out.println("Veuillez s�lectionner un Mod�le: ");
			for(String c:l) {
				System.out.println("["+c+"]");
			}
			System.out.println();

			modele=LectureClavier.lireChaine();

			while(!l.contains(modele)){
				System.out.println("Veuillez re-s�lectionner un Mod�le: ");
				modele=LectureClavier.lireChaine();

			}
		}else{
			System.out.println("Mod�le Incorrecte !");
		}

		System.out.println("Saisissez le Minimum de Place Classe �conomique: ");
		int nbeco=Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Saisissez le Minimum de Place Premi�re Classe: ");
		int nbpre=Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Saisissez le Minimum de Place Classe Affaire: ");
		int nbAff=Integer.parseInt(LectureClavier.lireChaine());
		
		Statement req = conn.createStatement();
		int insert = req.executeUpdate("INSERT INTO Avions VALUES("+numAvion+",'"+modele+"',"+nbeco+","+nbpre+","+nbAff+")");
		if(insert==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Avion Ajouter avec Succ�s!");
		}
	}
	
	public static void AfficherAvion(Connection Conn)throws SQLException{
		System.out.println("Les Avions Disponibles: ");
		Statement requete =conn.createStatement();
		ResultSet res =requete.executeQuery("SELECT * FROM Avions");
		ResultSetMetaData resultMeta = res.getMetaData();
		System.out.println("|Num�ro Avion|Mod�le|Nb Place �co|Nb Place Premi�re|Nb Place Affaire|");
		while(res.next())
		{      
			for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
				System.out.print("|"+res.getObject(i).toString());
			System.out.println("\n");
		}
	}
	public static void SupprimerAvion(Connection Conn) throws SQLException{
		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("SELECT numAvion FROM Avions");

		String numAvion=new String();
		ArrayList<String> l=new ArrayList<String>();
		if(res2.next()) {
			l.add((res2.getString("numAvion")));
			while(res2.next()) {
				l.add((res2.getString("numAvion")));
			}

			System.out.println("Veuillez s�lectionnez un Avion: ");
			for(String c:l) {
				System.out.println("["+c+"]");
			}

			numAvion=LectureClavier.lireChaine();

			while(!l.contains(numAvion)){
				System.out.println("Veuillez re-s�lectionner un AVion: ");
				numAvion=LectureClavier.lireChaine();
			}
		}else{
			System.out.println("Num�ro d'Avion Incorrecte !");
		}
		Statement requete1= conn.createStatement();
		int delete=requete1.executeUpdate("DELETE FROM Avions WHERE numAvion='"+numAvion+"'");
		if (delete==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Avion Supprimer avec Succ�s!");
		}
	}
}
