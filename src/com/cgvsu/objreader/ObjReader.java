
package com.cgvsu.objreader;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ObjReader {

    public static Model read(Path filePath) throws IOException {
        Model model = new Model();
        List<String> lines = Files.readAllLines(filePath);

        for (String line : lines) {
            String[] parts = line.trim().split("\s+");
            if (parts.length == 0) continue;

            switch (parts[0]) {
                case "v":
                    model.vertices.add(parseVertex(parts));
                    break;
                case "vn":
                    model.normals.add(parseVertex(parts));
                    break;
                case "f":
                    model.polygons.add(parsePolygon(parts));
                    break;
                default:
            }
        }

        return model;
    }

    private static Float[] parseVertex(String[] parts) {
        return new Float[]{
                Float.parseFloat(parts[1]),
                Float.parseFloat(parts[2]),
                Float.parseFloat(parts[3])
        };
    }

    private static Polygon parsePolygon(String[] parts) {
        Polygon polygon = new Polygon();
        List<Integer> vertexIndices = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            String[] indices = parts[i].split("/");
            vertexIndices.add(Integer.parseInt(indices[0]) - 1); // OBJ indices start at 1
        }

        polygon.setVertexIndices(vertexIndices);
        return polygon;
    }
}
