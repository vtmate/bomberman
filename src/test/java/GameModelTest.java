import com.example.demo.BombermanApplication;
import com.example.demo.Controller.InGameController;
import com.example.demo.Model.GameModel;
import javafx.application.Application;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {
    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX toolkit
        //Application.launch(BombermanApplicationa.class);
    }
    @Test
    public void testCheckInteraction1() {
        InGameController igc = new InGameController(null, "", "", "Dzsungel");
        GameModel gm = new GameModel("Dzsungel", null);
        assertTrue(gm.checkInteraction(10,20,30,40) );
    }
//    @Test
//    public void testCheckInteraction2() {
//        GameModel gm = new GameModel("Jungle", null);
//        assertFalse(gm.checkInteraction(80,20,40,40) );
//    }
}
