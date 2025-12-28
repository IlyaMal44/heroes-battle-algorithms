package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        if (unitsByRow == null) {
            return suitableUnits;
        }

        // unitsByRow - список из 3 рядов (y = 0, 1, 2)
        for (List<Unit> row : unitsByRow) {
            if (row == null || row.isEmpty()) {
                continue;
            }

            if (isLeftArmyTarget) {
                // Атакуем ЛЕВУЮ армию (компьютера) нужны самые левые юниты
                for (Unit unit : row) {
                    if (unit != null && unit.isAlive()) {
                        suitableUnits.add(unit);
                        break;
                    }
                }
            } else {
                // Атакуем ПРАВУЮ армию (игрока) нужны самые правые юниты
                for (int i = row.size() - 1; i >= 0; i--) {
                    Unit unit = row.get(i);
                    if (unit != null && unit.isAlive()) {
                        suitableUnits.add(unit);
                        break;
                    }
                }
            }
        }

        return suitableUnits;
    }
}

