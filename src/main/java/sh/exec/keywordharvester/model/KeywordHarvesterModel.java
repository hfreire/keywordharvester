package sh.exec.keywordharvester.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public abstract class KeywordHarvesterModel implements Serializable {
	private static final long serialVersionUID = 4711209097646443512L;
	public abstract String getKey();
}
