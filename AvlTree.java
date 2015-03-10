package com.tengen;

/**
 * Gurkan Ýllez
 * AVL Tree implementation 
 * Add,Remove functions are provided
 * @author grkn
 *
 * @param <T>
 */
public class AvlTree<T extends Comparable<T>> implements BalancedTree<T>{

	private Node<T> root;
	
	private class Node<T>{
		private T data;
		private Node<T> leftChild = null;
		private Node<T> rightChild = null;
		private Node<T> parent = null;
		private int balance = 0;
		
		public Node(T data,Node<T> left,Node<T> right) {
			this.data = data;
			this.leftChild = left;
			this.rightChild = right;
		}

		public Node<T> getParent() {
			return parent;
		}

		public void setParent(Node<T> parent) {
			this.parent = parent;
		}
		
		protected boolean isLeaf() {
			return leftChild == null && rightChild ==  null;
		}
	}
	
	@Override
	public void add(T val) {
		if(root == null){
			root = new Node<T>(val, null, null);
		}else{
			Node<T> tmp = root;
			Node<T> parent = root;
			while(tmp != null){
				if(tmp.data.compareTo(val) > 0){
					parent = tmp;
					tmp = tmp.leftChild;
				}else
				if(tmp.data.compareTo(val) < 0){
					parent = tmp;
					tmp = tmp.rightChild;
				}
				
			}
			
			if(parent.data.compareTo(val) > 0){
				addToLeftChild(parent,val);
			}else
			if(parent.data.compareTo(val) < 0){
				addToRightChild(parent,val);
			}
			
		}
	}

	private void addToRightChild(Node<T> parent, T val) {
		Node<T> newNode = new Node<T>(val, null, null);
		parent.rightChild = newNode;
		newNode.parent = parent;
		parent.balance = 1;
		reCalculateBalance(parent);
	}

	private void addToLeftChild(Node<T> parent, T val) {
		Node<T> newNode = new Node<T>(val, null, null);
		parent.leftChild = newNode;
		newNode.parent = parent;
		parent.balance = -1;
		reCalculateBalance(parent);
	}
	
	private void evaluateBalance(Node<T> node){
		if(node.balance == 2){
			if(node.rightChild.balance == 1){
				System.out.println("SHIFT LEFT");
				shiftLeft(node.rightChild);
			}
			if(node.rightChild.balance == -1){
				System.out.println("SHIFT RIGHT LEFT");
				shiftRightLeft(node.rightChild);
			}
		}else
		if(node.balance == -2){
			if(node.leftChild.balance == -1){
				System.out.println("SHIFT RIGHT");
				shiftRight(node.leftChild);
			}
			if(node.leftChild.balance == 1){
				System.out.println("SHIFT LEFT RIGHT");
				shiftLeftRight(node.leftChild);
			}
		}
	}
	
	private void reCalculateBalance(Node<T> node){
		while(node != null){	
			int left = findHeight(node.leftChild);
			int right = findHeight(node.rightChild);
			node.balance = right - left;
			evaluateBalance(node);
			node = node.parent;
		}

	}
	
	private int findHeight(Node<T> node) {
		if(node == null)
			return -1;
		return Math.max(findHeight(node.leftChild), findHeight(node.rightChild)) + 1; 
	}

	private void swapDataViaParent(Node<T> node){
		T data = node.data;
		node.data = node.parent.data;
		node.parent.data = data;
	}
	
	private void clearBalances(Node<T> node){
		node.balance = 0;
		node.leftChild.balance = 0;
		node.rightChild.balance = 0;
	}
	
