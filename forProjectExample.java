package Main;
import java.util.*;


enum Position{
	ADMIN,
	MANAGER,
	STAFF
};

enum Suppliers{
	SUPPLIEROFYOURMOM,
	SUPPLIEROFMYMOTHER,
	SUPPLIEROFMOTHERNATURE,
	SUPPLIEROFNATURESVALLEY,
	SUPPLIEROFVALLEYOFTHEBELLYOFTHEBEAST;
	
}
enum OrderStatus{
	PURCHASED,
	PENDING,
	DENIED;
}
class Reference{
	public static Set<Store> stores = new HashSet<Store>();
	public static Set<Employee> allEmployees = new HashSet<Employee>();
	public static Set<Employee> managers = new HashSet<Employee>();
	public static Set <Employee> staff = new HashSet <Employee>();
	public static Set <Employee> admin = new HashSet <Employee>();
	public static Set <String> categories = new HashSet<String>();
	public static Set <Item> items = new HashSet<Item>();
	public static Set <PurchaseOrder> POs = new HashSet<PurchaseOrder>();
	public static int PURCHASEORDERNUMBER = 0;
	public static Position position;
	
	public static boolean containsStore(Store store) {
		for (Store allStores : stores) {
			if(allStores.getNameOfStore().equals(store.getNameOfStore()))
				return true;
		}
		return false;
	}
	
	public static boolean containsItem(Item item) {
		for( Item allItems : items) {
			if(item.nameOfItem == allItems.nameOfItem && 
			   item.price == allItems.price &&
			   item.category == allItems.category
				)
				return true;
		}
		return false;
	}
	public static Item findItem(String nameOfItem) {
		for( Item item : items) {
			if(item.nameOfItem == nameOfItem)
				return item;
		}
		return null;
	}
	public static Employee findEmployee(String name) {
		for (Employee employee : allEmployees) {
			if(name.equals(employee.getName())) {
				return employee;
			}
		}
		System.out.println("No employee with that name");
		return null;
	}
}


class Item{
	String nameOfItem;
	double price;
	String category;
	Suppliers supplier;
	
	
	
	Item(String nameOfItem, double price, String category, Suppliers supplier){

		this.nameOfItem = nameOfItem;
		this.price = price;
		this.category = category;
		this.supplier = supplier;
		if(!Reference.categories.contains(category)) {
			Reference.categories.add(category);
		}
		
		Reference.items.add(this);
		
		
	}
	
	/*
	 * method that prints the item information
	 */
	void printItemInformation() {
		System.out.println("Name: " + this.nameOfItem);
		System.out.println("Price: " + this.price);
		System.out.println("Category: " + this.category);
		System.out.println("Supplier: " + this.supplier);
	}
}
/*
 * Operations for all employees with the ADMIN position
 */
class AdminOperations{

//-------------------Store Management----------------------------------------------------------
	/*
	 * Method to update the manager of the store passed as a the first parameter
	 * to the employee passed as the second parameter if the employee is a manager.
	 */
	void updateManager(Store store, Employee manager) {
		
		if(!manager.isManager()) {
			System.out.print(manager.getName() + "is not a manager, cannot be manager of the store.");
			
			return;
		}
		
		if(store.getManager() != manager) {
			store.setManager(manager);
		}
		else {
			System.out.println(manager.name + "is already the manager of" + store.getNameOfStore());
		}
		
	}
	/*
	 * Method that finds the store in the stores list
	 */
	
	Store findStore(String storeName) {
		for (Store store : Reference.stores) {
			if(store.getNameOfStore().equals(storeName)) {
				return store;
			}
		}
		System.out.println("Store doesn't exist");
		return null;
	}
	/*
	 * Finds the store.
	 * if the store exists, it is removed from list
	 * otherwise, method ends.
	 * 
	 */
	void removeStore(String storeName) {
		Store store = findStore(storeName);
		if(store == null) {
			return;
 		}
		
		Reference.stores.remove(store);
		
	}
	
	
	/*
	 * Method that creates store and assigns a manager at random.
	 */
	void createStore(String storeName) {
		int size = Reference.managers.size();
		int managerSelected = new Random().nextInt(size);
		Employee[] managerArray = (Employee[]) Reference.managers.toArray();
		Store newStore = new Store(storeName, managerArray[managerSelected]);
		//Allow no duplicates!!!!!!!!
		if(Reference.containsStore(newStore)) {
			Reference.stores.add(newStore); //add the store to list of stores
		}
		
	}
	//----------------------- Employee Role Management --------------------------------------------
	
