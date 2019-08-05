import it.ma.MySpringBootApplication;
import it.ma.service.MqttService;
import it.ma.domain.Equipment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBootApplication.class)
public class test {
    @Autowired
   private MqttService mqttService;
    @Test
   public  void  test(){
       Equipment equipment=new Equipment();
       equipment.setMessage("djsgjfdlkg");
       equipment.setEquipment_name("ma");
       equipment.setId(1);
        mqttService.insert(equipment);
   }

}
