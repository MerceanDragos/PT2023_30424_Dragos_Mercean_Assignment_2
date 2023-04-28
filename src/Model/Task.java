package Model;

import java.util.Comparator;


public class Task {

    private static int IDCounter = 1;

    private final int ID;
    private final int arrivalTime;
    private final int serviceTime;
    private int waitTime;

    public Task ( Integer minArrivalTime, Integer maxArrivalTime, Integer minServiceTime, Integer maxServiceTime ) {
        ID = IDCounter++;
        arrivalTime = ( int ) ( Math.random ( ) * ( maxArrivalTime - minArrivalTime ) + minArrivalTime );
        serviceTime = ( int ) ( Math.random ( ) * ( maxServiceTime - minServiceTime ) + minServiceTime );
    }

    public int getID ( ) {
        return ID;
    }

    public int getArrivalTime ( ) {
        return arrivalTime;
    }

    public int getServiceTime ( ) {
        return serviceTime;
    }

    public void setWaitTime ( int waitTimeParam ) {
        waitTime = waitTimeParam;
    }

    public int getWaitTime ( ) {
        return waitTime;
    }

    public static final Comparator< Task > taskArrivalTimeComparator = Comparator.comparing ( Task::getArrivalTime );

}