	private void shiftLeft(Node<T> node){
		swapDataViaParent(node);
		Node<T> tmp = node.leftChild;
		node.leftChild = node.parent.leftChild;
		node.parent.leftChild = tmp;
		if(node.leftChild != null)
		node.leftChild.parent = node;
		if(node.parent.leftChild != null)
		node.parent.leftChild.parent = node.parent;
		
		tmp = node.rightChild;
		Node<T> transferChild = node.parent.leftChild;
		node.parent.leftChild = node;
		node.rightChild = null;
		node.parent.rightChild = tmp;
		tmp.parent = node.parent;
		
		node = node.parent.leftChild;
		
		if(transferChild != null){
			while(node.rightChild != null)
				node = node.rightChild;
			transferChild.leftChild = null;
			transferChild.rightChild = null;
			node.rightChild = transferChild;
			transferChild.parent = node;
		}
		clearBalances(tmp.parent);
	}
	
	private void shiftRight(Node<T> node){
		swapDataViaParent(node);
		Node<T> tmp = node.rightChild;
		node.rightChild = node.parent.rightChild;
		node.parent.rightChild = tmp;
		if(node.rightChild != null)
			node.rightChild.parent = node;
		if(node.parent.rightChild != null)
			node.parent.rightChild.parent = node.parent;
		tmp = node.leftChild;
		
		Node<T> transferChild = node.parent.rightChild;
		node.parent.rightChild = node;
		node.leftChild = null;
		node.parent.leftChild = tmp;
		tmp.parent = node.parent;
		node = node.parent.rightChild;
		if(transferChild != null){
			while(node.leftChild != null)
				node = node.leftChild;
			transferChild.leftChild = null;
			transferChild.rightChild = null;
			node.leftChild = transferChild;
			transferChild.parent = node;
		}
		clearBalances(tmp.parent);
	}
	
	private void shiftLeftRight(Node<T> node){
		Node<T> parent = node.parent;
		Node<T> rChild = node.rightChild;
		
		Node<T> transferSubtree = rChild.leftChild;
		
		if(parent.leftChild.equals(node)){
			parent.leftChild = rChild;
			rChild.parent = parent;
		}else{
			parent.rightChild = rChild;
			rChild.parent = parent;
		}
		node.rightChild = null;
		node.parent = rChild;
		rChild.leftChild = node;
		Node<T> lChild = node;
		if(transferSubtree != null){
			while(lChild.rightChild != null)
				lChild = lChild.rightChild;
			lChild.rightChild = transferSubtree;
			transferSubtree.leftChild = null;
			transferSubtree.rightChild = null;
			transferSubtree.parent = lChild;
		}
		node = rChild;

		shiftRight(node);
	}
	
	private void shiftRightLeft(Node<T> node){
		Node<T> parent = node.parent;
		Node<T> lChild = node.leftChild;
		Node<T> transferSubtree = lChild.rightChild;
		if(parent.leftChild.equals(node)){
			parent.leftChild = lChild;
			lChild.parent = parent;
		}else{
			parent.rightChild = lChild;
			lChild.parent = parent;
		}
		node.leftChild = null;
		node.parent = lChild;
		lChild.rightChild = node;
		Node<T> rChild = node;
		if(transferSubtree != null){
			while(rChild.leftChild != null)
				rChild = rChild.leftChild;
			rChild.leftChild = transferSubtree;
			transferSubtree.leftChild = null;
			transferSubtree.rightChild = null;
			transferSubtree.parent = lChild;
		}
		node = lChild;
		shiftLeft(node);
	}
	
	protected void printTreeInOrder(Node<T> node){
		if(node == null)
			return;
		else{
			Node<T> tmp = node.parent;
			if(tmp != null){
				System.out.print(node.data+ " Balance : "+node.balance+" Parent : "+tmp.data);
				System.out.println( " lchild : " + (tmp.leftChild != null ? tmp.leftChild.data : -1) + " rchild : "+(tmp.rightChild != null ? tmp.rightChild.data : -1));
			}else{
				System.out.println(node.data+ " Balance : "+node.balance+" Parent : "+null);
			}
			printTreeInOrder(node.leftChild);
			printTreeInOrder(node.rightChild);
		}
	}
	
