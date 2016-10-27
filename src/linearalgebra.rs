
/*
// Issue #17056 -- externs are ignored in submodules.
#![feature(collections)]
#![feature(collections_range)]
extern crate collections;
*/

use std::ops::{Index, IndexMut, Range}; // Possible Range from here.
//use collections::range::RangeArgument; // When finally supported.

use rayon::prelude::*;

pub type Scalar = f32;

#[derive(Clone, Debug)]
pub struct Matrix {
	rows : usize,
	columns : usize,
	data : Vec<Scalar>,
}

impl Matrix {
	pub fn new() -> Matrix {
		Matrix {
			rows : 0,
			columns : 0,
			data : Vec::<Scalar>::new()
		}
	}

	pub fn new_from_fn(height : usize, width : usize, f : Box<Fn(usize, usize)->Scalar>) -> Matrix {
		let mut m = Vec::<Scalar>::new();
		for y in 0..height {
			for x in 0..width {
				m.push(f(y, x));
			}
		}
		Matrix {
			rows : height,
			columns : width,
			data : m
		}
	}

	pub fn zeros(height : usize, width : usize) -> Matrix {
		Matrix {
			rows : height,
			columns : width,
			data : vec![0.0 as Scalar; height*width],
		}
	}

	pub fn identity(height : usize, width : usize) -> Matrix {
		Matrix::new_from_fn(height, width, Box::new(|x, y| { if x == y { 1.0 as Scalar } else { 0.0 as Scalar } }))
	}

	pub fn get_slice(&self, rows : Range<isize>, columns : Range<isize>) -> Matrix {
		let rows_start = rows.start as usize;
		let rows_end = if rows.end >= 0 { rows.end as usize } else { (self.rows as isize+1+rows.end) as usize };
		let columns_start = columns.start as usize;
		let columns_end = if columns.end >= 0 { rows.end as usize } else { (self.columns as isize+1+columns.end) as usize };
		let mut m = Matrix::zeros(rows_end - rows_start, columns_end - columns_start);
		for r in rows_start..rows_end {
			for c in columns_start..columns_end {
				m[(r-rows_start, c-columns_start)] = self[(r, c)];
			}
		}
		m
	}

	pub fn set_slice(&mut self, rows : Range<isize>, columns : Range<isize>, values : &Matrix) {
		let rows_start = rows.start as usize;
		let rows_end = if rows.end >= 0 { rows.end as usize } else { (self.rows as isize+1+rows.end) as usize };
		let columns_start = columns.start as usize;
		let columns_end = if columns.end >= 0 { rows.end as usize } else { (self.columns as isize+1+columns.end) as usize };
		for r in rows_start..rows_end {
			for c in columns_start .. columns_end { // If we were using the range, we'd have to do columns.clone() on the inner loop.
				self[(r,c)] = values[(r-rows_start, c-columns_start)];
			}
		}
	}

	pub fn shape(&self) -> (usize, usize) {
		(self.rows, self.columns)
	}

	pub fn size(&self) -> usize {
		self.rows*self.columns
	}

	pub fn width(&self) -> usize {
		self.columns
	}

	pub fn height(&self) -> usize {
		self.rows
	}

	pub fn binop(lhs : &Matrix, rhs : &Matrix, target : &mut Matrix, f : Box<Fn(Scalar, Scalar)->Scalar>) {
		// TODO: Implement parallel with Rayon.
		//target.par_iter_mut().for_each(move |p| *p
		for i in 0..target.size() as usize {
			target.data[i] = f(lhs[i], rhs[i]);
		}
	}

	pub fn binop_i(&mut self, other : &Matrix, f : Box<Fn(Scalar, Scalar)->Scalar>) {
		for i in 0..self.size() as usize {
			self.data[i] = f(self.data[i], other[i]);
		}
	}

	pub fn unop_i(&mut self, f : Box<Fn(Scalar)->Scalar>) {
		//self.data.par_iter_mut().for_each(|p| *p = f(*p));
		for i in 0..self.size() as usize {
			self.data[i] = f(self.data[i]);
		}
	}

	pub fn transpose(&self) -> Matrix {
		let mut mt = Matrix::zeros(self.columns, self.rows);
		for r in 0..self.rows {
			for c in 0..self.columns {
				mt[(c,r)] = self[(r,c)];
			}
		}
		mt
	}

	pub fn matmul(&self, rhs : &Matrix) -> Matrix {
		//#![cfg_attr(linearalgebra, parallel)]
		// TODO: This does two memory allocations, one to set out the zeros, one to apply the values.  Wonder if there's a way to do it with just one mem access.
		let mut target = Matrix::zeros(self.rows, rhs.columns);
		for r in 0..self.rows as usize {
			for c in 0..rhs.columns as usize {
				let mut accumulator = 0.0 as Scalar;
				for k in 0..self.columns as usize {
					accumulator += self[(r,k)]*rhs[(k,c)];
				}
				target[(r,c)] = accumulator;
			}
		}
		target
		/*
		#[cg_attr(linearalgebra, parallel)]
		let mut target_data = vec![0.0; self.rows*rhs.columns];
		let target_columns = rhs.columns;
		(0 as usize..target_data.len()).collect().par_iter().for_each(|index| {
			let r = index % target_columns;
			let c = index / target_columns;
			let mut accumulator = 0.0 as Scalar;
			for k in 0..self.columns {
				accumulator += self[(r,k)]*rhs[(k,c)];
			}
			target_data[c + r*target_columns] = accumulator;
		});
		Matrix {
			rows : self.rows,
			columns : self.columns,
			data : target_data
		}
		*/
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

//#[test]

#[cfg(test)]
mod tests {
	use super::*;
	
	#[test]
	fn test_access() {
		let i = Matrix::identity(4,5);
		assert_eq!(i[0], 1.0);
		assert_eq!(i[1], 0.0);
		assert_eq!(i[(1, 1)], 1.0);
	}


	#[test]
	fn check_transpose() {
		let mut i = Matrix::zeros(2, 3);
		for j in 0..6 {
			i[j] = j as f32;
		}

		let it = i.transpose();
		assert_eq!(it.height(), i.width());
		assert_eq!(it.width(), i.height());
		assert_eq!(it[(0, 0)], i[(0, 0)]);
		assert_eq!(it[(0, 1)], i[(1, 0)]);
		assert_eq!(it[(1, 0)], i[(0, 1)]);

	}

	#[test]
	fn identity_test() {
		let mut i = Matrix::zeros(5, 3);
		let j = Matrix::identity(3, 3);

		i[(0,0)] = -1.0;
		i[(1,2)] = 4.0;
		i[(3,2)] = 3.1415;

		let k = i.matmul(&j);

		assert!(i.data == k.data);
	}

	#[test]
	fn test_range() {
		let mut a = Matrix::new_from_fn(5, 5, Box::new(|i,j| { (i*j) as f32 }));
		let mut b = Matrix::new_from_fn(10, 10, Box::new(|i, j| { (i*j) as f32 }));
		assert!(a.get_slice((1..-1), (1..-1)).data == b.get_slice((1..5), (1..5)).data);
	}
}

