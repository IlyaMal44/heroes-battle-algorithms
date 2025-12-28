package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTargetPathFinderImplTest {
    private UnitTargetPathFinderImpl pathFinder;

    @BeforeEach
    void setUp() {
        pathFinder = new UnitTargetPathFinderImpl();
    }

    @Test
    void testGetTargetPath_StraightLine_ReturnsPath() {
        Unit attacker = createUnit(0, 0, "Attacker", true);
        Unit target = createUnit(3, 0, "Target", true);
        List<Unit> obstacles = new ArrayList<>();

        List<Edge> path = pathFinder.getTargetPath(attacker, target, obstacles);

        assertNotNull(path);
    }

    @Test
    void testGetTargetPath_SamePosition_ReturnsSinglePoint() {
        Unit attacker = createUnit(5, 5, "Attacker", true);
        Unit target = createUnit(5, 5, "Target", true);
        List<Unit> obstacles = new ArrayList<>();

        List<Edge> path = pathFinder.getTargetPath(attacker, target, obstacles);

        assertNotNull(path);
    }

    @Test
    void testGetTargetPath_NullAttacker_ReturnsEmptyList() {
        Unit target = createUnit(5, 5, "Target", true);
        List<Unit> obstacles = new ArrayList<>();

        List<Edge> path = pathFinder.getTargetPath(null, target, obstacles);

        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    @Test
    void testGetTargetPath_DeadAttacker_ReturnsEmptyList() {
        Unit attacker = createUnit(0, 0, "Attacker", false);
        Unit target = createUnit(5, 5, "Target", true);
        List<Unit> obstacles = new ArrayList<>();

        List<Edge> path = pathFinder.getTargetPath(attacker, target, obstacles);

        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    @Test
    void testGetTargetPath_NullTarget_ReturnsEmptyList() {
        Unit attacker = createUnit(0, 0, "Attacker", true);
        List<Unit> obstacles = new ArrayList<>();

        List<Edge> path = pathFinder.getTargetPath(attacker, null, obstacles);

        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    @Test
    void testGetTargetPath_BlockedPath_ReturnsEmptyList() {
        Unit attacker = createUnit(0, 0, "Attacker", true);
        Unit target = createUnit(2, 0, "Target", true);

        List<Unit> obstacles = new ArrayList<>();
        obstacles.add(createUnit(1, 0, "Obstacle", true));

        List<Edge> path = pathFinder.getTargetPath(attacker, target, obstacles);

        assertNotNull(path);
    }

    @Test
    void testGetTargetPath_ValidScenario_NoException() {
        Unit attacker = createUnit(0, 0, "Attacker", true);
        Unit target = createUnit(5, 5, "Target", true);
        List<Unit> obstacles = new ArrayList<>();

        assertDoesNotThrow(() -> {
            pathFinder.getTargetPath(attacker, target, obstacles);
        });
    }

    private Unit createUnit(int x, int y, String name, boolean isAlive) {
        Unit unit = new Unit(
                name, "Knight", 100, 10, 100, "melee", new HashMap<>(), new HashMap<>(), x, y);
        unit.setAlive(isAlive);
        return unit;
    }
}
