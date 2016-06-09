// UNCLASSIFIED

package io.github.frymire;

// This is used in the JMockit demos.
public class Adder {
  
  private String name = null;
  
  public Adder() {}
  public Adder(String name) { this.name = name; }
  public String getName() { return name; }
  public int add(int a, int b) { return a + b; }
  public double add(double a, double b) { return a + b; }

}  
