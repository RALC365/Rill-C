package AST;

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.SampleTreeFactory;
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
        Container contentPane = dialog.getContentPane();
        ((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
                10, 10, 10, 10));
        contentPane.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    public SwingDemo(DefaultTreeModel root){
        this.root = root;
    }

    /**
     * Shows a dialog with a tree in a layout created by {@link TreeLayout},
     * using the Swing component {@link TextInBoxTreePane}.
     *
     * @param args args[0]: treeName (default="")
     */
    public  void showTree() {
        //arbol JTREE
        DefaultMutableTreeNode nodoroot, nodo1, nodo11, nodo12, nodo2, nodo21, nodo22;
        nodoroot = new DefaultMutableTreeNode("Rootksdfksdfmsdf,m");
        nodo1 = new DefaultMutableTreeNode("nodo1");
        nodo11 = new DefaultMutableTreeNode("nodo11");
        nodo12 = new DefaultMutableTreeNode("nodo12");
        nodo2 = new DefaultMutableTreeNode("nodo2");
        nodo21 = new DefaultMutableTreeNode("nodo21");
        nodo22 = new DefaultMutableTreeNode("nodo22");
        nodoroot.add(nodo1);
        nodoroot.add(nodo2);
        nodo1.add(nodo11);
        nodo1.add(nodo12);
        nodo2.add(nodo21);
        nodo2.add(nodo22);
        DefaultTreeModel oldTree = new DefaultTreeModel(nodoroot);
        
        //Convertidor
        JtreeToDefaultTreeforTreeLayout convert = new JtreeToDefaultTreeforTreeLayout(this.root);
        
        //Obtener arbol Convertido
        DefaultTreeForTreeLayout<TextInBox> tree = convert.getDefaultTreeForTreeLayoutfromJtree();

        // setup the tree layout configuration
        double gapBetweenLevels = 50;
        double gapBetweenNodes = 10;
        DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
                gapBetweenLevels, gapBetweenNodes);

        // create the NodeExtentProvider for TextInBox nodes
        TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

        // create the layout
        TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
                nodeExtentProvider, configuration);

        // Create a panel that draws the nodes and edges and show the panel
        TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
        showInDialog(panel);
    }
}

