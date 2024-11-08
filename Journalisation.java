package singletonTP;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeFormatter;

public class Journalisation {

	private static volatile Journalisation instance;
	private final String LOG_FILE = "transactions.log";
	private PrintWriter writer;
	private DateTimeFormatter dateFormatter;
	
	
	private Journalisation() {
		dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		try {
			writer = new PrintWriter(new FileWriter(LOG_FILE, true));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// Méthode pour obtenir l'instance unique avec double-checked locking
	public static synchronized Journalisation getInstance() {
		if (instance == null) {
			instance = new Journalisation();
		}
		return instance;
	}
	
	
	//logger depot & retrait
	public void logDeposit(String accNumber, double amount, double newBalance) {
		logTransaction("DEPOT", accNumber, amount, newBalance);
	}
	
	public void logRetrait(String accNumber, double amount, double newBalance) {
		logTransaction("RETRAIT", accNumber, amount, newBalance);
	}
	
	//logger transfert
	public void logTransfer(String fromAcc, String ToAcc, double amount) {
		String timeS = LocalDateTime.now().format(dateFormatter);
		String logMessage = String.format("%s - TRANSFERT: De compte %s vers compte %s, Montant: %.2fMAD",timeS,fromAcc,ToAcc,amount);
		writelog(logMessage);

	}
	
	//ecrire dans le fichier log
	private synchronized void writelog(String message) {
		if (writer != null) {
			writer.println(message);
			writer.flush();
		}
	}
	
	//Journalise les transactions
	private void logTransaction(String type, String accNumber, double amount, double newBalnce) {
		String timeS = LocalDateTime.now().format(dateFormatter);
		String logMessage = String.format("%s - %s : Compte %s, Montant: %.2fMAD, Nouveau solde: %.2fMAD",timeS,type,accNumber,amount,newBalnce);
		writelog(logMessage);
	}
	
	//fermer writer
	public void close() {
        if (writer != null) {
            writer.close();
        }
    }

	public static void main(String[] args) {
        Journalisation logger = Journalisation.getInstance();

        // Simulation de quelques transactions
        logger.logDeposit("FR123456789", 1000.0, 1000.0);
        logger.logRetrait("FR123456789", 200.0, 800.0);
        logger.logTransfer("FR123456789", "FR987654321", 300.0);

        // Fermeture du logger
        logger.close();
    }
	
	
	
	
}
