package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SimulationOptionsDialogue extends JDialog {

    private JTextField noClientsTextField = new JTextField ( );
    private JTextField noServersTextField = new JTextField ( );
    private JTextField simulationTimeTextField = new JTextField ( );
    private JTextField minArrivalTimeTextField = new JTextField ( );
    private JTextField maxArrivalTimeTextField = new JTextField ( );
    private JTextField minServiceTimeTextField = new JTextField ( );
    private JTextField maxServiceTimeTextField = new JTextField ( );

    SimulationOptionsDialogue ( ActionListener listener ) {
        setTitle ( "Simulation parameters" );
        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
        setResizable ( false );

        JPanel mainPanel = new JPanel ( new FlowLayout ( FlowLayout.CENTER, 50, 40 ) );
        mainPanel.setBackground ( Color.GRAY );
        mainPanel.setPreferredSize ( new Dimension ( 420, 240 ) );

        noClientsTextField.setPreferredSize ( new Dimension ( 60, 30 ) );
        noServersTextField.setPreferredSize ( new Dimension ( 60, 30 ) );
        simulationTimeTextField.setPreferredSize ( new Dimension ( 60, 30 ) );
        minArrivalTimeTextField.setPreferredSize ( new Dimension ( 50, 30 ) );
        maxArrivalTimeTextField.setPreferredSize ( new Dimension ( 50, 30 ) );
        minServiceTimeTextField.setPreferredSize ( new Dimension ( 50, 30 ) );
        maxServiceTimeTextField.setPreferredSize ( new Dimension ( 50, 30 ) );

        noClientsTextField.setText ( "N" );
        noServersTextField.setText ( "Q" );
        simulationTimeTextField.setText ( "t" );
        minArrivalTimeTextField.setText ( "minA" );
        maxArrivalTimeTextField.setText ( "maxA" );
        minServiceTimeTextField.setText ( "minS" );
        maxServiceTimeTextField.setText ( "maxS" );

        mainPanel.add ( noClientsTextField );
        mainPanel.add ( noServersTextField );
        mainPanel.add ( simulationTimeTextField );
        mainPanel.add ( minArrivalTimeTextField );
        mainPanel.add ( maxArrivalTimeTextField );
        mainPanel.add ( minServiceTimeTextField );
        mainPanel.add ( maxServiceTimeTextField );

        JButton applyButton = new JButton ( "Apply" );
        applyButton.setPreferredSize ( new Dimension ( 80, 50 ) );
        applyButton.setActionCommand ( "Apply" );
        applyButton.addActionListener ( listener );

        mainPanel.add ( applyButton );

        add ( mainPanel );

        pack ( );
        setMinimumSize ( getSize ( ) );
        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        setLocation ( new Point ( ( screenSize.width - getWidth ( ) ) / 2, ( screenSize.height - getHeight ( ) ) / 2 ) );
        setVisible ( true );
    }

    Integer[] getSimulationParameters ( ) {
        Integer[] parametersArray = new Integer[7];

        parametersArray[0] = Integer.valueOf ( noClientsTextField.getText ( ) );
        parametersArray[1] = Integer.valueOf ( noServersTextField.getText ( ) );
        parametersArray[2] = Integer.valueOf ( simulationTimeTextField.getText ( ) );
        parametersArray[3] = Integer.valueOf ( minArrivalTimeTextField.getText ( ) );
        parametersArray[4] = Integer.valueOf ( maxArrivalTimeTextField.getText ( ) );
        parametersArray[5] = Integer.valueOf ( minServiceTimeTextField.getText ( ) );
        parametersArray[6] = Integer.valueOf ( maxServiceTimeTextField.getText ( ) );

        return parametersArray;
    }

}
