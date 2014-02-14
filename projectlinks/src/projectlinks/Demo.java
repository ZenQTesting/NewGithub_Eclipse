package projectlinks;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLEditorKit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
 
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
 
public class Demo extends HTMLEditorKit.ParserCallback{
                private WebDriver driver;
                private WebElement element;
                private JOptionPane dialog;
                private static List<String> hrefValue=new ArrayList();
                
                @BeforeClass
                public void setup(){
                                
                                FirefoxProfile fp=new FirefoxProfile(new File("C:\\Users\\Admin\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\3oag0d6p.default"));
                                this.driver=new FirefoxDriver(fp);           
                }
                
                @Test(description="Checks the status of All llinks present in the Page")
                public void linkChecker()throws Exception{
                                List<String> urls;
                                Iterator itr1,itr2;
                                String URL,curentURL,App;
                                dialog=new JOptionPane();
                                App="http://www.certivox.com/default.aspx";
                                //App=JOptionPane.showInputDialog("Please enter Application URL... ");
                                if(App==null || App==" ")
                                                System.exit(1);
                                System.out.println("..............$Main Page$............ "+App);
                                driver.get(App);
                                curentURL=driver.getCurrentUrl();
                                String ps=driver.getPageSource();
                                
                                //URL turl = new URL(App);
                                Reader reader = new InputStreamReader(new ByteArrayInputStream(ps.getBytes()));
                                new ParserDelegator().parse(reader, new Demo(), false);
                                
                                
                                urls=getPageURLS();
                                itr1=urls.iterator();
                                while(itr1.hasNext()){
                                                URL=(String)itr1.next();
                                                if(!curentURL.equalsIgnoreCase(URL)){
                                                                System.out.println("..............$Sub Page$............"+URL);
                                                                driver.get(URL);
                                                                String ps2=driver.getPageSource();
                                                                //URL turl1 = new URL(App);
                                                                Reader reader1 = new InputStreamReader(new ByteArrayInputStream(ps2.getBytes()));
                                                                new ParserDelegator().parse(reader1, new Demo(), false);
                                                                
                                                                try{Thread.sleep(2000);}catch(Exception e){}
                                                                urls=getPageURLS();      
                                                }
                                }
                }
 
                public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
                      if (t == HTML.Tag.A) {
                                  String href;
                                  href=(String)a.getAttribute(HTML.Attribute.HREF);
                                  if(href!="" && href!=" " && href!=null)
                                                  hrefValue.add(href);
                                  
                      }
                    }
                
                
                public List<String> getPageURLS()throws Exception{
                                String xpath,url,durl;
                                List<String> urls=new ArrayList();
                                for(String href:hrefValue){
                                                boolean flag=true;
                                                xpath=".//*[contains(@href,'"+href+"')]";
                                                try{
                                                                element=driver.findElement(By.xpath(xpath));
                                                                url=(String)element.getAttribute("href");
                                                                if(url.contains("http://")||url.contains("https://")||url.contains("www")){
                                                                                Iterator it2=urls.iterator();
                                                                                while(it2.hasNext()){
                                                                                                durl=(String)it2.next();
                                                                                                if(durl.equals(url)){flag=false;break;}
                                                                                }
                                                                                if(flag){
                                                                                                urls.add(url);
                                                                                                try {
                                                                                                      HttpURLConnection.setFollowRedirects(false);
                                                                                                      HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                                                                                                      con.setConnectTimeout(100000);
                                                                                                      con.setRequestMethod("GET");
                                                                                                      System.out.println(con.getResponseCode()+": "+url);
                                                                                                    }
                                                                                                    catch (Exception e) {
                                                                                                       //e.printStackTrace();
                                                                                                                System.out.print(e);
                                                                                                                System.out.println(" with "+url);
                                                                                                    }
                                                                                }
                                                                }
                                                                
                                                }catch(NoSuchElementException e){
                                                                                
                                                }catch(NullPointerException e){
 
                                                }
                      }
                                hrefValue.clear();
                                return urls;
                }
                
                @AfterClass
                public void tearDown(){
                                driver.quit();
                                
                }
}
