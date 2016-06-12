// UNCLASSIFIED

package io.github.frymire;

// This is used in the JMockitTestClassInjection demo.
public class TestMe {

  private String name;
  private boolean flag;
  private Adder adder;

  public TestMe(String name, boolean flag, Adder adder) { 
    this.name = name; 
    this.flag = flag;
    this.adder = adder;
  }
  
  public String getName() { return name; }
  public boolean getFlag() { return flag; }
  public int add(int i, int j) { return adder.add(i, j); }
  
}
