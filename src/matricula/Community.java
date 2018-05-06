package matricula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Community object provides data files from which Matricula objects can be
 * loaded.
 * 
 * @author Josef Pichler
 * 
 */
public class Community {

	public static Community Hollenstein = new Community( 
			new String[] { 
					"files-0123-01/01-01.txt",
					"files-0123-02/01-02.txt",
					"files-0123-04/01-04.txt", 
					"files-0123-05/01-05.txt",
					"files-01-06/01-06.txt",
					"files-01-07/01-07.txt", 
					"files-01-08/01-08.txt",
					"files-01-09/01-09.txt", 
					"files-01-10/01-10.txt", 
					"files-01-11/01-11.txt", 
					"files-01-12/01-12.txt",
					"files-01-13/01-13.txt", 
					"files-01-14/01-14.txt" },
			new String[] { 
					"files-0123-01/02-01.txt",
					"files-0123-02/02-02.txt",
					"files-0123-04/02-04.txt", 
					"files-0123-05/02-05.txt", 
					"files-02-06/02-06.txt",
					"files-02-07/02-07.txt", 
					"files-02-08/02-08.txt",
					"files-02-09/02-09.txt", 
					"files/02-10.txt" });

	private String[] baptismalRegisterFileNames;
	private String[] marriageRegisterFileName;

	public Community(String[] baptismalRegisterFileNames, String[] marriageRegisterFileName) {
		this.baptismalRegisterFileNames = baptismalRegisterFileNames;
		this.marriageRegisterFileName = marriageRegisterFileName;
	}

	public String[] baptismalRegisterFileNames() {
		return baptismalRegisterFileNames;
	}

	public String[] marriageRegisterFileNames() {
		return marriageRegisterFileName;
	}

	public String[] registerFileNames() {
		List<String> registerFileNames = new ArrayList<String>();
		registerFileNames.addAll(Arrays.asList(baptismalRegisterFileNames));
		registerFileNames.addAll(Arrays.asList(marriageRegisterFileName));

		String[] result = new String[registerFileNames.size()];
		registerFileNames.toArray(result);
		return result;
	}
	
}
