package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratePresetImplTest {

    private GeneratePresetImpl generator;
    private List<Unit> unitTypes;

    @BeforeEach
    void setUp() {
        generator = new GeneratePresetImpl();
        unitTypes = new ArrayList<>();
        unitTypes.add(new Unit("Archer", "Archer", 30, 15, 40, "ranged",
                new HashMap<>(), new HashMap<>(), 0, 0));
        unitTypes.add(new Unit("Knight", "Knight", 100, 20, 100, "melee",
                new HashMap<>(), new HashMap<>(), 0, 0));
        unitTypes.add(new Unit("Swordsman", "Swordsman", 80, 18, 80, "melee",
                new HashMap<>(), new HashMap<>(), 0, 0));
        unitTypes.add(new Unit("Pikeman", "Pikeman", 60, 12, 50, "melee",
                new HashMap<>(), new HashMap<>(), 0, 0));
    }

    @Test
    void testGenerate_ValidInput_ReturnsArmy() {
        int maxPoints = 1500;

        Army result = generator.generate(unitTypes, maxPoints);

        assertNotNull(result);
        assertNotNull(result.getUnits());
        assertTrue(result.getPoints() > 0);
        assertTrue(result.getPoints() <= maxPoints);
    }

    @Test
    void testGenerate_RespectsMaxPoints() {
        int maxPoints = 500;
        Army result = generator.generate(unitTypes, maxPoints);
        assertTrue(
                result.getPoints() <= maxPoints,
                "Army cost " + result.getPoints() + " exceeds max " + maxPoints);
    }

    @Test
    void testGenerate_EmptyUnitList_ReturnsEmptyArmy() {
        List<Unit> emptyList = new ArrayList<>();
        int maxPoints = 1500;

        Army result = generator.generate(emptyList, maxPoints);

        assertNotNull(result);
        assertTrue(result.getUnits().isEmpty());
        assertEquals(0, result.getPoints());
    }

    @Test
    void testGenerate_NullUnitList_ReturnsEmptyArmy() {
        int maxPoints = 1500;

        Army result = generator.generate(null, maxPoints);

        assertNotNull(result);
        assertTrue(result.getUnits().isEmpty());
        assertEquals(0, result.getPoints());
    }

    @Test
    void testGenerate_MaxPointsZero_ReturnsEmptyArmy() {
        Army result = generator.generate(unitTypes, 0);

        assertNotNull(result);
        assertTrue(result.getUnits().isEmpty());
        assertEquals(0, result.getPoints());
    }

    @Test
    void testGenerate_ArmyHasValidUnits() {
        int maxPoints = 1500;

        Army result = generator.generate(unitTypes, maxPoints);

        for (Unit unit : result.getUnits()) {
            assertNotNull(unit.getName());
            assertNotNull(unit.getUnitType());
            assertTrue(unit.getHealth() > 0);
            assertTrue(unit.getBaseAttack() > 0);
            assertTrue(unit.getCost() > 0);
        }
    }


}
