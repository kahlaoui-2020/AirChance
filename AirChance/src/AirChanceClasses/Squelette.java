import java.sql.*;
//import java.util.Random;
//import java.util.ArrayList;
public class Squelette {
	private static final String configurationFile = "src/AirchanceF.propreties";
	static Connection conn;
	public static void main(String[] args) throws SQLException {


		try {
			// 1. Get a connection to database
			System.out.print("Loading Oracle driver... ");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());     
			System.out.println("loaded");

			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			System.out.print("Connecting to the database... ");
			conn = DriverManager.getConnection(dap.getDatabaseUrl(), dap.getUsername(),dap.getPassword());
			System.out.println("connected");
		}catch (Exception exc) {
				exc.printStackTrace();
			}
			
		System.out.println("---------------------Bienvenue sur AirChance-------------------------");
		System.out.println("----------------Veuillez choisir une opération--------------");
		System.out.println("1-Gestion des Modèles");
		System.out.println("2-Gestion des Avions");
		System.out.println("3-Gestion des Clients");
		System.out.println("4-Gestion des Vols.");
		System.out.println("5-Gestion des Personnels.");
		System.out.println("6-Gestion des Réservations.");
		System.out.println("7-Gestion des Places d'Avion.");

		int n = Integer.parseInt(LectureClavier.lireChaine());
		switch(n) {
			case 1:
				Modeles.GestionModeles(conn);
				break;
			case 2:
				Avions.GestionAvion(conn);
				break;
			case 3:
				Clients.GestionClient(conn);
				break;
			case 4:
				Vols.GestionVol(conn);
				break;
			case 5:
				Personnels.GestionPersonnels(conn);
				break;
			case 6:
				Reservations.GestionReservation(conn);
				break;
			case 7:
				Places.GestionPlaces(conn);
				break;
			}
		}
}

