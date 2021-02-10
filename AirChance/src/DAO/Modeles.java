import java.sql.*;
//import java.io.*;
//import oracle.jdbc.*;
import java.util.ArrayList;
public class Modeles {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void GestionModeles(Connection Conn) throws SQLException{
		try {
			// 1. Get a connection to database
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
		}catch (Exception exc) {
				exc.printStackTrace();
			}
	System.out.println("---------------------Gestion des Mod�les-------------------------");
	System.out.println("----------------Veuillez choisir une op�ration--------------");
	System.out.println("1-Ajouter un Mod�le");
	System.out.println("2-Afficher tous Les Mod�les");
	System.out.println("3-Supprimer un Mod�le");
	//System.out.println("3-Gestion des R�servations");

	int n = Integer.parseInt(LectureClavier.lireChaine());

	switch(n) {
	case 1:
		AjouterModele(conn);
		break;
	case 2:
		AfficherModeles(conn);
		break;
	case 3:
		SupprimerModele(conn);
		break;
	}
	}
		public static void AjouterModele(Connection Conn) throws SQLException{
			System.out.println("Le Mod�le: ");
			String mod = LectureClavier.lireChaine();

			System.out.println("Nombre d'�quipage: ");
			int nbEquipe = Integer.parseInt(LectureClavier.lireChaine());

			System.out.println("Rayon d'Action: ");
			float rayAct = Integer.parseInt(LectureClavier.lireChaine());

			Statement requete= conn.createStatement();

			int insert=requete.executeUpdate("INSERT INTO ModelesAvion Values('"+mod+"',"+nbEquipe+","+rayAct+")");
			if(insert==0) {
				System.out.println("Erreur!");
			}else {
				System.out.println("Mod�le Ajouter avec Succ�s!");
			}
		}
		public static void SupprimerModele(Connection Conn) throws SQLException{
			Statement requete =conn.createStatement();
			ResultSet res2 =requete.executeQuery("SELECT modele FROM ModelesAvion");

			String modele=new String();
			ArrayList<String> l=new ArrayList<String>();
			if(res2.next()) {
				l.add((res2.getString("modele")));
				while(res2.next()) {
					l.add((res2.getString("modele")));
				}

				System.out.println("Veuillez s�lectionnez un Mod�le: ");
				for(String c:l) {
					System.out.println("["+c+"]");
				}

				modele=LectureClavier.lireChaine();

				while(!l.contains(modele)){
					System.out.println("Veuillez re-s�lectionner un Mod�le: ");
					modele=LectureClavier.lireChaine();
				}
			}else{
				System.out.println("Mod�le Incorrecte !");
			}
			Statement requete1= conn.createStatement();
			int delete=requete1.executeUpdate("DELETE FROM ModelesAvion WHERE modele='"+modele+"'");
			if (delete==0) {
				System.out.println("Erreur!");
			}else {
				System.out.println("Mod�le Supprimer avec Succ�s!");
			}
		}

		public static void AfficherModeles(Connection Conn) throws SQLException{
			System.out.println("Les Mod�les Disponibles: ");
			Statement requete =conn.createStatement();
			ResultSet res =requete.executeQuery("SELECT modele FROM ModelesAvion");
			System.out.println("| Mod�le |");
			ArrayList<String> l=new ArrayList<String>();
			if(res.next()) {
				l.add(res.getString("modele"));
				while(res.next()) {
					l.add(res.getString("modele"));
				}
				for(String c:l) {
					System.out.println("["+c+"]");
				}
			}
		}
	}