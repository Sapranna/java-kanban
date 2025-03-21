import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        assertNotNull(Managers.getDefault(), "InMemoryTaskManager не найден");
    }

    @Test
    void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory(), "InMemoryHistoryManager не найден");
    }
}