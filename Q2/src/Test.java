import java.sql.*;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;

public class Test {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/BookStore";
    static final String USER = "postgres";
    static final String PASS = "beaubeau";
    //static final String QUERY = "SELECT prereq_id FROM prereq WHERE course_id = '";

    public static void main(String[] args) {

        //init
        //String last = "";
        //ArrayList<String> prereqs = new ArrayList<>();
        Scanner s = new Scanner(System.in);
        String Query = "";
        int choice = 0; // Temp user choice
        int login = 0; // Customer or Employee
        String input;
        boolean run = true; // Is the program running
        boolean confirmed = false; // Is this person logged in
        //main loop
        while (run) {

            //Login
            while (true) {
                if (login == 0) {
                    login = 0;
                    System.out.println("----------------------");
                    System.out.println("Welcome to Look Inna Book!");
                    System.out.println("----------------------");
                    System.out.println("1. Enter as Customer");
                    System.out.println("2. Enter as Employee");
                    System.out.println("----------------------");
                    System.out.print("Enter ('1' or '2') to choose an option: ");
                    login = Integer.parseInt(s.nextLine());
                }
                if (login == 1 || login == 2) {
                    break;
                }
            }

            //Being a customer
            if (login == 1) {
                ArrayList<String> basket = new ArrayList<>();
                ArrayList<Integer> basketQ = new ArrayList<>();
                String pass = "", email = "";

                //Get highest order number
                int orderNum = 1002;
                String tempNum = "";
                Query = "SELECT MAX(order_number) FROM checkout";
                // Open a connection
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(Query);) {

                    while (rs.next()) {
                        tempNum = rs.getString("max");
                        if( !tempNum.isEmpty() && !(tempNum.compareTo("")==0) ) {
                            orderNum = Integer.parseInt(tempNum);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                orderNum++;

                //Get highest order ID
                int orderID = 100000001;
                String tempID = "";
                Query = "SELECT MAX(order_id) FROM book_order";
                // Open a connection
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(Query);) {

                    while (rs.next()) {
                        tempNum = rs.getString("max");
                        if( !tempID.isEmpty() && !(tempID.compareTo("")==0) ) {
                            orderID = Integer.parseInt(tempID);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                orderID++;

                //Login
                while (true) {
                    if (choice == 0 && confirmed == false) {
                        choice = 0;
                        System.out.println("-----------------------------");
                        System.out.println("1. Already have an account");
                        System.out.println("2. Make a new account");
                        System.out.println("-----------------------------");
                        System.out.print("Enter ('1' or '2') to choose an option: ");
                        choice = Integer.parseInt(s.nextLine());
                    }
                    if (choice == 1 || choice == 2) {
                        break;
                    }
                }

                //Log in
                if (choice == 1 && confirmed == false) {
                    String recieved = "";

                    //Confirm account
                    while (true) {
                        System.out.print("Enter your email: ");
                        email = s.nextLine();
                        Query = "SELECT email FROM book_user WHERE email = '" + email + "'";

                        // Open a connection
                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                             Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(Query);) {

                            while (rs.next()) {
                                recieved = rs.getString("email");
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (recieved.compareTo(email) == 0) {

                            while(true){
                                System.out.print("Enter your password: ");
                                pass = s.nextLine();
                                Query = "SELECT pass FROM book_user WHERE pass = '" + pass + "'";

                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        recieved = rs.getString("pass");
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                if(recieved.compareTo(pass) == 0){
                                    confirmed = true;
                                    break;
                                } else if(pass.compareTo("exit")==0){
                                    choice = 2;
                                    break;
                                } else {
                                    System.out.println("Sorry that password isn't linked to an account");
                                    System.out.print("You can type 'exit' to create an account or ");
                                }
                            }
                            if(choice == 2 || confirmed == true){
                                break;
                            }

                        } else if(email.compareTo("exit")==0){

                            choice = 2;
                            break;

                        } else {

                            System.out.println("Sorry that email isn't linked to an account");
                            System.out.print("You can type 'exit' to create an account or ");

                        }
                    }
                }

                //Create account
                if (choice == 2 && confirmed == false) {

                    while(true){
                        System.out.print("Enter your email 'example@example.com': ");
                        email = s.nextLine();
                        System.out.print("Enter your password: ");
                        pass = s.nextLine();
                        System.out.print("Enter your name: ");
                        String username = s.nextLine();
                        System.out.print("Enter your billing address '7 Example Street, Example City, ON E2E1E0': ");
                        String billing = s.nextLine();
                        System.out.print("Enter your shipping address '7 Example Street, Example City, ON E2E1E0': ");
                        String shipping = s.nextLine();
                        System.out.print("Enter your phone number '(905) 555-4545': ");
                        String phone = s.nextLine();
                        Query = "INSERT INTO book_user VALUES ('"+email+"', '"+username+"', '"+pass+"', '"+billing+"', '"+shipping+"', '"+phone+"')";

                        // Open a connection
                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                             Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(Query);) {

                        } catch (SQLException e) {
                            //e.printStackTrace();
                        }

                        confirmed = true;
                        break;
                    }
                }

                //Logged in
                if(confirmed == true){
                    choice = 0;
                    System.out.println("\nLogged in as "+ email);
                    String recieved = "";
                    ArrayList<String> nameBL = new ArrayList();
                    ArrayList<String> isbnBL = new ArrayList();
                    ArrayList<String> pagesBL = new ArrayList();
                    ArrayList<String> authorBL = new ArrayList();
                    ArrayList<String> yearBL = new ArrayList();
                    ArrayList<String> genreBL = new ArrayList();
                    Query = "SELECT * FROM public.book ORDER BY isbn ASC";

                    while(true){
                        // Open a connection
                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                             Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(Query);) {

                            while (rs.next()) {
                                isbnBL.add(rs.getString("isbn"));
                                nameBL.add(rs.getString("book_name"));
                                authorBL.add(rs.getString("author"));
                                yearBL.add(rs.getString("year"));
                                genreBL.add(rs.getString("genre"));
                                pagesBL.add(rs.getString("pages"));
                            }
                            break;

                        } catch (SQLException e) {
                            e.printStackTrace();
                            break;
                        }
                    }

                    System.out.println("\nBook List");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.printf("     %-50s%-50s%-50s%-5s%-50s%-7s\n", "ISBN","Name","Author","Year","Genre","Pages");
                    for(int i = 0; i<isbnBL.size(); i++){
                        System.out.printf("%3d. %-50s%-50s%-50s%-5s%-50s%-7s\n", i+1, isbnBL.get(i),nameBL.get(i),authorBL.get(i),yearBL.get(i),genreBL.get(i),pagesBL.get(i));
                    }
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                    while(true) {

                        ArrayList<String> isbnS = new ArrayList<>();
                        ArrayList<String> nameS = new ArrayList<>();
                        ArrayList<String> authorS = new ArrayList<>();
                        ArrayList<String> yearS = new ArrayList<>();
                        ArrayList<String> genreS = new ArrayList<>();
                        ArrayList<String> pagesS = new ArrayList<>();

                        while (true) {
                            choice = 0;
                            System.out.println("1. Select a book");
                            System.out.println("2. Checkout");
                            System.out.println("3. Track Order");
                            System.out.println("4. Leave Store");
                            System.out.println("-----------------------------");
                            System.out.print("Enter ('1','2','3', or '4') to choose an option: ");
                            choice = Integer.parseInt(s.nextLine());
                            if (choice == 1 || choice == 2 || choice == 3 || choice == 4) {
                                break;
                            } else {
                                System.out.println("Sorry that is wrong ");
                            }
                        }
                        if(choice == 4){
                            run = false;
                            System.out.println("-----------------------------");
                            System.out.println("Thank you for coming!");
                            System.out.println("-----------------------------");
                            break;
                        } else if(choice == 1) {

                            //Select book
                            String temp = "";
                            String search = "";
                            int bookNumber = 0;
                            boolean found = false;

                            //Pick search type
                            while (true) {
                                choice = 0;
                                System.out.println("-----------------------------");
                                System.out.println("1. ISBN");
                                System.out.println("2. Name");
                                System.out.println("3. Author");
                                System.out.println("4. Year");
                                System.out.println("5. Genre");
                                System.out.println("6. Pages");
                                System.out.println("-----------------------------");
                                System.out.print("Enter ('1','2',...,'5','6') to choose an option: ");
                                choice = Integer.parseInt(s.nextLine());
                                if (choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5 || choice == 6) {
                                    if(choice == 1){
                                        search = "isbn";
                                    } else if(choice == 2){
                                        search = "book_name";
                                    }else if(choice == 3){
                                        search = "author";
                                    }else if(choice == 4){
                                        search = "year";
                                    }else if(choice == 5){
                                        search = "genre";
                                    }else if(choice == 6){
                                        search = "pages";
                                    }
                                    break;
                                } else {
                                    System.out.println("Sorry that is wrong ");
                                }
                            }

                            //Search for book
                            while (true) {
                                System.out.println("-------------------------------");
                                System.out.print("Search for: ");
                                temp = s.nextLine();
                                Query = "SELECT * FROM book WHERE "+search+" = '" + temp + "' ORDER BY isbn ASC";

                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        isbnS.add(rs.getString("isbn"));
                                        nameS.add(rs.getString("book_name"));
                                        authorS.add(rs.getString("author"));
                                        yearS.add(rs.getString("year"));
                                        genreS.add(rs.getString("genre"));
                                        pagesS.add(rs.getString("pages"));
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if(isbnS.isEmpty()){
                                    System.out.println("Sorry there were no books with that");
                                    break;
                                } else if(isbnS.size()==1){
                                    System.out.println("\nSearched Book");
                                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                    System.out.printf("     %-50s%-50s%-50s%-5s%-50s%-7s\n", "ISBN","Name","Author","Year","Genre","Pages");
                                    for(int i = 0; i<isbnS.size(); i++){
                                        System.out.printf("%3d. %-50s%-50s%-50s%-5s%-50s%-7s\n", i+1, isbnS.get(i),nameS.get(i),authorS.get(i),yearS.get(i),genreS.get(i),pagesS.get(i));
                                    }
                                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                    found = true;
                                    break;
                                } else {
                                    while(true) {
                                        System.out.println("\nSearched Book List");
                                        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                        System.out.printf("     %-50s%-50s%-50s%-5s%-50s%-7s\n", "ISBN", "Name", "Author", "Year", "Genre", "Pages");
                                        for (int i = 0; i < isbnS.size(); i++) {
                                            System.out.printf("%3d. %-50s%-50s%-50s%-5s%-50s%-7s\n", i + 1, isbnS.get(i), nameS.get(i), authorS.get(i), yearS.get(i), genreS.get(i), pagesS.get(i));
                                        }
                                        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                                        System.out.print("Choose book ('1','2','3',...): ");
                                        bookNumber = Integer.parseInt(s.nextLine());
                                        if (bookNumber > 0 && bookNumber <= isbnBL.size()) {
                                            bookNumber--;
                                            found = true;
                                            break;
                                        } else {
                                            System.out.println("Sorry that is wrong ");
                                        }
                                    }
                                    break;
                                }
                            }

                            //If you found a book
                            if(found == true) {

                                //Add to basket
                                while (true) {
                                    choice = 0;
                                    System.out.println("1. Add to Basket");
                                    System.out.println("2. Exit");
                                    System.out.println("-----------------------------");
                                    System.out.print("Enter ('1', or '2') to choose an option: ");
                                    choice = Integer.parseInt(s.nextLine());

                                    if (choice == 1 || choice == 2) {

                                        if (choice == 1) {
                                            int quantity = 0;
                                            System.out.println("-----------------------------");
                                            System.out.print("Enter Quantity: ");
                                            quantity = Integer.parseInt(s.nextLine());
                                            System.out.println("-----------------------------");
                                            basket.add(isbnS.get(bookNumber));
                                            basketQ.add(quantity);
                                        }
                                        break;
                                    } else if (choice == 2) {
                                        break;
                                    } else {
                                        System.out.println("Sorry that is wrong ");
                                    }
                                }
                            }
                        } else if(choice == 2) {

                            //Checkout
                            if(basket.isEmpty()){
                                System.out.println("-----------------------------");
                                System.out.println("There is nothing to checkout");
                                System.out.println("-----------------------------");
                                choice = 0;
                            } else{

                                //Print basket
                                System.out.printf("     %-50s%-11s\n", "ISBN", "Quantity");
                                System.out.println("------------------------------------------------------------------------------");
                                for (int i = 0; i < basket.size(); i++) {
                                    System.out.printf("%3d. %-50s%-11d\n", i + 1, basket.get(i), basketQ.get(i));
                                }
                                System.out.println("------------------------------------------------------------------------------");

                                while(true) {
                                    choice = 0;
                                    System.out.println("1. Checkout");
                                    System.out.println("2. Empty Cart");
                                    System.out.println("3. Exit");
                                    System.out.println("-----------------------------");
                                    System.out.print("Enter ('1','2', or '3') to choose an option: ");
                                    choice = Integer.parseInt(s.nextLine());
                                    if (choice == 1) {
                                        //Insert into checkout
                                        String ship,bill;
                                        System.out.print("Enter Shipping Address: ");
                                        ship = s.nextLine();
                                        System.out.print("Enter Billing Address: ");
                                        bill = s.nextLine();
                                        Query = "INSERT INTO checkout VALUES ('"+Integer.toString(orderNum)+"', '"+email+"', '"+bill+"', '"+ship+"');";
                                        // Open a connection
                                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                             Statement stmt = conn.createStatement();
                                             ResultSet rs = stmt.executeQuery(Query);) {

                                        } catch (SQLException e) {
                                            //e.printStackTrace();
                                        }

                                        //Insert the basket into book_orders and update old values based on isbn
                                        for(int i = 0; i < basket.size();i++) {
                                            Query = "INSERT INTO book_order VALUES ('"+orderID+"', '"+basket.get(i)+"', '"+orderNum+"', '"+basketQ.get(i)+"');";
                                            // Open a connection
                                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                                 Statement stmt = conn.createStatement();
                                                 ResultSet rs = stmt.executeQuery(Query);) {
                                                break;

                                            } catch (SQLException e) {
                                                //e.printStackTrace();
                                            }

                                            //get how many are sold and the publishers cut
                                            int leftover= 0;
                                            double publisherCut = 0, ourCut = 0;
                                            String pubid = "";
                                            Query = "SELECT * FROM isbn WHERE isbn = '"+basket.get(i)+"'";
                                            // Open a connection
                                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                                 Statement stmt = conn.createStatement();
                                                 ResultSet rs = stmt.executeQuery(Query);) {

                                                while (rs.next()) {
                                                    leftover = Integer.parseInt(rs.getString("quantity"))-basketQ.get(i);
                                                    publisherCut = Double.parseDouble(rs.getString("percent")) * basketQ.get(i) * Double.parseDouble(rs.getString("price"));
                                                    ourCut = (basketQ.get(i) * Double.parseDouble(rs.getString("price"))) - publisherCut;
                                                    pubid = rs.getString("publisher_id");
                                                }

                                            } catch (SQLException e) {
                                                //e.printStackTrace();
                                            }

                                            //Get sold, expenses, and sales from store
                                            int soldAlready = 0;
                                            double moreSales = 0,moreExpenses = 0;
                                            Query = "SELECT * FROM store WHERE isbn = '"+basket.get(i)+"'";
                                            // Open a connection
                                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                                 Statement stmt = conn.createStatement();
                                                 ResultSet rs = stmt.executeQuery(Query);) {

                                                while (rs.next()) {
                                                    soldAlready = Integer.parseInt(rs.getString("sold"));
                                                    moreSales = Double.parseDouble(rs.getString("sales"));
                                                    moreExpenses = Double.parseDouble(rs.getString("expenses"));
                                                }

                                            } catch (SQLException e) {
                                                //e.printStackTrace();
                                            }

                                            //get banking from publisher
                                            double banking = 0;
                                            Query = "SELECT publisher.banking FROM isbn INNER JOIN publisher ON isbn.publisher_id = publisher.publisher_id WHERE isbn = '"+basket.get(i)+"'";
                                            // Open a connection
                                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                                 Statement stmt = conn.createStatement();
                                                 ResultSet rs = stmt.executeQuery(Query);) {

                                                while (rs.next()) {
                                                    banking = Double.parseDouble(rs.getString("banking"));
                                                }

                                            } catch (SQLException e) {
                                                //e.printStackTrace();
                                            }

                                            //Update banking in publisher
                                            banking += publisherCut;
                                            Query = "UPDATE publisher SET banking = '"+banking+"' WHERE publisher_id = '"+pubid+"'";
                                            // Open a connection
                                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                                 Statement stmt = conn.createStatement();
                                                 ResultSet rs = stmt.executeQuery(Query);) {
                                                break;

                                            } catch (SQLException e) {
                                                //e.printStackTrace();
                                            }

                                            //Update sold, expenses, and sales in store
                                            Query = "UPDATE store SET sold = '"+( soldAlready + basketQ.get(i) )+"', sales = '"+( moreSales + ourCut )+"', expenses = '"+( moreExpenses + publisherCut )+"'  WHERE isbn = '"+basket.get(i)+"'";
                                            // Open a connection
                                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                                 Statement stmt = conn.createStatement();
                                                 ResultSet rs = stmt.executeQuery(Query);) {
                                                break;

                                            } catch (SQLException e) {
                                                //e.printStackTrace();
                                            }

                                            //Update quantity in isbn
                                            Query = "UPDATE isbn SET quantity = '"+leftover+"'  WHERE isbn = '"+basket.get(i)+"'";
                                            // Open a connection
                                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                                 Statement stmt = conn.createStatement();
                                                 ResultSet rs = stmt.executeQuery(Query);) {
                                                break;

                                            } catch (SQLException e) {
                                                //e.printStackTrace();
                                            }

                                            orderID++;
                                        }
                                        orderNum++;
                                        System.out.println("-----------------------------");
                                        System.out.println("Order Checked Out!");
                                        System.out.println("-----------------------------");
                                        basket.clear();
                                        basketQ.clear();
                                        break;

                                    } else if (choice == 2) {
                                        System.out.println("-----------------------------");
                                        System.out.println("Basket is empty ");
                                        System.out.println("-----------------------------");
                                        basket.clear();
                                        basketQ.clear();
                                        break;

                                    } else if(choice == 3){
                                        break;
                                    } else {
                                        System.out.println("Sorry that is not a choice");
                                    }
                                }
                            }
                        } else if(choice == 3) {

                            //Track order
                            ArrayList<Integer> numO = new ArrayList();
                            ArrayList<String> shippingO = new ArrayList();
                            Query = "SELECT * FROM checkout WHERE email = '"+email+"' ORDER BY order_number ASC";

                            // Open a connection
                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                 Statement stmt = conn.createStatement();
                                 ResultSet rs = stmt.executeQuery(Query);) {

                                while (rs.next()) {
                                    numO.add(Integer.parseInt(rs.getString("order_number")));
                                    shippingO.add(rs.getString("shipping"));
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            if(numO.isEmpty()){
                                System.out.println("You have no orders being shipped");
                            } else {
                                System.out.printf("     %-15s%-50s\n", "Order Number", "Shipping Address");
                                System.out.println("------------------------------------------------------------------------------");
                                for (int i = 0; i < numO.size(); i++) {
                                    System.out.printf("%3d. %-15d%-50s\n", i + 1, numO.get(i), shippingO.get(i));
                                }
                                System.out.println("------------------------------------------------------------------------------");
                                int track;
                                System.out.print("Enter the Order Number to track: ");
                                track = Integer.parseInt(s.nextLine());
                                System.out.println("--------------------------------------------------------------");
                                System.out.println("That order is leaving the warehouse located in 'Ottawa, Canada' right now");
                                System.out.println("--------------------------------------------------------------");
                            }
                        }
                    }
                }

            //Being a store manager
            } else {
                if(confirmed == false) {
                    while (true) {
                        System.out.print("Enter your username: ");
                        String username = s.nextLine();

                        if (username.compareTo("exit") == 0) {
                            login = 1;
                            break;
                        }

                        System.out.print("Enter your password: ");
                        String pass = s.nextLine();

                        if (username.compareTo("boss") == 0 && pass.compareTo("admin") == 0) {
                            confirmed = true;
                            break;
                        } else {
                            System.out.println("That is wrong try again or type 'exit' instead of a username to login as a customer");
                        }
                    }
                } else {
                    choice = 0;
                    System.out.println("-----------------------------");
                    System.out.println("Logged in as BOSS");
                    System.out.println("-----------------------------");

                    //Get highest isbn
                    int storeISBN = 12100;
                    String tempISBN = "";
                    Query = "SELECT MAX(isbn) FROM store";
                    // Open a connection
                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                         Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(Query);) {

                        while (rs.next()) {
                            tempISBN = rs.getString("max");
                            if( !tempISBN.isEmpty() && !(tempISBN.compareTo("")==0) ) {
                                storeISBN = Integer.parseInt(tempISBN);
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    storeISBN++;

                    //Get highest publisher_id
                    int storeID = 12100;
                    String tempID = "";
                    Query = "SELECT MAX(publisher_id) FROM publisher";
                    // Open a connection
                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                         Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(Query);) {

                        while (rs.next()) {
                            tempID = rs.getString("max");
                            if( !tempID.isEmpty() && !(tempID.compareTo("")==0) ) {
                                storeID = Integer.parseInt(tempID);
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    storeID++;

                    while(true) {
                        while (true) {
                            choice = 0;
                            System.out.println("1. Add/Remove Books");
                            System.out.println("2. Reports");
                            System.out.println("3. Automatic Orders");
                            System.out.println("4. Leave Store");
                            System.out.println("-----------------------------");
                            System.out.print("Enter ('1','2','3', or '4') to choose an option: ");
                            choice = Integer.parseInt(s.nextLine());
                            if (choice == 1 || choice == 2 || choice == 3 || choice == 4) {
                                break;
                            } else {
                                System.out.println("Sorry that is wrong ");
                            }
                        }

                        //Boss Choices
                        if(choice == 1){ //Add or Remove Books

                            String recieved = "";
                            ArrayList<String> nameBL = new ArrayList();
                            ArrayList<String> isbnBL = new ArrayList();
                            ArrayList<String> pagesBL = new ArrayList();
                            ArrayList<String> authorBL = new ArrayList();
                            ArrayList<String> yearBL = new ArrayList();
                            ArrayList<String> genreBL = new ArrayList();
                            Query = "SELECT * FROM public.book ORDER BY isbn ASC";

                            while(true){
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        isbnBL.add(rs.getString("isbn"));
                                        nameBL.add(rs.getString("book_name"));
                                        authorBL.add(rs.getString("author"));
                                        yearBL.add(rs.getString("year"));
                                        genreBL.add(rs.getString("genre"));
                                        pagesBL.add(rs.getString("pages"));
                                    }
                                    break;

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }

                            System.out.println("\nBook List");
                            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                            System.out.printf("     %-50s%-50s%-50s%-5s%-50s%-7s\n", "ISBN","Name","Author","Year","Genre","Pages");
                            for(int i = 0; i<isbnBL.size(); i++){
                                System.out.printf("%3d. %-50s%-50s%-50s%-5s%-50s%-7s\n", i+1, isbnBL.get(i),nameBL.get(i),authorBL.get(i),yearBL.get(i),genreBL.get(i),pagesBL.get(i));
                            }
                            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                            //Make choice
                            while (true) {
                                choice = 0;
                                System.out.println("1. Add Books");
                                System.out.println("2. Remove Books");
                                System.out.println("3. Exit");
                                System.out.println("-----------------------------");
                                System.out.print("Enter ('1','2', or '3') to choose an option: ");
                                choice = Integer.parseInt(s.nextLine());
                                if (choice == 1 || choice == 2 || choice == 3) {
                                    break;
                                } else {
                                    System.out.println("Sorry that is wrong ");
                                }
                            }
                            if(choice == 2){
                                while (true) {
                                    choice = 0;
                                    System.out.println("-----------------------------");
                                    System.out.print("Enter the number ('1','2','3',...) attributed to the book: ");
                                    choice = Integer.parseInt(s.nextLine());
                                    if ( choice > 0 && choice <= isbnBL.size() ) {
                                        break;
                                    } else {
                                        System.out.println("Sorry that is wrong ");
                                    }
                                }

                                Query = "DELETE FROM store WHERE isbn = '"+isbnBL.get(choice-1)+"'";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }
                                System.out.println("-----------------------------");
                                System.out.println("Removed: "+isbnBL.get(choice-1));
                                System.out.println("-----------------------------");

                            } else if(choice == 1){
                                //Book info
                                System.out.print("Enter book name: ");
                                String bookName = s.nextLine();
                                System.out.print("Enter book author: ");
                                String author = s.nextLine();
                                System.out.print("Enter the year the book was published (MUST BE A NUMBER): ");
                                String year = s.nextLine();
                                System.out.print("Enter book genre: ");
                                String genre = s.nextLine();
                                System.out.print("Enter the number of book pages (MUST BE A NUMBER): ");
                                String pages = s.nextLine();

                                //Publisher Info
                                System.out.print("Enter publisher name: ");
                                String publisherName = s.nextLine();
                                System.out.print("Enter publisher email: ");
                                String email = s.nextLine();
                                System.out.print("Enter publisher address: ");
                                String address = s.nextLine();
                                System.out.print("Enter publisher phone number: ");
                                String phone = s.nextLine();

                                //ISBN info
                                System.out.print("Enter publisher percent cut (MUST BE A NUMBER): ");
                                String percent = s.nextLine();
                                System.out.print("Enter book quantity (MUST BE A NUMBER): ");
                                String quantity = s.nextLine();
                                System.out.print("Enter book price (MUST BE A NUMBER): ");
                                String price = s.nextLine();

                                //Querys
                                Query = "INSERT INTO store VALUES ('"+storeISBN+"', '0', '0', '0', '0');";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }

                                Query = "INSERT INTO publisher VALUES ('"+storeID+"', '"+publisherName+"', '"+address+"', '"+email+"', '"+phone+"', '0');";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }

                                Query = "INSERT INTO isbn VALUES ('"+storeISBN+"', '"+storeID+"', '"+quantity+"', '"+percent+"', '"+price+"');";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }

                                Query = "INSERT INTO book VALUES ('"+storeISBN+"', '"+bookName+"', '"+author+"', '"+year+"', '"+genre+"', '"+pages+"');";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }

                                System.out.println("---------------------");
                                System.out.println(storeISBN+" Book Added");
                                System.out.println("---------------------");

                                //Update UID's
                                storeID++;
                                storeISBN++;
                            }
                        } else if(choice == 2){ //Reports
                            String isbn = "";
                            double sales=0,expenses=0;

                            //Make choice
                            while (true) {
                                choice = 0;
                                System.out.println("1. Sales vs Expenses");
                                System.out.println("2. Sales per 'X'");
                                System.out.println("3. Exit");
                                System.out.println("-----------------------------");
                                System.out.print("Enter ('1','2', or '3') to choose an option: ");
                                choice = Integer.parseInt(s.nextLine());
                                if (choice == 1 || choice == 2 || choice == 3) {
                                    break;
                                } else {
                                    System.out.println("Sorry that is wrong ");
                                }
                            }
                            if(choice == 1){ //Sales vs Expenses

                                //Get the sums
                                Query = "SELECT SUM(sales) AS salesSum FROM store";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        sales = Double.parseDouble(rs.getString("salesSum"));
                                    }
                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }
                                Query = "SELECT SUM(expenses) AS expensesSum FROM store";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        expenses = Double.parseDouble(rs.getString("expensesSum"));
                                    }
                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }

                                System.out.println("----------------------------------");
                                if((sales - expenses)>=0){
                                    System.out.printf("The store has made: $%-15.2f\n",(sales - expenses));
                                }else{
                                    System.out.printf("The store has lost: -$%-15.2f\n",((sales - expenses)*-1));
                                }
                                System.out.println("----------------------------------");

                            } else if(choice == 2){ //Sales per something

                                //Get the sum of sales
                                Query = "SELECT SUM(sales) AS salesSum FROM store";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        sales = Double.parseDouble(rs.getString("salesSum"));
                                    }
                                } catch (SQLException e) {
                                    //e.printStackTrace();
                                }

                                //MAke Choice
                                while (true) {
                                    choice = 0;
                                    System.out.println("Sales Per '______'");
                                    System.out.println("-----------------------------");
                                    System.out.println("1. Author");
                                    System.out.println("2. Genre");
                                    System.out.println("3. Publisher");
                                    System.out.println("4. Price");
                                    System.out.println("5. Exit");
                                    System.out.println("-----------------------------");
                                    System.out.print("Enter ('1','2','3',...) to choose an option: ");
                                    choice = Integer.parseInt(s.nextLine());
                                    if (choice >0 && choice <= 6) {
                                        break;
                                    } else {
                                        System.out.println("Sorry that is wrong ");
                                    }
                                }

                                if(choice == 1){

                                    //Get author
                                    System.out.print("Enter author name exactly: ");
                                    String authorName = s.nextLine();
                                    ArrayList<String> isbnX = new ArrayList();
                                    ArrayList<Integer> soldX = new ArrayList();
                                    ArrayList<Double> priceX = new ArrayList();

                                    Query = "SELECT book.isbn, store.sold FROM book INNER JOIN store ON book.isbn = store.isbn WHERE author = '"+authorName+"'";
                                    // Open a connection
                                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                         Statement stmt = conn.createStatement();
                                         ResultSet rs = stmt.executeQuery(Query);) {

                                        while (rs.next()) {
                                            isbnX.add(rs.getString("isbn"));
                                            soldX.add(Integer.parseInt(rs.getString("sold")));
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    for(int i = 0; i<isbnX.size();i++){
                                        Query = "SELECT * FROM isbn WHERE isbn = '"+isbnX.get(i)+"'";
                                        // Open a connection
                                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                             Statement stmt = conn.createStatement();
                                             ResultSet rs = stmt.executeQuery(Query);) {

                                            while (rs.next()) {
                                                priceX.add(Double.parseDouble(rs.getString("price")));

                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    double tempSales = 0;
                                    for(int i = 0; i<isbnX.size();i++){
                                        tempSales += (soldX.get(i)*priceX.get(i));
                                    }
                                    if(tempSales>0) {
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %s is: $%-15.2f\n", authorName, tempSales);
                                        System.out.println("----------------------------------------------------");
                                    }else{
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %s is: NOTHING\n", authorName);
                                        System.out.println("----------------------------------------------------");
                                    }
                                }
                                if(choice == 2){

                                    //Get genre
                                    System.out.print("Enter genre exactly: ");
                                    String genre = s.nextLine();
                                    ArrayList<String> isbnX = new ArrayList();
                                    ArrayList<Integer> soldX = new ArrayList();
                                    ArrayList<Double> priceX = new ArrayList();

                                    Query = "SELECT book.isbn, store.sold FROM book INNER JOIN store ON book.isbn = store.isbn WHERE genre = '"+genre+"'";
                                    // Open a connection
                                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                         Statement stmt = conn.createStatement();
                                         ResultSet rs = stmt.executeQuery(Query);) {

                                        while (rs.next()) {
                                            isbnX.add(rs.getString("isbn"));
                                            soldX.add(Integer.parseInt(rs.getString("sold")));
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    //get all isbns
                                    for(int i = 0; i<isbnX.size();i++){
                                        Query = "SELECT * FROM isbn WHERE isbn = '"+isbnX.get(i)+"'";
                                        // Open a connection
                                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                             Statement stmt = conn.createStatement();
                                             ResultSet rs = stmt.executeQuery(Query);) {

                                            while (rs.next()) {
                                                priceX.add(Double.parseDouble(rs.getString("price")));

                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    double tempSales = 0;
                                    for(int i = 0; i<isbnX.size();i++){
                                        tempSales += (soldX.get(i)*priceX.get(i));
                                    }
                                    if(tempSales>0) {
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %s is: $%-15.2f\n", genre, tempSales);
                                        System.out.println("----------------------------------------------------");
                                    }else{
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %s is: NOTHING\n", genre);
                                        System.out.println("----------------------------------------------------");
                                    }
                                }
                                if(choice == 3){

                                    //Get publisher
                                    System.out.print("Enter publisher exactly: ");
                                    String publisher = s.nextLine();
                                    ArrayList<String> isbnX = new ArrayList();
                                    ArrayList<Integer> soldX = new ArrayList();
                                    ArrayList<Double> priceX = new ArrayList();

                                    Query = "SELECT isbn.isbn FROM isbn INNER JOIN publisher ON publisher.publisher_id = isbn.publisher_id WHERE publisher_name = '"+publisher+"'";
                                    // Open a connection
                                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                         Statement stmt = conn.createStatement();
                                         ResultSet rs = stmt.executeQuery(Query);) {

                                        while (rs.next()) {
                                            isbnX.add(rs.getString("isbn"));
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    //get all prices
                                    for(int i = 0; i<isbnX.size();i++){
                                        Query = "SELECT price FROM isbn WHERE isbn = '"+isbnX.get(i)+"'";
                                        // Open a connection
                                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                             Statement stmt = conn.createStatement();
                                             ResultSet rs = stmt.executeQuery(Query);) {

                                            while (rs.next()) {
                                                priceX.add(Double.parseDouble(rs.getString("price")));
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    //get all solds
                                    for(int i = 0; i<isbnX.size();i++){
                                        Query = "SELECT sold FROM store WHERE isbn = '"+isbnX.get(i)+"'";
                                        // Open a connection
                                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                             Statement stmt = conn.createStatement();
                                             ResultSet rs = stmt.executeQuery(Query);) {

                                            while (rs.next()) {
                                                soldX.add(Integer.parseInt(rs.getString("sold")));
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    double tempSales = 0;
                                    for(int i = 0; i<isbnX.size();i++){
                                        tempSales += (soldX.get(i)*priceX.get(i));
                                    }
                                    if(tempSales>0) {
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %s is: $%-15.2f\n", publisher, tempSales);
                                        System.out.println("----------------------------------------------------");
                                    }else{
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %s is: NOTHING\n", publisher);
                                        System.out.println("----------------------------------------------------");
                                    }
                                }
                                if(choice == 4){

                                    //Get price
                                    System.out.print("Enter price exactly: ");
                                    double price = Double.parseDouble(s.nextLine());
                                    ArrayList<String> isbnX = new ArrayList();
                                    ArrayList<Integer> soldX = new ArrayList();
                                    ArrayList<Double> priceX = new ArrayList();

                                    Query = "SELECT isbn.isbn, store.sold, isbn.price FROM isbn INNER JOIN store ON store.isbn = isbn.isbn WHERE price = '"+price+"'";
                                    // Open a connection
                                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                         Statement stmt = conn.createStatement();
                                         ResultSet rs = stmt.executeQuery(Query);) {

                                        while (rs.next()) {
                                            isbnX.add(rs.getString("isbn"));
                                            soldX.add(Integer.parseInt(rs.getString("sold")));
                                            priceX.add(Double.parseDouble(rs.getString("price")));
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    double tempSales = 0;
                                    for(int i = 0; i<isbnX.size();i++){
                                        tempSales += (soldX.get(i)*priceX.get(i));
                                    }
                                    if(tempSales>0){
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %.2f is: $%-15.2f\n", price, tempSales);
                                        System.out.println("----------------------------------------------------");
                                    }else{
                                        System.out.println("----------------------------------------------------");
                                        System.out.printf("The sales for %.2f is: NOTHING\n", price);
                                        System.out.println("----------------------------------------------------");
                                    }
                                }
                            }
                        } else if(choice == 3){ //Orders
                            System.out.print("Enter a minimum quantity of books: ");
                            int quantity = Integer.parseInt(s.nextLine());
                            ArrayList<String> isbnA = new ArrayList<>();
                            ArrayList<String> pubA = new ArrayList<>();
                            ArrayList<Integer> quantityA = new ArrayList<>();

                            Query = "SELECT * FROM isbn WHERE quantity<"+quantity;
                            // Open a connection
                            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                 Statement stmt = conn.createStatement();
                                 ResultSet rs = stmt.executeQuery(Query);) {

                                while (rs.next()) {
                                    isbnA.add(rs.getString("isbn"));
                                    pubA.add(rs.getString("publisher_id"));
                                    quantityA.add(Integer.parseInt(rs.getString("quantity")));
                                }
                            } catch (SQLException e) {
                                //e.printStackTrace();
                            }

                            System.out.println("------------------------------------------------------------------------------------");
                            for(int i = 0; i< isbnA.size();i++){
                                String tempBName = "",tempPName = "";
                                int tempSold = 0;

                                //get names from db
                                Query = "SELECT * FROM book WHERE isbn = '"+isbnA.get(i)+"'";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        tempBName = rs.getString("book_name");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                //get names from db
                                Query = "SELECT * FROM store WHERE isbn = '"+isbnA.get(i)+"'";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        tempSold = Integer.parseInt(rs.getString("sold_last_month"));
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                Query = "SELECT * FROM publisher WHERE publisher_id = '"+pubA.get(i)+"'";
                                // Open a connection
                                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                     Statement stmt = conn.createStatement();
                                     ResultSet rs = stmt.executeQuery(Query);) {

                                    while (rs.next()) {
                                        tempPName = rs.getString("publisher_name");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                System.out.printf("Sent an email to order "+tempSold+" of the Book called '"+tempBName+"' from the publisher called '"+tempPName+"' for next month\n");
                            }
                            System.out.println("------------------------------------------------------------------------------------");
                        } else if(choice == 4){ //Leave
                            System.out.println("Goodbye Boss! ");
                            run = false;
                            break;
                        }
                    }
                }
            }
        }
    }
}