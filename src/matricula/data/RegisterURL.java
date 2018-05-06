package matricula.data;

public class RegisterURL {

	public final static String register0210Url = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761654&count=23&sess_id=a65e8037086522b9c389c3d5aa93a629&key=8cb3b3529d51fbf49811ebc47c7f35af";

	public final static String marker = "<<>>";
	public final static String register0210UrlMarker = "http://www.data.matricula.info/php/gtpc.php?ar_id=3670&be_id=2480&ve_id=761654&count=<<>>&sess_id=a65e8037086522b9c389c3d5aa93a629&key=8cb3b3529d51fbf49811ebc47c7f35af";

	public static String getUrlWithCount(int count) {
		String result = register0210UrlMarker.replace(marker, Integer.toString(count));
		return result;
	}

}
