
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//Veri Tabanına Bağlanma işleleri tamamı bu kadar.
public class Operations {
    //Kullanılacak import edilecekler.
    Connection con = null;
    Statement sta = null;
    PreparedStatement psta = null;
    
    public int bookCount(){
        int label = 0;
        String sorgu = "SELECT COUNT(*) FROM  books_database";
        try {
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery(sorgu);
            rs.next();
            label = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }   
        return label;
    }
    
    public void bookDelete(int id){
        String sorgu = "DELETE FROM books_database WHERE id = ?";
        try {
            psta = con.prepareStatement(sorgu);
            psta.setInt(1,id);
            psta.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void bookUpdate(int id, String new_name,String new_writer,String new_type,String new_publisher){
        String sorgu ="UPDATE books_database SET book_name=?,book_writer=?,book_type=?,book_publisher=? WHERE id=?";
        try {
            psta = con.prepareStatement(sorgu);
            psta.setString(1, new_name);
            psta.setString(2, new_writer);
            psta.setString(3, new_type);
            psta.setString(4, new_publisher);
            psta.setInt(5, id);
            psta.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   //Add işlemlerinin yapılıyor.
    public void bookAdd(String name, String writer, String type, String publisher){
        String sorgu ="Insert into books_database( book_name, book_writer, book_type, book_publisher) VALUES (?,?,?,?)";
        try {
            psta = con.prepareStatement(sorgu);
            psta.setString(1, name);
            psta.setString(2, writer);
            psta.setString(3, type);
            psta.setString(4, publisher);
            psta.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public ArrayList<Book> BookCome(){
        ArrayList<Book> list = new ArrayList<Book>();
        String sorgu = "Select * from books_database";
        
        try {
            sta = con.createStatement();
            ResultSet rs = sta.executeQuery(sorgu);
            
            while(rs.next()){
                int id = rs.getInt("id");
                String book_name = rs.getString("book_name");
                String book_writer = rs.getString("book_writer");
                String book_type = rs.getString("book_type");
                String book_publisher = rs.getString("book_publisher");
                list.add(new Book(id,book_name,book_writer,book_type,book_publisher));
                
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    
        
    }
  
    public boolean Login(String id, String password){
        String sorgu = "Select * from admin where id = ? and password =?";
        try {
             psta = con.prepareStatement(sorgu);
             psta.setString(1,id);
             psta.setString(2, password);
             ResultSet rs = psta.executeQuery();
             return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
            
        }
       
    } 
   
    public Operations() {
        //url-> jdbs:mysql://host:port/db_name;id;password;
        String url = "jdbc:mysql://"+Database.host+":"+Database.port+"/"+Database.db_name+"?useUnicode=true&charakterEncoding=UTF-8";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,Database.id,Database.password);
            System.out.println("Veritabanına Başarıyla Bağlandınız. :");
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Driver Çalışmadı:/");
        } catch (SQLException ex) {
            Logger.getLogger(Operations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection Çalışmadı:/");
        }
    }
    
    public static void main(String[] args) {
        Operations op = new Operations();
        
        
    }
}
