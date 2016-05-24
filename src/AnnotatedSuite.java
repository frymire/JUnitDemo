
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// JUnit Suite Test
@RunWith(Suite.class)
@Suite.SuiteClasses({TestWithSetupAndTeardown.class ,TestAssertions.class})
public class AnnotatedSuite {}