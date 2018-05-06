package genealogy.model;

public class Marriage implements IRecordWithRegisterDate{

	private final RegisterDate dateOfMarriage;
	private Person bride;
	private Person bridegroom;

	public Marriage(RegisterDate dateOfMarriage, Person bride, Person bridegroom) {
		this.dateOfMarriage = dateOfMarriage;
		this.bride = bride;
		this.bridegroom = bridegroom;
	}

	public RegisterDate getDateOfMarriage() {
		return dateOfMarriage;
	}
	
	public Person getBridge(){
		return bride;
	}
	
	public Person getBridegroom(){
		return bridegroom;
	}

	@Override
	public RegisterDate getRegisterDate() {
		return dateOfMarriage;
	}

}
