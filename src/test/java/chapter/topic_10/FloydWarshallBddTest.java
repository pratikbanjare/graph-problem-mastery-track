package chapter.topic_10;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/chapter/topic_10")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "chapter.topic_10")
public class FloydWarshallBddTest {
}