	/*
	 * Method that finds the employee
	 * If the employee is found, the position is updated
	 * Otherwise, nothing happens
	 */
	void updateRole(Employee employee, Position pos) {
		Employee emp = Reference.findEmployee(employee.getName());
		if(emp.equals(null))
				return;
		if(!emp.getPosition().equals(pos))
			emp.setPosition(pos);
	}
	
	/*
	 * Never pass in a null value here.
	 * Removes the employee from the allEmployees set
	 * Removes the employee from the set that corresponds to the employee's position
	 */
	void removeEmployee(Employee employee) {
		Reference.allEmployees.remove(employee);
		
		switch(employee.getPosition()) {
			case ADMIN:
	 			Reference.admin.remove(employee);
	 			break;
			case MANAGER:
				Reference.managers.remove(employee);
				break;
			default:
				Reference.staff.remove(employee);
				break;
		}
		
		
	}
	
	void createEmployee(String name) {
		Employee newEmp = new Employee(name);
	}
	void createEmployee(String name, Position pos) {
		
		Employee newEmp = new Employee(name, pos);
	}
	//-----------------------Category Management---------------------------------------------------
	
	/*
	 * Method that creates categories and inserts them into the category set.
	 * if it already exists, then it doens't do anything. 
	 */
	void createCategory(String newCat) {
		Reference.categories.add(newCat); 
	}
	/*
	 * Method that removes a category from the set
	 * If there are still items in the inventory that are in this category, 
	 * then the category doesn't get removed.
	 */
	void removeCategory(String category) {
		for(Item item : Reference.items) {
			if(item.category == category)
			{
				System.out.println("There are still items in this category. "
						+ "Please sell all items in category before deleting");
				return;
			}
		}
		Reference.categories.remove(category); //removes from category set if it exists
	}

//----------------------Item Management---------------------------------------------------------
	/*
	 * Method that creates the items
	 */
	void createItem(String nameOfItem, double price, String category, Suppliers supplier) {
		
		Item newItem = new Item(nameOfItem, price, category, supplier);
		if(Reference.containsItem(newItem))
			return;
		else
			Reference.items.add(newItem);
		
	}
	/*
	 * Make static until can find a way to get only this method
	 * to be accessible to the Store Managers
	 */
	public static boolean buyItem(String nameOfItem, int amountOfItem, Store store) { 
		/*
		 * finds if there's the item is already in inventory 
		 * if so, creates a purchase order with purchased or pending status
		 * otherwise creates a purchase order with denied status.
		 */
		Item newItem = Reference.findItem(nameOfItem);
		
		if(newItem != null) {
			PurchaseOrder newPO = new PurchaseOrder(store, newItem, amountOfItem);
			if(Reference.position == Position.ADMIN) {
				newPO.status = OrderStatus.PURCHASED;
			}
			else if(Reference.position == Position.MANAGER) {
				newPO.status = OrderStatus.PENDING;
			}
			else if(Reference.position == Position.STAFF) {
				System.out.println("Staff can't place orders!");
				newPO.status = OrderStatus.DENIED;
			}
			return true;
		}
		else {
			System.out.println("Cannot buy item that doesn't exist in company DB. "
					+ "Please request item for future purchases.");
			PurchaseOrder newPO = new PurchaseOrder(store, newItem, amountOfItem);
			newPO.status = OrderStatus.DENIED;
			return false;
		}
		
	}
//---------------------Purchase Order Management------------------------------------------------
	void DisplayAllPurchaseOrders() {
		//Print all POs
		for(PurchaseOrder PO : Reference.POs) {
				PO.displayPurchaseOrder();
		}
	}
	void DisplayPOsByStore(Store store) {
		for(PurchaseOrder PO : Reference.POs) {
			if(PO.store == store) {
				PO.displayPurchaseOrder();
			}
		}
	}
	
