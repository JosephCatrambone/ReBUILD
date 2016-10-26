
use glium::backend::Facade;
use glium::vertex::VertexBuffer;

#[derive(Copy, Clone)]
pub struct Vertex3f {
	position : [f32; 3],
	color : [f32; 3],
	normal : [f32; 3],
	uv : [f32; 2],
}

implement_vertex!(Vertex3f, position, color, normal, uv);

/*
let data = &[
    MyVertex {
        position: [0.0, 0.0, 0.4],
        texcoords: [0.0, 1.0]
    },
    MyVertex {
        position: [12.0, 4.5, -1.8],
        texcoords: [1.0, 0.5]
    },
    MyVertex {
        position: [-7.124, 0.1, 0.0],
        texcoords: [0.0, 0.4]
    },
];

let vertex_buffer = glium::vertex::VertexBuffer::new(&display, data);

// drawing with a single vertex buffer
frame.draw(&vertex_buffer, &indices, &program, &uniforms, &Default::default()).unwrap();

// drawing with two parallel vertex buffers
frame.draw((&vertex_buffer, &vertex_buffer2), &indices, &program,
           &uniforms, &Default::default()).unwrap();

// drawing without a vertex source
frame.draw(glium::vertex::EmptyVertexAttributes { len: 12 }, &indices, &program,
           &uniforms, &Default::default()).unwrap();

// drawing a slice of a vertex buffer
frame.draw(vertex_buffer.slice(6 .. 24).unwrap(), &indices, &program,
           &uniforms, &Default::default()).unwrap();

// drawing slices of two vertex buffers
frame.draw((vertex_buffer.slice(6 .. 24).unwrap(), vertex_buffer2.slice(128 .. 146).unwrap()),
           &indices, &program, &uniforms, &Default::default()).unwrap();

// treating `vertex_buffer2` as a source of attributes per-instance instead of per-vertex
frame.draw((&vertex_buffer, vertex_buffer2.per_instance().unwrap()), &indices,
           &program, &uniforms, &Default::default()).unwrap();

// instancing without any per-instance attribute
frame.draw((&vertex_buffer, glium::vertex::EmptyInstanceAttributes { len: 36 }), &indices,
           &program, &uniforms, &Default::default()).unwrap();
*/

pub struct Mesh {
	// Don't worry about location or rotation in the world transform place.  Assume always local.
	vertices : Vec<Vertex3f>,
	faces : Vec<usize>,
	material : usize,
}

pub struct MeshRenderer<'a> {
	display : &'a Facade,
	/*
	GLuint vbo;
	glGenBuffers(1, &vbo); // Generate 1 buffer
	glBindBuffer(GL_ARRAY_BUFFER, vbo); // Set active buffer.
	glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW); // Copy data.
	// GL_STATIC_DRAW -> Upload once and draw a bunch.
	// GL_DYNAMIC_DRAW -> Change rarely.  Draw a bunch.
	// GL_STREAM_DRAW -> Change basically every frame.  UI stuff.
	*/
	vertex_buffer : f32, // Array of vertices on the GPU.
	faces : i32, // Indices of the objects.

	// glium::index::NoIndices(glium::index::PrimitiveType::TrianglesList)
	// vertex_buffer = glium::VertexBuffer::new(&display, &shape).unwrap()
	// target.draw(&vertex_buffer, &indices, &program, &glium::uniforms::EmptyUniform, &Default::default()).unwrap()
}


impl<'a> MeshRenderer<'a> {
	pub fn new(display_reference : &'a Facade) -> MeshRenderer<'a> {
		MeshRenderer {
			display : display_reference,
			vertex_buffer : 2f32,
			faces : 1,

		}
	}
}