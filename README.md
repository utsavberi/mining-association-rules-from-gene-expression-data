<h2>Mining Association Rules from Gene Expression Data</h2>

<ol>
<li>Implement the Apriori algorithm to find all frequent itemsets. Report the number of frequent
itemsets for support of 30%, 40%, 50%, 60%, and 70%, respectively.
<li>Generate association rules based on the templates you specify. Test templates:
<ul>
<li>Template 1:
{RULE|BODY|HEAD} HAS ({ANY|NUMBER|NONE}) OF (ITEM1, ITEM2, ..., ITEMn)
<li> Template 2:
SizeOf({BODY|HEAD|RULE}) ≥ NUMBER.
<li> Template 3: Any combined templates using AND or OR. For example:
HEAD HAS (1) OF (Disease) AND BODY HAS (NONE) OF (Disease)
</ul>
</ol>
