package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import java.awt.Color;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import com.google.common.eventbus.Subscribe;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Shader extends Module {
    public enum ShaderType {
        SHADER_6385281682528407296,
        SHADER_MINUS_6862787127220354815,
        SHADER_MINUS_7183123058599273620,
        SHADER_MINUS_8099616702159452490,
        SHADER_1539974198544823836,
        SHADER_2138326405829089144,
        SHADER_217053583388222802
    }

    public final Setting<ShaderType> shaderType = this.register(new Setting<>("ShaderType", ShaderType.SHADER_6385281682528407296));
    public final Setting<Boolean> fastLines = this.register(new Setting<>("FastLines", false));
    public final Setting<Float> glow = this.register(new Setting<>("Glow", 1.0f, 0.0f, 5.0f));
    public final Setting<Integer> width = this.register(new Setting<>("Width", 2, 0, 10));
    // public final Setting<Color> fillColor = this.register(new Setting<>("FillColor", Color.WHITE));
    // public final Setting<Color> outlineColor = this.register(new Setting<>("OutlineColor", Color.BLACK));
    public final Setting<Boolean> decorator = this.register(new Setting<>("Decorator", false)); // Controls Dots
    public final Setting<Integer> dots = this.register(new Setting<>("Dots", 0, 0, 2));
    public final Setting<Integer> dotsRadius = this.register(new Setting<>("DotsRadius", 2, 1, 10));
    public final Setting<Float> dotsAlpha = this.register(new Setting<>("DotsAlpha", 1.0f, 0.0f, 1.0f));
    // public final Setting<Color> fillColor2 = this.register(new Setting<>("FillColor2", Color.WHITE));
    // public final Setting<Color> outlineColor2 = this.register(new Setting<>("OutlineColor2", Color.BLACK));
    public final Setting<Float> step = this.register(new Setting<>("Step", 1.0f, 0.0f, 10.0f));
    public final Setting<Float> time = this.register(new Setting<>("Time", 0.0f, 0.0f, 1000.0f));
    public final Setting<Float> fillOffset = this.register(new Setting<>("FillOffset", 0.0f, -1.0f, 1.0f));
    public final Setting<Float> fillStrength = this.register(new Setting<>("FillStrength", 0.0f, 0.0f, 10.0f));
    public final Setting<Float> outlineOffset = this.register(new Setting<>("OutlineOffset", 0.0f, -1.0f, 1.0f));
    public final Setting<Float> outlineStrength = this.register(new Setting<>("OutlineStrength", 0.0f, 0.0f, 10.0f));
    public final Setting<Integer> radius = this.register(new Setting<>("Radius", 0, 0, 100));
    public final Setting<Integer> glowQuality = this.register(new Setting<>("GlowQuality", 1, 1, 10));
    public final Setting<Boolean> image = this.register(new Setting<>("Image", false));
    public final Setting<Float> overlayAlpha = this.register(new Setting<>("OverlayAlpha", 1.0f, 0.0f, 1.0f));
    public final Setting<Integer> shapeMode = this.register(new Setting<>("ShapeMode", 0, 0, 1));
    public final Setting<Boolean> player = this.register(new Setting<>("Player", true));
    public final Setting<Boolean> hand = this.register(new Setting<>("Hand", true));
    public final Setting<Boolean> item = this.register(new Setting<>("Item", true));
    public final Setting<Boolean> crystal = this.register(new Setting<>("Crystal", true));
    // ... diğer ayarlar ...

    private int shaderProgramId = 0;
    private int vaoId = 0;
    private int vboId = 0;

    public Shader() {
        super("Shader", "Renders custom shaders with configurable parameters.", Category.RENDER, true, false, false);
    }

    private String readShaderSourceFromFile(String resourcePath) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) return "";
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getFragmentShaderPath() {
        switch (this.shaderType.getValue()) {
            case SHADER_6385281682528407296:
                return "shader/6385281682528407296.fsh";
            case SHADER_MINUS_6862787127220354815:
                return "shader/-6862787127220354815.fsh";
            case SHADER_MINUS_7183123058599273620:
                return "shader/-7183123058599273620.fsh";
            case SHADER_MINUS_8099616702159452490:
                return "shader/-8099616702159452490.fsh";
            case SHADER_1539974198544823836:
                return "shader/1539974198544823836.fsh";
            case SHADER_2138326405829089144:
                return "shader/2138326405829089144.fsh";
            case SHADER_217053583388222802:
                return "shader/217053583388222802.fsh";
            default:
                return "shader/6385281682528407296.fsh";
        }
    }

    private void loadShader() {
        String vertexSource = "#version 330 core\n" +
                "layout(location = 0) in vec2 aPos;\n" +
                "void main() {\n" +
                "    gl_Position = vec4(aPos, 0.0, 1.0);\n" +
                "}";
        String fragmentSource = readShaderSourceFromFile(getFragmentShaderPath());
        if (fragmentSource.isEmpty()) {
            fragmentSource = "#version 330 core\nout vec4 color;\nvoid main() { color = vec4(1.0, 0.0, 0.0, 1.0); }";
        }
        // TODO: fragmentSource'u dosyadan oku ve yukarıya ata

        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexShader, vertexSource);
        GL20.glCompileShader(vertexShader);

        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentShader, fragmentSource);
        GL20.glCompileShader(fragmentShader);

        shaderProgramId = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgramId, vertexShader);
        GL20.glAttachShader(shaderProgramId, fragmentShader);
        GL20.glLinkProgram(shaderProgramId);

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        float[] vertices = {
            -1f, -1f,
             1f, -1f,
             1f,  1f,
            -1f,  1f
        };
        vaoId = GL30.glGenVertexArrays();
        vboId = GL20.glGenBuffers();
        GL30.glBindVertexArray(vaoId);
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboId);
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertices, GL20.GL_STATIC_DRAW);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
        GL30.glBindVertexArray(0);
    }

    public int getShaderProgramId() {
        return shaderProgramId;
    }

    public void setShaderUniforms() {
        GL20.glUseProgram(shaderProgramId);
        int loc;
        // Main uniforms
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_FastLines");
        if (loc != -1) GL20.glUniform1i(loc, this.fastLines.getValue() ? 1 : 0);
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_GlowMultiplier");
        if (loc != -1) GL20.glUniform1f(loc, this.glow.getValue());
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Width");
        if (loc != -1) GL20.glUniform1i(loc, this.width.getValue());
        // loc = GL20.glGetUniformLocation(shaderProgramId, "u_FillColor");
        // if (loc != -1) {
        //     Color c = this.fillColor.getValue();
        //     GL20.glUniform4f(loc, c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
        // }
        // loc = GL20.glGetUniformLocation(shaderProgramId, "u_OutlineColor");
        // if (loc != -1) {
        //     Color c = this.outlineColor.getValue();
        //     GL20.glUniform4f(loc, c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
        // }
        // Extra colors
        // loc = GL20.glGetUniformLocation(shaderProgramId, "u_FillColor2");
        // if (loc != -1) {
        //     Color c = this.fillColor2.getValue();
        //     GL20.glUniform4f(loc, c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
        // }
        // loc = GL20.glGetUniformLocation(shaderProgramId, "u_OutlineColor2");
        // if (loc != -1) {
        //     Color c = this.outlineColor2.getValue();
        //     GL20.glUniform4f(loc, c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
        // }
        // Dots/Decorator
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Dots");
        if (loc != -1) GL20.glUniform1i(loc, this.decorator.getValue() ? this.dots.getValue() : 0);
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_DotsRadius");
        if (loc != -1) GL20.glUniform1i(loc, this.dotsRadius.getValue());
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_DotsAlpha");
        if (loc != -1) GL20.glUniform1f(loc, this.dotsAlpha.getValue());
        // Gradient/animation
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Step");
        if (loc != -1) GL20.glUniform1f(loc, this.step.getValue());
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Time");
        if (loc != -1) GL20.glUniform1f(loc, this.time.getValue());
        // Fill/Outline offsets/strengths
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Fill_Offset");
        if (loc != -1) GL20.glUniform1f(loc, this.fillOffset.getValue());
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Fill_Strength");
        if (loc != -1) GL20.glUniform1f(loc, this.fillStrength.getValue());
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Outline_Offset");
        if (loc != -1) GL20.glUniform1f(loc, this.outlineOffset.getValue());
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Outline_Strength");
        if (loc != -1) GL20.glUniform1f(loc, this.outlineStrength.getValue());
        // Radius
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Radius");
        if (loc != -1) GL20.glUniform1i(loc, this.radius.getValue());
        // Glow quality
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_GlowQuality");
        if (loc != -1) GL20.glUniform1i(loc, this.glowQuality.getValue());
        // Image/overlay
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_Image");
        if (loc != -1) GL20.glUniform1i(loc, this.image.getValue() ? 1 : 0);
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_OverlayAlpha");
        if (loc != -1) GL20.glUniform1f(loc, this.overlayAlpha.getValue());
        // Shape mode
        loc = GL20.glGetUniformLocation(shaderProgramId, "u_ShapeMode");
        if (loc != -1) GL20.glUniform1i(loc, this.shapeMode.getValue());
    }

    // Singleton instance for mixin/entity render hooks
    private static Shader INSTANCE;
    public static Shader getInstance() {
        if (INSTANCE == null) INSTANCE = new Shader();
        return INSTANCE;
    }
    @Override
    public void onEnable() {
        super.onEnable();
        loadShader();
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (shaderProgramId != 0) {
            GL20.glDeleteProgram(shaderProgramId);
            shaderProgramId = 0;
        }
        if (vaoId != 0) {
            GL30.glDeleteVertexArrays(vaoId);
            vaoId = 0;
        }
        if (vboId != 0) {
            GL20.glDeleteBuffers(vboId);
            vboId = 0;
        }
    }

    // Remove fullscreen quad rendering from onRender3D
    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Entity render integration will be handled via mixin or render hooks.
        // This method intentionally left blank.
    }
} 