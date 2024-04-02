import com.example.demo.Model.GameModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameModelTest {
    @Test
    public void testAddition() {
        // Arrange
        GameModel gm = new GameModel("Jungle", null);
        assertTrue(gm.checkInteraction(10,20,30,40) );

    }
}
