import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import com.mysql.cj.xdevapi.Type;
//import java.util.Random;
public class Reservations {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void GestionReservation(Connection Conn) throws SQLException{
		try {
			// 1. Get a connection to database
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
		}catch (Exception exc) {
			exc.printStackTrace();
		}
		System.out.println("---------------------Gestion des Réservations-------------------------");
		System.out.println("----------------Veuillez choisir une opération--------------");
		System.out.println("1-Ajouter une réservation");
		System.out.println("2-Afficher les Réservations d'un Client");
		System.out.println("3-Supprimer une réservation");

		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			AjoutReservation(conn);
			break;
		case 3:
			AnnulerReservation(conn);
			break;
		case 2:
			AfficheReservation(conn);
			break;
		}
	}
	public static void AnnulerReservation(Connection Conn)throws SQLException{
		CallableStatement mySt=null;

		try {
			System.out.println("Numéro Client: ");
			int numc = Integer.parseInt(LectureClavier.lireChaine());


			Statement requete =conn.createStatement();
			ResultSet res2 =requete.executeQuery("select numReservation from Reservations WHERE numClient ="+numc+"");
			int numR = 0;
			ArrayList<Integer> l=new ArrayList<Integer>();
			if(res2.next()) {
				l.add(Integer.parseInt(res2.getString("numReservation")));
				while(res2.next()) {
					l.add(Integer.parseInt(res2.getString("numReservation")));
				}


				System.out.println("Veuillez sélectionner le Numéro de Réservation: ");
				for(int c:l) {
					System.out.println("["+c+"]");
				}
				System.out.println();

				numR=Integer.parseInt(LectureClavier.lireChaine());

				while(!l.contains(numR)){
					System.out.println("Veuillez re-sélectionner un Numéro de Réservation: ");
					numR=Integer.parseInt(LectureClavier.lireChaine());

				}
			}else{
				System.out.println("Vous n'avez Aucune Réservation !");
			}
			//System.out.println("Numéro de Réservation");
			//int num_r=Integer.parseInt(LectureClavier.lireChaine());

			Statement requete1=conn.createStatement();
			int res1 = requete.executeUpdate("DELETE FROM RESERVATIONS WHERE numReservation="+numR+"'");
			if(res1 != 0) {
				System.out.println("Supprimé avec Succès !");
			}
		}catch(Exception ex) {
			System.err.println(ex.getMessage());
		}finally {
			if(mySt!=null) {
				mySt.close();
			}
			if(conn!=null) {
				conn.close();
			}
		}
	}

	public static void AjoutReservation(Connection Conn)throws SQLException{
		try {
			Random n = new Random();
			int numR = n.nextInt(100000);

			System.out.println("Veuillez Entrez votre Numéro: ");
			int numC=Integer.parseInt(LectureClavier.lireChaine());

			System.out.println("Aéroport de Départ: ");
			String aerod=LectureClavier.lireChaine();

			System.out.println("Aéroport d'Arrivée: ");
			String aeroa=LectureClavier.lireChaine();

			AfficherVol(aerod,aeroa);

			System.out.println("Le Vol Choisi: ");
			int numvc=Integer.parseInt(LectureClavier.lireChaine());


			System.out.println("Combien de Places Souhaitez Vous Réserver ?: ");
			int nbPlace=Integer.parseInt(LectureClavier.lireChaine());
			System.out.println("Les Places Disponibles:");
			AfficherPlaces(numvc);
			System.out.println("Veuillez Sélectionnez la Place: ");
			String place=LectureClavier.lireChaine();

			int prixtot=CalculPrixTotal(place,nbPlace);

			Statement requete3 =conn.createStatement();
			ResultSet res3 =requete3.executeQuery("select point_cumul from Clients WHERE numClient ="+numC+"");

			ArrayList<Integer> l=new ArrayList<Integer>();
			if(res3.next()) {
				l.add(Integer.parseInt(res3.getString("point_cumul")));
				while(res3.next()) {
					l.add(Integer.parseInt(res3.getString("point_cumul")));
				}
				for(int c:l) {
					if(c>=50) {
						System.out.println(" Vous Avez : " + c + " Points Vous Voulez Les Utilisez ? (oui/non)" );
						String rep=LectureClavier.lireChaine();
						if(rep.equals("oui")) {
							Transaction(numR, numC, numvc, place, prixtot);
						}else if(rep.contentEquals("non")) {
							Procedure(numR, numC, numvc, place, prixtot);
						}else {
							System.out.println("Veuillez Répondre par Oui ou Non");
						}
					}else {
						Procedure(numR, numC, numvc, place, prixtot);
					}
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(conn!=null) {
				conn.close();
			}
		}
	}
	public static void AfficherVol(String aerod,String aeroa) throws SQLException{
		try {
			Statement requete1 =conn.createStatement();
			ResultSet res2 =requete1.executeQuery("select * from Vols WHERE aero_dep ='"+aerod+"' AND aero_arrv='"+aeroa+"'");
			ResultSetMetaData resultMeta = res2.getMetaData();
			System.out.println("|Numéro Vol|Numéro d'Avion|Aéroport Départ|Aéroport Arrivée|Horaire|Date départ|");
			while(res2.next())
			{      
				for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
					System.out.print("|"+res2.getObject(i).toString());
				System.out.println("\n");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(conn!=null) {
				conn.close();
			}
		}
	}
	public static void AfficherPlaces(int numvc)throws SQLException{
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());

			Statement requete1 =conn.createStatement();
			ResultSet res2 =requete1.executeQuery("select numPlace,classe from Places P natural join Avions A natural join Vols V WHERE V.numVol ="+numvc+"");
			ResultSetMetaData resultMeta = res2.getMetaData();
			System.out.println("|Numéro de Place dans l'Avion|Classe|");
			while(res2.next())
			{      
				for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
					System.out.print("|"+res2.getObject(i).toString());
				System.out.println("\n");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public static int CalculPrixTotal(String Place,int nbPlaces)throws SQLException{
		DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
		conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());

		Statement requete1 =conn.createStatement();
		ResultSet res2 =requete1.executeQuery("select prix from Places WHERE numPlace ='"+Place+"'");
		int prix=0;
		if(res2.next()) {
			prix=Integer.parseInt(res2.getString("prix")) * nbPlaces;
		}
		System.out.println("Prix Total: "+prix);

		return prix;
	}
	public static void Transaction(int numR, int numClient,int numvc, String Place, int prixtot)throws SQLException{
		CallableStatement mySt=null;
		try {	
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());

			conn.setAutoCommit(false);
			
			int prixtotMod;
			prixtotMod=prixtot-(prixtot*5)/100;

			Statement requete=conn.createStatement();
			ResultSet res2=requete.executeQuery("UPDATE Clients SET point_cumul=point_cumul-50 WHERE numClient="+numClient+"");
			
			System.out.println("Le Nouveau Prix est: "+prixtotMod);
			Statement requete1=conn.createStatement();
			ResultSet res1=requete1.executeQuery("INSERT INTO Reservations VALUES("+numR+",'"+Place+"',"+numvc+","+numClient+",sysdate,"+prixtot+")");
			/*mySt=conn.prepareCall("{call INSERTION_RESERVATION(?,?,?,?,?,?)}");
			mySt.setInt(1, numR);
			mySt.setString(2, Place);
			mySt.setInt(3, numvc);
			mySt.setInt(4, numClient);
			mySt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
			mySt.setInt(6, prixtot);*/
			
			
			System.out.println("Est ce que vous voulez valider la réservation ?");
			String rep=LectureClavier.lireChaine();
			if(rep.equals("oui")) {
				conn.commit();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>Validé<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}else if (rep.equals("non")) {
				conn.rollback();
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>Annulation<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public static void Procedure(int numR, int numClient,int numvc, String Place, int prixtot)throws SQLException{
		CallableStatement mySt=null;
		try {	
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
			
			conn.setAutoCommit(false);
			
			Statement requete1=conn.createStatement();
			int res1 =requete1.executeUpdate("INSERT INTO Reservations VALUES("+numR+",'"+Place+"',"+numvc+","+numClient+",sysdate,"+prixtot+")");
			if(res1==0) {
				System.out.println("Erreur");
			}
			
			/*mySt=conn.prepareCall("{call INSERTION_RESERVATION(?,?,?,?,?,?)}");
			mySt.setInt(1, numR);
			mySt.setString(2, Place);
			mySt.setInt(3, numvc);
			mySt.setInt(4, numClient);
			mySt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
			mySt.setInt(6, prixtot);*/
			
			System.out.println("Est ce que vous voulez valider la réservation ?");
			String rep=LectureClavier.lireChaine();
			if(rep.equals("oui")) {
				conn.commit();
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>Validé<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}else if (rep.equals("non")) {
				conn.rollback();
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>Annulation<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(conn!=null) {
				conn.close();
			}else if(mySt!=null) {
				mySt.close();
			}
		}
	}
	public static void AfficheReservation(Connection Conn)throws SQLException{
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
			
			System.out.println("Numéro du Client: ");
			int numc=Integer.parseInt(LectureClavier.lireChaine());
			
			System.out.println("Les Réservations trouvées: ");
			Statement req = conn.createStatement();
			ResultSet res =req.executeQuery("SELECT * FROM Reservations where numClient = '"+numc+"'");
			ResultSetMetaData resultMeta = res.getMetaData();
			System.out.println("|Numéro Réservation|Numéro de Place|Numéro de Vol |Numéro du Client |Date de la réservation|Prix Totale");
			while(res.next())
			{      
				for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
					System.out.print("|"+res.getObject(i).toString());
				System.out.println("\n");
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(conn!=null) {
				conn.close();
			}
		}
	}
}

