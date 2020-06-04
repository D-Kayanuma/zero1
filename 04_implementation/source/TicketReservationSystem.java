package チケット予約システム;

import java.util.Date;

public class TicketReservationSystem {

	private CUI cui;

	private TicketList ticketList;

	private MemberList memberList;

	private ReservationList reservationList;
	
	private Member currentMember;

	public TicketReservationSystem(TicketList ticketList, MemberList memberList, ReservationList reservationList) {
		this.ticketList = ticketList;
		this.memberList = memberList;
		this.reservationList = reservationList;
		cui = new CUI();
	}

	public Member login() {
		Member user = null;
		while(user == null) {
			String ID = cui.inputID();
			String password = cui.inputPW();
			user = memberList.searchMember(ID,password);
			if(user == null) {
				cui.showMessage("ログイン情報が間違っています");
			}
		}
		return user;
	}

	public void logout() {
		this.currentMember = null;
	}

	public void makeReservation() {
		this.viewTicket();
		Date date = new Date();
		boolean judge = true;
		Ticket ticket = null;
		int ticketAmount = 0;
		while(ticket == null) {
			int inputTicketNo = cui.inputTicketNo();
			for(Ticket tmpTicket : ticketList.getAllTicket()) {
				if(inputTicketNo == tmpTicket.getticketNo()) {
					ticket = tmpTicket;
					break;
				}
			}
			if(ticket == null) {
				System.out.println("指定されたチケットは存在しません");
				continue;
			}
			int inputTicketAmount = cui.inputTicketAmount();
			if(inputTicketAmount > ticket.getStock()) {
				ticket = null;
				System.out.println("在庫がありません");
			}
			else {
				ticketAmount = inputTicketAmount;
			}
			
		}
		boolean j = cui.confirm("予約してもよろしいですか？");
		if(j == true) {
			ticket.reduceStock(ticketAmount);
			int reservationNo = (int)(Math.random()*10000);
			for(Reservation res : reservationList.getReservationList()) {
				if(res.getReservationNo() == reservationNo) {
					reservationNo++;
				}
			}
			Reservation  res = new Reservation(reservationNo,currentMember,ticket,ticketAmount, date);
			reservationList.addReservation(res);
			reservationList.getAllReservation(currentMember.getId());
			cui.display(reservationList.getAllReservation(currentMember.getId()));
		}
		
	}
	
	public void viewTicket() {
		cui.display(ticketList.getAllTicket());
	}


	public void viewReservation() {
		Reservation[] reservationList = this.reservationList.getAllReservation(currentMember.getId());
		
		if(reservationList != null) {
			cui.display(reservationList);
		}
		else {
			cui.showMessage("現在予約されているチケットはありません。\n機能選択画面へ戻ります。\n");
			return;
		}
	}

	public void cancelReservation() {
		Reservation[] reservationList = this.reservationList.getAllReservation(currentMember.getId());
		int inputNumber = 0;
		int canceledAmount = 0;
		
		if(reservationList != null) {
			cui.display(reservationList);
		}
		else {
			cui.showMessage("現在予約されているチケットはありません。\n機能選択画面へ戻ります。\n");
			return;
		}
		boolean reservationFindFlag = true;
		while(reservationFindFlag) {
			boolean con = cui.confirm("予約キャンセルを続けますか？");
			if(con == false) {
				cui.showMessage("機能選択画面に戻ります。\n");
				return;
			}
			
			while(true) {
				inputNumber = cui.inputReservationNo();
				
				if(inputNumber != -1) {
					break;
				}
			}
			
			for(Reservation res : reservationList.getReservationList()) {
				int resNum = res.getReservationNo();
				
				if(inputNumber == resNum) {
					canceledAmount = res.getAmount();
					reservationFindFlag = false;
					break;
				}
			}
			cui.showMessage("予約番号が一致していません。再入力してください");
		}
		
		if(cui.confirm("このまま予約をキャンセルしますか？")) {
			this.ticketList.addTicketStock(inputNumber,canceledAmount);
			this.reservationList.deleteReservation(inputNumber);
		}
		else {
			return;
		}
	}

	public void start() {
		while(true) {
			currentMember = login();
			while(currentMember != null) {
				SystemFunction func = cui.selectFunction();
				System.out.println(func == SystemFunction.Logout);
				if(func == SystemFunction.Logout) {
					logout();
					break;
				}
				switch(func) {
					case TicketReservation:
						makeReservation();
						break;
					case TicketViewing:
						viewTicket();
						break;
					case CancelReservation:
						cancelReservation();
						break;
					case ReservationViewing:
						viewReservation();
						break;
					case Quit:
						return;
					default:
						cui.showMessage("入力値が不適切です");
				}
			}
		}

	}

}
