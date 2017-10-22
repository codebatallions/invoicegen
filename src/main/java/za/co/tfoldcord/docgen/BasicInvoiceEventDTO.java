package za.co.tfoldcord.docgen;

import java.util.List;

public class BasicInvoiceEventDTO {
	private String teamName;
	private String teamContact;
	private String teamAddress;
	private String eventName;	
	private String packageName;
	private String packageAmount;
	private List<String> activities;
	private String paymentDetails;
	private String teamTotal;
	
	
	public BasicInvoiceEventDTO() {
		
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamContact() {
		return teamContact;
	}
	public void setTeamContact(String teamContact) {
		this.teamContact = teamContact;
	}
	public String getTeamAddress() {
		return teamAddress;
	}
	public void setTeamAddress(String teamAddress) {
		this.teamAddress = teamAddress;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getPackageAmount() {
		return packageAmount;
	}
	public void setPackageAmount(String packageAmount) {
		this.packageAmount = packageAmount;
	}

	public List<String> getActivities() {
		return activities;
	}

	public void setActivities(List<String> activities) {
		this.activities = activities;
	}

	public String getTeamTotal() {
		return teamTotal;
	}

	public void setTeamTotal(String teamTotal) {
		this.teamTotal = teamTotal;
	}

	public String getPaymentDetails() {
		return paymentDetails;
	}
	public void setPaymentDetails(String paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
	
	
	
}
