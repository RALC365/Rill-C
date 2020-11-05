/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.abego.treelayout.demo.TextInBox;

/**
 *
 * @author Usuario
 */
public class JtreeToDefaultTreeforTreeLayout {

    private DefaultTreeModel oldTree = null;
    private DefaultTreeForTreeLayout<TextInBox> newTree = null;
    private TextInBox actualRoot;

    public JtreeToDefaultTreeforTreeLayout(DefaultTreeModel oldtree) {
        this.oldTree = oldtree;
    }

    public DefaultTreeForTreeLayout<TextInBox> getDefaultTreeForTreeLayoutfromJtree() {
        actualRoot = new TextInBox(oldTree.getRoot().toString(), (oldTree.getRoot().toString().length() * 9), 20);
        newTree = new DefaultTreeForTreeLayout<TextInBox>(actualRoot);
        //Convert((DefaultMutableTreeNode) oldTree.getRoot());
        convert(oldTree, oldTree.getRoot());
        return newTree;
    }

    private void convert(DefaultTreeModel model, Object o) {
        int cc = model.getChildCount(o);
        for (int i = 0; i < cc; i++) {
            Object child = model.getChild(o, i);
            if (model.isLeaf(child)) {
                int x = 9;
                if ((child.toString().length()) < 5) {
                    x = 15;
                }
                TextInBox node = new TextInBox(child.toString(), (child.toString().length() * x), 20);
                newTree.addChild(actualRoot, node);
            } else {
                int x = 9;
                if ((child.toString().length()) < 5) {
                    x = 15;
                }
                TextInBox node = new TextInBox(child.toString(), (child.toString().length() * x), 20);
                newTree.addChild(actualRoot, node);
                actualRoot = node;
                convert(model, child);
                actualRoot = newTree.getParent(node);
            }
        }
    }
}
