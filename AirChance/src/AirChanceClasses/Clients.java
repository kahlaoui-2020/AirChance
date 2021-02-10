import java.sql.*;
import java.util.Random;
import java.util.ArrayList;
public class Clients {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void GestionClient(Connection Conn) throws SQLException{
		try {
			// 1. Get a connection to database
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
		}catch (Exception exc) {
				exc.printStackTrace();
			}
		System.out.println("---------------------Gestion des Vols-------------------------");
		System.out.println("----------------Veuillez choisir une opération--------------");
		System.out.println("1-Ajouter un Client");
		System.out.println("2-Afficher tous les Clients");
		System.out.println("3-Supprimer un Client");
		//System.out.println("3-Gestion des Réservations");

		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			AjouterClient(conn);
			break;
		case 2:
			AfficherClient(conn);
			break;
		case 3:
			SupprimerClient(conn);
			break;
		}
	}
	public static void AjouterClient(Connection Conn)throws SQLException{
		Random n = new Random();
		int numCl = n.nextInt(1000);
		System.out.println("Numéro de Passeoprt:");
		String numpass = LectureClavier.lireChaine();	
		System.out.println("Points Cumuls: ");
		int pointcumul = Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Nom: ");
		String nom=LectureClavier.lireChaine();
		System.out.println("Prénom: ");
		String prenom=LectureClavier.lireChaine();
		System.out.println("Numéro: ");
		int num=Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Rue: ");
		String rue=LectureClavier.lireChaine();
		System.out.println("Code Postale: ");
		int codep=Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Ville: ");
		String ville=LectureClavier.lireChaine();
		System.out.println("Pays: ");
		String pays=LectureClavier.lireChaine();

		Statement req = conn.createStatement();
		int insert = req.executeUpdate("INSERT INTO Clients VALUES("+numCl+","+numpass+","+pointcumul+",'"+nom+"','"+prenom+"',"+num+",'"+rue+"',"+codep+",'"+ville+"','"+pays+"')");
		if(insert==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Client Ajouter avec Succès!");
		}
	}
	public static void AfficherClient(Connection Conn)throws SQLException{
		System.out.println("Liste des Clients: ");
		Statement requete =conn.createStatement();
		ResultSet res =requete.executeQuery("SELECT * FROM Clients");
		ResultSetMetaData resultMeta = res.getMetaData();
		System.out.println("|Numéro Client|Num passeport|Point Cumuls|Nom|Prénom|Numéro|rue|Code Postale|Ville|Pays");
		System.out.println("\n");
		while(res.next())
		{      
			for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
				System.out.print("|"+res.getObject(i).toString());
			System.out.println("\n");
		}
	}
	public static void SupprimerClient(Connection Conn) throws SQLException{
		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("SELECT nom FROM Clients");

		String nom=new String();
		ArrayList<String> l=new ArrayList<String>();
		if(res2.next()) {
			l.add((res2.getString("nom")));
			while(res2.next()) {
				l.add((res2.getString("nom")));
			}

			System.out.println("Veuillez sélectionnez un Nom: ");
			for(String c:l) {
				System.out.println("["+c+"]");
			}

			nom=LectureClavier.lireChaine();

			while(!l.contains(nom)){
				System.out.println("Veuillez re-sélectionner un Nom: ");
				nom=LectureClavier.lireChaine();
			}
		}else{
			System.out.println("Numéro d'Avion Incorrecte !");
		}
		Statement requete1= conn.createStatement();
		int delete=requete1.executeUpdate("DELETE FROM Clients WHERE nom='"+nom+"'");
		if (delete==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Client Supprimer avec Succès!");
		}
	}
}
