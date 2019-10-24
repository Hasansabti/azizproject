package tech.sabtih.azizproject.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.sabtih.azizproject.models.Renter_Car;

public class DummyRentercar {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Renter_Car> ITEMS = new ArrayList<Renter_Car>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Renter_Car> ITEM_MAP = new HashMap<String, Renter_Car>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.


        addItem(createDummyItem(0,"Camry","Toyota",130));
        addItem(createDummyItem(1,"Cerato","Kia",230));
        addItem(createDummyItem(3,"BBBB","Kia",230));
    }

    private static void addItem(Renter_Car item) {
        ITEMS.add(item);
        ITEM_MAP.put(""+item.getId(), item);
    }

    private static Renter_Car createDummyItem(int position,String name,String owner,double price) {
        return new Renter_Car(position, name, owner,price,makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);

        return builder.toString();
    }


}
