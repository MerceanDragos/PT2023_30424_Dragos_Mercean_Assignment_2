package Model;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Thread.sleep;

public class Server implements Runnable {

    private final ArrayList< Task > taskQueue = new ArrayList<> ( );
    private final ArrayList< Task > taskGarbage = new ArrayList<> ( );
    private Integer queueWaitTime = 0;
    private Integer queueSize = 0;

    private final Object lock = new Object ( );
    private volatile boolean suspended = false, stopped = false;

    public Server ( ) {
    }

    public Integer getQueueWaitTime ( ) {
        return queueWaitTime;
    }

    public Integer getQueueSize ( ) {
        return queueSize;
    }

    synchronized public String getServerQueue ( ) {
        StringBuilder sb = new StringBuilder ( );
        for ( int i = 0; i < taskQueue.size ( ); i++ ) {
            sb.append ( "(" + taskQueue.get ( i ).getID ( ) + ", " + taskQueue.get ( i ).getArrivalTime ( ) + ", " + taskQueue.get ( i ).getServiceTime ( ) + ")" );
            if ( i + 1 < taskQueue.size ( ) )
                sb.append ( ", " );
        }
        return sb.toString ( );
    }

    private static final Comparator< Server > ServerWaitTimeComparator = Comparator.comparing ( Server::getQueueWaitTime );

    private static final Comparator< Server > ServerQueueSizeComparator = Comparator.comparing ( Server::getQueueSize );

    public Boolean isEmptierThan ( Server anotherServer ) {
        return ServerQueueSizeComparator.compare ( this, anotherServer ) < 0;
    }

    public Boolean isFasterThan ( Server anotherServer ) {
        return ServerWaitTimeComparator.compare ( this, anotherServer ) < 0;
    }

    public Double getAverageWaitTime ( ) {
        double averageWaitTime = 0;

        for ( Task task : taskGarbage )
            averageWaitTime += task.getWaitTime ( );

        return averageWaitTime / taskGarbage.size ( );
    }

    public Double getAverageServiceTime ( ) {
        double averageServiceTime = 0;

        for ( Task task : taskGarbage )
            averageServiceTime += task.getServiceTime ( );

        return averageServiceTime / taskGarbage.size ( );
    }

    public void addTask ( Task newTask ) {
        newTask.setWaitTime ( queueWaitTime );
        synchronized ( taskQueue ) {
            taskQueue.add ( newTask );
        }
        queueWaitTime = queueWaitTime + newTask.getServiceTime ( );
        queueSize++;
    }

    private void executeNextTask ( ) {
        Task task;
        try {
            synchronized ( taskQueue ) {
                task = taskQueue.get ( 0 );
            }

            sleep ( task.getServiceTime ( ) * 1000L );

            queueWaitTime = queueWaitTime - task.getServiceTime ( );
            queueSize--;
            taskGarbage.add ( task );
            synchronized ( taskQueue ) {
                taskQueue.remove ( 0 );
            }

        } catch ( InterruptedException e ) {
            System.out.println ( "Interrupted" );
        }
    }

    @Override
    public void run ( ) {

        stopped = false;
        suspended = false;
        taskQueue.clear ( );
        taskGarbage.clear ( );

        while ( !stopped ) {
            while ( !suspended ) {
                if ( !taskQueue.isEmpty ( ) ) {
                    executeNextTask ( );
                }
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

    public void suspend ( ) {
        suspended = true;
    }

    public void unsuspend ( ) {
        suspended = false;
        synchronized ( lock ) {
            lock.notify ( );
        }
    }

    public void stop ( ) {
        suspended = true;
        stopped = true;
        synchronized ( lock ) {
            lock.notify ( );
        }
    }

}
