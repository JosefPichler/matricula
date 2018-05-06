package matricula.report;

import genealogy.model.IRecordWithRegisterDate;

import java.util.Comparator;

public class RecordDateComparator implements Comparator<IRecordWithRegisterDate> {

	@Override
	public int compare(IRecordWithRegisterDate a, IRecordWithRegisterDate b) {

		return a.getRegisterDate().compareTo(b.getRegisterDate());
	}

}
