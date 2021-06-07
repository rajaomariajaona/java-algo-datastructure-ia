package mg.jaona.datastructure.tree;

public class TreeDFS {
    @FunctionalInterface
    public interface DFSCallback<T>{
        void compute(T value);
    }
    public static <T> void inorder(Tree<T> node, DFSCallback<T> callback){
        if(node.getChildren().isEmpty()){
            callback.compute(node.getValue());
        }else{
            for (Tree<T> t : node.getChildren()) {
                TreeDFS.inorder(t, callback);
                callback.compute(node.getValue());
            }
        }
    }
}
