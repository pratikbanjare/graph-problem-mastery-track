package practice.graph;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiSurfaceTest {

    @Test
    void graphFormatterIsNotPublicApi() {
        assertFalse(Modifier.isPublic(GraphFormatter.class.getModifiers()));
    }

    @Test
    void graphOperationRemainsPublicApi() {
        assertTrue(Modifier.isPublic(GraphOperation.class.getModifiers()));
    }

    @Test
    void demoEntryPointsAreNotPublishedInMainSourceSet() {
        assertThrows(ClassNotFoundException.class, () -> Class.forName("practice.Main"));
        assertThrows(ClassNotFoundException.class, () -> Class.forName("practice.graph.GraphMain"));
    }
}
