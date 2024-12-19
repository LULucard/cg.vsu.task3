
package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.utils.ModelProcessor;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите путь к файлу модели (.obj): ");
            String inputPath = scanner.nextLine();
            
            System.out.println("Загружаем модель...");
            Model model = ObjReader.read(Path.of(inputPath));
            System.out.println("Модель успешно загружена!");

            System.out.println("Введите номера полигонов для удаления (через запятую): ");
            String input = scanner.nextLine();
            List<Integer> polygonsToDelete = ModelProcessor.parsePolygonIndices(input);

            System.out.println("Удалить лишние вершины? (да/нет): ");
            boolean removeUnusedVertices = scanner.nextLine().equalsIgnoreCase("да");

            System.out.println("Создать новую модель с удалёнными полигонами или изменять текущую? (новая/текущая): ");
            boolean createNewModel = scanner.nextLine().equalsIgnoreCase("новая");

            Model processedModel = createNewModel
                    ? ModelProcessor.createNewModelWithDeletedPolygons(model, polygonsToDelete, removeUnusedVertices)
                    : ModelProcessor.deletePolygonsInPlace(model, polygonsToDelete, removeUnusedVertices);

            System.out.println("Введите путь для сохранения модели: ");
            String outputPath = scanner.nextLine();

            ObjWriter.write(outputPath, processedModel);
            System.out.println("Модель успешно сохранена в " + outputPath);
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
