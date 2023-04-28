package GUI;

import BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame implements ActionListener {

    SimulationManager simulationManager;

    public void updateFrame ( Integer[] sizes ) {
        for ( int i = 0; i < queuePanels.length; i++ ) {
            queuePanels[i].removeAll ( );
            for ( int j = 0; j < sizes[i]; j++ )
                queuePanels[i].add ( clientPanelGenerator ( ) );
        }
        getContentPane ( ).revalidate ( );
        getContentPane ( ).repaint ( );
    }

    @Override
    public void actionPerformed ( ActionEvent e ) {
        switch ( e.getActionCommand ( ) ) {
            case "Start" -> {
                simulationManager.generateRandomTasks ( );
                activateQueues ( );
                simulationManager.startSimulation ( );
                deactivateQueues ( );
            }
            case "Options" -> {
                optionsDialogue = new SimulationOptionsDialogue ( this );
            }
            case "Apply" -> {
                Integer[] options = optionsDialogue.getSimulationParameters ( );
                simulationManager.setNoTasks ( options[0] );
                simulationManager.setNoServers ( options[1] );
                changeNoQueues ( options[1] );
                simulationManager.setSimulationTime ( options[2] );
                simulationManager.setArrivalInterval ( options[3], options[4] );
                simulationManager.setServiceInterval ( options[5], options[6] );
                optionsDialogue.dispose ( );
            }
        }
    }

    private JPanel mainPanelBuilder ( ) {
        JPanel mainPanel = new JPanel ( new BorderLayout ( ) );
        mainPanel.setBackground ( Color.RED );

        return mainPanel;
    }

    private JPanel queuePanelBuilder ( ) {
        FlowLayout layout = new FlowLayout ( FlowLayout.LEFT, 0, 0 );
        JPanel queuePanel = new JPanel ( layout );

        queuePanel.setBackground ( Color.DARK_GRAY );
        queuePanel.setBorder ( BorderFactory.createLineBorder ( Color.DARK_GRAY ) );
        queuePanel.setPreferredSize ( new Dimension ( 1920, 60 ) );

        return queuePanel;
    }

    private JScrollPane queuesPanelBuilder ( Integer noQueues ) {

        SpringLayout layout = new SpringLayout ( );
        queuesPanel = new JPanel ( layout );

        queuePanels = new JPanel[noQueues];

        for ( int i = 0; i < noQueues; i++ ) {
            queuePanels[i] = queuePanelBuilder ( );
            queuesPanel.add ( queuePanels[i] );
            layout.putConstraint ( SpringLayout.WEST, queuesPanel, 0, SpringLayout.WEST, queuePanels[i] );
            layout.putConstraint ( SpringLayout.EAST, queuesPanel, 0, SpringLayout.EAST, queuePanels[i] );
        }

        layout.putConstraint ( SpringLayout.NORTH, queuesPanel, 0, SpringLayout.NORTH, queuePanels[0] );

        for ( int i = 0; i < noQueues - 1; i++ ) {
            layout.putConstraint ( SpringLayout.NORTH, queuePanels[i + 1], 0, SpringLayout.SOUTH, queuePanels[i] );
        }

        layout.putConstraint ( SpringLayout.SOUTH, queuesPanel, 0, SpringLayout.SOUTH, queuePanels[noQueues - 1] );

        JScrollPane scrollPane = new JScrollPane ( queuesPanel );
        scrollPane.setVerticalScrollBarPolicy ( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setHorizontalScrollBarPolicy ( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scrollPane.setPreferredSize ( new Dimension ( 800, noQueues * 60 + 5 ) );

        return scrollPane;
    }

    private JPanel optionsPanelBuilder ( ) {
        SpringLayout layout = new SpringLayout ( );
        JPanel optionsPanel = new JPanel ( layout );
        optionsPanel.setBackground ( Color.DARK_GRAY );
        optionsPanel.setPreferredSize ( new Dimension ( 300, 60 ) );

        JButton optionsButton = new JButton ( "⚙" );
        optionsButton.setPreferredSize ( new Dimension ( 60, 60 ) );
        optionsButton.setFont ( new Font ( Font.SANS_SERIF, Font.BOLD, 24 ) );
        optionsButton.setBackground ( Color.GRAY );
        optionsButton.setFocusable ( false );
        optionsButton.setActionCommand ( "Options" );
        optionsButton.addActionListener ( this );

        optionsPanel.add ( optionsButton );

        JButton startButton = new JButton ( "▶" );
        startButton.setPreferredSize ( new Dimension ( 60, 60 ) );
        startButton.setFont ( new Font ( Font.SANS_SERIF, Font.BOLD, 24 ) );
        startButton.setBackground ( Color.GRAY );
        startButton.setForeground ( Color.DARK_GRAY );
        startButton.setFocusable ( false );
        startButton.setActionCommand ( "Start" );
        startButton.addActionListener ( this );

        optionsPanel.add ( startButton );

        layout.putConstraint ( SpringLayout.EAST, startButton, 0, SpringLayout.EAST, optionsPanel );

        return optionsPanel;
    }

    private JPanel clientPanelGenerator ( ) {
        JPanel clientPanel = new JPanel ( );
        clientPanel.setBackground ( Color.LIGHT_GRAY );
        clientPanel.setPreferredSize ( new Dimension ( 20, 60 ) );
        return clientPanel;
    }

    private JPanel mainPanel = mainPanelBuilder ( );
    private JPanel queuesPanel;
    private JPanel optionsPanel = optionsPanelBuilder ( );

    private JPanel[] queuePanels;

    private SimulationOptionsDialogue optionsDialogue;

    public SimulationFrame ( Integer noQueues ) {
        simulationManager = new SimulationManager ( this, noQueues );

        setTitle ( "Queue Simulation" );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );

        mainPanel.add ( queuesPanelBuilder ( noQueues ), BorderLayout.CENTER );
        mainPanel.add ( optionsPanel, BorderLayout.NORTH );
        add ( mainPanel );

        pack ( );
        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        setLocation ( new Point ( ( screenSize.width - getWidth ( ) ) / 2, ( screenSize.height - getHeight ( ) ) / 2 ) );
        setVisible ( true );
    }

    private void changeNoQueues ( Integer noQueues ) {
        setVisible ( false );

        optionsPanel = optionsPanelBuilder ( );

        mainPanel.removeAll ( );
        mainPanel.add ( queuesPanelBuilder ( noQueues ), BorderLayout.CENTER );
        mainPanel.add ( optionsPanel, BorderLayout.NORTH );

        pack ( );
        Dimension screenSize = Toolkit.getDefaultToolkit ( ).getScreenSize ( );
        setLocation ( new Point ( ( screenSize.width - getWidth ( ) ) / 2, ( screenSize.height - getHeight ( ) ) / 2 ) );
        setVisible ( true );
    }

    private void activateQueues ( ) {
        for ( JPanel queuePanel : queuePanels ) {
            queuePanel.setBackground ( Color.GRAY );
        }
        getContentPane ( ).revalidate ( );
        getContentPane ( ).repaint ( );
    }

    private void deactivateQueues ( ) {
        for ( JPanel queuePanel : queuePanels )
            queuePanel.setBackground ( Color.DARK_GRAY );
    }

}
