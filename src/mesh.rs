
#[derive(Copy, Clone)]
pub struct Vertex3f {
	position : [f32; 3],
	color : [f32; 3],
	normal : [f32; 3],
	uv : [f32; 2],
}

implement_vertex!(Vertex3f, position, color, normal, uv);

pub struct Mesh {
	// Don't worry about location or rotation in the world transform place.  Assume always local.
	vertices : Vec<Vertex3f>,
	faces : Vec<usize>,
	material : usize,
}


