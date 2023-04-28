package BusinessLogic;

import GUI.SimulationFrame;
import Model.Task;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static Model.Task.taskArrivalTimeComparator;
import static java.lang.Thread.sleep;

public class SimulationManager implements Runnable {

    private SimulationFrame frame;

    private Integer simulationTime = 60;
    private Integer noTasks = 50;
    private Integer minArrivalTime = 2;
    private Integer maxArrivalTime = 40;
    private Integer minServiceTime = 1;
    private Integer maxServiceTime = 7;

    private final Scheduler scheduler;
    private final Logger logger = new Logger ( "/home/dragos/IdeaProjects/QueuePT/Log Folder" );

    private final AtomicInteger currentTime = new AtomicInteger ( );
    private final ArrayList< Task > tasks = new ArrayList<> ( );

    private final Object lock = new Object ( );
    private volatile boolean stopped = false, suspended = false;


    public SimulationManager ( SimulationFrame frameParam, Integer noServers ) {
        frame = frameParam;
        scheduler = new Scheduler ( noServers, SelectionPolicy.SHORTEST_TIME );
    }


    public void setSimulationTime ( Integer newSimulationTime ) {
        simulationTime = newSimulationTime;
    }

    public void setNoTasks ( Integer newNoTasks ) {
        noTasks = newNoTasks;
    }

    public void setNoServers ( Integer noServers ) {
        scheduler.changeNoServers ( noServers );
    }

    public void setArrivalInterval ( Integer newMinArrivalTime, Integer newMaxArrivalTime ) {
        minArrivalTime = newMinArrivalTime;
        maxArrivalTime = newMaxArrivalTime;
    }


    public void setServiceInterval ( Integer newMinServiceTime, Integer newMaxServiceTime ) {
        minServiceTime = newMinServiceTime;
        maxServiceTime = newMaxServiceTime;
    }

    public void generateRandomTasks ( ) {
        for ( int i = 0; i < noTasks; i++ )
            tasks.add ( new Task ( minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime ) );
        tasks.sort ( taskArrivalTimeComparator );
    }

    @Override
    public void run ( ) {
        currentTime.set ( 0 );
        while ( !stopped ) {
            while ( !suspended && currentTime.get ( ) < simulationTime ) {

                while ( !tasks.isEmpty ( ) && tasks.get ( 0 ).getArrivalTime ( ) == currentTime.get ( ) ) {
                    scheduler.dispatchTask ( tasks.get ( 0 ) );
                    tasks.remove ( 0 );
                }

                frame.updateFrame ( scheduler.getServerQueueSizes ( ) );
                System.out.println ( currentTime );

                logger.logSimulationStatus ( currentTime.get ( ), tasks, scheduler.getServerStatuses ( ) );
                scheduler.updatePeakHour ( currentTime.get ( ) );

                try {
                    sleep ( 1000 );
                } catch ( InterruptedException e ) {
                    System.out.println ( e.getMessage ( ) );
                }
                currentTime.incrementAndGet ( );
            }

            if ( currentTime.get ( ) == simulationTime ) {
                logger.logSimulationStop ( scheduler.getAverageWaitTime ( ), scheduler.getAverageServiceTime ( ), scheduler.getPeakHour ( ), currentTime.get ( ), simulationTime );
                scheduler.stopServers ( );
                break;
            }

            if ( !stopped ) {
                synchronized ( lock ) {
                    try {
                        lock.wait ( );
                    } catch ( InterruptedException e ) {
                        System.out.println ( "Interrupted" );
                    }
                }
            }
        }
    }

    public void startSimulation ( ) {

        suspended = false;
        stopped = false;
        scheduler.startServers ( );

        System.out.println ( "Starting simulation" );
        logger.logSimulationStart ( simulationTime, noTasks, scheduler.getNoServers ( ), minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, tasks );
        Thread simulation = new Thread ( this );
        simulation.start ( );
//        try {
//            simulation.join ( );
//        } catch ( InterruptedException e ) {
//            System.out.println ( "Interrupted" );
//        }
    }

    public void pauseSimulation ( ) {
        suspended = true;
        scheduler.suspendServers ( );
    }

    public void unpauseSimulation ( ) {
        suspended = false;
        scheduler.unsuspendServers ( );
        synchronized ( lock ) {
            lock.notify ( );
        }
    }

    public void stopSimulation ( ) {
        stopped = true;
        suspended = true;
        synchronized ( lock ) {
            lock.notify ( );
        }
        scheduler.stopServers ( );
        logger.logSimulationStop ( scheduler.getAverageWaitTime ( ), scheduler.getAverageServiceTime ( ), scheduler.getPeakHour ( ), currentTime.get ( ), simulationTime );
    }

}
