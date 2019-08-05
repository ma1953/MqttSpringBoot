package it.ma.reportmqtt;
import common.utils.JsonUtils;
import it.ma.domain.Equipment;
import it.ma.mapper.DataMessageMapper;
import it.ma.service.MqttService;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;


@Component
public class ReportMqtt  implements MqttCallback {
    @Value("${host}")
    public   String HOST ;
    @Value("${SH_INCOMING_TOPIC}")
    public  String TOPIC;
    @Value("${name}")
    private  String name;
    @Value("${password}")
    private  String passWord ;

    private MqttClient client;
    private MqttConnectOptions options;
     @Autowired
    private MqttService   mqttService;
     private Equipment  equipment;
    String  clientid= String.valueOf(System.currentTimeMillis());
    @PostConstruct
    public  void  result(){
        try {
            // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(name);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(3600);
            // 设置回调
            client.setCallback(this);
            client.connect(options);
            //订阅消息
            int[] Qos  = {1};
            String[] topic1 = {TOPIC};
            client.subscribe(topic1, Qos);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void connectionLost(Throwable throwable) {
        try {
            client.close();
            this.result();
        } catch (MqttException e) {
           e.printStackTrace();
        }

    }

    public void messageArrived(String topic, MqttMessage mqttMessage){
            if(mqttMessage.getPayload().length>0) {
                  if(JsonUtils.parse(new String(mqttMessage.getPayload()), Equipment.class)!=null) {
                         equipment = JsonUtils.parse(new String(mqttMessage.getPayload()), Equipment.class);
                        if(equipment.getId()!=0&&equipment.getMessage()!=null&&equipment.getMessage()!=null)
                                  mqttService.insert(equipment);
                  }
                System.out.println(
                        "接收消息主题:" + topic
                                + "接收消息id:" + mqttMessage.getId()
                                + "接收消息Qos:" + mqttMessage.getQos()
                                + "接收消息内容: " + new String(mqttMessage.getPayload()));
            }

    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
