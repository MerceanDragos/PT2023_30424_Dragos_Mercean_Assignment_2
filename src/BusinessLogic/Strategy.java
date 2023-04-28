package BusinessLogic;

import java.util.ArrayList;

import Model.Server;
import Model.Task;

public interface Strategy {

    public void addTask ( ArrayList< Server > servers, Task task );
}
