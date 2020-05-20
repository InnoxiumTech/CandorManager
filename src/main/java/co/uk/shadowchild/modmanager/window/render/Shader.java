package co.uk.shadowchild.modmanager.window.render;

import co.uk.shadowchild.modmanager.util.Errors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    // Our Shader Program Pointer
    private int program;
    // Our Vertex and Fragment Shaders
    private int vs, fs;

    public Shader(String fileName) {

        program = glCreateProgram();

        // Load and compile our Vertex Shader
        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, createShader(fileName + ".vs.glsl"));
        glCompileShader(vs);
        if(glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {

            System.out.println(glGetShaderInfoLog(vs));
            System.exit(Errors.SHADER_ERROR);
        }

        // Load and compile out Fragment Shader
        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, createShader(fileName + ".fs.glsl"));
        glCompileShader(fs);
        if(glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {

            System.out.println(glGetShaderInfoLog(fs));
            System.exit(Errors.SHADER_ERROR);
        }

        // Attach to our shader program
        glAttachShader(program, vs);
        glAttachShader(program, fs);

        // Bind Attributes
        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "uv");

        // Link and validate program
        glLinkProgram(program);
        if(glGetProgrami(program, GL_LINK_STATUS) != 1) {

            System.out.println(glGetShaderInfoLog(program));
            System.exit(Errors.SHADER_ERROR);
        }
        glValidateProgram(program);
        if(glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {

            System.out.println(glGetShaderInfoLog(program));
            System.exit(Errors.SHADER_ERROR);
        }
    }

    public CharSequence createShader(String fileName) {

        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try {

            br = new BufferedReader(new FileReader(new File("./bin/shaders/" + fileName)));
            String line;
            while((line = br.readLine()) != null) {

                sb.append(line);
                sb.append("\n");
            }
            br.close();
        } catch(IOException e) {

            e.printStackTrace();
        }
        return sb.toString();
    }

    public void bindShader() {

        glUseProgram(program);
    }

    public void setUniform(String val, Colour colour) {

        int location = glGetUniformLocation(program, val);
        if(location != 1) {

            glUniform4f(
                    location,
                    colour.r,
                    colour.g,
                    colour.b,
                    colour.a
                    );
        }
    }

    public void unBindShader() {

        glUseProgram(0);
    }
}
