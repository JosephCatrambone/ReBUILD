
use std::ops::{Index, IndexMut};
type Scalar = f32;

#[derive(Clone, Debug)]
pub struct Matrix {
	rows : usize,
	columns : usize,
	data : Vec<Scalar>,
}

impl Matrix {
	fn new() -> Matrix {
		Matrix {
			rows : 0,
			columns : 0,
			data : Vec::<Scalar>::new()
		}
	}

	fn new_from_fn(height : usize, width : usize, f : Box<Fn(usize, usize)->Scalar>) -> Matrix {
		let mut m = Vec::<Scalar>::new();
		for y in (0..height) {
			for x in (0..width) {
				m.push(f(y, x));
			}
		}
		Matrix {
			rows : height,
			columns : width,
			data : m
		}
	}

	fn zeros(height : usize, width : usize) -> Matrix {
		Matrix {
			rows : height,
			columns : width,
			data : vec![0.0 as Scalar; height*width],
		}
	}

	fn identity(height : usize, width : usize) -> Matrix {
		Matrix::new_from_fn(height, width, Box::new(|x, y| { if x == y { 1.0 as Scalar } else { 0.0 as Scalar } }))
	}

	fn size(&self) -> usize {
		self.rows*self.columns
	}

	fn binop_i(&mut self, other : &Matrix, f : Box<Fn(Scalar, Scalar)->Scalar>) {
		for i in (0..self.size() as usize) {
			self.data[i] = f(self.data[i], other[i]);
		}
	}
}

impl Index<usize> for Matrix {
	type Output = Scalar;

	#[inline]
	fn index(&self, index : usize) -> &Scalar {
		&self.data[index]
	}
}

impl Index<(usize, usize)> for Matrix {
	type Output = Scalar;

	#[inline]
	fn index(&self, pos : (usize, usize)) -> &Scalar {
		let (r, c) = pos;
		&self.data[c + r*self.columns]
	}
}

impl IndexMut<usize> for Matrix {
	#[inline]
	fn index_mut<'a>(&'a mut self, index : usize) -> &'a mut Scalar {
		&mut self.data[index]
	}
}

impl IndexMut<(usize, usize)> for Matrix {
	#[inline]
	fn index_mut(&mut self, pos : (usize, usize)) -> &mut Scalar {
		let (r, c) = pos;
		&mut self.data[c + r*self.columns]
	}
}