		/*check if category exists, if not return
		*otherwise look through all POs, and check item category
		*if a match, display purchase order
		* otherwise, return
		*/
	void DisplayPOsByCategory(String category) {
		for(PurchaseOrder PO : Reference.POs) {
			if(PO.item.category.equals(category)) {
				PO.displayPurchaseOrder();
			}
		}
	}
	
}

class PurchaseOrder{
	Store store;
	Item item;
	int quantity;
	Suppliers supplier;
	int PONumber;
	OrderStatus status;
	
	PurchaseOrder(Store store, Item item, int quantity){
		this.store = store;
		this.item = item;
		this.quantity = quantity;
		this.supplier = item.supplier;
		this.PONumber  = Reference.PURCHASEORDERNUMBER++;
		Reference.POs.add(this);
	}
	
	public void displayPurchaseOrder(){
		System.out.println("Purchase Order Number: " + this.PONumber);
		System.out.println("Item: " + this.item.nameOfItem);
		System.out.println("Price: " + this.item.price);
		System.out.println("Store: " + this.store.getNameOfStore());
		System.out.println("--------------------------------------");
	}
	
}

class Store{
	
	
	private String nameOfStore;
	Map <Item, Integer> itemsInStore = new HashMap<Item, Integer>();
	private Employee manager;
	Employee staffMember;
	
	void setNameOfStore(String nameOfStore) {
		this.nameOfStore = nameOfStore;
	}
	String getNameOfStore() {
		return this.nameOfStore;
	}
	void setManager(Employee newMan) {
		this.manager = newMan;
	}
	Employee getManager() {
		return this.manager;
	}
	Store(String nameOfStore, Employee manager) {
		this.nameOfStore = nameOfStore;
		Reference.stores.add(this);
		System.out.println("Store Created: " + this.nameOfStore);
	}
	
	boolean sellItem(Item item, int quantity) {
		if(!itemsInStore.containsKey(item)) {
			System.out.println("We don't have that item.");
			return false;
		}
		if(itemsInStore.get(item) >= quantity) {
			itemsInStore.merge(item, -quantity, Integer::sum);
			return true;
		}
		else {
			System.out.println("Not enough of that item in inventory, we must order more first.");
			return false;
		}
	}
	
	boolean addItem(Item item) {
		int initialQuantity = 30;
		if(itemsInStore.containsKey(item)) {
			System.out.println("Cannot add an item that already exists");
			return false;
		}
		else {
			itemsInStore.put(item, initialQuantity);
			return true;
		}
	}
	
	
		
	void buyItems(Item item, int amount) {
		if(AdminOperations.buyItem(item.nameOfItem, amount, this)) {
			/*
			 * if key doesn't exist, puts it in map with the amount, 
			 * Otherwise it sums amount that is in there with the amount given
			 */
			itemsInStore.merge(item, amount, Integer::sum); 
		}
	}
	void printAllItemInventory() {
		for(Item item : Reference.items) {
			item.printItemInformation();
		}
	}
	
}



class Employee{
	String name;
	private Position position;
	Store store;
	
	Employee(String name, Position position, Store store) {
		this.name = name;
		this.position = position;
		this.store = store;
	}
	Employee(String name, Position position) {
			this.name = name;
			this.position = position;
			this.store = null;
	}
	Employee(String name){
		this.name = name;
		this.position = null;
		this.store = null;
	}
	
	String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	void setName(Position pos) {
		this.position = pos;
	}

	/*
	 * method that returns whether or not this employee is a manager or not.
	 */
	boolean isManager() {
		return (this.position.equals(Position.MANAGER));
	}
	boolean isAdmin() {
		return (this.position.equals(Position.ADMIN));
	}
	boolean isStaff() {
		return (this.position.equals(Position.STAFF));
	}
	
	Position getPosition() {
		return this.position;
	}
	void setPosition(Position position) {
		this.position = position;
	}
}

class Main{
	public static void main(String[] args) {
		Employee manager1 = new Employee("Phil", Position.MANAGER);
		
		Store storeOne = new Store("MyFirstStore",manager1);
		Store secondStore = new Store("Second Store", manager1);
		Store thirdStore = new Store ("Best Buy Above All", manager1);
		
		
		
		
		
		
		
	}
	}
