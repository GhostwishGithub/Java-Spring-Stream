package com.dcc.jpa_stream_lab.service;

import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.jpa_stream_lab.repository.ProductsRepository;
import com.dcc.jpa_stream_lab.repository.RolesRepository;
import com.dcc.jpa_stream_lab.repository.ShoppingcartItemRepository;
import com.dcc.jpa_stream_lab.repository.UsersRepository;
import com.dcc.jpa_stream_lab.models.Product;
import com.dcc.jpa_stream_lab.models.Role;
import com.dcc.jpa_stream_lab.models.ShoppingcartItem;
import com.dcc.jpa_stream_lab.models.User;

@Transactional
@Service
public class StreamLabService {
	
	@Autowired
	private ProductsRepository products;
	@Autowired
	private RolesRepository roles;
	@Autowired
	private UsersRepository users;
	@Autowired
	private ShoppingcartItemRepository shoppingcartitems;


    // <><><><><><><><> R Actions (Read) <><><><><><><><><>

    public List<User> RDemoOne() {
    	// This query will return all the users from the User table.
    	return users.findAll().stream().toList();
    }

    public long RProblemOne()
    {
        // Return the COUNT of all the users from the User table.
        // You MUST use a .stream(), don't listen to the squiggle here!
        // Remember yellow squiggles are warnings and can be ignored.
    	return users.findAll().stream().count();
    }

    public List<Product> RDemoTwo()
    {
        // This query will get each product whose price is greater than $150.
    	return products.findAll().stream().filter(p -> p.getPrice() > 150).toList();
    }

    public List<Product> RProblemTwo()
    {
        // Write a query that gets each product whose price is less than or equal to $100.
        // Return the list
        return products.findAll().stream().filter(p -> p.getPrice() <= 100).toList();
    }

    public List<Product> RProblemThree()
    {
        // Write a query that gets each product that CONTAINS an "s" in the products name.
        // Return the list
    	return products.findAll().stream().filter(p -> p.getName().contains("s")).toList();
    }

    public List<User> RProblemFour()
    {
        // Write a query that gets all the users who registered BEFORE 2016
        // Return the list
        // Research 'java create specific date' and 'java compare dates'
        // java create specific date -
        // You may need to use the helper classes imported above!
        Calendar dateToCompare = Calendar.getInstance();
        dateToCompare.set(2016,01,01);

        Date beforeDate = dateToCompare.getTime();
        return users.findAll().stream().filter(u -> u.getRegistrationDate().before(beforeDate)).toList();
    }

    public List<User> RProblemFive()
    {
        // Write a query that gets all of the users who registered AFTER 2016 and BEFORE 2018
        // Return the list

        Calendar dateToCompare = Calendar.getInstance();
        dateToCompare.set(2016,01,01);
        Calendar dateToCompare2 = Calendar.getInstance();
        dateToCompare2.set(2018, 01, 01);

        Date afterDate = dateToCompare.getTime();
        Date beforeDate = dateToCompare2.getTime();
        return users.findAll().stream().filter(u -> u.getRegistrationDate().before(beforeDate) && u.getRegistrationDate().after(afterDate)).toList();
    }

    // <><><><><><><><> R Actions (Read) with Foreign Keys <><><><><><><><><>

    public List<User> RDemoThree()
    {
        // Write a query that retrieves all of the users who are assigned to the role of Customer.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	List<User> customers = users.findAll().stream().filter(u -> u.getRoles().contains(customerRole)).toList();

    	return customers;
    }

    public List<Product> RProblemSix()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "afton@gmail.com".
        // Return the list
        User customerID = users.findAll().stream().filter(r -> r.getEmail().equals("afton@gmail.com")).findFirst().orElse(null);
        List<ShoppingcartItem> idList = shoppingcartitems.findAll().stream().filter(u -> u.getUser().equals(customerID)).toList();
        List<Product> userProducts = new ArrayList<>();
        for (ShoppingcartItem u :
                idList) {
           userProducts.add(products.findById((u.getProduct().getId())).orElse(null));
        }

