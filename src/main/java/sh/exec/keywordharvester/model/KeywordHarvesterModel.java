package sh.exec.keywordharvester.model;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public abstract class KeywordHarvesterModel {

}
