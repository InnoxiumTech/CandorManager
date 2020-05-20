package co.uk.shadowchild.modmanager.window.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    // UV Id's
    private int u_id, v_id;
    // Vertex Array Object
    private int vao;

    // Verts and Uvs before allocation
    private float[] verts, uvs;

    public Mesh(float[] verts, float[] uvs) {

        this.verts = verts;
        this.uvs = uvs;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(verts), GL_STATIC_DRAW);
        glVertexAttribPointer(0, verts.length / 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        u_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, u_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(uvs), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, u_id);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, 6);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private FloatBuffer createBuffer(float[] data) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public void cleanUp() {

        glDeleteVertexArrays(vao);
        glDeleteBuffers(v_id);
    }
}
