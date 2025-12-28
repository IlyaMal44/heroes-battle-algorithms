package programs;

import com.battle.heroes.army.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuitableForAttackUnitsFinderImplTest {

    private SuitableForAttackUnitsFinderImpl finder;

    @BeforeEach
    void setUp() {
        finder = new SuitableForAttackUnitsFinderImpl();
    }

    @Test
    void testGetSuitableUnits_NullInput_ReturnsEmptyList() {
        List<Unit> result = finder.getSuitableUnits(null, true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetSuitableUnits_EmptyRows_ReturnsEmptyList() {
        List<List<Unit>> unitsByRow = new ArrayList<>();
        unitsByRow.add(new ArrayList<>());
        unitsByRow.add(new ArrayList<>());
        unitsByRow.add(new ArrayList<>());

        List<Unit> result = finder.getSuitableUnits(unitsByRow, true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetSuitableUnits_AllDeadUnits_ReturnsEmptyList() {
        List<List<Unit>> unitsByRow = new ArrayList<>();
        List<Unit> row = new ArrayList<>();
        row.add(createUnit("DeadUnit", false));
        row.add(createUnit("DeadUnit2", false));
        unitsByRow.add(row);

        List<Unit> result = finder.getSuitableUnits(unitsByRow, true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private Unit createUnit(String name, boolean isAlive) {
        Unit unit = new Unit(
                name, "Knight", 100, 10, 100, "melee",
                new HashMap<>(), new HashMap<>(), 0, 0);
        unit.setAlive(isAlive);
        return unit;
    }

}
