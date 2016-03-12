package net.jselby.escapists.game.desktop;

import com.badlogic.gdx.Gdx;
import net.jselby.escapists.DebugFrame;
import net.jselby.escapists.EscapistsRuntime;
import net.jselby.escapists.NodeProxy;
import net.jselby.escapists.data.chunks.Events;
import net.jselby.escapists.data.events.interpreter.InterpreterActionConditionGroup;
import net.jselby.escapists.data.events.interpreter.InterpreterEventGroup;
import net.jselby.escapists.game.EscapistsGame;
import net.jselby.escapists.game.Layer;
import net.jselby.escapists.game.Scene;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.ArrayList;

/**
 * @see net.jselby.escapists.DebugFrame
 */
public class DesktopDebugFrame extends DebugFrame {
    private static final String START_ROW = "<tr><td>";
    private static final String MID_ROW = "</td><td>";
    private static final String END_ROW = "</td></tr>";

    private JFrame frame;

    private JTextPane stats;

    private JScrollPane eventsPanel;
    private JTree eventsTree = new JTree(
            new DefaultMutableTreeNode("<root>"));

    private JTable variablesPanel;

    private Scene scene;
    private EscapistsGame game;
    private InterpreterActionConditionGroup[] events;
    private boolean tickSwitch = false;

    @Override
    public void start() {
        frame = new JFrame("Debug View");

        frame.setMinimumSize(new Dimension(300, 200));
        frame.setSize(new Dimension(300, Display.getDisplayMode().getHeight() + 10));
        frame.setLocation(new Point(Display.getX() + Display.getWidth() + 10, Display.getY()));

        frame.setResizable(true);
        frame.setAlwaysOnTop(true);

        JTabbedPane pane = new JTabbedPane();
        pane.add("Stats", stats = new JTextPane());
        pane.add("Events", eventsPanel = new JScrollPane(eventsTree));

        pane.add("Variables", new JScrollPane(variablesPanel = new JTable(new TableModel() {
            @Override
            public int getRowCount() {
                return scene == null ? 0 : scene.getVariables().size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return columnIndex == 0 ? "Key" : "Value";
            }

            public String getRowName(int rowIndex) {
                return (String) scene.getVariables().keySet().toArray()[rowIndex];
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 1;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return columnIndex == 0 ? getRowName(rowIndex) : scene.getVariables().get(getRowName(rowIndex));
            }

            @Override
            public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
                if (columnIndex != 1) {
                    return;
                }
                scene.getVariables().put(getRowName(columnIndex), newValue);
            }

            @Override
            public void addTableModelListener(TableModelListener l) {}

            @Override
            public void removeTableModelListener(TableModelListener l) {}
        })));

        eventsTree.setRootVisible(false);

        stats.setEditable(false);
        stats.setContentType("text/html");

        frame.add(pane);

        frame.setVisible(true);
    }

    @Override
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void setEventsForScene(InterpreterActionConditionGroup[] events) {
        this.events = events;

        DefaultMutableTreeNode top =
                new DefaultMutableTreeNode("<root>");
        iterateEventStack(top, events);

        eventsTree = new JTree(top);
        int index = 1;
        while(index <= eventsTree.getRowCount()) {
            eventsTree.expandRow(index);
            index++;
        }
        eventsPanel.setViewportView(eventsTree);
    }

    public void iterateEventStack(DefaultMutableTreeNode root, InterpreterActionConditionGroup[] events) {
        for (InterpreterActionConditionGroup group : events) {
            if (group instanceof InterpreterEventGroup) {
                ArrayList<InterpreterActionConditionGroup> newEvents = ((InterpreterEventGroup) group).getChildren();
                InterpreterActionConditionGroup[] newEventsArray = new InterpreterActionConditionGroup[newEvents.size()];

                DefaultMutableTreeNode newTree =
                        new DefaultMutableTreeNode("Group #" + ((InterpreterEventGroup) group).getId());
                root.add(newTree);

                iterateEventStack(newTree, newEvents.toArray(newEventsArray));
            } else {
                DefaultMutableTreeNode newTree =
                        new DefaultMutableTreeNode("Statement");

                DefaultMutableTreeNode conditions =
                        new DefaultMutableTreeNode("Conditions");
                DefaultMutableTreeNode actions =
                        new DefaultMutableTreeNode("Actions");

                assert group.getGroup() != null;

                for (final Events.Condition condition : group.getGroup().conditions) {
                    final DefaultMutableTreeNode conditionNode = new DefaultMutableTreeNode("NONE: " + condition.name);
                    group.addConditionNode(condition, new NodeProxy() {
                        @Override
                        public void setState(boolean state) {
                            conditionNode.setUserObject((state ? "PASS" : "FAIL") + ": " + condition.name);
                        }
                    });
                    conditions.add(conditionNode);
                }

                for (Events.Action action : group.getGroup().actions) {
                    actions.add(new DefaultMutableTreeNode(action.name));
                }

                newTree.add(conditions);
                newTree.add(actions);

                root.add(newTree);
            }
        }
    }

    public void tick(EscapistsGame game) {
        if (frame == null) {
            return;
        }

        tickSwitch = !tickSwitch;
        if (tickSwitch) {
            return;
        }

        this.game = game;

        // Update stats panel
        Layer[] layersInScene = scene.getLayers();
        String layers = "";
        for (int i = 0; i < layersInScene.length; i++) {
            Layer layer = layersInScene[i];
            if (layer.isVisible()) {
                if (layers.length() > 0) {
                    layers += ", ";
                }
                layers += i + " (" + layer.getName() + ")";
            }
        }

        int usedMem = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;

        stats.setText(
                "Escapists Runtime v" + EscapistsRuntime.VERSION + " on " + Gdx.app.getType().name() +
                "<table>" +
                START_ROW + "FPS: " +                         MID_ROW + Gdx.graphics.getFramesPerSecond() + END_ROW + "\n" +
                START_ROW + "TPS: " +                         MID_ROW + game.lastTPS + END_ROW + "\n" +
                START_ROW + "Memory: "+                         MID_ROW + usedMem + " MB" + END_ROW + "\n" +
                START_ROW + "Mouse X: " +                     MID_ROW + game.getMouseX() + END_ROW + "\n" +
                START_ROW + "Mouse Y: " +                     MID_ROW + game.getMouseY() + END_ROW + "\n\n" +
                START_ROW + "Scene: " +                       MID_ROW + scene.getName() + END_ROW + "\n" +
                START_ROW + "Instance count: " +              MID_ROW + scene.getObjects().size() + END_ROW + "\n" +
                START_ROW + "Visible layers: " +              MID_ROW + "[" + layers + "]" + END_ROW + "\n" +
                START_ROW + "Event Handler implementation: "+ MID_ROW + scene.eventTicker.getClass().getName() + END_ROW +
                "</table>");

        // Update variables
        variablesPanel.updateUI();
    }
}
