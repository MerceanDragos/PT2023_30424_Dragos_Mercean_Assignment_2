package BusinessLogic;

import Model.Server;
import Model.Task;

import javax.swing.text.NumberFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Logger {

    private FileWriter file;
    private String logFolderPath;

    public Logger ( String logFolderPathParam ) {
        logFolderPath = logFolderPathParam;
    }

    private void openFile ( ) {
        String simID = String.valueOf ( ThreadLocalRandom.current ( ).nextInt ( 1000000, 2000001 ) );
        try {
            file = new FileWriter ( logFolderPath + "/Simulation Log " + simID );
        } catch ( IOException e ) {
            System.out.println ( "FileWriter exception" );
        }
    }

    private void closeFile ( ) {
        try {
            file.close ( );
        } catch ( IOException e ) {
            System.out.println ( "FileWriter exception" );
        }
    }

    private void printWaitingClients ( ArrayList< Task > tasks ) {
        try {
            for ( int i = 0; i < 10 && i < tasks.size ( ); i++ ) {

                Task task = tasks.get ( i );
                file.write ( "(" + task.getID ( ) + ", " + task.getArrivalTime ( ) + ", " + task.getServiceTime ( ) + ")" );

                if ( i + 1 == 10 && tasks.size ( ) > 10 )
                    file.write ( ",...\n" );
                else if ( i + 1 < 10 )
                    file.write ( ", " );
            }
        } catch ( IOException e ) {
            System.out.println ( "FileWriter exception" );
        }
    }

    public void logSimulationStart ( Integer simulationTime, Integer noClients, Integer noServers, Integer minArrivalTime, Integer maxArrivalTime, Integer minServiceTime, Integer maxServiceTime, ArrayList< Task > tasks ) {
        openFile ( );
        try {
            file.write ( "Simulation parameters\n" );
            file.write ( "Simulation time: " + simulationTime + "\n" );
            file.write ( "Num. of clients: " + noClients + "\n" );
            file.write ( "Num. of servers: " + noServers + "\n" );
            file.write ( "Arrival interval: [ " + minArrivalTime + ", " + maxArrivalTime + " ]\n" );
            file.write ( "Service time range: [ " + minServiceTime + ", " + maxServiceTime + " ]\n" );

            file.write ( "Clients: " );
            printWaitingClients ( tasks );
            file.write ( "\n\n" );
        } catch ( IOException e ) {
            System.out.println ( "FileWriter exception" );
        }
    }

    public void logSimulationStatus ( Integer currentTime, ArrayList< Task > tasks, String[] serverStatuses ) {

        try {
            file.write ( "Time " + currentTime + '\n' );
            file.write ( "Waiting clients: " );

            printWaitingClients ( tasks );
            file.write ( '\n' );

            for ( String serverStatus : serverStatuses )
                file.write ( serverStatus + '\n' );

            file.write ( '\n' );
        } catch ( IOException e ) {
            System.out.println ( "FileWriter exception" );
        }

    }

    public void logSimulationStop ( Double averageWaitTime, Double averageServiceTime, Integer peakHour, Integer currentTime, Integer simulationTime ) {

        DecimalFormat nf = new DecimalFormat ( "#0.00" );

        try {
            if ( currentTime < simulationTime )
                file.write ( "Simulation finished prematurely at " + currentTime + "s\n" );
            else
                file.write ( "Simulation finished\n" );

            file.write ( "Average wait time: " + nf.format ( averageWaitTime ) + "s\n" );
            file.write ( "Average service time: " + nf.format ( averageServiceTime ) + "s\n" );
            file.write ( "Peak second: " + peakHour + "s\n" );

            file.write ( '\n' );
        } catch ( IOException e ) {
            System.out.println ( "FileWriter exception" );
        }

        closeFile ( );
    }

}