        return userProducts;
    }

    public long RProblemSeven()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "oda@gmail.com" and returns the sum of all of the products prices.
    	// Remember to break the problem down and take it one step at a time!
        User customerID = users.findAll().stream().filter(r -> r.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        // This works if the customer only has one of each item. Fails if the customer has multiples
        List<ShoppingcartItem> idList = shoppingcartitems.findAll().stream().filter(u -> u.getUser().equals(customerID)).toList();
        long sum = 0;
        for (ShoppingcartItem u :
                idList) {
            sum += (products.findById((u.getProduct().getId())).orElse(null)).getPrice();
        }
    	return sum;

    }

    public List<Product> RProblemEight()
    {
        // Write a query that retrieves all of the products in the shopping cart of users who have the role of "Employee".
    	// Return the list
        Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Employee")).findFirst().orElse(null);
        List<User> customers = users.findAll().stream().filter(u -> u.getRoles().contains(customerRole)).toList();
        List<Product> userProducts = new ArrayList<>();
        for (User i : customers) {
            List<ShoppingcartItem> idList = shoppingcartitems.findAll().stream().filter(u -> u.getUser().equals(i)).toList();
            for (ShoppingcartItem u :
                    idList) {
                userProducts.add(products.findById((u.getProduct().getId())).orElse(null));
            }
        }

    	return userProducts;
        // Does not return the count of the items
        // Why is this one fellow buying 5 apple watches? Alert security please.
    }

    // <><><><><><><><> CUD (Create, Update, Delete) Actions <><><><><><><><><>

    // <><> C Actions (Create) <><>

    public User CDemoOne()
    {
        // Create a new User object and add that user to the Users table.
        User newUser = new User();        
        newUser.setEmail("david@gmail.com");
        newUser.setPassword("DavidsPass123");
        users.save(newUser);
        return newUser;
    }

    public Product CProblemOne()
    {
        // Create a new Product object and add that product to the Products table.
        // Return the product
        Product newProduct = new Product();
        newProduct.setName("Sahleen");
        newProduct.setDescription("Tantalizing");
        newProduct.setPrice(100);
        products.save(newProduct);

    	return newProduct;

    }

    public List<Role> CDemoTwo()
    {
        // Add the role of "Customer" to the user we just created in the UserRoles junction table.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	User david = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
    	david.addRole(customerRole);
    	return david.getRoles();
    }

    public ShoppingcartItem CProblemTwo()
    {
    	// Create a new ShoppingCartItem to represent the new product you created being added to the new User you created's shopping cart.
        // Add the product you created to the user we created in the ShoppingCart junction table.
        // Return the ShoppingcartItem
        User david = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
        ShoppingcartItem cart = new ShoppingcartItem();
        cart.setUser(david);
        cart.setProduct(products.findAll().stream().filter(g -> g.getName().equals("Sahleen")).findFirst().orElse(null));
        cart.setQuantity(400);
        shoppingcartitems.save(cart);
    	return cart;
    	
    }

    // <><> U Actions (Update) <><>

    public User UDemoOne()
    {
         //Update the email of the user we created in problem 11 to "mike@gmail.com"
          User user = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
          user.setEmail("mike@gmail.com");
          return user;
    }

    public Product UProblemOne()
    {
        // Update the price of the product you created to a different value.
        // Return the updated product
        Product product = products.findAll().stream().filter(g -> g.getName().equals("Sahleen")).findFirst().orElse(null);
        product.setPrice(200);
        // due to sudden and disturbing demand
    	return product;
    }

    public User UProblemTwo()
    {
        // Change the role of the user we created to "Employee"
        // HINT: You need to delete the existing role relationship and then create a new UserRole object and add it to the UserRoles table
        Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
        Role employeeRole = roles.findAll().stream().filter(r -> r.getName().equals("Employee")).findFirst().orElse(null);
        User customerID = users.findAll().stream().filter(r -> r.getEmail().equals("mike@gmail.com")).findFirst().orElse(null);
        customerID.removeRole(customerRole);
        customerID.addRole(employeeRole);
    	return customerID;
    }

    //BONUS:
    // <><> D Actions (Delete) <><>

    // For these bonus problems, you will also need to create their associated routes in the Controller file!
    
    // DProblemOne
    // Delete the role relationship from the user who has the email "oda@gmail.com".
    public Role DProblemOne(){
        User customerID = users.findAll().stream().filter(r -> r.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        Role userRole = customerID.getRoles().get(0);
        customerID.removeRole(userRole);
        return userRole;
    }

    // DProblemTwo
    // Delete all the product relationships to the user with the email "oda@gmail.com" in the ShoppingCart table.
    public List<ShoppingcartItem> DProblemTwo(){
        User customerID = users.findAll().stream().filter(r -> r.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        List<ShoppingcartItem> idList = shoppingcartitems.findAll().stream().filter(u -> u.getUser().equals(customerID)).toList();
        for (ShoppingcartItem u :
                idList) {
            shoppingcartitems.delete(u);
        }
        return idList;
    }
    // DProblemThree
    // Delete the user with the email "oda@gmail.com" from the Users table.
    public User DProblemThree(){
        User customerID = users.findAll().stream().filter(r -> r.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        users.delete(customerID);
        return customerID;
        //get rekt, should have ordered more Sahleens
    }
}
