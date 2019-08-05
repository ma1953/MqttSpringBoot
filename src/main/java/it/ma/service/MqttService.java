package it.ma.service;

import it.ma.domain.Equipment;
import it.ma.mapper.DataMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
@Autowired
private DataMessageMapper dataMessageMapper;
public  void  insert(Equipment equipment){
             dataMessageMapper.insert(equipment);
}

}
