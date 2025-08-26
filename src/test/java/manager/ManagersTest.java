package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
        void getDefaultShouldReturnInitializedManager() {
            assertNotNull(Managers.getDefault());
    }

    @Test
        void getDefaultHistoryShouldReturnInitializedHistoryManager() {
            assertNotNull(Managers.getDefaultHistory());
    }
}
