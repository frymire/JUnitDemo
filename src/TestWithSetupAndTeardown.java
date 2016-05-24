
import java.util.Collection;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class TestWithSetupAndTeardown {

  private Collection<String> collection;

  @BeforeClass
  public static void oneTimeSetUp() { System.out.println("@BeforeClass - oneTimeSetUp"); }

  @AfterClass
  public static void oneTimeTearDown() { System.out.println("@AfterClass - oneTimeTearDown"); }

  @Before
  public void setUp() {
    collection = new ArrayList<String>();
    System.out.println("@Before - setUp");
  }

  @After
  public void tearDown() {
    collection.clear();
    System.out.println("@After - tearDown");
  }

  @Test
  public void testEmptyCollection() {
    assertTrue(collection.isEmpty());
    System.out.println("@Test - testEmptyCollection");
  }

  @Test
  public void testOneItemCollection() {
    collection.add("itemA");
    assertEquals(1, collection.size());
    System.out.println("@Test - testOneItemCollection");
  }
  
}