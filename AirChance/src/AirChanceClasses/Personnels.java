import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
public class Personnels {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void GestionPersonnels(Connection Conn) throws SQLException{
		try {
			// 1. Get a connection to database
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
		}catch (Exception exc) {
			exc.printStackTrace();
		}
		System.out.println("---------------------Gestion des Personnels-------------------------");
		System.out.println("----------------Veuillez choisir une opération--------------");
		System.out.println("1-Gestion des Pilotes");
		System.out.println("2-Gestion des Hotesses");
		System.out.println("3-Affecter un Equipage à un Avion");

		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			GestionPilotes(conn);
			break;
		case 2:
			GestionHotesses(conn);
			break;
		case 3:
			Affecter(conn);
		}
	}
	public static void GestionPilotes(Connection Conn) throws SQLException{
		System.out.println("---------------------Gestion des Pilotes-------------------------");
		System.out.println("----------------Veuillez choisir une opération--------------");
		System.out.println("1-Ajouter un Pilote");
		System.out.println("2-Afficher tous Les Pilotes");
		System.out.println("3-Supprimer un Pilote");
		System.out.println("4-Ajouter un Qualife");


		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			AjouterPilote(conn);
			break;
		case 2:
			AfficherPilotes(conn);
			break;
		case 3:
			SupprimerPilote(conn);
			break;
		case 4:
			AjouterQualife(conn);
		}
	}
	public static void AjouterPilote(Connection Conn)throws SQLException{
		Random n = new Random();
		int numP = n.nextInt(1000);
		System.out.println("Nombre d'Heures de Vol:");
		int nbvol = Integer.parseInt(LectureClavier.lireChaine());	
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
		int insert = req.executeUpdate("INSERT INTO Pilotes VALUES("+numP+","+nbvol+",'"+nom+"','"+prenom+"',"+num+",'"+rue+"',"+codep+",'"+ville+"','"+pays+"')");
		if(insert==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Pilote Ajouter avec Succès!");
		}
	}
	public static void AfficherPilotes(Connection Conn)throws SQLException{
		System.out.println("Liste des Pilotes: ");
		Statement requete =conn.createStatement();
		ResultSet res =requete.executeQuery("SELECT * FROM Pilotes");
		ResultSetMetaData resultMeta = res.getMetaData();
		System.out.println("|Numéro Pilote|Nombre d'Heures.Vol|Nom|Prénom|Numéro|rue|Code Postale|Ville|Pays");
		System.out.println("\n");
		while(res.next())
		{      
			for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
				System.out.print("|"+res.getObject(i).toString());
			System.out.println("\n");
		}
	}
	public static void SupprimerPilote(Connection Conn) throws SQLException{
		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("SELECT numPilote FROM Pilotes");
		int nump=0;
		ArrayList<Integer> l=new ArrayList<Integer>();
		if(res2.next()) {
			l.add(Integer.parseInt(res2.getString("numPilote")));
			while(res2.next()) {
				l.add(Integer.parseInt(res2.getString("numPilote")));
			}

			System.out.println("Veuillez sélectionnez un Numéro de Pilote: ");
			for(int c:l) {
				System.out.println("["+c+"]");
			}

			nump=Integer.parseInt(LectureClavier.lireChaine());

			while(!l.contains(nump)){
				System.out.println("Veuillez re-sélectionner un Numéro de Pilote: ");
				nump=Integer.parseInt(LectureClavier.lireChaine());
			}
		}else{
			System.out.println("Numéro de Pilote Incorrecte !");
		}
		Statement requete1= conn.createStatement();
		int delete=requete1.executeUpdate("DELETE FROM Pilotes WHERE numPilote='"+nump+"'");
		if (delete==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Pilote Supprimer avec Succès!");
		}
	}
	public static void AjouterQualife(Connection Conn) throws SQLException{
		System.out.println("-------------------------------------------------------- ");
		Statement requete1 =conn.createStatement();
		ResultSet res1 =requete1.executeQuery("select modele from ModelesAvion");
		String modele = new String();
		ArrayList<String> l=new ArrayList<String>();
		if(res1.next()) {
			l.add((res1.getString("modele")));
			while(res1.next()) {
				l.add((res1.getString("modele")));
			}


			System.out.println("Veuillez sélectionner un Modèle: ");
			for(String c:l) {
				System.out.println("["+c+"]");
			}
			System.out.println();

			modele=LectureClavier.lireChaine();

			while(!l.contains(modele)){
				System.out.println("Veuillez re-sélectionner un Modèle: ");
				modele=LectureClavier.lireChaine();

			}
		}else{
			System.out.println("Modèle Incorrecte !");
		}

		System.out.println("Sélectionnez le Pilote: ");
		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("SELECT numPilote FROM Pilotes");
		int nump=0;
		ArrayList<Integer> l1=new ArrayList<Integer>();
		if(res2.next()) {
			l1.add(Integer.parseInt(res2.getString("numPilote")));
			while(res2.next()) {
				l1.add(Integer.parseInt(res2.getString("numPilote")));
			}

			System.out.println("Veuillez sélectionnez un Numéro de Pilote: ");
			for(int c:l1) {
				System.out.println("["+c+"]");
			}

			nump=Integer.parseInt(LectureClavier.lireChaine());

			while(!l1.contains(nump)){
				System.out.println("Veuillez re-sélectionner un Numéro de Pilote: ");
				nump=Integer.parseInt(LectureClavier.lireChaine());
			}
		}else{
			System.out.println("Numéro de Pilote Incorrecte !");
		}
		Statement req= conn.createStatement();
		int insert=req.executeUpdate("INSERT INTO QUALIFIE VALUES('"+modele+"',"+nump+")");
		if (insert==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Qualife Ajouter !");
		}
	}
	public static void GestionHotesses(Connection Conn) throws SQLException{
		System.out.println("---------------------Gestion des Hotesses-------------------------");
		System.out.println("----------------Veuillez choisir une opération--------------");
		System.out.println("1-Ajouter une Hotesses");
		System.out.println("2-Afficher toutes Les Hotesses");
		System.out.println("3-Supprimer une Hotesse");

		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			AjouterHotesse(conn);
			break;
		case 2:
			AfficherHotesses(conn);
			break;
		case 3:
			SupprimerPilote(conn);
			break;
		case 4:
			AjouterQualife(conn);
		}
	}
	public static void AjouterHotesse(Connection Conn)throws SQLException{
		Random n = new Random();
		int numH = n.nextInt(1000);
		System.out.println("Nombre d'Heures de Vol:");
		int nbvol = Integer.parseInt(LectureClavier.lireChaine());	
		System.out.println("Langues parlées: ");
		String lang=LectureClavier.lireChaine();
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
		int insert = req.executeUpdate("INSERT INTO Hotesses VALUES("+numH+","+nbvol+",'"+lang+"','"+nom+"','"+prenom+"',"+num+",'"+rue+"',"+codep+",'"+ville+"','"+pays+"')");
		if(insert==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Hotesse Ajouter avec Succès!");
		}
	}
	public static void AfficherHotesses(Connection Conn)throws SQLException{
		System.out.println("Liste des Hotesses: ");
		Statement requete =conn.createStatement();
		ResultSet res =requete.executeQuery("SELECT * FROM Hotesses");
		ResultSetMetaData resultMeta = res.getMetaData();
		System.out.println("|Numéro Hotesse|Nombre d'Heures.Vol|Nom|Prénom|Numéro|rue|Code Postale|Ville|Pays");
		System.out.println("\n");
		while(res.next())
		{      
			for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
				System.out.print("|"+res.getObject(i).toString());
			System.out.println("\n");
		}
	}
	public static void Affecter(Connection Conn)throws SQLException{
		CallableStatement mySt=null;
		try {

			Statement requete =conn.createStatement();
			ResultSet res =requete.executeQuery("SELECT numAvion FROM Avions");

			int numAvion=0;
			ArrayList<Integer> l=new ArrayList<Integer>();
			if(res.next()) {
				l.add(Integer.parseInt(res.getString("numAvion")));
				while(res.next()) {
					l.add(Integer.parseInt(res.getString("numAvion")));
				}

				System.out.println("Veuillez sélectionnez un Avion: ");
				for(int c:l) {
					System.out.println("["+c+"]");
				}

				numAvion=Integer.parseInt(LectureClavier.lireChaine());

				while(!l.contains(numAvion)){
					System.out.println("Veuillez re-sélectionner un AVion: ");
					numAvion=Integer.parseInt(LectureClavier.lireChaine());
				}
			}else{
				System.out.println("Numéro d'Avion Incorrecte !");
			}

			System.out.println("Entrez un numéro d'Hotesse");
			int numH=Integer.parseInt(LectureClavier.lireChaine());

			Statement requete2 =conn.createStatement();
			ResultSet res2 =requete2.executeQuery("SELECT numPilote FROM Qualifie M natural join Avions A where A.numAvion='"+numAvion+"'");
			ResultSetMetaData resultMeta = res.getMetaData();
			System.out.println("|Pilote Qualifié|");
			while(res2.next())
			{      
				for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
					System.out.print(res2.getObject(i).toString());
				System.out.println("\n");
			}
			System.out.println("Pilote choisi: ");
			int numP=Integer.parseInt(LectureClavier.lireChaine());

			mySt=conn.prepareCall("{call CHECK_AFFECTATION(?,?,?)}");
			mySt.setInt(1,numAvion);
			mySt.setInt(2,numH);
			mySt.setInt(3,numP);

			mySt.execute();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(mySt!=null) {
				mySt.close();
			}
			if(conn!=null) {
				conn.close();
			}

		}
	}
}
