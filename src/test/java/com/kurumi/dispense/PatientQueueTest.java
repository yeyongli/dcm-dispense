package com.kurumi.dispense;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.kurumi.dispense.entity.Patient;
import com.kurumi.dispense.util.JsonUtils;
import com.kurumi.dispense.util.SendMSMQUtil;


public class PatientQueueTest {

	@Before
	public void before() {
		
	}
	
	@Test
	public void test() {
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
		patient.setPatientKey(UUID.randomUUID().toString());
		String patientJson = JsonUtils.objectToJson(patient);
		
		SendMSMQUtil.putMessageQueue("direct=tcp:192.168.21.85\\private$\\patientQueue", "patientQueue", patientJson, UUID.randomUUID().toString());
	}
	
}
