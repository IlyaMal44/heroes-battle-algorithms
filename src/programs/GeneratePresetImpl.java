package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;
import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army army = new Army();
        List<Unit> units = new ArrayList<>();
        army.setUnits(units);
        army.setPoints(0);

        if (unitList == null || unitList.isEmpty()) {
            return army;
        }

        // 1. Сортируем юнитов по эффективности
        List<Unit> sorted = new ArrayList<>(unitList);
        sorted.sort((a, b) -> {
            double effA = (double) a.getBaseAttack() / a.getCost();
            double effB = (double) b.getBaseAttack() / b.getCost();
            return Double.compare(effB, effA);
        });

        // 2. Создаём список всех возможных позиций (3x21 = 63 позиции)
        List<int[]> allPositions = new ArrayList<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 21; y++) {
                allPositions.add(new int[]{x, y});
            }
        }

        // 3. Перемешиваем позиции для случайности
        Collections.shuffle(allPositions);

        // 4. Счётчики и занятые позиции
        Map<String, Integer> typeCount = new HashMap<>();
        Set<String> occupied = new HashSet<>();
        int totalCost = 0;
        int positionIndex = 0;

        // 5. Жадный алгоритм со случайными позициями
        for (Unit unit : sorted) {
            String type = unit.getUnitType();
            int count = typeCount.getOrDefault(type, 0);
            int cost = unit.getCost();

            while (count < 11 && totalCost + cost <= maxPoints && positionIndex < allPositions.size()) {
                // Берём следующую случайную позицию
                int[] pos = allPositions.get(positionIndex);
                String posKey = pos[0] + "," + pos[1];

                // Если позиция уже занята, пропускаем
                if (occupied.contains(posKey)) {
                    positionIndex++;
                    continue;
                }

                // Создаём юнита
                Unit newUnit = new Unit(
                        type + " " + (count + 1),
                        type,
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        cost,
                        unit.getAttackType(),
                        unit.getAttackBonuses() != null ? unit.getAttackBonuses() : new HashMap<>(),
                        unit.getDefenceBonuses() != null ? unit.getDefenceBonuses() : new HashMap<>(),
                        pos[0],
                        pos[1]
                );

                units.add(newUnit);
                count++;
                typeCount.put(type, count);
                totalCost += cost;
                occupied.add(posKey);
                positionIndex++;

                if (totalCost > maxPoints) {
                    // Откат
                    units.remove(units.size() - 1);
                    totalCost -= cost;
                    occupied.remove(posKey);
                    break;
                }
            }
        }

        army.setPoints(totalCost);
        return army;
    }
}