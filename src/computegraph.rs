
use std::f32;
use std::vec;
use std::collections::HashMap;
use std::collections::hash_set::HashSet;

use linearalgebra::Matrix;

type NodeIndex = usize;

enum Operation {
        Input,
	Variable,
        MatrixMultiply(NodeIndex, NodeIndex),
        //MatrixMultiplyTranspose(NodeIndex, NodeIndex), // Second item is the transpose.  More efficient than matmul(a, transpose(b))
        Transpose(NodeIndex),
	SetVariable(NodeIndex, NodeIndex), // Fron/To
        Unary(NodeIndex, Box<Fn(f32)->f32>, Box<Fn(f32)->f32>), // x, f, df/dx
        Binary(NodeIndex, NodeIndex, Box<Fn(f32,f32)->f32>, Box<Fn(f32,f32)->f32>, Box<Fn(f32,f32)->f32>), // LHS, RHS, F, dF/dLHS, dF/dRHS
}

pub struct Node {
	id : NodeIndex,
	name : String, // For debugging.
	operation : Operation,
}

pub struct Graph {
	nodes : Vec<Node>,
	variables : Vec<Matrix>,
}

impl Graph {
	fn new() -> Graph {
		Graph {
			nodes : vec![],
			variables : vec![],
		}
	}

	fn get_output(&self, node : NodeIndex, input_feed : HashMap<NodeIndex, Matrix>) -> Matrix {
		Matrix::new()
	}

	fn get_output_with_cache(&self, node : NodeIndex, input_feed : HashMap<NodeIndex, Matrix>, cached_output : HashMap<NodeIndex, Matrix>) {
	}

	fn get_gradients(&self, node : NodeIndex, input_feed : HashMap<NodeIndex, Matrix>) -> HashMap<NodeIndex, Matrix> {
		HashMap::<NodeIndex, Matrix>::new()
	}
}
