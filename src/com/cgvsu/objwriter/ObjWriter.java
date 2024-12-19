
package com.cgvsu.objwriter;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ObjWriter {

    public static void write(String outputPath, Model model) throws IOException {
        StringBuilder objContent = new StringBuilder();

        for (Float[] vertex : model.vertices) {
            objContent.append(String.format("v %.6f %.6f %.6f%n", vertex[0], vertex[1], vertex[2]));
        }

        for (Float[] normal : model.normals) {
            objContent.append(String.format("vn %.6f %.6f %.6f%n", normal[0], normal[1], normal[2]));
        }

        for (Polygon polygon : model.polygons) {
            objContent.append("f");
            for (Integer index : polygon.getVertexIndices()) {
                objContent.append(" ").append(index + 1);
            }
            objContent.append(System.lineSeparator());
        }

        Files.write(Path.of(outputPath), objContent.toString().getBytes());
    }
}
