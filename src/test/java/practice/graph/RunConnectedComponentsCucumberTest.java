package practice.graph;

import io.cucumber.junit.platform.engine.Cucumber;
import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;

@Cucumber
@SelectClasspathResource("features/graph")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "practice.graph")
class RunConnectedComponentsCucumberTest {
}
