import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created by Lenovo on 2019/11/4.
 */
public class Test {

  private static Logger logger=Logger.getLogger(Test.class.getClass());



  public void testLog(){
    for(int i=0;i<10;i++){
      logger.debug("debug");
      logger.info("info");
    }
  }

}
