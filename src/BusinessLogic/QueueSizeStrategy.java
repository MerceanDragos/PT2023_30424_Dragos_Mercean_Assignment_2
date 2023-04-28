package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;

public class QueueSizeStrategy implements Strategy {

    private static Server findEmptiestServer ( ArrayList< Server > servers ) {

        Server bestServer = servers.get ( 0 );

        for ( Server candidate : servers )
            if ( candidate.isEmptierThan ( bestServer ) )
                bestServer = candidate;

        return bestServer;
    }

    @Override
    public void addTask ( ArrayList< Server > servers, Task task ) {

        Server emptiestServer = QueueSizeStrategy.findEmptiestServer ( servers );

        emptiestServer.addTask ( task );
    }
}
