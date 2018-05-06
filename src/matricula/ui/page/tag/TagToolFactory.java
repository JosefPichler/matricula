package matricula.ui.page.tag;

import matricula.model.AddressTag;
import matricula.model.MarriageTag;
import matricula.model.ParentTag;
import matricula.model.PersonTag;
import matricula.model.Tag;

public class TagToolFactory {

	public TagTool create(Tag tag) {
		if (tag instanceof PersonTag) {
			return new PersonTagTool((PersonTag) tag);
		} else if (tag instanceof AddressTag) {
			return new AddressTagTool((AddressTag) tag);
		} else if (tag instanceof MarriageTag) {
			return new MarriageTagTool((MarriageTag) tag);
		} else if (tag instanceof ParentTag) {
			return new ParentTagTool((ParentTag)tag);
		}

		return new TagTool(tag);
	}
}
