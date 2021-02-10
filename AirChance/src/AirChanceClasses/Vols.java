import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
public class Vols {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void GestionVol(Connection Conn) throws SQLException{
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
		System.out.println("1-Ajouter un Vol");
		System.out.println("2-Afficher tous Les Vols");
		System.out.println("3-Supprimer un Vols");
		System.out.println("4-Affectation du Vol � une �quipe");

		int n = Integer.parseInt(LectureClavier.lireChaine());

		switch(n) {
		case 1:
			AjouterVol(conn);
			break;
		case 2:
			AfficherVols(conn);
			break;
		case 3:
			SupprimerVol(conn);
			break;
		case 4:
			EffectueVol(conn);
			break;
		}
	}
	public static void AjouterVol(Connection Conn)throws SQLException{
		Random n = new Random();
		int numV = n.nextInt(1000);
		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("SELECT numAvion FROM Avions");

		int numA=0;
		ArrayList<Integer> l=new ArrayList<Integer>();
		if(res2.next()) {
			l.add(Integer.parseInt(res2.getString("numAvion")));
			while(res2.next()) {
				l.add(Integer.parseInt(res2.getString("numAvion")));
			}

			System.out.println("Veuillez s�lectionnez un Nom: ");
			for(int c:l) {
				System.out.println("["+c+"]");
			}

			numA=Integer.parseInt(LectureClavier.lireChaine());

			while(!l.contains(numA)){
				System.out.println("Veuillez re-s�lectionner un Nom: ");
				numA=Integer.parseInt(LectureClavier.lireChaine());
			}
		}else{
			System.out.println("Num�ro d'Avion Incorrecte !");
		}
		System.out.println("A�roport de D�part:");
		String aeroD = LectureClavier.lireChaine();	
		System.out.println("A�roport d'Arriv�e: ");
		String aeroA = LectureClavier.lireChaine();
		System.out.println("Horaire (HH:MI:SS): ");
		String horaire=LectureClavier.lireChaine();
		System.out.println("Date du Vol(YYYY-MM-DD): ");
		String dVol=LectureClavier.lireChaine();
		System.out.println("Dur�e: ");
		String duree=LectureClavier.lireChaine();
		System.out.println("Distance: ");
		int dist=Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Min des Places Classe �conomique: ");
		int pEco=Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Min des Places Classe Premi�re: ");
		int pPre=Integer.parseInt(LectureClavier.lireChaine());
		System.out.println("Min des Places Classe Affaire: ");
		int pAff=Integer.parseInt(LectureClavier.lireChaine());
		int insert = requete.executeUpdate("INSERT INTO Vols"+" VALUES("+numV+","+numA+",'"+aeroD+"','"+aeroA+"',to_date('"+horaire+"','HH24:MI:SS'),TO_DATE('"+dVol+"', 'YYYY-MM-DD'),'"+duree+"',"+dist+","+pEco+","+pPre+","+pAff+")");
		if(insert==0){
			System.out.println("Erreur!");
		}else {
			System.out.println("Vol Ajouter avec Succ�s!");
		}
	}
	public static void AfficherVols(Connection Conn)throws SQLException{
		System.out.println("Liste des Vols: ");
		Statement requete =conn.createStatement();
		ResultSet res =requete.executeQuery("SELECT * FROM Vols");
		ResultSetMetaData resultMeta = res.getMetaData();
		System.out.println("|Num�ro Vol|Num Avion|A�ro.D�part|A�ro.Arriv�e|Horaire|Date d�part|Dur�e|Distance|Min.Place �co|Min.Place Premi�re|Min.Place Affaire");
		System.out.println("\n");
		while(res.next())
		{      
			for(int i = 1; i <=  resultMeta.getColumnCount(); i++)
				System.out.print("|"+res.getObject(i).toString());
			System.out.println("\n");
		}
	}
	public static void SupprimerVol(Connection Conn) throws SQLException{
		Statement requete =conn.createStatement();
		ResultSet res2 =requete.executeQuery("SELECT numVol FROM Vols");
		int numv=0;
		ArrayList<Integer> l=new ArrayList<Integer>();
		if(res2.next()) {
			l.add(Integer.parseInt(res2.getString("numVol")));
			while(res2.next()) {
				l.add(Integer.parseInt(res2.getString("numVol")));
			}

			System.out.println("Veuillez s�lectionnez un Num�ro de Vol: ");
			for(int c:l) {
				System.out.println("["+c+"]");
			}

			numv=Integer.parseInt(LectureClavier.lireChaine());

			while(!l.contains(numv)){
				System.out.println("Veuillez re-s�lectionner un Num�ro de Vol: ");
				numv=Integer.parseInt(LectureClavier.lireChaine());
			}
		}else{
			System.out.println("Num�ro de Vol Incorrecte !");
		}
		Statement requete1= conn.createStatement();
		int delete=requete1.executeUpdate("DELETE FROM Vols WHERE numVol='"+numv+"'");
		if (delete==0) {
			System.out.println("Erreur!");
		}else {
			System.out.println("Vol Supprimer avec Succ�s!");
		}
	}
	public static void EffectueVol(Connection Conn)throws SQLException{
		CallableStatement mySt=null;
		try {
			System.out.println("Saisissez le Num�ro de Vol: ");
			int numv= Integer.parseInt(LectureClavier.lireChaine());
			
			System.out.println("Saisissez le Num�ro d'Avion: ");
			int numa= Integer.parseInt(LectureClavier.lireChaine());
			
			System.out.println("Saisissez le Num�ro d'Hotesse: ");
			int numh= Integer.parseInt(LectureClavier.lireChaine());
			
			System.out.println("Saisissez le Num�ro du Pilote: ");
			int nump=Integer.parseInt(LectureClavier.lireChaine());
			
			mySt=conn.prepareCall("{call CHECK_EFFECTUE(?,?,?,?)}");
				mySt.setInt(1,numv);
				mySt.setInt(2,numa);
				mySt.setInt(3,numh);
				mySt.setInt(4,nump);
				//mySt.registerOutParameter(5, Types.VARCHAR);
			mySt.execute();
			//String msg = mySt.getString(5);
			
			System.out.println(">>>>>>>>>>>>>Valid�<<<<<<<<<<<<<<<");
			
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
