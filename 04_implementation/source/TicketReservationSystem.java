package チケット予約システム;

public class TicketReservationSystem {

	private CUI cui;

	private TicketList ticketList;

	private MemberList memberList;

	private ReservationList reservationList;
	
	private Member currentMember;

	public TicketReservationSystem(TicketList ticketList, MemberList memberList, ReservationList reservationList) {

	}

	public Member login() {
		return null;
	}

	public void logout() {

	}

	public void makeReservation() {

	}

	public void viewTicket() {

	}

	public void viewReservation() {

	}

	public void cancelReservation() {
		reservationList.getAllReservation(currentMember.getId());
	}

	public void start() {

	}

}
