package it.polito.tdp.yelp.db;

public class TestDao {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		YelpDao dao = new YelpDao();
//		System.out.println(String.format("Users: %d\nBusiness: %d\nReviews: %d\nCitta: %d\n", 
//				dao.getAllUsers().size(), dao.getAllBusiness().size(), dao.getAllReviews().size(),
//				dao.getAllCitta().size()));
//		System.out.printf("# vertici: %d", dao.getVertici("Wickenburg", 2009, null));
		System.out.printf("media: %.2f\n", dao.calcolaPeso("0wRPvS-sG5x-pEMKVuDBJg"));
		System.out.printf("# adiacenze: %d\n", dao.getAdiacenze("wickenburg", 2009).size());

	}

}
