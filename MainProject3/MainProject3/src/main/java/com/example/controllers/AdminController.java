package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.daos.AdminRepository;
import com.example.daos.ComplaintDao;
import com.example.daos.UserDao;
import com.example.entities.Complaint;
import com.example.entities.DeptName;
import com.example.entities.User;
import com.example.helper.Helper;
import com.example.services.ComplaintActions;
import com.example.services.DeptNameAction;

@CrossOrigin
@RestController
@RequestMapping("/")
public class AdminController {

	@Autowired
	private UserDao userRepository;
	
	@Autowired
	private ComplaintDao complaintRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private ComplaintActions complaintAction;
	
	@Autowired
	private DeptNameAction deptnameaction;
	
	@Autowired
	private Helper helper;
	
	
//	@GetMapping("/getallusers")
//     public List<User> getAllUser(){
//		return userRepository.findAll();
//		
//	}
//	
//	@GetMapping("/getjoinusers")
//	public List<Complaint> getJoinInformation(){
//		return complaintRepository.findAll();
//	}
	
	@PostMapping("/viewadmincomplaints")
	public ResponseEntity<List<Complaint>> getcomplaints(@RequestBody String userId) {
		try {
			System.out.println(userId);
      //List<String> pincodes= adminRepository.findForPincode(userId);
      //System.out.println(pincodes);
			String s=userId.substring(0,userId.length()-1);
			System.out.println(s);
   List<Complaint> complaints=complaintRepository.findForComplaints(Integer.parseInt(s));
   System.out.println(complaints);
			return ResponseEntity.ok(complaints);
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/accept")
	public ResponseEntity<?> updatestatus(@RequestBody int complaintId, String dept ) {
  
	   try {
		Complaint complaint=complaintAction.getComplaint(complaintId);
		   complaint.setAcknowledgement("Accepted");//work in progress
		   complaint.setDept(dept);
		  // DeptName deptname=deptnameaction.getDeptname(complaintId);
		 //  deptname.setPincode(dept);
		  // deptname.setAdharId("accepted");
		   User user=userRepository.findByUserId(Integer.parseInt(complaint.getUserId()));
		   helper.sendEmail(user.getEmail(),"Complaint Acceptance", "Hi,\r\n"
		   		+ "\r\n"
		   		+ "Thanks for contacting Municipal Corporation Complaint Portal!\r\n"
		   		+ "\r\n"
		   		+ " Your complaint has been accepted.and forwarded to"+dept+"\r\n"
		   		+ "\r\n"
		   		+ "Note:This is an automatically generated email – please do not reply to it.\r\n"
		   		+ "\r\n"
		   		+ "With regards,\r\n"
		   		+ "Admin");
		   complaintRepository.save(complaint);
		   
			return ResponseEntity.ok("success");
	} catch (Exception e) {
		return new ResponseEntity<>("somethinf went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	}
	
	@PostMapping("/reject")
	public ResponseEntity<?> updaterejected(@RequestBody Complaint complaintinput) {
  
		try 
		{System.out.println(complaintinput);
				Complaint complaint=complaintAction.getComplaint(complaintinput.getComplaintId());
				System.out.println(complaintinput.getRejectReason());
				System.out.println(complaint);
				if(complaintinput.getDept()==null) {
				   complaint.setAcknowledgement("rejected");
				   complaint.setRejectReason(complaintinput.getRejectReason());
				   System.out.println(complaint);
				   User user=userRepository.findByUserId(Integer.parseInt(complaint.getUserId()));
				   helper.sendEmail(user.getEmail(),"Complaint Rejection", "Hi,\r\n"
				   		+ "\r\n"
				   		+ "Thanks for contacting Municipal Corporation Complaint Portal!\r\n"
				   		+ "\r\n"
				   		+ " Your complaint has been rejected because of insufficient information. We need more information to forward your complaint.\r\n"
				   		+ "\r\n"
				   		+"resaon :"+complaintinput.getRejectReason()+"\r\n"
				   		+ "Note:This is an automatically generated email – please do not reply to it.\r\n"
				   		+ "\r\n"
				   		+ "With regards,\r\n"
				   		+ "Admin");
				   complaintRepository.save(complaint);
					return ResponseEntity.ok("success");}
				else {
				   complaint.setAcknowledgement("accepted");
				   complaint.setDept(complaintinput.getDept());
				   User user=userRepository.findByUserId(Integer.parseInt(complaint.getUserId()));
				   helper.sendEmail(user.getEmail(),"Complaint Acceptance", "Hi,\r\n"
				   		+ "\r\n"
				   		+ "Thanks for contacting Municipal Corporation Complaint Portal!\r\n"
				   		+ "\r\n"
				   		+ " Your complaint has been accepted.and forwarded to"+complaint.getDept()+"\r\n"
				   		+ "\r\n"
				   		+ "Note:This is an automatically generated email – please do not reply to it.\r\n"
				   		+ "\r\n"
				   		+ "With regards,\r\n"
				   		+ "Admin");
				   complaintRepository.save(complaint);
				   
					return ResponseEntity.ok("success");
				   }
				
				   
			} catch (Exception e) {
				return new ResponseEntity<>("somthing went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
				
			}
		}

	@PostMapping("/completed")
	public ResponseEntity<?> updatecomplete(@RequestBody Complaint complaintinput) {
  
		try 
		{ 
			    System.out.println(complaintinput);
				Complaint complaint=complaintAction.getComplaint(complaintinput.getComplaintId());
				System.out.println(complaint);
				if(complaintinput.getDept()==null) {
				   complaint.setAcknowledgement("completed");
				  
				   System.out.println(complaint);
				   User user=userRepository.findByUserId(Integer.parseInt(complaint.getUserId()));
				   helper.sendEmail(user.getEmail(),"Complaint completence", "Hi,\r\n"
				   		+ "\r\n"
				   		+ "Thanks for contacting Municipal Corporation Complaint Portal!\r\n"
				   		+ "\r\n"
				   		+ " Your complaint is been compeleted by "+complaint.getDept()+"\r\n"
				   		+"\r\n"
				   		+ "Note:This is an automatically generated email – please do not reply to it.\r\n"
				   		+ "\r\n"
				   		+ "With regards,\r\n"
				   		+ "Admin");
				   complaintRepository.save(complaint);
					return ResponseEntity.ok("success");}
				else {
				   complaint.setAcknowledgement("accepted");
				   complaint.setDept(complaintinput.getDept());
				   User user=userRepository.findByUserId(Integer.parseInt(complaint.getUserId()));
				   helper.sendEmail(user.getEmail(),"Complaint acceptance", "Hi,\r\n"
				   		+ "\r\n"
				   		+ "Thanks for contacting Municipal Corporation Complaint Portal!\r\n"
				   		+ "\r\n"
				   		+ " Your complaint has been received and send to "+complaint.getDept()+"\r\n"
				   		+ "\r\n"
				   		+ "Note:This is an automatically generated email – please do not reply to it.\r\n"
				   		+ "\r\n"
				   		+ "With regards,\r\n"
				   		+ "Admin");
				   complaintRepository.save(complaint);
				   
					return ResponseEntity.ok("success");
				   }
				
				   
			} catch (Exception e) {
				return new ResponseEntity<>("somthing went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
				
			}
		}
//	@PostMapping("/completed")
//	public ResponseEntity<?> updatecomplete(@RequestBody int complaintId,String dept ) {
//  
//	   try {
//		Complaint complaint=complaintAction.getComplaint(complaintId);
//		   complaint.setAcknowledgement("Completed");
//		  //complaint.setDept(dept);
//		   complaint.getDept();
//		   User user=userRepository.findByUserId(Integer.parseInt(complaint.getUserId()));
//		   helper.sendEmail(user.getEmail(),"Complaint Completence", "Hi,\r\n"
//		   		+ "\r\n"
//		   		+ "Thanks for contacting Municipal Corporation Complaint Portal!\r\n"
//		   		+ "\r\n"
//		   		+ " Your complaint has been completed by "+dept+"\r\n"
//		   		+ "\r\n"
//		   		+ "Note:This is an automatically generated email – please do not reply to it.\r\n"
//		   		+ "\r\n"
//		   		+ "With regards,\r\n"
//		   		+ "Admin");
//		   complaintRepository.save(complaint);
//		   
//			return ResponseEntity.ok("success");
//	} catch (Exception e) {
//		return new ResponseEntity<>("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
//		
//	}
//	}
//	@PutMapping("/updatecomplaint/{userId}/{complaintId}")
//	public User updateStatus(@PathVariable int userId,@PathVariable int complaintId, @RequestBody Complaint complaint) {
//
//		User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Complaint not exist with id :" +complaintId));
//		
//		//List<Complaint> complaintList=user.getComplaint();
//		//complaintList.get(complaintId-1).setAcknowledgement(complaint.getAcknowledgement());
//		
//		//user.setComplaint(complaintList);
//		User updateAck=userRepository.save(user);
//		return updateAck ;
//	}
	
//	@GetMapping("/getcomplaint/{userId}/{complaintId}")
//	public Complaint getComplaintById(@PathVariable int userId,@PathVariable int complaintId) {
//		User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Complaint not exist with id :" +complaintId));
//		Complaint complaint=user.getComplaint();
//		System.out.println(complaint);
////		int complaintId=complaintList.get(complaintId);
//		return complaintRepository.findById(complaintId).orElseThrow(()-> new ResourceNotFoundException("Complaint not exist with id :" +complaintId));
//	}
	
	@PostMapping("/viewadmincomplaints/accepted")
	public ResponseEntity<List<Complaint>> getacceptedcomplaints(@RequestBody String userId) {
		try {
			System.out.println(userId);
      //List<String> pincodes= adminRepository.findForPincode(userId);
      //System.out.println(pincodes);
			String s=userId.substring(0,userId.length()-1);
			System.out.println(s);
   List<Complaint> complaints=complaintRepository.findForAccComplaints(Integer.parseInt(s));
   System.out.println(complaints);
			return ResponseEntity.ok(complaints);
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/viewadmincomplaints/rejected")
	public ResponseEntity<List<Complaint>> getrejectedcomplaints(@RequestBody String userId) {
		try {
			System.out.println(userId);
      //List<String> pincodes= adminRepository.findForPincode(userId);
      //System.out.println(pincodes);
			String s=userId.substring(0,userId.length()-1);
			System.out.println(s);
   List<Complaint> complaints=complaintRepository.findForRejComplaints(Integer.parseInt(s));
   System.out.println(complaints);
			return ResponseEntity.ok(complaints);
		} catch (Exception e) {
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
	

