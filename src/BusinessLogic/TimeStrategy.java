package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;

public class TimeStrategy implements Strategy {

    private static Server findFastestServer ( ArrayList< Server > servers ) {

        Server bestServer = servers.get ( 0 );

        for ( Server candidate : servers )
            if ( candidate.isFasterThan ( bestServer ) )
                bestServer = candidate;

        return bestServer;
    }

    @Override
    public void addTask ( ArrayList< Server > servers, Task task ) {

        Server fastestServer = TimeStrategy.findFastestServer ( servers );

        fastestServer.addTask ( task );
    }
}
