import java.util.HashSet;


public class AssociationRule {
	public HashSet<String> head;
	public HashSet<String> body;
	public int confidence;
	public AssociationRule(){}
	public AssociationRule(HashSet<String> body,HashSet<String> head,int confidence){
		this.body = body;
		this.head = head;
		this.confidence = confidence;
	}
	
	@Override
	public String toString() {
		return body.toString()+" -> "+head.toString()+" ("+confidence+")";
	}
}
