package com.denger.client.utils.rect;

import com.denger.client.utils.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;

import static com.denger.client.MainNative.mc;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtil {
    private final int programID;
    private long time;

    public ShaderUtil(String fragmentShaderLoc) {
        int program = glCreateProgram();
        int fragmentShaderID;
        switch (fragmentShaderLoc) {
            case "roundedRect":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundedRect.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "roundedRectGradient":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundedRectGradient.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "roundedTexturedShader":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundedTexturedShader.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "roundRectOutline":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundRectOutline.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "jumpcirlce":
                fragmentShaderID = createShader(new ByteArrayInputStream(jumpcirlce.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "kawaseDownBloom":
                fragmentShaderID = createShader(new ByteArrayInputStream(kawaseDownBloom.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "kawaseUpBloom":
                fragmentShaderID = createShader(new ByteArrayInputStream(kawaseUpBloom.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "bloom":
                fragmentShaderID = createShader(new ByteArrayInputStream(bloom.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "bloomy":
                fragmentShaderID = createShader(new ByteArrayInputStream(bloomy.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "rounded_texture":
                fragmentShaderID = createShader(new ByteArrayInputStream(rounded_texture.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "blur":
                fragmentShaderID = createShader(new ByteArrayInputStream(blur.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "outline":
                fragmentShaderID = createShader(new ByteArrayInputStream(outline.getBytes()),GL_FRAGMENT_SHADER);
                break;
            default:
                fragmentShaderID = 0;
        }
        glAttachShader(program, fragmentShaderID);


        glLinkProgram(program);
        int status = glGetProgrami(program, GL_LINK_STATUS);

        if (status == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = program;
        time = System.currentTimeMillis();
    }


    public void load() {
        GL30.glUseProgram(programID);
    }

    public void unload() {
        GL30.glUseProgram(0);
    }

    public int getUniform(String name) {
        return glGetUniformLocation(programID, name);
    }

    public long getTime() {
        return time;
    }

    public static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {
        //MainWindow mainWindow = mc.getWindow();
        roundedTexturedShader.setUniformf("location", (float) (x * 1),
                (float) ((600 - (height * 1)) - (y * 1)));
        roundedTexturedShader.setUniformf("rectSize", (float) (width * 1), (float) (height * 1));
        roundedTexturedShader.setUniformf("radius", (float) (radius * 1));
    }

    public void setUniformf(String name, float... args) {
        int loc = glGetUniformLocation(programID, name);
        switch (args.length) {
            case 1:
                GL30.glUniform1f(loc, args[0]);
                break;
            case 2:
                GL30.glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
                GL30.glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
                GL30.glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }

    public void setUniformfb(String name, FloatBuffer buffer) {
        GL30.glUniform1fv(GL30.glGetUniformLocation(this.programID, name), buffer);
    }

    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(programID, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    public static void drawQuads(float x, float y, float width, float height) {
        GL30.glBegin(GL_QUADS);
        GL30.glTexCoord2d(0, 0);
        GL30.glVertex2d(x, y);
        GL30.glTexCoord2d(0, 1);
        GL30.glVertex2d(x, y + height);
        GL30.glTexCoord2d(1, 1);
        GL30.glVertex2d(x + width, y + height);
        GL30.glTexCoord2d(1, 0);
        GL30.glVertex2d(x + width, y);
        GL30.glEnd();
    }
    public static void drawQuadsRevers(float x, float y, float width, float height) {
        GL30.glBegin(GL_QUADS);
        GL30.glTexCoord2d(0, 1);
        GL30.glVertex2d(x, y);
        GL30.glTexCoord2d(0, 0);
        GL30.glVertex2d(x, y + height);
        GL30.glTexCoord2d(1, 0);
        GL30.glVertex2d(x + width, y + height);
        GL30.glTexCoord2d(1, 1);
        GL30.glVertex2d(x + width, y);
        GL30.glEnd();

    }

    public static void drawQuads(MatrixStack matrixStack, float x, float y, float width, float height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        Matrix4f matrix = matrixStack.last().pose();
        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.vertex(matrix, x, y, 0).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x, y + height, 0).uv(0.0F, 1.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, 0).uv(1.0F, 1.0F).endVertex();
        buffer.vertex(matrix, x + width, y, 0).uv(1.0F, 0.0F).endVertex();
        tessellator.end();

    }
    public static void drawQuadsRevers(MatrixStack matrixStack, float x, float y, float width, float height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        Matrix4f matrix = matrixStack.last().pose();
        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.vertex(matrix, x, y, 0).uv(0.0F, 1.0F).endVertex();
        buffer.vertex(matrix, x, y + height, 0).uv(0.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y + height, 0).uv(1.0F, 0.0F).endVertex();
        buffer.vertex(matrix, x + width, y, 0).uv(1.0F, 1.0F).endVertex();

        tessellator.end();

    }
    public static void drawQuads() {
        MainWindow mainWindow = mc.getWindow();
        drawQuads(0, 0, mainWindow.getGuiScaledWidth(), mainWindow.getGuiScaledHeight());
    }
    public static void drawQuadsRevers() {
        MainWindow mainWindow = mc.getWindow();
        drawQuadsRevers(0, 0, mainWindow.getGuiScaledWidth(), mainWindow.getGuiScaledHeight());
    }
    private int createShader(InputStream inputStream, int shaderType) {
        int shader = glCreateShader(shaderType);
        GL30.glShaderSource(shader, Utils.readInputStream(inputStream));
        glCompileShader(shader);


        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            System.out.println(glGetShaderInfoLog(shader, 4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }

        return shader;
    }

private final String outline = "#version 120 \n" +
        "uniform sampler2D sampler;\n" +
        "void main(void)\n" +
        "{\n" +
        "float s = 0.001;" +
        "vec4 t = texture2D(sampler, vec2(gl_TexCoord[0].x, 1. - gl_TexCoord[0].y)); \n" +
        "if(texture2D(sampler, vec2(gl_TexCoord[0].x + s, 1. - gl_TexCoord[0].y)).a == 0. && t.a != 0." +
        "   || texture2D(sampler, vec2(gl_TexCoord[0].x - s, 1. - gl_TexCoord[0].y)).a == 0. && t.a != 0." +
        "   || texture2D(sampler, vec2(gl_TexCoord[0].x, 1. - gl_TexCoord[0].y - s)).a == 0. && t.a != 0." +
        "   || texture2D(sampler, vec2(gl_TexCoord[0].x, 1. - gl_TexCoord[0].y + s)).a == 0. && t.a != 0.) {" +
        "   t.rgb = vec3(0., 0., 0.);" +
        "} else t = vec4(.0);" +
        "gl_FragColor = t; \n" +
        "}\n";
    private final String roundedTexturedShader = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform sampler2D textureIn;\n" +
            "uniform float radius, alpha;\n" +
            "\n" +
            "float roundedBoxSDF(vec2 centerPos, vec2 size, float radius) {\n" +
            "    return length(max(abs(centerPos) -size, 0.)) - radius;\n" +
            "}\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "    float distance = roundedBoxSDF((rectSize * .5) - (gl_TexCoord[0].st * rectSize), (rectSize * .5) - radius - 1., radius);\n" +
            "    float smoothedAlpha =  (1.0-smoothstep(0.0, 2.0, distance)) * alpha;\n" +
            "    gl_FragColor = vec4(texture2D(textureIn, gl_TexCoord[0].st).rgb, smoothedAlpha);\n" +
            "}";
    private final String roundedRectGradient = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform vec4 color1, color2, color3, color4;\n" +
            "uniform float radius;\n" +
            "\n" +
            "#define NOISE .5/255.0\n" +
            "\n" +
            "float roundSDF(vec2 p, vec2 b, float r) {\n" +
            "    return length(max(abs(p) - b , 0.0)) - r;\n" +
            "}\n" +
            "\n" +
            "vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){\n" +
            "    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);\n" +
            "    //Dithering the color\n" +
            "    // from https://shader-tutorial.dev/advanced/color-banding-dithering/\n" +
            "    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));\n" +
            "    return color;\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 st = gl_TexCoord[0].st;\n" +
            "    vec2 halfSize = rectSize * .5;\n" +
            "    \n" +
            "    float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius))) * color1.a;\n" +
            "    gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);\n" +
            "}";

    private final String roundRectOutline = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform vec4 color, outlineColor;\n" +
            "uniform float radius, outlineThickness;\n" +
            "\n" +
            "float roundedSDF(vec2 centerPos, vec2 size, float radius) {\n" +
            "    return length(max(abs(centerPos) - size + radius, 0.0)) - radius;\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    float distance = roundedSDF(gl_FragCoord.xy - location - (rectSize * .5), (rectSize * .5) + (outlineThickness *.5) - 1.0, radius);\n" +
            "\n" +
            "    float blendAmount = smoothstep(0., 2., abs(distance) - (outlineThickness * .5));\n" +
            "\n" +
            "    vec4 insideColor = (distance < 0.) ? color : vec4(outlineColor.rgb,  0.0);\n" +
            "    gl_FragColor = mix(outlineColor, insideColor, blendAmount);\n" +
            "\n" +
            "}";
    private final String roundedRect = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform vec4 color;\n" +
            "uniform float radius;\n" +
            "uniform bool blur;\n" +
            "\n" +
            "float roundSDF(vec2 p, vec2 b, float r) {\n" +
            "    return length(max(abs(p) - b, 0.0)) - r;\n" +
            "}\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 rectHalf = rectSize * .5;\n" +
            "    // Smooth the result (free antialiasing).\n" +
            "    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;\n" +
            "    gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);\n" +
            "\n" +
            "}";
    private final String jumpcirlce =
            "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "uniform float time;\n" +
                    "uniform vec2 resolution;\n" +
                    "\n" +
                    "void main( void ) \n" +
                    "{\n" +
                    "\tvec2 p = (2.0 * gl_FragCoord.xy - resolution.xy) / resolution.y;\n" +
                    "    \tfloat r = length(p) * 1.0;\n" +
                    "\tr = 2.0 * r - 1.0;\n" +
                    "\tfloat alpha = 1.0 / (20.0 * abs(r));\n" +
                    "\tgl_FragColor = vec4(alpha * vec3(1.5, 2.0, 1.5), alpha);\n" +
                    "}";
    private final String kawaseDownBloom = "#version 120\n" +
            "\n" +
            "            uniform sampler2D inTexture;\n" +
            "            uniform vec2 offset, halfpixel, iResolution;\n" +
            "\n" +
            "            void main() {\n" +
            "                vec2 uv = vec2(gl_FragCoord.xy / iResolution);\n" +
            "                vec4 sum = texture2D(inTexture, gl_TexCoord[0].st);\n" +
            "                sum.rgb *= sum.a;\n" +
            "                sum *= 4.0;\n" +
            "                vec4 smp1 = texture2D(inTexture, uv - halfpixel.xy * offset);\n" +
            "                smp1.rgb *= smp1.a;\n" +
            "                sum += smp1;\n" +
            "                vec4 smp2 = texture2D(inTexture, uv + halfpixel.xy * offset);\n" +
            "                smp2.rgb *= smp2.a;\n" +
            "                sum += smp2;\n" +
            "                vec4 smp3 = texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);\n" +
            "                smp3.rgb *= smp3.a;\n" +
            "                sum += smp3;\n" +
            "                vec4 smp4 = texture2D(inTexture, uv - vec2(halfpixel.x, -halfpixel.y) * offset);\n" +
            "                smp4.rgb *= smp4.a;\n" +
            "                sum += smp4;\n" +
            "                vec4 result = sum / 8.0;\n" +
            "                gl_FragColor = vec4(result.rgb / result.a, result.a);\n" +
            "            }";

    private final String kawaseUpBloom = "#version 120\n" +
            "\n" +
            "            uniform sampler2D inTexture, textureToCheck;\n" +
            "            uniform vec2 halfpixel, offset, iResolution;\n" +
            "            uniform int check;\n" +
            "\n" +
            "            void main() {\n" +
            "                vec2 uv = vec2(gl_FragCoord.xy / iResolution);\n" +
            "\n" +
            "                vec4 sum = texture2D(inTexture, uv + vec2(-halfpixel.x * 2.0, 0.0) * offset);\n" +
            "                sum.rgb *= sum.a;\n" +
            "                vec4 smpl1 =  texture2D(inTexture, uv + vec2(-halfpixel.x, halfpixel.y) * offset);\n" +
            "                smpl1.rgb *= smpl1.a;\n" +
            "                sum += smpl1 * 2.0;\n" +
            "                vec4 smp2 = texture2D(inTexture, uv + vec2(0.0, halfpixel.y * 2.0) * offset);\n" +
            "                smp2.rgb *= smp2.a;\n" +
            "                sum += smp2;\n" +
            "                vec4 smp3 = texture2D(inTexture, uv + vec2(halfpixel.x, halfpixel.y) * offset);\n" +
            "                smp3.rgb *= smp3.a;\n" +
            "                sum += smp3 * 2.0;\n" +
            "                vec4 smp4 = texture2D(inTexture, uv + vec2(halfpixel.x * 2.0, 0.0) * offset);\n" +
            "                smp4.rgb *= smp4.a;\n" +
            "                sum += smp4;\n" +
            "                vec4 smp5 = texture2D(inTexture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);\n" +
            "                smp5.rgb *= smp5.a;\n" +
            "                sum += smp5 * 2.0;\n" +
            "                vec4 smp6 = texture2D(inTexture, uv + vec2(0.0, -halfpixel.y * 2.0) * offset);\n" +
            "                smp6.rgb *= smp6.a;\n" +
            "                sum += smp6;\n" +
            "                vec4 smp7 = texture2D(inTexture, uv + vec2(-halfpixel.x, -halfpixel.y) * offset);\n" +
            "                smp7.rgb *= smp7.a;\n" +
            "                sum += smp7 * 2.0;\n" +
            "                vec4 result = sum / 12.0;\n" +
            "                gl_FragColor = vec4(result.rgb / result.a, mix(result.a, result.a * (1.0 - texture2D(textureToCheck, gl_TexCoord[0].st).a),check));\n" +
            "            }";
    private final String bloomy ="#version 120\n" +
            "\n" +
            "uniform sampler2D sampler1;\n" +
            "uniform sampler2D sampler2;\n" +
            "uniform vec2 texelSize;\n" +
            "uniform vec2 direction;\n" +
            "uniform float radius;\n" +
            "uniform float kernel[64];\n" +
            "\n" +
            "void main(void)\n" +
            "{\n" +
            "    vec2 uv = gl_TexCoord[0].st;\n" +
            "    uv.y = uv.y;\n" +
            "\n" +
            "\n" +
            "    vec4 pixel_color = texture2D(sampler1, uv);\n" +
            "    pixel_color.rgb *= pixel_color.a;\n" +
            "    pixel_color *= kernel[0];\n" +
            "\n" +
            "    for (float f = 1; f <= radius; f++) {\n" +
            "        vec2 offset = f * texelSize * direction;\n" +
            "        vec4 left = texture2D(sampler1, uv - offset);\n" +
            "        vec4 right = texture2D(sampler1, uv + offset);\n" +
            "        left.rgb *= left.a;\n" +
            "        right.rgb *= right.a;\n" +
            "        pixel_color += (left + right) * kernel[int(f)];\n" +
            "    }\n" +
            "\n" +
            "    gl_FragColor = vec4(pixel_color.rgb / pixel_color.a, pixel_color.a);\n" +
            "}\n";
    private final String bloom =
            "#version 120\n" +
                    "\n" +
                    "uniform sampler2D sampler1;\n" +
                    "uniform sampler2D sampler2;\n" +
                    "uniform vec2 texelSize;\n" +
                    "uniform vec2 direction;\n" +
                    "uniform float radius;\n" +
                    "uniform float kernel[64];\n" +
                    "\n" +
                    "void main(void)\n" +
                    "{\n" +
                    "    vec2 uv = gl_TexCoord[0].st;\n" +
                    "\n" +
                    "    if (direction.x == 0.0 && texture2D(sampler2, uv).a > 0.0) {\n" +
                    "    \tdiscard;\n" +
                    "    }\n" +
                    "\n" +
                    "    vec4 pixel_color = texture2D(sampler1, uv);\n" +
                    "    pixel_color.rgb *= pixel_color.a;\n" +
                    "    pixel_color *= kernel[0];\n" +
                    "\n" +
                    "    for (float f = 1; f <= radius; f++) {\n" +
                    "        vec2 offset = f * texelSize * direction;\n" +
                    "        vec4 left = texture2D(sampler1, uv - offset);\n" +
                    "        vec4 right = texture2D(sampler1, uv + offset);\n" +
                    "        left.rgb *= left.a;\n" +
                    "        right.rgb *= right.a;\n" +
                    "        pixel_color += (left + right) * kernel[int(f)];\n" +
                    "    }\n" +
                    "\n" +
                    "    gl_FragColor = vec4(pixel_color.rgb / pixel_color.a, pixel_color.a);\n" +
                    "}";

    private final String rounded_texture =
            "#version 120\n" +
                    "\n" +
                    "uniform float round;\n" +
                    "uniform sampler2D texture;\n" +
                    "uniform vec2 size;\n" +
                    "\n" +
                    "float dstfn(vec2 p, vec2 b) {\n" +
                    "    return length(max(abs(p) - b, 0.0)) - round;\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 tex = gl_TexCoord[0].st;\n" +
                    "    vec4 smpl = texture2D(texture, tex);\n" +
                    "    vec2 pixel = gl_TexCoord[0].st * size;\n" +
                    "    vec2 centre = 0.5 * size;\n" +
                    "    float sa = 1.f - smoothstep(0.0, 1, dstfn(centre - pixel, centre - round - 1));\n" +
                    "    gl_FragColor = vec4(smpl.rgb, smpl.a * sa);\n" +
                    "}";
    private final String blur =
            "#version 120\n" +
                    "\n" +
                    "uniform sampler2D sampler1;\n" +
                    "uniform sampler2D sampler2;\n" +
                    "uniform vec2 texelSize;\n" +
                    "uniform vec2 direction;\n" +
                    "uniform float radius;\n" +
                    "uniform float kernel[64];\n" +
                    "\n" +
                    "void main()\n" +
                    "{\n" +
                    "    vec2 uv = gl_TexCoord[0].st;\n" +
                    "    uv.y = 1.0f - uv.y;\n" +
                    "\n" +
                    "    float alpha = texture2D(sampler2, uv).a;\n" +
                    "    if (direction.x == 0.0 && alpha == 0.0) {\n" +
                    "        discard;\n" +
                    "    }\n" +
                    "\n" +
                    "    vec4 pixel_color = texture2D(sampler1, uv) * kernel[0];\n" +
                    "    for (float f = 1; f <= radius; f++) {\n" +
                    "        vec2 offset = f * texelSize * direction;\n" +
                    "        pixel_color += texture2D(sampler1, uv - offset) * kernel[int(f)];\n" +
                    "        pixel_color += texture2D(sampler1, uv + offset) * kernel[int(f)];\n" +
                    "    }\n" +
                    "\n" +
                    "    gl_FragColor = vec4(pixel_color.rgb, direction.x == 0.0 ? alpha : 1.0);\n" +
                    "}\n";

    public static void bindTexture(int texture) {
        GlStateManager._bindTexture(texture);
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    public int getProgramID() {
        return programID;
    }
}
