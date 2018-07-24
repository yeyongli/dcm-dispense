package com.kurumi.dispense;
import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kurumi.dispense.entity.Patient;
import com.kurumi.dispense.service.PatientService;

public class PatientServiceTest {

	@Test
	public void test() {
		//检查和患者都不存在
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		PatientService patientService = (PatientService) context.getBean("patientService");
		
		Patient patient = new Patient();
		patient.setPatientID("111");
		patient.setPatientsName("zhangshang");
		patient.setPatientsNameCn("张三");
		patient.setPatientsBirthDate("2017-03-04");
		patient.setPatientsSex("男");
		patient.setPatientsAge(11);
		patient.setPatientsHeight("11.3");
		patient.setPatientsWeight("70");
		patient.setStudyInstanceUID("39234023940294");
		patient.setDepartmentName("销售部");
		patient.setDepartmentNumber("111");
		patient.setSessionId("2222");
		
		patientService.insertPatient(patient);
		
		//基础和患者信息都存在, 只修改患者的信息
		patientService.insertPatient(patient);
	}
	
	@Test
	public void test1() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		PatientService patientService = (PatientService) context.getBean("patientService");
		
		//检查为空,添加新的一条患者的记录
		Patient patient = new Patient();
		patient.setPatientID("111-1");
		patient.setPatientsName("zhangshang-1");
		patient.setPatientsNameCn("张三-1");
		patient.setPatientsBirthDate("2017-03-04");
		patient.setPatientsSex("男-1");
		patient.setPatientsAge(11);
		patient.setPatientsHeight("11.3-1");
		patient.setPatientsWeight("70-1");
		//patient.setStudyInstanceUID("39234023940294-1");
		patient.setDepartmentName("销售部-1");
		patient.setDepartmentNumber("111-1");
		patient.setSessionId("2222-1");
		
		patientService.insertPatient(patient);
	}
	
	
	
	
	
	
	
	
	
}
