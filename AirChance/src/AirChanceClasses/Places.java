import java.sql.*;
import java.util.ArrayList;

public class Places {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void GestionPlaces(Connection Conn) throws SQLException{
		try {
			// 1. Get a connection to database
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
		}catch (Exception exc) {
				exc.printStackTrace();
			}
		System.out.println("---------------------Gestion des Places-------------------------");
		System.out.println("----------------Veuillez choisir une opération--------------");
		System.out.println("1-Ajouter une Place");
		System.out.println("2-Supprimer une Place");

		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			AjoutPlace(conn);
			break;
		case 2:
			//GestionHotesses(conn);
			break;
		}
	}
	public static void AjoutPlace(Connection Conn)throws SQLException{
		System.out.println("Entrez un Numéro de Place:  ");
		String nump=LectureClavier.lireChaine();
		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("select numAvion from Avions");
		int numA = 0;
		ArrayList<Integer> l=new ArrayList<Integer>();
		if(res2.next()) {
			l.add(Integer.parseInt(res2.getString("numAvion")));
			while(res2.next()) {
				l.add(Integer.parseInt(res2.getString("numAvion")));
			}


			System.out.println("Veuillez sélectionner un Avion: ");
			for(int c:l) {
				System.out.println("["+c+"]");
			}
			System.out.println();

			numA=Integer.parseInt(LectureClavier.lireChaine());

			while(!l.contains(numA)){
				System.out.println("Veuillez re-sélectionner un Modèle: ");
				numA=Integer.parseInt(LectureClavier.lireChaine());

			}
		}else{
			System.out.println("Numéro d'Avion Incorrecte !");
		}
		System.out.println("Position(Hublot,Couloir,Centre: ");
		String pos=LectureClavier.lireChaine();
		
		System.out.println("Classe(Eco,Premiere,Affaire: ");
		String clas=LectureClavier.lireChaine();
		
		System.out.println("Prix de la Place");
		int prix=Integer.parseInt(LectureClavier.lireChaine());
		
		Statement req1 = conn.createStatement();
		int insert = req1.executeUpdate("INSERT INTO Places VALUES('"+nump+"',"+numA+",'"+pos+"','"+clas+"',"+prix+")");
		if(insert==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Place Ajouter avec Succès!");
		}

	}
}
