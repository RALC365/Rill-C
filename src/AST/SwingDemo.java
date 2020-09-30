package AST;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.demo.TextInBoxNodeExtentProvider;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

/**
 * Demonstrates how to use the {@link TreeLayout} to render a tree in a Swing
 * application.
 * <p>
 * Intentionally the sample code is kept simple. I.e. it does not include stuff
 * like anti-aliasing and other stuff one would add to make the output look
 * nice.
 * <p>
 * Screenshot:
 * <p>
 * <img src="doc-files/swingdemo.png" alt="A tree rendered using Swing">
 *
 * @author Will
 */
public class SwingDemo {
    public DefaultTreeModel root;
    private void showInDialog(JComponent panel) {
        JDialog dialog = new JDialog();
        JScrollPane scrollPane = new JScrollPane();
        dialog.setBounds(100, 100, 1300, 700);
        dialog.getContentPane().setLayout(new BorderLayout(0, 0));
        dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(panel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        //full screen
        dialog.setLocationRelativeTo(dialog);
        dialog.setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT));
        dialog.setVisible(true);
    }
    public SwingDemo(DefaultTreeModel root){
        this.root = root;
    }

    /**
     * Shows a dialog with a tree in a layout created by {@link TreeLayout},
     * using the Swing component {@link TextInBoxTreePane}.
     *
     */
    public void showTree() {

        //Convertidor
        JtreeToDefaultTreeforTreeLayout convert = new JtreeToDefaultTreeforTreeLayout(this.root);
        
        //Obtener arbol Convertido
        DefaultTreeForTreeLayout<TextInBox> tree = convert.getDefaultTreeForTreeLayoutfromJtree();

        // setup the tree layout configuration
        double gapBetweenLevels = 50;
        double gapBetweenNodes = 10;
        DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<>(
                gapBetweenLevels, gapBetweenNodes);

        // create the NodeExtentProvider for TextInBox nodes
        TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

        // create the layout
        TreeLayout<TextInBox> treeLayout = new TreeLayout<>(tree,
                nodeExtentProvider, configuration);

        // Create a panel that draws the nodes and edges and show the panel
        TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
        showInDialog(panel);
    }
}

