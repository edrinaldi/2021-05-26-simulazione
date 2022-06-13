package it.polito.tdp.yelp.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m = new Model();
		
		// esempio 1
		System.out.println(m.creaGrafo("Wickenburg", 2009));
		m.localeMigliore();
		
		// esempio 2
		m.creaGrafo("Phoenix", 2005);
		m.localeMigliore();
		
		// esempio 3
		m.creaGrafo("Tempe", 2013);
		m.localeMigliore();


	}

}
