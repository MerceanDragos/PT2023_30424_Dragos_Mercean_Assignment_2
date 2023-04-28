package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;

public class Scheduler {

    private Integer noServers;
    private final ArrayList< Server > servers;
    private Strategy strategy;

    private Integer peakHour = 0;
    private Integer peakHourNoTasks = 0;

    Scheduler ( Integer noServersParam, SelectionPolicy selectionPolicy ) {
        noServers = noServersParam;
        servers = new ArrayList<> ( );
        changeStrategy ( selectionPolicy );
    }

    void createServers ( ) {
        stopServers ( );
        servers.clear ( );
        for ( int i = 0; i < noServers; i++ )
            servers.add ( new Server ( ) );
    }

    void changeNoServers ( Integer newNoServers ) {
        noServers = newNoServers;
        createServers ( );
    }

    Integer getNoServers ( ) {
        return noServers;
    }

    public String[] getServerStatuses ( ) {
        String[] serverStatuses = new String[noServers];

        for ( int i = 0; i < servers.size ( ); i++ ) {
            StringBuilder sb = new StringBuilder ( "Queue " );
            sb.append ( i + 1 );
            sb.append ( ": " );
            sb.append ( servers.get ( i ).getServerQueue ( ) );
            serverStatuses[i] = sb.toString ( );
        }

        return serverStatuses;
    }

    void changeStrategy ( SelectionPolicy selectionPolicy ) {
        switch ( selectionPolicy ) {
            case SHORTEST_QUEUE -> strategy = new QueueSizeStrategy ( );
            default -> strategy = new TimeStrategy ( );
        }
    }

    SelectionPolicy getStrategy ( ) {
        return switch ( strategy.getClass ( ).getName ( ) ) {
            case "QueueSizeStrategy" -> SelectionPolicy.SHORTEST_QUEUE;
            default -> SelectionPolicy.SHORTEST_TIME;
        };
    }

    Double getAverageWaitTime ( ) {
        double averageWaitTime = 0;

        for ( Server server : servers )
            averageWaitTime += server.getAverageWaitTime ( );

        return averageWaitTime / noServers;
    }

    Double getAverageServiceTime ( ) {
        double averageServiceTime = 0;

        for ( Server server : servers )
            averageServiceTime += server.getAverageServiceTime ( );

        return averageServiceTime / noServers;
    }

    void updatePeakHour ( Integer currentTime ) {
        int noTasks = 0;

        for ( Server server : servers ) {
            noTasks += server.getQueueSize ( );
        }

        if ( noTasks > peakHourNoTasks ) {
            peakHour = currentTime;
            peakHourNoTasks = noTasks;
        }
    }

    Integer getPeakHour ( ) {
        return peakHour;
    }

    public Integer[] getServerQueueSizes ( ) {
        Integer[] sizes = new Integer[noServers];

        for ( int i = 0; i < noServers; i++ )
            sizes[i] = servers.get ( i ).getQueueSize ( );

        return sizes;
    }

    void dispatchTask ( Task task ) {
        strategy.addTask ( servers, task );
    }

    void startServers ( ) {
        createServers ( );
        for ( Server server : servers )
            new Thread ( server ).start ( );
    }

    void suspendServers ( ) {
        for ( Server server : servers )
            server.suspend ( );
    }

    void unsuspendServers ( ) {
        for ( Server server : servers )
            server.unsuspend ( );
    }

    void stopServers ( ) {
        for ( Server server : servers )
            server.stop ( );
    }

}
