
package com.cgvsu.utils;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.*;
import java.util.stream.Collectors;

public class ModelProcessor {

    public static List<Integer> parsePolygonIndices(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static Model createNewModelWithDeletedPolygons(Model model, List<Integer> polygonsToDelete, boolean removeUnusedVertices) {
        Model newModel = new Model();
        newModel.vertices = new ArrayList<>(model.vertices);
        newModel.normals = new ArrayList<>(model.normals);
        newModel.textureVertices = new ArrayList<>(model.textureVertices);

        Set<Integer> deletedPolygons = new HashSet<>(polygonsToDelete);
        for (int i = 0; i < model.polygons.size(); i++) {
            if (!deletedPolygons.contains(i)) {
                newModel.polygons.add(model.polygons.get(i));
            }
        }

        if (removeUnusedVertices) {
            removeUnreferencedVertices(newModel);
        }
        return newModel;
    }

    public static Model deletePolygonsInPlace(Model model, List<Integer> polygonsToDelete, boolean removeUnusedVertices) {
        Set<Integer> deletedPolygons = new HashSet<>(polygonsToDelete);
        model.polygons.removeIf(polygon -> deletedPolygons.contains(model.polygons.indexOf(polygon)));

        if (removeUnusedVertices) {
            removeUnreferencedVertices(model);
        }
        return model;
    }

    private static void removeUnreferencedVertices(Model model) {
        Set<Integer> referencedVertices = new HashSet<>();
        for (Polygon polygon : model.polygons) {
            referencedVertices.addAll(polygon.getVertexIndices());
        }

        List<Integer> newIndexMap = new ArrayList<>(Collections.nCopies(model.vertices.size(), -1));
        List<Float[]> newVertices = new ArrayList<>();
        for (int i = 0; i < model.vertices.size(); i++) {
            if (referencedVertices.contains(i)) {
                newIndexMap.set(i, newVertices.size());
                newVertices.add(model.vertices.get(i));
            }
        }

        model.vertices = newVertices;
        for (Polygon polygon : model.polygons) {
            List<Integer> updatedIndices = polygon.getVertexIndices().stream()
                    .map(newIndexMap::get)
                    .collect(Collectors.toList());
            polygon.setVertexIndices(updatedIndices);
        }
    }
}
