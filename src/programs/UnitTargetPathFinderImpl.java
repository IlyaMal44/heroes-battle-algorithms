package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    // Вспомогательный класс для узла пути
    static class PathNode implements Comparable<PathNode> {
        int x, y;
        PathNode parent;
        int g; // стоимость от старта
        int h; // эвристика до цели
        int f; // f = g + h

        PathNode(int x, int y, PathNode parent, int g, int h) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        @Override
        public int compareTo(PathNode other) {
            return Integer.compare(this.f, other.f);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PathNode node = (PathNode) obj;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        // Проверка входных данных
        if (attackUnit == null || targetUnit == null ||
                !attackUnit.isAlive() || !targetUnit.isAlive()) {
            return Collections.emptyList();
        }

        // Размеры игрового поля
        final int WIDTH = 27;
        final int HEIGHT = 21;

        // Получаем координаты
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        // Проверка координат
        if (!isValid(startX, startY, WIDTH, HEIGHT) || !isValid(targetX, targetY, WIDTH, HEIGHT)) {
            return Collections.emptyList();
        }

        // Если атакующий и цель на одной клетке
        if (startX == targetX && startY == targetY) {
            List<Edge> path = new ArrayList<>();
            path.add(new Edge(startX, startY));
            return path;
        }

        // Создаем карту препятствий
        boolean[][] obstacles = new boolean[WIDTH][HEIGHT];

        // Помечаем занятые клетки (кроме атакующего и цели)
        if (existingUnitList != null) {
            for (Unit unit : existingUnitList) {
                if (unit != null && unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                    int x = unit.getxCoordinate();
                    int y = unit.getyCoordinate();
                    if (isValid(x, y, WIDTH, HEIGHT)) {
                        obstacles[x][y] = true;
                    }
                }
            }
        }

        // Если старт или цель на препятствии
        if (obstacles[startX][startY] || obstacles[targetX][targetY]) {
            return Collections.emptyList();
        }

        // Используем алгоритм A* для поиска пути
        return aStarSearch(startX, startY, targetX, targetY, obstacles, WIDTH, HEIGHT);
    }

    private boolean isValid(int x, int y, int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private List<Edge> aStarSearch(int startX, int startY, int targetX, int targetY, boolean[][] obstacles, int width, int height) {
        // Приоритетная очередь для открытых узлов
        PriorityQueue<PathNode> openSet = new PriorityQueue<>();

        // Мапы для отслеживания узлов
        Map<String, PathNode> allNodes = new HashMap<>();
        boolean[][] closedSet = new boolean[width][height];

        // Начальный узел
        PathNode startNode = new PathNode(startX, startY, null, 0, heuristic(startX, startY, targetX, targetY));
        openSet.add(startNode);
        allNodes.put(key(startX, startY), startNode);

        // 8 направлений движения (с диагоналями)
        int[][] directions = {
                {0, 1}, {1, 0}, {0, -1}, {-1, 0},    // ортогональные
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}   // диагональные
        };

        // Стоимость перемещения
        int straightCost = 10;
        int diagonalCost = 14;
        int[] moveCost = {straightCost, straightCost, straightCost, straightCost, diagonalCost, diagonalCost, diagonalCost, diagonalCost};

        while (!openSet.isEmpty()) {
            // Берем узел с наименьшей f-стоимостью
            PathNode current = openSet.poll();

            // Если достигли цели
            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            closedSet[current.x][current.y] = true;

            // Проверяем всех соседей
            for (int i = 0; i < directions.length; i++) {
                int newX = current.x + directions[i][0];
                int newY = current.y + directions[i][1];

                // Проверяем валидность
                if (!isValid(newX, newY, width, height)) {
                    continue;
                }

                // Проверяем препятствие
                if (obstacles[newX][newY]) {
                    continue;
                }

                // Проверяем, не в закрытом ли множестве
                if (closedSet[newX][newY]) {
                    continue;
                }

                // Вычисляем стоимость до соседа
                int newG = current.g + moveCost[i];

                String nodeKey = key(newX, newY);
                PathNode existingNode = allNodes.get(nodeKey);

                if (existingNode == null || newG < existingNode.g) {
                    // Создаем или обновляем узел
                    int newH = heuristic(newX, newY, targetX, targetY);
                    PathNode neighbor = new PathNode(newX, newY, current, newG, newH);

                    if (existingNode != null) {
                        openSet.remove(existingNode);
                    }

                    openSet.add(neighbor);
                    allNodes.put(nodeKey, neighbor);
                }
            }
        }

        // Путь не найден
        return Collections.emptyList();
    }

    // Эвристическая функция (расстояние Чебышева для диагонального движения)
    private int heuristic(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return 10 * Math.max(dx, dy);
    }

    private String key(int x, int y) {
        return x + ":" + y;
    }

    private List<Edge> reconstructPath(PathNode endNode) {
        List<Edge> path = new LinkedList<>();
        PathNode current = endNode;

        while (current != null) {
            path.add(0, new Edge(current.x, current.y));
            current = current.parent;
        }

        return path;
    }
}