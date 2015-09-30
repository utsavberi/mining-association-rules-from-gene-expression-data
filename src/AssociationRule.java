import java.util.HashSet;


public class AssociationRule {
	public HashSet<String> head;
	public HashSet<String> body;
	public double confidence;
	public AssociationRule(HashSet<String> s, double d){}
	public AssociationRule(HashSet<String> body,HashSet<String> head,double confidence){
		this.body = body;
		this.head = head;
		this.confidence = confidence;
	}
	
	@Override
	public String toString() {
		return body.toString()+" -> "+head.toString()+" ("+confidence+")";
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		AssociationRule rule = (AssociationRule)obj;
		return (head.equals(rule.head) && body.equals(rule.body));//super.equals(obj);
	}
	
	@Override
    public int hashCode() {
        return head.hashCode()*body.hashCode();
    }
	
}