	@Override
	public void remove(T val) {
		Node<T> tmp = root;
		while(true){
			if(tmp.data.compareTo(val) < 0)
				tmp = tmp.rightChild;
			else
			if(tmp.data.compareTo(val) > 0)
				tmp = tmp.leftChild;
			else
				if(tmp.data.compareTo(val) == 0){
					break;
				}
			if(tmp == null)
				return;
			
		}
		//remove tmp
		if(tmp.isLeaf())
		{
			if(tmp.parent.rightChild != null && tmp.parent.rightChild.equals(tmp)){
				tmp.parent.rightChild = null;
			}else
			if(tmp.parent.leftChild != null && tmp.parent.leftChild.equals(tmp)){
				tmp.parent.leftChild = null;
			}
			reCalculateBalance(tmp.parent);
		}else{
			//Bigger value will be replaced (My choice maybe lower value can be assigned as well)
			Node<T> rChilds = tmp.rightChild;
			Node<T> lChilds = tmp.leftChild;
			Node<T> parent = tmp.parent;
			if(lChilds != null && rChilds != null){
				Node<T> transferSubtree = rChilds.leftChild;
				if(parent.leftChild.equals(tmp)){
					parent.leftChild = rChilds;
				}else{
					parent.rightChild = rChilds;
				}
				rChilds.parent = parent;
				rChilds.leftChild = tmp.leftChild;
				tmp.leftChild.parent = rChilds;
				if(transferSubtree != null){
					lChilds = rChilds.leftChild;
					while(lChilds.rightChild != null){
						lChilds = lChilds.rightChild;
					}
					lChilds.rightChild = transferSubtree;
					transferSubtree.leftChild = null;
					transferSubtree.rightChild = null;
					transferSubtree.parent = lChilds;
				}
				reCalculateBalance(lChilds);
			}else
			if(lChilds != null && rChilds == null){
				if(parent.leftChild.equals(tmp)){
					parent.leftChild = lChilds;
				}else{
					parent.rightChild = lChilds;
				}
				lChilds.parent = parent;
				reCalculateBalance(lChilds);
			}else{
				if(parent.leftChild.equals(tmp)){
					parent.leftChild = rChilds;
				}else{
					parent.rightChild = rChilds;
				}
				rChilds.parent = parent;
				reCalculateBalance(rChilds);
			}
			
		}
	}

	@Override
	public T get(T val) {
		Node<T> tmp = root;
		while(true){
			if(tmp == null)
				return null;
			if(tmp.data.compareTo(val) < 0)
				tmp = tmp.rightChild;
			else
			if(tmp.data.compareTo(val) > 0)
				tmp = tmp.leftChild;
			else
			if(tmp.data.compareTo(val) == 0){
				break;
			}
		}
		return tmp.data;
	}
	
	public static void main(String[] args) {
		Integer[]arr = {20,19,18,15,12,5,1,13,14,16,17};
		AvlTree<Integer> tree = new AvlTree<Integer>();
		for (Integer integer : arr) {
			System.out.println("INSERTING : "+integer);
			tree.add(integer);
		}
		System.out.println("FINALLL");
		tree.printTreeInOrder(tree.root);
		System.out.println("*****************");
		tree.remove(5);
		tree.printTreeInOrder(tree.root);
		System.out.println("*****************");
		tree.remove(14);
		tree.printTreeInOrder(tree.root);
		System.out.println("*****************");
		tree.remove(12);
		tree.printTreeInOrder(tree.root);
		System.out.println("*****************");
		tree.remove(1);
		tree.printTreeInOrder(tree.root);
		System.out.println(tree.get(100));
//		tree.remove(21);
//		tree.printTreeInOrder(tree.root);
//		tree.remove(18);
//		tree.printTreeInOrder(tree.root);
//		tree.remove(19);
//		tree.printTreeInOrder(tree.root);
		
		
	}

}
