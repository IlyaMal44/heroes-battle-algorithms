package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;
import java.util.PriorityQueue;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        if (playerArmy == null || computerArmy == null) return;

        // 1. Создаем PriorityQueue для автоматической сортировки
        PriorityQueue<Unit> turnQueue = new PriorityQueue<>(
                (u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack())
        );

        // 2. Добавляем всех юнитов
        addAliveUnitsToQueue(playerArmy, turnQueue);
        addAliveUnitsToQueue(computerArmy, turnQueue);

        // 3. Реализуем цикл
        while (!turnQueue.isEmpty() && hasAliveUnits(playerArmy) && hasAliveUnits(computerArmy)) {
            // 4. Берем юнита с максимальной атакой
            Unit attacker = turnQueue.poll();
            if (attacker == null || !attacker.isAlive()) continue;

            // 5. Атака
            Unit target = (attacker.getProgram() != null) ?
                    attacker.getProgram().attack() : null;

            if (target != null && target.isAlive()) {
                // Наносим урон
                target.setHealth(target.getHealth() - attacker.getBaseAttack());
                if (target.getHealth() <= 0) {
                    target.setAlive(false);
                    // Удаляем цель из очереди, если она там есть
                    turnQueue.remove(target);
                }

                // Логирование
                if (printBattleLog != null) {
                    printBattleLog.printBattleLog(attacker, target);
                }
            }

            // 6. Возвращаем атакующего в очередь, если он жив
            if (attacker.isAlive()) {
                turnQueue.offer(attacker);
            }

            Thread.sleep(10);
            if (Thread.interrupted()) throw new InterruptedException();
        }
    }

    private void addAliveUnitsToQueue(Army army, PriorityQueue<Unit> queue) {
        if (army == null || army.getUnits() == null) return;
        for (Unit unit : army.getUnits()) {
            if (unit != null && unit.isAlive()) {
                queue.offer(unit);
            }
        }
    }

    private boolean hasAliveUnits(Army army) {
        if (army == null || army.getUnits() == null) return false;
        for (Unit unit : army.getUnits()) {
            if (unit != null && unit.isAlive()) return true;
        }
        return false;
    }
}